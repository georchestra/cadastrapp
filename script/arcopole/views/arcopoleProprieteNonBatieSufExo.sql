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
		proprietenonbatiesufexo.fcexb,
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
			CAST (exosuf.rcexnba AS INTEGER) as fcexb,
			CAST (exosuf.pexn AS INTEGER) as pexn
		from #DBSchema_arcopole.dgi_local local
			left join #DBSchema_arcopole.dgi_invar invar on local.id_local=invar.invar
			left join #DBSchema_arcopole.dgi_nbati nbati on invar.codparc=nbati.codparc
			left join #DBSchema_arcopole.dgi_suf suf on suf.codlot=local.codlot and suf.CODPARC=nbati.CODPARC
			left join #DBSchema_arcopole.dgi_exosuf exosuf on exosuf.id_suf=suf.id_suf and exosuf.CODPARC=suf.CODPARC'::text) 
	proprietenonbatiesufexo(
		id_local character varying(16), 
		parcelle character varying(19), 
		comptecommunal character varying(12), 
		dnupro character varying(12), 
		cgocommune character varying(6), 
		ccopre text, 
		ccosec text, 
		dnupla character varying(4), 
		jdatat character varying(8), 
		dnvoiri character varying(4), 
		dindic character varying(1), 
		natvoi character varying(4), 
		dvoilib character varying(30), 
		ccoriv character varying(4), 
		dparpi character varying(4), 
		gpafpd character varying(1), 
		ccostn character varying(1), 
		ccosub character varying(2), 
		cgrnum character varying(2), 
		dsgrpf character varying(2), 
		dclssf character varying(2), 
		cnatsp character varying(5), 
		dcntsf integer, 
		drcsuba integer, 
		pdl character varying(3), 
		dnulot character varying(7), 
		ccolloc character varying(2), 
		gnextl character varying(2), 
		jandeb character varying(4), 
		janimp character varying(4), 
		dreflf character varying(9),
		fcexb character varying(10),
		pexn integer,
		majposa integer,
		bisufad integer,
		bisufad_dep integer,
		bisufad_reg integer,
		rcexnba integer);

ALTER TABLE #schema_cadastrapp.proprietenonbatiesufexo OWNER TO #user_cadastrapp;



