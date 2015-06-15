-- View: cadastreapp_qgis.commune

-- DROP VIEW cadastreapp_qgis.commune;

CREATE OR REPLACE VIEW cadastreapp_qgis.commune AS 
 SELECT commune.ccoinsee, commune.commune, commune.annee, commune.ccodep, commune.ccodir, commune.ccocom, commune.clerivili, commune.libcom, commune.libcom_maj, commune.libcom_min, commune.typcom
   FROM dblink('host=MQ-CMS-CRAI-001.fasgfi.fr dbname=qadastre user=cadastreapp password=c'::text, 'select ccodep||ccodir||ccocom as ccoinsee,commune,
annee,
ccodep,
ccodir,
ccocom,
clerivili,
libcom,
upper(libcom) as libcom_maj,
initcap(lower(libcom)) as libcom_min,
typcom 
from commune'::text) commune(ccoinsee character varying(6), commune character varying(10), annee character varying(4), ccodep character varying(2), ccodir character varying(1), ccocom character varying(3), clerivili character varying(1), libcom character varying(30), libcom_maj character varying(30), libcom_min character varying(30), typcom character varying(1));

ALTER TABLE cadastreapp_qgis.commune
  OWNER TO postgres;


