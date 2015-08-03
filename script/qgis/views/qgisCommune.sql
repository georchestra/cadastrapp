-- Create commune Views based on Qgis Models
-- RTRIM is made on libcom because space exist in data from Qgis Model

CREATE OR REPLACE VIEW #schema_cadastrapp.commune AS 
	SELECT commune.cgocommune, 
			commune.annee, 
			commune.clerivili, 
			commune.libcom, 
			commune.libcom_maj, 
			commune.libcom_min, 
			commune.typcom
		FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
			'select 
				communeqgis.ccodep|| communeqgis.ccodir|| communeqgis.ccocom as cgocommune,
				communeqgis.annee,
				communeqgis.clerivili,
				rtrim(communeqgis.libcom),
				rtrim(upper(communeqgis.libcom) )as libcom_maj,
				rtrim(initcap(lower(communeqgis.libcom))) as libcom_min,
				typcom 
			from commune communeqgis where typcom is not null'::text) 
	commune(
		cgocommune character varying(6),
		annee character varying(4), 
		clerivili character varying(1), 
		libcom character varying(30), 
		libcom_maj character varying(30), 
		libcom_min character varying(30), 
		typcom character varying(1));

ALTER TABLE #schema_cadastrapp.commune OWNER TO #user_cadastrapp;


