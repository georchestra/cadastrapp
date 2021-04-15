-- Create lot Views based on Qgis Models

--DROP MATERIALIZED VIEW #schema_cadastrapp.lot ;

CREATE MATERIALIZED VIEW #schema_cadastrapp.lot AS 
	SELECT
		lot.id_local, -- Identifiant du local
		lot.dnulot, -- Numéro du lot - Le lot de BND se présente sous la forme 00Axxxx
		lot.dnumql, -- Numérateur du lot
		lot.ddenql -- Dénominateur du lot
	FROM 
	(
		SELECT 
			distinct l.local10 as id_local,
			ltrim(l.dnulot, '0') as dnulot,
			ltrim(l.dnumql, '0') as dnumql,
			ltrim(l.ddenql, '0') as ddenql
		FROM #DBSchema_qgis.lotslocaux l
	) AS lot ;

ALTER TABLE #schema_cadastrapp.lot OWNER TO #user_cadastrapp;
