-- View: cadastrapp_arcopole.section

-- DROP VIEW cadastrapp_arcopole.section;

CREATE OR REPLACE VIEW #schema_cadastrapp.section AS 
 SELECT edi_sectio.ccoinsee, edi_sectio.ccosec, edi_sectio.ccopre, edi_sectio.geo_section
   FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=c'::text, '
   select codcomm as ccoinsee,substr(id_sect,10,2) as ccosec ,substr(id_sect,7,3) as ccopre,id_sect
 as geo_section from #DBSchema_arcopole.edi_sectio
'::text) edi_sectio(ccoinsee character varying(6), ccosec character varying(2), ccopre character varying(3), geo_section character varying(12));

ALTER TABLE #schema_cadastrapp.section OWNER TO #user_cadastrapp;
