-- Create view section based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.uf_parcelle AS
	SELECT
		uf_parcelle.parcelle,
		uf_parcelle.uf,
		uf_parcelle.comptecommunal
	FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
		'select 
			distinct p.geo_parcelle as parcelle,
			z.id as uf,
			z.comptecommunal as comptecommunal
		from #DBSchema_qgis.geo_parcelle AS p
		 join #DBSchema_qgis.geo_unite_fonciere AS z ON	ST_Contains(z.geom, p.geom)
		order by z.id,z.comptecommunal'::text) 
	uf_parcelle (
		parcelle character varying(19), 
		uf integer, 
		comptecommunal character varying(15)
	);

ALTER TABLE #schema_cadastrapp.uf_parcelle OWNER TO #user_cadastrapp;

