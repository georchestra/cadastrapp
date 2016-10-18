-- Create view proprietenonbatiesufexo based on Arcopole Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatiesufexo AS 
	SELECT 
		proprietenonbatiesufexo.parcelle,
		proprietenonbatiesufexo.id_local, 
		proprietenonbatiesufexo.comptecommunal, 
		proprietenonbatiesufexo.cgocommune,
		proprietenonbatiesufexo.ccolloc, 
		proprietenonbatiesufexo.gnexts, 
		proprietenonbatiesufexo.jandeb, 
		proprietenonbatiesufexo.jfinex,  
		proprietenonbatiesufexo.rcexnba,
		proprietenonbatiesufexo.fcexn,
		proprietenonbatiesufexo.pexn
	FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select 
			nbati.codparc as parcelle,
			suf.id_suf as id_local,
			nbati.dnupro as comptecommunal, 
			nbati.codcomm as cgocommune,
			exosuf.ccolloc,
			exosuf.gnexts,
			exosuf.jandeb,
			exosuf.jfinex,
			CAST (exosuf.rcexnba AS NUMERIC) as rcexnba,
			exosuf.vecexn as fcexn,
			CAST (exosuf.pexn AS INTEGER) as pexn
		from #DBSchema_arcopole.dgi_nbati nbati
			left join #DBSchema_arcopole.dgi_suf suf on nbati.codparc=suf.codparc
			left join #DBSchema_arcopole.dgi_exosuf exosuf on suf.id_suf=exosuf.id_suf'::text) 
	proprietenonbatiesufexo(
		parcelle character varying(19), 
		id_local character varying(17), 
		comptecommunal character varying(12), 
		cgocommune character varying(6), 
		ccolloc character varying(2), 
		gnexts character varying(2), 
		jandeb character varying(4), 
		jfinex character varying(4), 
		rcexnba numeric(10,2),
		fcexn character varying(9),
		pexn integer);

ALTER TABLE #schema_cadastrapp.proprietenonbatiesufexo OWNER TO #user_cadastrapp;



