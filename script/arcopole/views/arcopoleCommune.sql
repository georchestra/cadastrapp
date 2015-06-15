-- View: cadastrapp_arcopole.commune

-- DROP VIEW cadastrapp_arcopole.commune;

CREATE OR REPLACE VIEW cadastrapp_arcopole.commune AS 
 SELECT commune.ccoinsee, commune.commune, commune.annee, commune.ccodep, commune.ccodir, commune.ccocom, commune.libcom, commune.libcom_maj, commune.libcom_min
   FROM dblink('host=MQ-CMS-CRAI-001.fasgfi.fr dbname=cadastreArcopole user=cadastrapp password=c'::text, 'select id_comm as ccoinsee,
         annmaj||id_comm as commune, annmaj as annee,
coddep as ccodep,substr(id_comm,3,1) as ccodir,
substr(id_comm,4,3) as ccocom ,nomcomm as libcom ,nomcomm as libcom_maj,
initcap(lower(nomcomm)) as libcom_min
from cadastre.dgi_comm'::text) commune(ccoinsee character varying(6), commune character varying(10), annee character varying(4), ccodep character varying(2), ccodir character varying(1), ccocom character varying(3), libcom character varying(30), libcom_maj character varying(30), libcom_min character varying(30));

ALTER TABLE cadastrapp_arcopole.commune
  OWNER TO postgres;

