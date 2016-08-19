-- Create view making link beetween parcelle and owners based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietaire_parcelle AS 
	SELECT proprietaire_parcelle.parcelle, 
		proprietaire_parcelle.comptecommunal
  	FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
  		'select
			p.parcelle,
			p.comptecommunal
		from #DBSchema_qgis.parcelle p'::text) 
	proprietaire_parcelle (
		parcelle  character varying(19), 
		comptecommunal character varying(15));

ALTER TABLE #schema_cadastrapp.proprietaire_parcelle OWNER TO #user_cadastrapp;
