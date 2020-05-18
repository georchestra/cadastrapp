
Guide développeur
======================================

.. toctree::
   :maxdepth: 2

 

 
Matrice des fonctionnalités
^^^^^^^^^^^^^^^^^^^^^^^^^^^

* {string} = texte libre
* {code} = doit correspondre à une valeur en base ou une valeur codée
* {0|1} = liste de valeurs autorisées


+------------------------------------+-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|  Bloc                              |  Fonctionnalité             |  Responsive  |  Action                         | CNIL 0 | CNIL 1 | CNIL 2 |  Appel API                                                                              |
+====================================+=============================+==============+=================================+========+========+========+=========================================================================================+
|  **Rechercher des parcelles**      |  Recherche par référence    |      X       |  Sélectionner une commune       |    X   |    X   |    X   |  GET /cadastrapp/services/getCommune?libcom={string}                                    |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  GET /cadastrapp/services/getCommune?cgocommune={string}                                |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Sélectionner une section       |    X   |    X   |    X   |  GET /cadastrapp/services/getSection?cgocommune={code}                                  |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Sélectionner une parcelle      |    X   |    X   |    X   |  GET /cadastrapp/services/getDnuplaList?cgocommune={code}&ccopre={code}&ccosec={code}   |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Afficher le résultat           |    X   |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : cgocommune={code}&dnupla={code}&ccopre={code}&ccosec={code}                |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  Recherche par identifiant  |              |  Chercher une parcelle          |    X   |    X   |    X   |  GET /geoserver/wfs?request=getfeature&version=1.0.0&service=wfs                        |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |  sur la carte                   |        |        |        |  &typename={workspace:layer}&outputFormat=application/json                              |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &cql_filter=geo_parcelle='{code}'                                                      |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Afficher le résultat           |    X   |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : parcelle={code}                                                            |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  Recherche par adresse      |      X       |  Sélectionner une commune       |    X   |    X   |    X   |  GET /cadastrapp/services/getCommune?libcom={string}                                    |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |  cadastrale                 |              |                                 |        |        |        |  GET /cadastrapp/services/getCommune?cgocommune={string}                                |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Sélectionner une voie ou       |    X   |    X   |    X   |  GET /cadastrapp/services/getVoie?cgocommune={code}&dvoilib={string}                    |
|                                    |                             |              |  un lieu-dit                    |        |        |        |                                                                                         |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Afficher le résultat           |    X   |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : cgocommune={code}&dvoilib={string}&dnvoiri={number}&dindic={string}        |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  Recherche par lot          |              |  Afficher le résultat           |    X   |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : parcelle={code}%0{code}…                                                   |
+------------------------------------+-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|  **Rechercher des propriétaires**  |  Recherche par nom d'usage  |      X       |  Sélectionner une commune       |        |    X   |    X   |  GET /cadastrapp/services/getCommune?libcom={string}                                    |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |  ou nom de naissance        |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  GET /cadastrapp/services/getCommune?cgocommune={string}                                |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Recherche par nom d'usage      |        |    X   |    X   |  GET /cadastrapp/services/getProprietaire?cgocommune={code}                             |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &ddenom={string}&birthsearch=false                                                     |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Recherche par nom de naissance |        |    X   |    X   |  GET /cadastrapp/services/getProprietaire?cgocommune={code}                             |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &ddenom={string}&checkBoxSearchByBirthNames=on&details={integer}&birthsearch=true      |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Afficher le résultat           |        |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : comptecommunal={string}                                                    |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  Recherche par compte       |              |  Sélectionner une commune       |        |    X   |    X   |  GET /cadastrapp/services/getCommune?libcom={string}                                    |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |  propriétaire               |              |                                 |        |        |        |  GET /cadastrapp/services/getCommune?cgocommune={string}                                |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Rechercher un compte proprio   |        |    X   |    X   |  GET /cadastrapp/services/getProprietaire?cgocommune={code}                             |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &details={integer}&dnupro={string}                                                     |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Afficher le résultat           |        |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : comptecommunal={string}                                                    |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  Recherche par lot          |              |  Afficher le résultat           |        |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : comptecommunal={code}&comptecommunal={code}&…                              |
+------------------------------------+-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|  **Sélection graphique**           |  Par point                  |      X       |  Sélectionner une parcelle      |    X   |    X   |    X   |  GET /geoserver/wfs?request=getfeature&version=1.0.0&service=wfs                        |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |  sur la carte                   |        |        |        |  &typename={workspace:layer}&outputFormat=application/json                              |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &filter=<Filter xmlns:gml="http://www.opengis.net/gml"><Intersects><PropertyName>      |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  geom</PropertyName><gml:Point><gml:coordinates>{coordinates}</gml:coordinates>         |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  </gml:Point></Intersects></Filter>                                                     |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Afficher le résultat           |    X   |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : parcelle={code}                                                            |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  Par ligne                  |      ?       |  Sélectionner des parcelles     |    X   |    X   |    X   |  GET /geoserver/wfs?request=getfeature&version=1.0.0&service=wfs                        |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |  sur la carte                   |        |        |        |  &typename={workspace:layer}&outputFormat=application/json                              |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &filter=<Filter xmlns:gml="http://www.opengis.net/gml"><Intersects><PropertyName>      |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  geom</PropertyName><gml:LineString><gml:coordinates>{coordinates}</gml:coordinates>    |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  </gml:LineString></Intersects></Filter>                                                |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Afficher le résultat           |    X   |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : parcelle={code}&parcelle={code}&…                                          |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  Par polygone               |      ?       |  Sélectionner des parcelles     |    X   |    X   |    X   |  GET /geoserver/wfs?request=getfeature&version=1.0.0&service=wfs                        |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |  sur la carte                   |        |        |        |  &typename={workspace:layer}&outputFormat=application/json                              |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &filter=<Filter xmlns:gml="http://www.opengis.net/gml"><Intersects><PropertyName>      |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  geom</PropertyName><gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  <gml:coordinates>{coordinates}</gml:coordinates>                                       |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  </gml:LinearRing></gml:outerBoundaryIs></gml:Polygon></Intersects></Filter>            |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Afficher le résultat           |    X   |    X   |    X   |  POST /cadastrapp/services/getParcelle                                                  |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  FORM_DATA : parcelle={code}&parcelle={code}&…                                          |
+------------------------------------+-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|  Fiche info parcelle               |  onglet Parcelle            |      X       |  Afficher les infos             |    X   |    X   |    X   |  GET /cadastrapp/services/getFIC?parcelle={code}&onglet=0                               |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Bordereau parcellaire          |    X   |    X   |    X   |  GET /cadastrapp/services/createBordereauParcellaire?parcelle={code}                    |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &personaldata={0|1}&basemapindex={0|n}                                                 |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  &fillcolor=81BEF7&opacity=0.4&strokecolor=111111&strokewidth=3  (3)                    |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  onglet Propriétaires       |      X       |  Afficher les infos             |        |    X   |    X   |  GET /cadastrapp/services/getProprietairesByParcelles?parcelles={code}                  |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Relevé de propriété PDF        |        |    X   |    X   |  GET /cadastrapp/services/createRelevePropriete?                                        |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  compteCommunal={code}&parcelleId={NULL|code}&exportType=on  (4)                        |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Relevé de propriété CSV        |        |    X   |    X   |  GET /cadastrapp/services/createReleveProprieteAsCSV?                                   |
|                                    |                             |              |                                 |        |        |        |                                                                                         |
|                                    |                             |              |                                 |        |        |        |  compteCommunal={code}&parcelleId={NULL|code}&exportType=on  (4)                        |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  onglet Copropriétaires     |      X       |  Afficher les infos             |        |    X   |    X   |  GET /cadastrapp/services/getCoProprietaire?start=0&limit=25&parcelle={code} (1)        |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Relevé de propriété PDF        |        |    X   |    X   |  -> Relevé de propriété de l'onglet Propriétaires                                       |
|                                    |                             |              +---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |                             |              |  Relevé de propriété CSV        |        |    X   |    X   |  -> Relevé de propriété de l'onglet Propriétaires                                       |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  onglet Bâtiments           |      X       |  Afficher les infos             |        |        |    X   |  GET /cadastrapp/services/getBatiments?dnubat=%20A&parcelle={code} (2)                  |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  onglet Subdivisions        |      X       |  Afficher les infos             |        |        |    X   |  GET /cadastrapp/services/getFIC?parcelle={code}&onglet=3                               |
|                                    |  fiscales                   |              |                                 |        |        |        |                                                                                         |
|                                    +-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+
|                                    |  onglet Historique de       |      X       |  Afficher les infos             |    X   |    X   |    X   |  GET /cadastrapp/services/getFIC?parcelle={code}&onglet=4                               |
|                                    |  mutation                   |              |                                 |        |        |        |                                                                                         |
+------------------------------------+-----------------------------+--------------+---------------------------------+--------+--------+--------+-----------------------------------------------------------------------------------------+


Notes :

1. pagination

2. par défaut on sélectionne le premier bâtiment, soit le bâtiment A

3. ces informations de style proviennent des préférences

4. parcelleId={NULL} pour un relevé de propriété de toutes les parcelles



Notes de réflexion :

* Rechercher des parcelles > Recherche par identifiant : utilise uniquement les parcelles du plan cadastral. Ce n'est pas logique.

* Rechercher des co-propriétés : accessible uniquement par le menu de recherche avancée : est-ce nécessaire de le maintenir ?




Documentation de l'API
^^^^^^^^^^^^^^^^^^^^^^

Un fichier `WADL <https://fr.wikipedia.org/wiki/Web_Application_Description_Language>`_ est disponible après installation de la webapp :

https://my_sdi/cadastrapp/apidocs/wadl/application.wadl

Télécharger :download:`le fichier WADL <application.wadl>` pour la version courante.


Javadoc
^^^^^^^

Une documentation Javadoc construite automatiquement est diponible après installation de la webapp :

https://my_sdi/cadastrapp/apidocs/index.html


.. image:: _images/javadoc.png

