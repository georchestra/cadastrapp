-- Create view section based on Qgis Models

--DROP MATERIALIZED VIEW #schema_cadastrapp.uf_parcelle ;

CREATE MATERIALIZED VIEW #schema_cadastrapp.uf_parcelle AS
	SELECT
		uf_parcelle.parcelle,
		uf_parcelle.uf,
		uf_parcelle.comptecommunal
  FROM 
  (
    SELECT 
			distinct p.geo_parcelle as parcelle,
			z.id as uf,
			z.comptecommunal as comptecommunal
  	FROM #DBSchema_qgis.geo_parcelle AS p
  		JOIN #DBSchema_qgis.geo_unite_fonciere AS z ON	ST_Contains(z.geom, p.geom)
		ORDER BY z.id,z.comptecommunal
  ) AS uf_parcelle ;

ALTER TABLE #schema_cadastrapp.uf_parcelle OWNER TO #user_cadastrapp;

