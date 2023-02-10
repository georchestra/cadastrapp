
# Prérequis

Cadastrapp est une application composée techniquement de 2 modules :

- un module serveur en Java qui est une API d’accès aux données de la matrice foncière
- un add-on (plugin) au visualiseur de geOrchestra



## Une instance geOrchestra


Cadastrapp a une dépendance à geOrchestra en ce qui concerne la gestion de la sécurité (via des en-têtes HTTP spécifiques) et donc la gestion des accréditations des utilisateurs aux données à caractère personnel de la matrice foncière du cadastre. Des routes spécifiques seront donc créées dans le proxy de sécurité de geOrchestra.

Cadastrapp requiert une instance de **geOrchestra version >= 16.12**.


## Un serveur Tomcat

Le module serveur est une API développée en Java. Il faut donc un Tomcat supportant une version **Java >= 1.7**.



## Un serveur PostgreSQL / PostGIS

Cadastrapp a besoin d'une base de données **PostgreSQL version 9.4 ou supérieure avec l'extension DBlink.** Cette base de données contiendra un schéma traditionnellement nommé :code:`cadastrapp`. Il contiendra les données cadastrales spécialement optimisées pour Cadastrapp.

Des couches de données cadastrales devront être préalablement disponibles dans un autre schéma traditionnellement nommé :code:`cadastre_qgis` voire une toute autre base de données. Ce sont ces couches qui seront publiées sur GeoServer pour disposer d'un fond de plan complet et des couches applicatives nécessaires pour Cadastrapp.




