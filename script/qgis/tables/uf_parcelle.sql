-- Create link table beetween parcelle and unite fonciere for qgis model

CREATE TABLE #schema_cadastrapp.uf_parcelle AS
	SELECT uf_parcelle.parcelle,
		uf_parcelle.uf,
		uf_parcelle.comptecommunal
	FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
  		'select 
  			p.geo_parcelle as parcelle,
  			z.id as uf,
  			z.comptecommunal as comptecommunal
		from geo_parcelle AS p
			join geo_unite_fonciere AS z ON  ST_Contains(z.geom, p.geom) 
			order by z.id,z.comptecommunal'::text) 
	uf_parcelle(
		parcelle character varying(15), 
		uf integer, 
		comptecommunal character varying(12)
);

