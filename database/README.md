# Alimentation de la base de données de cadastrapp

Ceci est la documentation des traitements qui mettent à jour la base de données applicative de cadastrapp.

=======
2 scripts existent :
- 1 script de purge + rechargement complet des données : `cadastrapp_load_data.sh`
- 1 script de rafraîchissement des vues matérialisées : `cadastrapp_update_data.sh`


**Pour une mise à jour incluant une mise à jour des données foncières (données MAJIC) il faut faire un rechargement complet de la base de données car, généralement il y a chaque année des mises à jour de valeurs des nomenclatures. En revanche, en cas de mise à jour des données du plan cadastral (EDIGEO) uniquement, on peut se contenter de rafraîchir les vues matérialisées.**

## Principe de fonctionnement

Avant de configurer et jouer les scripts, vous devez disposez d'une base de données PostgreSQL / PostGIS contenant des données cadastrales créées en utilisant le greffon **[QGIS cadastre](https://plugins.qgis.org/plugins/cadastre/)**.


Le fichier de configuration des scripts vous permet de définir si les données au format cadastre QGIS sont dans la même base de données que les données cadastrapp ou pas, sur le même serveur ou pas.
La base de données _source_ sera lue par les scripts. 

Le script de purge + rechargement complet des données va créer les tables et **[les vues matérialisées](https://www.postgresql.org/docs/current/sql-creatematerializedview.html)** dont cadastrapp a besoin pour fonctionner ; **SAUF** les tables liées aux demandes d'information foncières qui contiennent des archives statistiques. Ces tables ne sont JAMAIS supprimées par les scripts.

L'utilisation des vues matérialisées permet de gagner du temps lors d'une mise à jour concernant uniquement le plan cadastral car un simple `REFRESH MATERIALIZED VIEW table_name` suffit à relire la base de données source.

## Prérequis

### Versions PostgreSQL et PostGIS

* un système linux type Debian 9 + ou Ubuntu 16+
* PostgreSQL > 9.6
* PostGIS > 2.1 mais < 3.0

### Si toutes les données sont dans la même base de données

C'est le cas le plus simple.

Dans ce cas il faut 2 schémas. Exemples :
* `cadastre_gis` : il contiendra les données cadastre produites par le plugin cadastre de QGIS
* `cadastrapp` : il contiendra les données applicatives pour cadastrapp et créées par notre script

### Si les données sont dans 2 bases de données distinctes

Dans ce cas on aura 2 bases de données différentes (sur la même machine ou pas) et donc 2 schémas différents. Exemples :
* `base_1.cadastre_gis` : il contiendra les données cadastre produites par le plugin cadastre de QGIS
* `base_2.cadastrapp` : il contiendra les données applicatives pour cadastrapp et créées par notre script

La base de données cible qui contiendra les données de cadastrapp devra comporter l'extension **[dblink](http://www.postgresql.org/docs/current//dblink.html)**.

### Si on souhaite remonter les autorisations geographiques depuis les groupes georchestra

Il faudra que le [fichier de correspondance fourni à la console](https://github.com/georchestra/georchestra/tree/master/console#custom-areas) comporte les codes INSEE des communes.

La base de données cible qui contiendra les données de cadastrapp devra : 
* Comporter l'extension **[multicorn](https://multicorn.org/)**
* Avoir accès au serveur LDAP de l'instance geOrchestra

#### Installation de multicorn (Debian)

Dans Debian 10, multicorn est disponible via un paquet : 

`$ sudo apt install postgresql-11-python3-multicorn`

Cette commande est à adapter en fonction de votre version de PostgreSQL.

#### Création du Foreign Data Wrapper pour LDAP

Afin de récupérer les emprises géographiques définies pour l'organisation des utilisateurs, il est necessaire de configurer une connexion de la base de données vers le LDAP de Georchestra.

Commencer par installer l'extension multicorn sur la BDD précedemment créée : 

```
CREATE EXTENSION multicorn;
```

Puis, créer le lien vers le serveur LDAP : 

```
CREATE SERVER ldap_srv foreign data wrapper multicorn options (
    wrapper 'multicorn.ldapfdw.LdapFdw'
);
ALTER SERVER ldap_srv
    OWNER TO #user_cadastrapp;
```
> **Note:** Remplacer `#user_cadastrapp` par l'utilisateur utilisé par cadastrapp

## Configuration

Sous linux ou git bash sous Windows :
* aller dans le répertoire `database`
* dupliquer le fichier `config_sample.sh`
* le renommer en `config.sh`
* l'ouvrir et compléter les informations de connection aux bases de données
* si les données cadastre QGIS et cadastrapp sont dans la même base de données, laisser `uniqueDB=True` sinon mettre `uniqueDB=False`
* si vous souhaitez remonter les autorisations cartographiques de puis les organisations geOrchestra (LDAP), mettre `orgsAutorisations=True` sinon laisser `orgsAutorisations=False`

## Purge + rechargement complet des données

Sous linux ou git bash sous Windows :
* aller dans le répertoire `database`
* exécuter le script `cadastrapp_load_data.sh`

Note : il est possible de l'utiliser en mode silencieux avec l'option `-s` ou `--silent`. Si précisé, le script n'attendra pas de validation de la part de l'utilisateur.

## Mise à jour des données

Convient pour une mise à jour intermédiaire ne concernant pas une mise à jour des données foncières.

Sous linux ou git bash sous Windows :
* aller dans le répertoire `database`
* exécuter le script `cadastrapp_update_data.sh`

Note : il est possible de l'utiliser en mode silencieux avec l'option `-s` ou `--silent`. Si précisé, le script n'attendra pas de validation de la part de l'utilisateur.

## Mise en place d'un CRON pour rafraichir les autorisations liées aux organisations

Si vous avez mis en place la gestion des autorisations geographiques depuis les groupes georchestra, le script a créé une vue materialisée contenant les codes INSEE pour chaque groupe qui ont été remontés depuis le LDAP.
Ceci permet de ne pas surcharger ce dernier avec des requêtes à chaque sollicitation de cadastrapp.

Néanmoins, pour que les changements sur les périmètres des organisations faits dans la console geOrchestra soient pris en compte, cette vue materialisée devra être rafraichie.

Pour ce faire, il est conseillé de mettre en place un CRON qui permettra de la rafraichir à interval régulier.

### Mise en place d'un 'cron job' dans debian

Créez un fichier de script `cadastrapp_refresh_ldap_view.sh` sur le serveur contenant la base de données cadastrapp avec le contenu suivant :

```
#!/bin/sh
PGPASSWORD=#cadastrapp_password psql -h #cadastrapp_db_host -p $cadastrapp_db_port -d $cadastrapp_db_name -U $cadastrapp_user -c 'refresh materialized view #cadastrapp_schema.org_autorisation'
```
> **Note:** Remplacez les variables précédées d'un `#` par la valeur appropriée
Déplacez le fichier dans le répertoire cron correspondant à la fréquence souhaitée :

* /etc/cron.hourly => toutes les heures
* /etc/cron.daily => tous les jours
* /etc/cron.weekly => toutes les semaines
* /etc/cron.monthly => tous les mois

Ou bien si vous souhaitez un parametrage plus poussé, référez vous à la [documentation de crontab](https://debian-facile.org/doc:systeme:crontab)

Enfin pensez-bien à rendre le fichier executable : 

```
chmod +x cadastrapp_refresh_ldap_view.sh
```

### Mise en place d'un 'cron job' via pg_cron

Commencez par installer l'extension `cron` de postgresql : 

```
sudo apt install postgresql-11-cron
```

Une fois installée, partie OS, il faudra modifier les paramètres base de données dans PostgreSQL afin de pouvoir utiliser cette extension.

Le fichier “postgresql.conf” devra indiquer les éléments suivants pour permettre la création de cette extension en base :

* share_preload_libraries
* cron.database_name

```
sudo nano /etc/postgesql/11/main/postgresql.conf
```

Ajoutez à la fin du fichier : 

```
shared_preload_libraries = 'pg_cron'
cron.database_name = '#cadastrappDBName'
```

> **Note:** Remplacez `#cadastrappDBName` par le nom de la base de donnée hébergeant cadastrapp

Autorisez la connexion de l'utilisateur en local via trust pour permettre l'accès à la tache CRON,
pour cela dans le fichier `/etc/postgresql/11/main/pg_hba.conf` modifiez la ligne : 

```
host    #cadastrappDBName   #user_cadastrapp    127.0.0.1/32            trust
host    all                 all                 127.0.0.1/32            md5
```
> **Note:** Remplacez les variables précédées d'un `#` par la valeur appropriée
Relancez postgresql : 

```
sudo service postgresql restart
```

Activez l'extention dans la base de donnée hebergeant cadastrapp : 

```
CREATE EXTENSION pg_cron;
```

Autorisez l'utilisation pour l'utilisateur cadastrapp : 

```
GRANT USAGE ON SCHEMA cron TO #user_cadastrapp;
GRANT INSERT ON TABLE cron.job TO #user_cadastrapp;
GRANT USAGE ON SEQUENCE cron.jobid_seq TO #user_cadastrapp;
```
> **Note:** Remplacer `#user_cadastrapp` par l'utilisateur utilisé par cadastrapp
Enfin lancez le script de definition la tache CRON : 

```
./create_ldap_cronjob.sh
```
> **Note:** Par défaut, le script défini le rafraichissement de la vue mateiralisée toutes les heures, si cela ne vous convient pas, vous pouvez modifier cette configuration dans le fichier `sql/ldap/cronjob.sql`.
