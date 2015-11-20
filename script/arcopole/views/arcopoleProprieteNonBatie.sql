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
		proprietenonbatie.bisufad,
		proprietenonbatie.bisufad_dep,
		proprietenonbatie.bisufad_reg
	FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select 
			local.id_local,
			nbati.codparc as parcelle,
			local.dnupro as comptecommunal, 
			nbati.codcomm as cgocommune,
			ltrim(substr(nbati.codparc,7,3), ''0'') as ccopre,
			ltrim(substr(nbati.codparc,10,2), ''0'') as ccosec ,
			COALESCE(ltrim(to_char(nbati.dnupla,''999'')),'''') as dnupla,
			local.jdatat,
			ltrim(invar.dnvoiri, ''0'') as dnvoiri,
			invar.dindic,
			voie.nature as natvoi,
			invar.dvoilib,
			invar.ccoriv,
			nbati.dparpi,
			nbati.GPARNF gpafpd,
			suf.ccostn,
			suf.ccosub,
			suf.cgrnum,
			suf.dsgrpf,
			suf.dclssf,
			suf.cnatsp,
			CAST (suf.dcntsf AS INTEGER) as dcntsf,
			CAST (suf.drcsuba AS INTEGER) as drcsuba,
			nbati.DNUPDL as pdl,
			suf.DNulot,
			nbati.dreflf,
			CAST (taxsuf.majposa AS INTEGER) as majposa,
			CAST (taxsuf.bisufad AS INTEGER) as bisufad,
			CAST (taxsuf.bisufad_dep AS INTEGER) as bisufad_dep,
			CAST (taxsuf.bisufad_reg AS INTEGER) as bisufad_reg
		from #DBSchema_arcopole.dgi_local local
			left join #DBSchema_arcopole.dgi_invar invar on local.id_local=invar.invar
			left join #DBSchema_arcopole.dgi_nbati nbati on invar.codparc=nbati.codparc
			left join #DBSchema_arcopole.dgi_voie voie on voie.id_voie=invar.id_voie
			left join #DBSchema_arcopole.dgi_suf suf on suf.codlot=local.codlot and suf.CODPARC=nbati.CODPARC
			left join #DBSchema_arcopole.dgi_pev pev on pev.codlot=invar.codlot and pev.invar=invar.invar
			left join #DBSchema_arcopole.dgi_taxsuf taxsuf on taxsuf.id_suf=suf.id_suf and taxsuf.CODPARC=suf.CODPARC'::text) 
	proprietenonbatie(
		id_local character varying(16), 
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
		cgrnum character varying(2), 
		dsgrpf character varying(2), 
		dclssf character varying(2), 
		cnatsp character varying(5), 
		dcntsf integer, 
		drcsuba integer, 
		pdl character varying(3), 
		dnulot character varying(7), 
		dreflf character varying(9),
		majposa integer,
		bisufad integer,
		bisufad_dep integer,
		bisufad_reg integer);

ALTER TABLE #schema_cadastrapp.proprietenonbatie OWNER TO #user_cadastrapp;



