Cadastrapp - GeoServer
======================

/pci

This is an basic example workspace configuration for GeoServer, provided by CRAIG.
Once copied in GeoServer datadir/workspaces, you need to configure your database connection in pci/qadastre/datastore.xml and regenerate the bounding box for each layer/layergroup.
It assumes the data in postgis is in EPSG:2154 projection, and uses the QGIS model.
REPROJECT_TO_DECLARED (and declared SRS is EPSG:3857) is mandatory for cadastrapp to work fine.
Styles are provided as examples too, contributions/tweaks welcome.

/pci_qgis

This is another datadir example provided by CIGAL trying to reproduct QGIS cadastre plugin styling.
It assumes the data in postgis is in EPSG:3948 projection, and uses the QGIS model.
Styles are provided as examples too, contributions/tweaks welcome.

sample
![stylepci1](https://cloud.githubusercontent.com/assets/5012040/20055112/f987fb0c-a4df-11e6-88a4-2e267ff84f84.png)

![stylepci2](https://cloud.githubusercontent.com/assets/5012040/20055115/fc4cfa18-a4df-11e6-8661-08265b278616.png)

![stylepci3](https://cloud.githubusercontent.com/assets/5012040/20055118/feeb9e96-a4df-11e6-80c9-01b599fa2d82.png)

![stylepci4](https://cloud.githubusercontent.com/assets/5012040/20055124/02f75d18-a4e0-11e6-9c0e-f8971df10948.png)
