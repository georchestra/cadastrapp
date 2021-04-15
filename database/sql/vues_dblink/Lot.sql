-- Create lot Views based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.lot AS 
	SELECT
		lot.id_local, -- Identifiant du local
		lot.dnulot, -- Numéro du lot - Le lot de BND se présente sous la forme 00Axxxx
		lot.dnumql, -- Numérateur du lot
		lot.ddenql -- Dénominateur du lot
	FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
		'SELECT 
			distinct l.local10 as id_local,
			ltrim(l.dnulot, ''0'') as dnulot,
			ltrim(l.dnumql, ''0'') as dnumql,
			ltrim(l.ddenql, ''0'') as ddenql
		FROM #DBSchema_qgis.lotslocaux l'::text)
	lot(
		id_local character varying(17),
		dnulot character varying(15), 
		dnumql character varying(7), 
		ddenql character varying(7));

ALTER TABLE #schema_cadastrapp.lot OWNER TO #user_cadastrapp;
