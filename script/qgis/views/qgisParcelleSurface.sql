-- View: cadastreapp_qgis.parcelle

-- DROP VIEW #schema_cadastrapp.v_parcelle_surfc;

create view #schema_cadastrapp.v_parcelle_surfc as select  * 
	FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
	'select distinct geo_parcelle as parcelle,st_area(geom) as surfc from geo_parcelle'::text)
parcelle_surfc(parcelle character varying(19),surfc float);

ALTER TABLE #schema_cadastrapp.v_parcelle_surfc OWNER TO #user_cadastrapp;


