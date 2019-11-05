# Cadastrapp - Script de création et d'alimentation de la base de données

Ceci est la documentation du script qui crée ou met à jour la base de données applicative de Cadastrapp.


## Principe de fonctionnement

Avant de configurer et jouer ce script, vous devez disposez d'une base de données PostgreSQL / PostGIS contenant des données cadastrales créées en utilisant le greffon **QGIS [cadastre](https://plugins.qgis.org/plugins/cadastre/)**

Cette base de données sera lue par le script qui va créer les tables et **[les vues matérialisées](https://www.postgresql.org/docs/9.3/static/sql-creatematerializedview.html)** dont cadastrapp a besoin pour fonctionner dans une autre base de données. Cette nouvelle base de données est souvent installée à côté des autres bases de données PostgreSQL de geOrchestra, sur votre serveur.

L'utilisation des vues matérialisées permet de gagner du temps lors de la mise de vos données car un simple REFRESH ```MATERIALIZED VIEW table_name``` suffit à relire la base de données source.


## Prérequis

- 1 base de données PostgreSQL / PostGIS contenant vos données cadastrales. Cette base doit être accessible par le réseau.
- 1 base de données PostgreSQL > 9.3 / PostGIS (version ?) avec l'extension **[dblink](http://www.postgresql.org/docs/9.3/static/dblink.html)**  qui va abriter la base de données de Cadastrapp


## Création de la base de données de Cadastrapp

En tant que postgres, créer la base de données :

```
CREATE DATABASE cadastrapp WITH OWNER = postgres ENCODING = 'UTF8' ;
```

Sur cette base de données, installer l'extension dblink :

```
CREATE EXTENSION dblink ;
```


Puis créer un rôle de connexion et attribuer les droits sur la base de données :

```
CREATE USER cadastrapp WITH PASSWORD 'votre_mdp' NOSUPERUSER INHERIT NOCREATEDB NOCREATEROLE NOREPLICATION ;

GRANT ALL PRIVILEGES ON DATABASE cadastrapp TO cadastrapp ;
```


## Utilisation du script
:warning: Le modèle arcOpole ne sera plus supporté à partir de la version 1.9 de Cadastrapp.

Ouvrir le fichier correspondant au modèle de données Qgis :

- QGIS : ```/script/qgis/createDBUsingQgisModel.sh```

et saisir les paramètres de connexion aux différentes base de données : [https://github.com/georchestra/cadastrapp/blob/master/script/qgis/createDBUsingQgisModel.sh#L45-L59](https://github.com/georchestra/cadastrapp/blob/master/script/qgis/createDBUsingQgisModel.sh#L45-L59)

Exécuter ensuite ce script en tant qu'utilisateur postgres sur votre machine ou avec le rôle qui possède la base de données *cadastrapp* créée auparavant.

## Modèle de données Cadastrapp
Vous trouverez le modèle de données Cadastrapp 2019 sur [cette page](https://github.com/georchestra/cadastrapp/wiki/Mod%C3%A8le-de-donn%C3%A9es-Cadastrapp).




