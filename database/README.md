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

Il suffit ensuite de passer l'option `ldapAreas.enable` à true dans le fichier cadastrapp.properties, les zones de compétence des organisations seront alors remontées depuis le LDAP Georchestra toutes les heure par défaut. Si vous souhaitez changer la periodicité, modifier l'option `ldapAreas.cronExpression`.

## Configuration

Sous linux ou git bash sous Windows :
* aller dans le répertoire `database`
* dupliquer le fichier `config_sample.sh`
* le renommer en `config.sh`
* l'ouvrir et compléter les informations de connection aux bases de données
* si les données cadastre QGIS et cadastrapp sont dans la même base de données, laisser `uniqueDB=True` sinon mettre `uniqueDB=False`

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
