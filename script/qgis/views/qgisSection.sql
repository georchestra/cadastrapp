-- Create view section based on Qgis Models

CREATE OR REPLACE VIEW #schema_cadastrapp.section as 
select  
  *
   FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis', 'select 
   distinct ccodep||ccodir||ccocom 
as ccoinsee,ccosec,ccopre,ccodep||ccodir||ccocom||ccopre||ccosec as geo_section
  from parcelle
'::text) parcelle(ccoinsee character varying(6), 
	ccosec character varying(2), 
	ccopre character varying(3),
	geo_section character varying(12));
	
	
ALTER TABLE #schema_cadastrapp.section OWNER TO #user_cadastrapp;
