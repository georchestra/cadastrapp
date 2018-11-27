-- Create view proprietenonbatie based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatie AS 
	SELECT 
		proprietenonbatie.id_local, 
		proprietenonbatie.jdatat, 
		proprietenonbatie.comptecommunal, 
		proprietenonbatie.cgocommune,  
		proprietenonbatie.ccopre, 
		proprietenonbatie.ccosec,
		proprietenonbatie.dnupla, 
		proprietenonbatie.parcelle, 
		proprietenonbatie.ccovoi, 
		proprietenonbatie.voie, 
		proprietenonbatie.ccoriv, 
		proprietenonbatie.dnvoiri, 
		proprietenonbatie.dindic, 
		proprietenonbatie.natvoi,
		proprietenonbatie.cconvo, 
		proprietenonbatie.dvoilib,
		proprietenonbatie.dparpi, 
		proprietenonbatie.gparnf, 
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
	FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
		'select 
			suf.suf as id_local,
			COALESCE(p.jdatat, '''') as jdatat,
			p.comptecommunal,
			p.ccodep || p.ccodir ||	p.ccocom as cgocommune,
			ltrim(p.ccopre) as ccopre,
			ltrim(p.ccosec) as ccosec,
			ltrim(p.dnupla, ''0'') as dnupla,
			p.parcelle,
			p.ccovoi,
			p.voie,
			p.ccoriv,
			ltrim(p.dnvoiri, ''0'') as dnvoiri,
			p.dindic,
			v.natvoi,
			p.cconvo,
			rtrim(v.libvoi) as dvoilib,
			p.dparpi,
			p.gparnf,
			suf.ccostn,
			suf.ccosub,
			gnc.cgrnum_lib,
			sga.dsgrpf_lib,
			suf.dclssf,
			cncs.cnatsp_lib,
			suf.dcntsf,
			suf.drcsuba,
			suf.pdl,
			suf.dnulot,
			p.dreflf,
			suftax.c1majposa as majposa,
			suftax.c1bisufad as bisufad_com,
			suftax.c2bisufad as bisufad_dep,
			suftax.c4bisufad as bisufad_gp
		from #DBSchema_qgis.parcelle p
			left join #DBSchema_qgis.voie v on v.voie=p.voie
			left join #DBSchema_qgis.suf on suf.comptecommunal=p.comptecommunal and p.parcelle=suf.parcelle
			left join #DBSchema_qgis.suftaxation as suftax on suftax.suf=suf.suf
			left join #DBSchema_qgis.cgrnum as gnc on gnc.cgrnum = suf.cgrnum
			left join #DBSchema_qgis.dsgrpf as sga on sga.dsgrpf = suf.dsgrpf
			left join #DBSchema_qgis.cnatsp as cncs on cncs.cnatsp = suf.cnatsp'::text)
	proprietenonbatie(
		id_local character varying(21),  
		jdatat character varying(10),  
		comptecommunal character varying(15), 
 		cgocommune character varying(6), 
 		ccopre character varying(3),
 		ccosec character varying(2),  
 		dnupla character varying(4), 
 		parcelle character varying(19),
 		ccovoi character varying(5), 
 		voie character varying(19), 
 		ccoriv character varying(4), 
 		dnvoiri character varying(4), 
 		dindic character varying(1), 
 		natvoi character varying(4), 
 		cconvo character varying(4), 
 		dvoilib character varying(26), 
 		dparpi character varying(4), 
 		gparnf character varying(1),
 		ccostn character varying(1), 
 		ccosub character varying(2), 
 		cgrnum character varying,
 		dsgrpf character varying, 
 		dclssf character varying(2), 
 		cnatsp character varying,
 		dcntsf integer, 
 		drcsuba numeric(10,2), 
 		pdl character varying(22), 
 		dnulot character varying(7), 
 		dreflf character varying(5),
 		majposa numeric(10,2),
		bisufad_com numeric(10,2),
		bisufad_dep numeric(10,2),
		bisufad_gp numeric(10,2));
ALTER TABLE #schema_cadastrapp.proprietenonbatie OWNER TO #user_cadastrapp;
