-- Create view making link beetween parcelle and owners based on Arcopole Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.co_propriete_parcelle AS 
	SELECT 
		co_propriete_parcelle.lots, 
		co_propriete_parcelle.parcelle, 
		co_propriete_parcelle.comptecommunal, 
		co_propriete_parcelle.dnulot
	FROM dblink('host=#DBHost_arcopole port=#DBPort_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select  
			d.codlot as lots,
			d.codparc as parcelle,
			lo.dnupro as comptecommunal,
			ll.dnulot 
		from #DBSchema_arcopole.dgi_invar d 
			left join #DBSchema_arcopole.dgi_local lo on lo.id_local = d.invar
			left join #DBSchema_arcopole.dgi_lotloc ll on lo.id_local=ll.invloc'::text) 
	co_propriete_parcelle(
		lots character varying(255), 
		parcelle character varying(16), 
		comptecommunal character varying(12), 
		dnulot character varying(7));

ALTER TABLE #schema_cadastrapp.co_propriete_parcelle  OWNER TO #user_cadastrapp;

