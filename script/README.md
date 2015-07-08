Cadastrapp - Database Script
=========================== 

Database creation script for Arcopole and Qgis models.

Application is base on views creation linked to specific model. To create new database you will need to active DBLINK extension for postgresql.

```
CREATE EXTENSION dblink
```

See http://www.postgresql.org/docs/9.1/static/dblink.html



##  Arcopole model:

Configure and launch with postgres user ```arcopole/createDBUsingArcopoleModel.sh ```


##  Qgis model:

Configure and launch with postgres user ```qgis/createDBUsingQgisModel.sh ```
	
