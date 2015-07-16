-- View: cadastreapp_qgis.commune

-- DROP VIEW #schema_cadastrapp.commune;

CREATE OR REPLACE VIEW #schema_cadastrapp.commune AS 
 SELECT commune.ccoinsee, commune.commune, commune.annee, commune.ccodep, commune.ccodir, commune.ccocom, commune.clerivili, commune.libcom, commune.libcom_maj, commune.libcom_min, commune.typcom
   FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
    'select ccodep||ccodir||ccocom as ccoinsee,commune,
	annee,
	ccodep,
	ccodir,
	ccocom,
	clerivili,
	rtrim(libcom),
	rtrim(upper(libcom) )as libcom_maj,
	rtrim(initcap(lower(libcom))) as libcom_min,
	typcom 
	from commune where typcom is not null'::text) 
	commune(ccoinsee character varying(6),
	commune character varying(10), 
	annee character varying(4), 
	ccodep character varying(2), 
	ccodir character varying(1), 
	ccocom character varying(3), 
	clerivili character varying(1), 
	libcom character varying(30), 
	libcom_maj character varying(30), 
	libcom_min character varying(30), 
	typcom character varying(1));

ALTER TABLE #schema_cadastrapp.commune OWNER TO #user_cadastrapp;


