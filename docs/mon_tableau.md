


|  Fonctionnalité             |  Action                                                       | Appel API                                                                                          |
|-----------------------------|---------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|                             | Récupérer l'ID de la parcelle  cadastrale  depuis la carte    | GET  /geoserver/app/ows?SERVICE-WMS&LAYERS-'{ws:name}                                              |
|                             |                                                               |                                                                                                    |
|                             |                                                               | &QUERY_LAYERS-{ws:name}&STYLES-&SERVICE-WMS&VERSION-1.3.0                                          |
|                             |                                                               |                                                                                                    |
|                             |                                                               | &REQUEST-GetFeatureInfo&EXCEPTIONS-XML&BBOX-{code}                                                 |
|                             |                                                               |                                                                                                    |
|                             |                                                               | &FEATURE_COUNT-{code}&HEIGHT-{code}&WIDTH-{code}                                                   |
|                             |                                                               |                                                                                                    |
|                             |                                                               | &FORMAT-image%2Fpng&INFO_FORMAT-application%2Fvnd.ogc.gml                                          |
|                             |                                                               |                                                                                                    |
|                             |                                                               | &CRS-EPSG%3A3857&I-1065&J-432                                                                      |
|                             |---------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|                             | Récupérer la commune via cadastrapp                           | GET /cadastrapp/services/getCommune?cgocommune-{code}                                              |
|    Afficher la fiche NRU    |---------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|                             | Récupération des informations parcellaires                    | GET /cadastrapp/services/getParcelle?parcelle-{code}                                               |
|                             |---------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|                             | Récupération de la liste des mentions                         | GET /urbanisme/renseignUrba?parcelle-{code}                                                        |
|                             |---------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|                             | Récupération infos complémentaires parcelle                   | GET /cadastrapp/services/getFIC?parcelle-{code}&onglet-1                                           |
|                             |---------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|                             | Récupération infos complémentaires parcelle                   | GET /cadastrapp/services/getFIC?parcelle-{code}&onglet-0                                           |
|                             |---------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
|                             | Récupération infos complémentaires sur les RU                 | GET /urbanisme/renseignUrbaInfos?code_commune-{code}                                               |
|-----------------------------|---------------------------------------------------------------|----------------------------------------------------------------------------------------------------|
 