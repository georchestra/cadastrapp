-- View: cadastreapp_arcopole.proprietaire_parcelle

-- DROP VIEW #schema_cadastrapp.proprietaire_parcelle;

CREATE OR REPLACE VIEW #schema_cadastrapp.proprietaire_parcelle AS 
 SELECT proprietaire_parcelle.lots, proprietaire_parcelle.parcelle, proprietaire_parcelle.comptecommunal, proprietaire_parcelle.dnupro, proprietaire_parcelle.dnulot
  FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, '
	select  d.codlot as lots,
		d.codparc as parcelle,
		lo.dnupro as comptecommunal,
		lo.dnupro , 
		ll.dnulot 
	from #DBSchema_arcopole.dgi_invar d 
		left join #DBSchema_arcopole.dgi_local lo on lo.id_local = d.invar
		left join #DBSchema_arcopole.dgi_lotloc ll on lo.id_local=ll.invloc
		order by dnulot,parcelle asc'::text) 
	proprietaire_parcelle(lots character varying(255), 
						parcelle character varying(16), 
						comptecommunal character varying(12), 
						dnupro character varying(12), 
						dnulot character varying(7));

ALTER TABLE #schema_cadastrapp.proprietaire_parcelle  OWNER TO #user_cadastrapp;

