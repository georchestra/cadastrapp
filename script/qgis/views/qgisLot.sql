-- Create lot Views based on Qgis Models

DROP VIEW cadastrapp_qgis.lot;

CREATE OR REPLACE VIEW #schema_cadastrapp.lot AS 
	SELECT	lot.id_local, 
			lot.dnulot, 
			lot.dnumql, 
			lot.ddenql
		FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
 	 		'select distinct
				l.local10 as id_local,
				l.dnulot as dnulot,
				l.dnumql as dnumql,
				l.ddenql as ddenql
			from public.lotslocaux l where ddenql not like ''0000000''
			'::text) 
		lot(
			id_local character varying(14), 
			dnulot character varying(15), 
			dnumql character varying(7), 
			ddenql character varying(7));

ALTER TABLE #schema_cadastrapp.lot OWNER TO cadastrapp;