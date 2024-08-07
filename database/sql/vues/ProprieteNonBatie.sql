-- Create view proprietenonbatie based on Qgis Models

-- DROP MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatie ;

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatie AS 
	SELECT 
		proprietenonbatie.id_local, -- Identifiant du local
		proprietenonbatie.jdatat,  -- Date de l'acte
		proprietenonbatie.comptecommunal, -- Compte communal
		proprietenonbatie.cgocommune,  -- Code comme INSEE
		proprietenonbatie.ccopre, -- Préfixe de section ou quartier servi pour les communes associées
		proprietenonbatie.ccosec, -- Section cadastrale
		proprietenonbatie.dnupla, -- Numéro de plan
		proprietenonbatie.parcelle, -- Parcelle
		proprietenonbatie.ccovoi, -- Code MAJIC2 de la voie
		proprietenonbatie.voie,
		proprietenonbatie.ccoriv, -- Code Rivoli de la voie
		proprietenonbatie.dnvoiri, -- Numéro de voirie
		proprietenonbatie.dindic, -- Indice de répétition de voirie
		proprietenonbatie.natvoi, -- Nature de la voie
		proprietenonbatie.cconvo,  -- Code nature de la voie
		proprietenonbatie.dvoilib, -- Libellé de la voie
		proprietenonbatie.dparpi, -- Numéro de parcelle primitive
		proprietenonbatie.gparnf, -- indicateur de parcelle non figurée au plan
		proprietenonbatie.ccostn, -- Série tarif
		proprietenonbatie.ccosub, -- lettres indicatives de subdivision fiscale
		proprietenonbatie.cgrnum, -- Groupe de nature de culture
		proprietenonbatie.dsgrpf, -- Sous-groupe alphabétique
		proprietenonbatie.dclssf, -- Classe dans le groupe et la série tarif
		proprietenonbatie.cnatsp, -- Code nature de culture spéciale
		proprietenonbatie.dcntsf, -- Contenance de la subdivision fiscale 
		proprietenonbatie.drcsuba, -- Revenu cadastral revalorisé en valeur du 01/01 de l’année
		proprietenonbatie.pdl, -- Propriété divisée en lots
		proprietenonbatie.dnulot, -- Numéro du lot - Le lot de BND se présente sous la forme 00Axxxx
		proprietenonbatie.dreflf, -- Référence livre foncier
		proprietenonbatie.majposa, -- Majoration terrain constructible
		proprietenonbatie.bisufad_com, -- Base d'imposition de la subdivision fiscale - commune
		proprietenonbatie.bisufad_dep, -- Base d'imposition de la subdivision fiscale - département
		proprietenonbatie.bisufad_gp -- Base d'imposition de la subdivision fiscale - groupement communal
	FROM 
	(
		SELECT 
			suf.suf as id_local,
			COALESCE(p.jdatat, '') as jdatat,
			p.comptecommunal,
			p.ccodep || p.ccodir ||	p.ccocom as cgocommune,
			ltrim(p.ccopre) as ccopre,
			ltrim(p.ccosec) as ccosec,
			ltrim(p.dnupla, '0') as dnupla,
			p.parcelle,
			p.ccovoi,
			p.voie,
			p.ccoriv,
			ltrim(p.dnvoiri, '0') as dnvoiri,
			p.dindic,
			v.natvoi,
			p.cconvo,
			rtrim(v.libvoi) as dvoilib,
			p.dparpi,
			p.gparnf,
			suf.ccostn,
			suf.ccosub,
			gnc.cgrnum_lib as cgrnum,
			sga.dsgrpf_lib as dsgrpf,
			suf.dclssf,
			cncs.cnatsp_lib as cnatsp,
			suf.dcntsf,
			suf.drcsuba,
			suf.pdl,
			suf.dnulot,
			p.dreflf,
			suftax.c1majposa as majposa,
			suftax.c1bisufad as bisufad_com,
			suftax.c2bisufad as bisufad_dep,
			suftax.c4bisufad as bisufad_gp
		FROM #DBSchema_qgis.parcelle p
			LEFT JOIN #DBSchema_qgis.voie v on v.ccodep||v.ccodir||v.ccocom||v.ccoriv=p.ccodep||p.ccodir||p.ccocom||p.ccoriv
			LEFT JOIN #DBSchema_qgis.suf on suf.comptecommunal=p.comptecommunal and p.parcelle=suf.parcelle
			LEFT JOIN #DBSchema_qgis.suftaxation as suftax on suftax.suf=suf.suf
			LEFT JOIN #DBSchema_qgis.cgrnum as gnc on gnc.cgrnum = suf.cgrnum
			LEFT JOIN #DBSchema_qgis.dsgrpf as sga on sga.dsgrpf = suf.dsgrpf
			LEFT JOIN #DBSchema_qgis.cnatsp as cncs on cncs.cnatsp = suf.cnatsp
	) AS proprietenonbatie ;

ALTER TABLE #schema_cadastrapp.proprietenonbatie OWNER TO #user_cadastrapp;

