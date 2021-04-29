# Cadastrapp - Fond de plan cadastral de Rennes Métropole

Voici un espace de travail complet tel qu'il est utilisé par Rennes Métropole afin de disposer d'un fond de plan cadastral complet.

L'espace de travail exposera les couches suivantes :

* Cadastre : Communes	(`ref_cad:commune`)
* Cadastre : Sections	(`ref_cad:section`)
* Cadastre : Parcelles	(`ref_cad:parcelle`)
* Cadastre : Bâtiments	(`ref_cad:batiment`)

Et les 2 agrégats suivants :

* Cadastre Rennes Métropole	(`ref_cad:cadastre`)
* Cadastre Rennes Métropole (N&B)	(`ref_cad:cadastre_nb`)

Ce sont les agrégats qu'il conviendra de tuiler / mettre en cache.


![](https://public.sig.rennesmetropole.fr/geoserver/ref_cad/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=ref_cad%3Acadastre&exceptions=application%2Fvnd.ogc.se_inimage&SRS=EPSG%3A3948&STYLES=&WIDTH=301&HEIGHT=251&BBOX=1348271.3605096072%2C7208053.914425999%2C1348629.2579826668%2C7208352.162320215)  ![](https://public.sig.rennesmetropole.fr/geoserver/ref_cad/wms?SERVICE=WMS&VERSION=1.1.1&REQUEST=GetMap&FORMAT=image%2Fpng&TRANSPARENT=true&LAYERS=ref_cad%3Acadastre_nb&exceptions=application%2Fvnd.ogc.se_inimage&SRS=EPSG%3A3948&STYLES=&WIDTH=301&HEIGHT=251&BBOX=1348271.3605096072%2C7208053.914425999%2C1348629.2579826668%2C7208352.162320215)

Pour augmenter la lisibilité cartographique du plan cadastral, Rennes Métropole calcule 4 couches de données à partir des données cadastrales :

* `rm_comm_lin` : topologie de lignes pour afficher correctement les limites communales avec un style en pointillés
* `rm_sectio_lin` : topologie de lignes pour afficher correctement les limites de sections avec un style en pointillés
* `rm_suf_lin` : topologie de lignes pour afficher correctement les limites de subdivisions fiscales intra parcellaire avec un style en pointillés
* `rm_parc_rejetee` : couche de points calculés au centre de gravité des parcelles qui figurent au plan mais n'ont pas d'équivalent dans la matrice foncière.


Le répertoire `couches_rm/` contient les traitements FME ou SQL pour créer ces 4 surcouches.

Enfin, pour de bonnes performances, toutes les couches GeoServer utilisent une connexion JNDI vers la pbase de données PostGIS. Pour créer cette connexion (également appelé pool de connexion) il faut ajouter le bloc suivant dans le fichier `/var/lib/tomcat9/conf/context.xml`

```
    <Resource name="jdbc/bdu"
        auth="Container"
        type="javax.sql.DataSource"
        driverClassName="org.postgresql.Driver"
        url="jdbc:postgresql://{host}:5432/{db_name}"
        username="{pg_role_name}" password="{pg_role_password}"
        maxActive="20"
        initialSize="0"
        minIdle="0"
        maxIdle="8"
        maxWait="10000"
        timeBetweenEvictionRunsMillis="30000"
        minEvictableIdleTimeMillis="60000"
        testWhileIdle="true"
        validationQuery="SELECT 1"
        maxAge="600000"
        rollbackOnReturn="true"
    />
```

Penser à adapter manuellement le fichier `bdu.cadastre_qgis/datastore.xml` à l'organisation de votre base de données.
