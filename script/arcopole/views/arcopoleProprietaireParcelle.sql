-- Create view making link beetween parcelle and owners based on Arcopole Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietaire_parcelle AS 
	SELECT 
		proprietaire_parcelle.parcelle, 
		proprietaire_parcelle.comptecommunal
	FROM dblink('host=#DBHost_arcopole port=#DBPort_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select 
			p.codparc as parcelle,
			p.dnupro as comptecommunal
		from cadastre.dgi_nbati p'::text) 
	proprietaire_parcelle(
		parcelle character varying(16), 
		comptecommunal character varying(12));

ALTER TABLE #schema_cadastrapp.proprietaire_parcelle  OWNER TO #user_cadastrapp;

