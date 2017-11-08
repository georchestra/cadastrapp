--- Create lot Views based on Arcopole Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.lot AS 
	SELECT 
		lot.codlot,
		lot.id_local, 
		lot.dnulot, 
		lot.dnumql, 
		lot.ddenql
  	FROM dblink('host=#DBHost_arcopole port=#DBPort_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
  		'select distinct
  				l.codlot,
				l.invloc as id_local,
				ltrim(l.dnulot, ''0'') as dnulot,
				ltrim(l.dnumql, ''0'') as dnumql,
				ltrim(l.ddenql, ''0'') as ddenql
			from #DBSchema_arcopole.dgi_lotloc l 
			'::text) 
	lot(
		codlot character varying(255), 
		id_local character varying(19), 
		dnulot character varying(7), 
		dnumql character varying(7), 
		ddenql character varying(7));

ALTER TABLE #schema_cadastrapp.lot OWNER TO #user_cadastrapp;

