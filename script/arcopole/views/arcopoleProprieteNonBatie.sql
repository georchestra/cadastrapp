-- Create view proprietenonbatie based on Arcopole Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatie AS 
	SELECT 
		proprietenonbatie.id_local, 
		proprietenonbatie.parcelle,
		proprietenonbatie.comptecommunal, 
		proprietenonbatie.cgocommune,
		proprietenonbatie.ccopre, 
		proprietenonbatie.ccosec, 
		proprietenonbatie.dnupla, 
		proprietenonbatie.jdatat, 
		proprietenonbatie.dnvoiri, 
		proprietenonbatie.dindic, 
		proprietenonbatie.natvoi, 
		proprietenonbatie.dvoilib, 
		proprietenonbatie.ccoriv, 
		proprietenonbatie.dparpi, 
		proprietenonbatie.gpafpd, 
		proprietenonbatie.ccostn, 
		proprietenonbatie.ccosub, 
		proprietenonbatie.cgrnum, 
		proprietenonbatie.dsgrpf, 
		proprietenonbatie.dclssf, 
		proprietenonbatie.cnatsp, 
		proprietenonbatie.dcntsf, 
		proprietenonbatie.drcsuba, 
		proprietenonbatie.pdl, 
		proprietenonbatie.dnulot, 
		proprietenonbatie.dreflf,
		proprietenonbatie.majposa,
		proprietenonbatie.bisufad_com,
		proprietenonbatie.bisufad_dep,
		proprietenonbatie.bisufad_gp
	FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select 
			suf.id_suf as id_local,
			nbati.codparc as parcelle,
			nbati.dnupro as comptecommunal, 
			nbati.codcomm as cgocommune,
			ltrim(substr(nbati.codparc,7,3), ''0'') as ccopre,
			ltrim(substr(nbati.codparc,10,2), ''0'') as ccosec ,
			COALESCE(ltrim(to_char(nbati.dnupla,''999'')),'''') as dnupla,
			nbati.jdatat,
			ltrim(nbati.dnvoirie, ''0'') as dnvoiri,
			nbati.dindic,
			'''' as natvoi,
			nbati.dvoilib,
			nbati.ccoriv,
			nbati.dparpi,
			nbati.gparnf as gpafpd,
			suf.ccostn,
			suf.ccosub,
			gnc.description,
			sga.description,
			suf.dclssf,
			cncs.description,
			CAST (suf.dcntsf AS INTEGER) as dcntsf,
			CAST (suf.drcsuba AS NUMERIC) as drcsuba,
			nbati.dnupdl as pdl,
			suf.dnulot,
			nbati.dreflf,
			CAST (taxsuf.majposa AS NUMERIC) as majposa,
			CAST (taxsuf.bisufad AS NUMERIC) as bisufad_com,
			CAST (taxsuf.bisufad_dep AS NUMERIC) as bisufad_dep,
			CAST (taxsuf.bisufad_gcom AS NUMERIC) as bisufad_gp
		FROM #DBSchema_arcopole.dgi_nbati nbati
			left join #DBSchema_arcopole.dgi_suf suf on nbati.codparc=suf.codparc
			left join #DBSchema_arcopole.dgi_taxsuf taxsuf on taxsuf.id_suf=suf.id_suf
			left join #DBSchema_arcopole.dgi_voie voie on voie.id_voie=nbati.id_voie
			left join #DBSchema_arcopole.dom_cgrnum as gnc on gnc.code=suf.cgrnum
			left join #DBSchema_arcopole.dom_dsgrpf as sga on sga.code=suf.dsgrpf
			left join #DBSchema_arcopole.dom_cnatsp as cncs on cncs.code=suf.cnatsp'::text)
	proprietenonbatie(
		id_local character varying(17), 
		parcelle character varying(19), 
		comptecommunal character varying(12), 
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
		cgrnum character varying, 
		dsgrpf character varying, 
		dclssf character varying(2), 
		cnatsp character varying, 
		dcntsf integer, 
		drcsuba numeric(10,2),
		pdl character varying(3), 
		dnulot character varying(7), 
		dreflf character varying(9),
 		majposa numeric(10,2),
		bisufad_com numeric(10,2),
		bisufad_dep numeric(10,2),
		bisufad_gp numeric(10,2));

ALTER TABLE #schema_cadastrapp.proprietenonbatie OWNER TO #user_cadastrapp;



