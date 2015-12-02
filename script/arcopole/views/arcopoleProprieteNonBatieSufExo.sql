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
			local.id_local,
			local.dnupro as comptecommunal, 
			nbati.codcomm as cgocommune,
			exosuf.ccolloc,
			exosuf.gnexts,
			exosuf.jandeb,
			exosuf.jfinex,
			CAST (exosuf.rcexnba AS INTEGER) as rcexnba,
			exosuf.vecexn as fcexn,
			CAST (exosuf.pexn AS INTEGER) as pexn
		from #DBSchema_arcopole.dgi_local local
			left join #DBSchema_arcopole.dgi_invar invar on local.id_local=invar.invar
			left join #DBSchema_arcopole.dgi_nbati nbati on invar.codparc=nbati.codparc
			left join #DBSchema_arcopole.dgi_suf suf on suf.codlot=local.codlot and suf.CODPARC=nbati.CODPARC
			left join #DBSchema_arcopole.dgi_exosuf exosuf on exosuf.id_suf=suf.id_suf and exosuf.CODPARC=suf.CODPARC'::text) 
	proprietenonbatiesufexo(
		parcelle character varying(19), 
		id_local character varying(16), 
		comptecommunal character varying(12), 
		cgocommune character varying(6), 
		ccolloc character varying(2), 
		gnexts character varying(2), 
		jandeb character varying(4), 
		jfinex character varying(4), 
		rcexnba integer,
		fcexn character varying(9),
		pexn integer);

ALTER TABLE #schema_cadastrapp.proprietenonbatiesufexo OWNER TO #user_cadastrapp;



