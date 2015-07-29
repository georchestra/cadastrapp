-- Create view section based on Arcopole Models

CREATE OR REPLACE VIEW #schema_cadastrapp.section AS 
	SELECT 
		section.ccoinsee, 
		section.ccosec, 
		section.ccopre, 
		section.geo_section
   FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
   		'select 
	   		codcomm as ccoinsee,
	   		substr(id_sect,10,2) as ccosec ,
	   		substr(id_sect,7,3) as ccopre,
	   		id_sect as geo_section 
		from #DBSchema_arcopole.edi_sectio '::text) 
	section(
		ccoinsee character varying(6), 
		ccosec character varying(2), 
		ccopre character varying(3), 
		geo_section character varying(12));

ALTER TABLE #schema_cadastrapp.section OWNER TO #user_cadastrapp;
