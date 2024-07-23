-- Create view proprietebatie based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietebatie AS 
	SELECT proprietebatie.id_local, -- Identifiant local
		proprietebatie.comptecommunal, -- Compte communal -- différence avec comptecommunal ?
		proprietebatie.dnupro, --  Compte communal du propriétaire de la parcelle
		proprietebatie.cgocommune, -- Code commune INSEE
		proprietebatie.ccopre, -- Préfixe
		proprietebatie.ccosec, -- Section
		proprietebatie.dnupla, -- Numéro de plan
		proprietebatie.jdatat, -- Date de l'acte de mutation
		proprietebatie.voie, -- Voie
		proprietebatie.dnvoiri, -- Numéro de voirie
		proprietebatie.dindic, -- Indice de répétition
		proprietebatie.natvoi, --Nature voie
		proprietebatie.ccovoi, -- Code MAJIC voie
		proprietebatie.dvoilib, -- Libellée voie
		proprietebatie.ccoriv, -- Code Rivoli voie
		proprietebatie.dnubat, -- Lettre bâtiment
		proprietebatie.descr, -- Numéro d’entrée -- champ DESC et non DESCR dans fichier 2018
		proprietebatie.dniv, -- Niveau étage
		proprietebatie.dpor, -- Numéro de loca
		proprietebatie.invar, -- Numéro invariant du local
		proprietebatie.ccoaff, -- Affectation de la PEV
		proprietebatie.ccoeva, -- Code évaluation
		proprietebatie.cconlc, -- Code nature de local
		proprietebatie.dcapec, -- Catégorie de classement cadastral
		proprietebatie.ccolloc, -- Code de collectivité locale accordant l’exonération
		proprietebatie.gnextl, -- Nature d’exonération temporaire
		proprietebatie.jandeb, -- Année de début d’exonération
		proprietebatie.janimp, -- Année de retour à imposition 
		proprietebatie.gtauom, -- Zone de ramassage des ordures ménagères
		proprietebatie.jannat, -- Année de construction
		proprietebatie.revcad, -- Revenu cadastral
		proprietebatie.rcexba2, -- Revenu cadastral exonéré
		proprietebatie.rcbaia_tse,
		proprietebatie.rcbaia_com,
		proprietebatie.rcbaia_dep,
		proprietebatie.rcbaia_gp,
		proprietebatie.pexb, -- Taux d'exonération accordée
		proprietebatie.parcelle,
		proprietebatie.ccocac -- Code catégorie du local 
	FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,  
		'select 
			l.local00 as id_local,
			c.comptecommunal,
			c.dnupro,
			c.ccodep || c.ccodir ||	c.ccocom as cgocommune,
			ltrim(l.ccopre) as ccopre,
			ltrim(l.ccosec) as ccosec,
			ltrim(l.dnupla, ''0'') as dnupla,
			COALESCE(l.jdatat, '''') as jdatat,
			v.voie,
			ltrim(l.dnvoiri, ''0'') as dnvoiri,
			l00.dindic,
			v.natvoi,
			l.ccovoi,
			rtrim(v.libvoi) as dvoilib,
			l00.ccoriv,
			l00.dnubat,
			l00.descr,
			l00.dniv,
			l00.dpor,
			l00.invar,
			pev.ccoaff,
			l.ccoeva,
			cconlc.cconlc_lib as cconlc,
			pev.dcapec,
			pevx.ccolloc,
			pevx.gnextl,
			pevx.jandeb,
			pevx.janimp,
			l.gtauom,
			l.jannat,
			( CASE WHEN pev.dvlpera::text <> '''' THEN ROUND(CEIL(CAST(pev.dvlpera AS NUMERIC)/2),2) END ) AS revcad,
			ROUND(CAST(pevx.rcexba2 AS numeric),2) as rcexba2,
			ROUND(CAST(pevtax.tse_bipevla AS numeric),2) as rcbaia_tse,
			ROUND(CAST(pevtax.co_bipevla AS numeric),2) as rcbaia_com,
			ROUND(CAST(pevtax.de_bipevla AS numeric),2) as rcbaia_dep,
			ROUND(CAST(pevtax.gp_bipevla AS numeric),2) as rcbaia_gp,
			pevx.pexb,
			l.parcelle,
			pev.ccocac
		from #DBSchema_qgis.comptecommunal c
			left join #DBSchema_qgis.local10 as l on c.comptecommunal=l.comptecommunal
			left join #DBSchema_qgis.local00 as l00 on l00.local00=l.local00
			left join #DBSchema_qgis.voie as v on  l.ccodep||l.ccodir||l.ccocom||l.ccoriv=v.ccodep||v.ccodir||v.ccocom||v.ccoriv
			left join #DBSchema_qgis.pev  on pev.local10=l.local10
			left join #DBSchema_qgis.pevexoneration_imposable as pevx on pevx.pevexoneration_imposable=pev.pev
			left join #DBSchema_qgis.pevtaxation as pevtax on pevtax.pev=pev.pev
			left join #DBSchema_qgis.cconlc on cconlc.cconlc = l.cconlc
		order by l.parcelle,l00.ccoriv,v.libvoi,l00.dnubat,l00.descr,l00.dniv,l00.dpor'::text) 
	proprietebatie(
		id_local character varying(17),
		comptecommunal character varying(15), 
		dnupro character varying(6), 
		cgocommune character varying(6), 
		ccopre character varying(3), 
		ccosec character varying(2), 
		dnupla character varying(4), 
		jdatat character varying(10), 
		voie character varying(19), 
		dnvoiri character varying(4), 
		dindic character varying(1), 
		natvoi character varying(4), 
		ccovoi character varying(5), 
		dvoilib character varying(26),
		ccoriv character varying(4), 
		dnubat character varying(2), 
		descr character varying(2), 
		dniv character varying(2), 
		dpor character varying(5), 
		invar character varying(10),
  		ccoaff character varying(1), 
  		ccoeva character varying(1), 
  		cconlc character varying(255), 
  		dcapec character varying(2), 
  		ccolloc character varying(2), 
		gnextl character varying(2), 
		jandeb character varying(4), 
		janimp character varying(4),
		gtauom character varying(2),
		jannat character varying(4),
		revcad numeric(10,2),
		rcexba2 numeric(10,2),
		rcbaia_tse numeric(10,2),
		rcbaia_com numeric(10,2),
		rcbaia_dep numeric(10,2),
		rcbaia_gp numeric(10,2),
		pexb numeric,
		parcelle  character varying(19),
		ccocac character varying(4));

ALTER TABLE #schema_cadastrapp.proprietebatie OWNER TO #user_cadastrapp;
