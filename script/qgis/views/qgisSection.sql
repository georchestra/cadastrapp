create view cadastreapp_qgis.section as 
select  
  *
   FROM dblink('host=MQ-CMS-CRAI-001.fasgfi.fr dbname=qadastre user=cadastreapp password=c', 'select 
   distinct ccodep||ccodir||ccocom 
as ccoinsee,ccosec,ccopre,ccodep||ccodir||ccocom||ccopre||ccosec as geo_section
  from parcelle
'::text) parcelle(ccoinsee character varying(6), 
	ccosec character varying(2), 
	ccopre character varying(3),
	geo_section character varying(12));