-- Create view making link beetween parcelle, owners and lots based on Qgis Models

-- DROP MATERIALIZED VIEW #schema_cadastrapp.co_propriete_parcelle ;

CREATE MATERIALIZED VIEW #schema_cadastrapp.co_propriete_parcelle AS 
	SELECT
    co_propriete_parcelle.lots, 
		co_propriete_parcelle.parcelle,
		co_propriete_parcelle.comptecommunal -- Compte communal du propriétaire
    FROM 
    (
      SELECT  
  			lot.lots,
  			lot.parcelle,
  			lot.comptecommunal
		  FROM #DBSchema_qgis.lots lot
    ) AS co_propriete_parcelle ;

ALTER TABLE #schema_cadastrapp.co_propriete_parcelle OWNER TO #user_cadastrapp;
