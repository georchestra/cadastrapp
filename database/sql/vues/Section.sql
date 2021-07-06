-- Create view section based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.section AS 
	SELECT 
		cgocommune, -- Code INSEE de la commune
		ccosec, -- Section cadastrale
		ccopre -- Préfixe de section ou quartier servi pour les communes associées
	FROM 
	(
		SELECT 
			distinct ccodep||ccodir||ccocom as cgocommune,
			ltrim(ccosec) as ccosec,
			ltrim(ccopre) as ccopre
		FROM #DBSchema_qgis.parcelle
	) AS parcelle ;
	
ALTER TABLE #schema_cadastrapp.section OWNER TO #user_cadastrapp;
