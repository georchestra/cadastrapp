-- Create view making link beetween parcelle and owners based on Qgis Models

-- DROP MATERIALIZED VIEW #schema_cadastrapp.proprietaire_parcelle ;

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietaire_parcelle AS 
	SELECT
		proprietaire_parcelle.parcelle, -- parcelle
		proprietaire_parcelle.comptecommunal -- compte communal
	FROM 
	(
		SELECT 
			p.parcelle,
			p.comptecommunal
		FROM #DBSchema_qgis.parcelle p
	) AS proprietaire_parcelle ;

ALTER TABLE #schema_cadastrapp.proprietaire_parcelle OWNER TO #user_cadastrapp;
