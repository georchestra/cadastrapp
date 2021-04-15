# Alimentation de la base de données de cadastrapp

Ceci est la documentation des traitements qui mettent à jour la base de données applicative de cadastrapp.


## Principe de fonctionnement

Avant de configurer et jouer ce script, vous devez disposez d'une base de données PostgreSQL / PostGIS contenant des données cadastrales créées en utilisant le greffon **QGIS [cadastre](https://plugins.qgis.org/plugins/cadastre/)**

Cette base de données sera lue par le script qui va créer les tables et **[les vues matérialisées](https://www.postgresql.org/docs/current/sql-creatematerializedview.html)** dont cadastrapp a besoin pour fonctionner.

Le fichier de configuration du script vous permet de définir si les données cadastre QGIS sont dans la même base de données que les données cadastrapp ou pas, sur le même serveur ou pas.

L'utilisation des vues matérialisées permet de gagner du temps lors de la mise de vos données car un simple REFRESH ```MATERIALIZED VIEW table_name``` suffit à relire la base de données source.


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


## Configuration du script

Sous linux :
* aller dans le répertoire `database`
* dupliquer le fichier `config_sample.sh`
* le renommer en `config.sh`
* l'ouvrir et compléter les informations de connection aux bases de données
* si les données cadastre QGIS et cadastrapp sont dans la même base de données, laisser `uniqueDB=True` sinon mettre `uniqueDB=False`


## Utilisation du script

Sous linux :
* aller dans le répertoire `database`
* exécuter le script `cadastrapp_load_data.sh`




