-- View: cadastreapp_qgis.proprietaire

-- DROP VIEW #schema_cadastrapp.proprietaire;

create view #schema_cadastrapp.proprietaire_parcelle as 
select *
  FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
	'select  lo.lots,lo.parcelle,lo.comptecommunal,l.dnupro,lo.dnulot from lots lo
		left join local10 l on l.comptecommunal=lo.comptecommunal
		order by lots,dnulot,parcelle asc'::text)
proprietaire_parcelle (
	lots character varying(29), 
	parcelle  character varying(19), 
	comptecommunal character varying(15),
	dnupro character varying(6), 
	dnulot character varying(7));

ALTER TABLE #schema_cadastrapp.proprietaire_parcelle OWNER TO #user_cadastrapp;
