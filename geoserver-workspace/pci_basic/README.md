Cadastrapp - Style Basic
======================

This is an basic example workspace configuration for GeoServer, provided by CRAIG.
Once copied in GeoServer datadir/workspaces, you need to configure your database connection in pci/qadastre/datastore.xml and regenerate the bounding box for each layer/layergroup.
It assumes the data in postgis is in EPSG:2154 projection, and uses the QGIS model.
REPROJECT_TO_DECLARED (and declared SRS is EPSG:3857) is mandatory for cadastrapp to work fine.
Styles are provided as examples too, contributions/tweaks welcome.
