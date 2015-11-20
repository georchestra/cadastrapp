Cadastrapp - Database Script
=========================== 

Prerequise for this installation, Postgresql 9.2 for materialized view, and have access to a arcopole or Qgis cadastre database.

Database creation script for Arcopole and Qgis models.

Application is base on views creation linked to specific model. To create new database you will need to active DBLINK extension for postgresql.

```
CREATE EXTENSION dblink
```

See http://www.postgresql.org/docs/9.1/static/dblink.html


If your are using Postgresql upper to 9.2 version, you could change script to use MATERIALIZED view, application will be quiker because DLink with the other database won't be query each time :

http://www.postgresql.org/docs/9.4/static/sql-creatematerializedview.html

To update your view data only a refresh will be need.


##  Arcopole model:

Configure and launch with postgres user ```arcopole/createDBUsingArcopoleModel.sh ```


##  Qgis model:

Configure and launch with postgres user ```qgis/createDBUsingQgisModel.sh ```
	
Those script can be launch with parameters, see commentary inside scripts.
