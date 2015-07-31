-- Create view section based on Qgis Models

CREATE OR REPLACE VIEW #schema_cadastrapp.section AS 
	SELECT 
		cgocommune,
		ccosec,
		ccopre
	FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis', 
		'select 
	   		distinct ccodep||ccodir||ccocom as cgocommune,
			ccosec,
			ccopre
	  	from parcelle'::text) 
	parcelle(
		cgocommune character varying(6), 
		ccosec character varying(2), 
		ccopre character varying(3));
	
ALTER TABLE #schema_cadastrapp.section OWNER TO #user_cadastrapp;
