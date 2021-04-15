-- Create view proprietaire based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietaire AS 
	SELECT proprietaire.id_proprietaire, 
		proprietaire.dnupro, -- Compte communal
		proprietaire.lot,
		proprietaire.dnulp, -- Numéro de libellé partiel
		proprietaire.ccocif, -- Code CDIF - not use
		proprietaire.dnuper, -- Numéro de personne MAJIC - not use
		proprietaire.ccodro_c, -- Code du droit réel ou particulier - not use
		proprietaire.ccodem_c, -- Code du démembrement/indivision - not use
		proprietaire.gdesip, -- Indicateur du destinataire de l'avis d'imposition - not use
		proprietaire.gtoper, -- Indicateur de personne pyshique ou morale - not use
		proprietaire.ccoqua_c, -- not use
		proprietaire.dnatpr_c,
		proprietaire.ccogrm_c, 
		proprietaire.dsglpm, -- Sigle de personne morale - not use
		proprietaire.dforme, -- Forme juridique abrégée MAJIC - not use
		proprietaire.ddenom, -- dénomination de la personne physique  « nom/prénoms »
		proprietaire.gtyp3, -- Adresse à l’étranger avec codification du pays à la donnée CCOPAY - not use
		proprietaire.gtyp4, -- Aadresse à l’étranger, sans codification du pays - not use
		proprietaire.gtyp5, -- Adresse incodifiable - not use
		proprietaire.gtyp6, -- Adresse cedex - not use
		proprietaire.dlign3, -- 3eme ligne d’adresse
		proprietaire.dlign4, -- 4eme ligne d’adresse
		proprietaire.dlign5, -- 5eme ligne d’adresse
		proprietaire.dlign6, -- 6eme ligne d’adresse
		proprietaire.ccopay, -- Code de pays étranger et TOM - not use 
		proprietaire.ccodep1a2, -- Code département de l’adresse - not use 
		proprietaire.ccodira, -- Code commune de l’adresse - not use 
		proprietaire.ccocom_adr, -- Code commune INSEE de l’adresse - not use
		proprietaire.ccovoi, -- -- Code majic2 de la voie - only use by other scripts - not use 
		proprietaire.ccoriv, -- Code rivoli de la voie -
		proprietaire.dnvoiri, -- numéro de voirie -
		proprietaire.dindic, -- indice de répétition de voirie
		proprietaire.ccopos, -- Code postal - not use
		proprietaire.dnirpp, -- not use
		proprietaire.dqualp, -- Qualité abrégée - M, MME ou MLE
		proprietaire.dnomlp, -- Nom d’usage -
		proprietaire.dprnlp, -- Prénoms associés au nom d’usage
		proprietaire.jdatnss, -- date de naissance - sous la forme jj/mm/aaaa
		proprietaire.dldnss, -- lieu de naissance
		proprietaire.epxnee, -- mention du complément - EPX ou NEE si complément Mise à blanc version 2016 - not use
		proprietaire.dnomcp, -- Nom complément - Mise à blanc v2016
		proprietaire.dprncp, -- Prénoms associés au complément
		proprietaire.dnomus, -- Nom d'usage (Depuis 2015)
		proprietaire.dprnus, -- Prénom d'usage (Depuis 2015)
		proprietaire.dformjur, -- Forme juridique
		proprietaire.dsiren, -- numéro siren - not use
		proprietaire.cgocommune, 
		proprietaire.comptecommunal, 
		proprietaire.app_nom_usage,
		proprietaire.app_nom_naissance,
		prop_ccodro.ccodro, -- code du droit réel ou particulier - Nouveau code en 2009 : C (fiduciaire)
		prop_ccodro.ccodro_lib, 
		prop_ccoqua.ccoqua, -- Code qualité de personne physique - 1, 2 ou 3
		prop_ccoqua.ccoqua_lib, 
		prop_ccogrm.ccogrm, -- not use - only by other scripts
		prop_ccogrm.ccogrm_lib, -- not use
		prop_ccodem.ccodem, -- code du démembrement/indivision - C S L I V
		prop_ccodem.ccodem_lib, 
		prop_dnatpr.dnatpr, -- Code nature de personne physique ou morale - Voir $ 2.2.7 - only by other scripts
		prop_dnatpr.dnatpr_lib -- not use
	FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
		'select 
			pqgis.proprietaire,
			pqgis.dnupro,
			pqgis.lot,
			pqgis.dnulp,
			pqgis.ccocif,
			pqgis.dnuper,
			pqgis.ccodro as ccodro_c,
			pqgis.ccodem as ccodem_c,
			pqgis.gdesip,
			pqgis.gtoper,
			pqgis.ccoqua as ccoqua_c,
			pqgis.dnatpr as dnatpr_c,
			pqgis.ccogrm as ccogrm_c,
			pqgis.dsglpm,
			pqgis.dforme,
			REPLACE(rtrim(pqgis.ddenom),''/'','' '') as ddenom,
			pqgis.gtyp3,
			pqgis.gtyp4,
			pqgis.gtyp5,
			pqgis.gtyp6,
			rtrim(pqgis.dlign3) as dlign3,
			rtrim(ltrim(pqgis.dlign4, ''0'')) as dlign4,
			rtrim(pqgis.dlign5) as dlign5,
			rtrim(pqgis.dlign6) as dlign6,
			pqgis.ccopay,
			pqgis.ccodep1a2,
			pqgis.ccodira,
			pqgis.ccocom_adr,
			pqgis.ccovoi,
			pqgis.ccoriv,
			ltrim(pqgis.dnvoiri, ''0'') as dnvoiri,
			pqgis.dindic,
			pqgis.ccopos,
			pqgis.dnirpp,
			pqgis.dqualp,
			rtrim(pqgis.dnomlp) as dnomlp,
			rtrim(pqgis.dprnlp) as dprnlp,
			COALESCE(pqgis.jdatnss, '''') as jdatnss,
			rtrim(pqgis.dldnss) as dldnss,
			pqgis.epxnee,
			rtrim(pqgis.dnomcp) as dnomcp,
			rtrim(pqgis.dprncp) as dprncp,
			rtrim(pqgis.dnomus) as dnomus,
			rtrim(pqgis.dprnus) as dprnus,
			pqgis.dformjur,
			pqgis.dsiren,
			pqgis.ccodep || pqgis.ccodir || pqgis.ccocom as cgocommune,
			pqgis.comptecommunal, 
			CASE
				WHEN gtoper = ''1'' THEN COALESCE(rtrim(dqualp),'''')||'' ''||COALESCE(rtrim(dnomus),'''')||'' ''||COALESCE(rtrim(dprnus),'''')
				WHEN gtoper = ''2'' THEN rtrim(ddenom)
			END AS app_nom_usage,
			CASE
				WHEN gtoper = ''1'' THEN COALESCE(rtrim(dqualp),'''')||'' ''||REPLACE(rtrim(ddenom),''/'','' '')
			END AS app_nom_naissance
		from #DBSchema_qgis.proprietaire pqgis'::text)
	proprietaire(
		id_proprietaire character varying(20), 
		dnupro character varying(6), 
		lot character varying, 
		dnulp character varying(2), 
		ccocif character varying(4), 
		dnuper character varying(6), 
		ccodro_c character varying(1), 
		ccodem_c character varying(1), 
		gdesip character varying(1), 
		gtoper character varying(1), 
		ccoqua_c character varying(1), 
		dnatpr_c character varying(3), 
		ccogrm_c character varying(2), 
		dsglpm character varying(10), 
		dforme character varying(7), 
		ddenom character varying(60), 
		gtyp3 character varying(1), 
		gtyp4 character varying(1), 
		gtyp5 character varying(1), 
		gtyp6 character varying(1), 
		dlign3 character varying(30), 
		dlign4 character varying(36), 
		dlign5 character varying(30), 
		dlign6 character varying(32), 
		ccopay character varying(3), 
		ccodep1a2 character varying(2), 
		ccodira character varying(1), 
		ccocom_adr character varying(3), 
		ccovoi character varying(5), 
		ccoriv character varying(4), 
		dnvoiri character varying(4),
		dindic character varying(1), 
		ccopos character varying(5), 
		dnirpp character varying(10), 
		dqualp character varying(3), 
		dnomlp character varying(30), 
		dprnlp character varying(15), 
		jdatnss character varying(10), 
		dldnss character varying(58), 
		epxnee character varying(3), 
		dnomcp character varying(30), 
		dprncp character varying(15), 
		dnomus character varying(60),
		dprnus character varying(40),
		dformjur character varying(4), 
		dsiren character varying(10),
		cgocommune character varying(6), 
		comptecommunal character varying(15),
		app_nom_usage character varying(120),
		app_nom_naissance character varying(70))
	LEFT JOIN #schema_cadastrapp.prop_ccodro ON proprietaire.ccodro_c::text = prop_ccodro.ccodro::text
	LEFT JOIN #schema_cadastrapp.prop_ccoqua ON proprietaire.ccoqua_c::text = prop_ccoqua.ccoqua::text
	LEFT JOIN #schema_cadastrapp.prop_ccogrm ON proprietaire.ccogrm_c::text = prop_ccogrm.ccogrm::text
	LEFT JOIN #schema_cadastrapp.prop_ccodem ON proprietaire.ccodem_c::text = prop_ccodem.ccodem::text
	LEFT JOIN #schema_cadastrapp.prop_dnatpr ON proprietaire.dnatpr_c::text = prop_dnatpr.dnatpr::text;

ALTER TABLE #schema_cadastrapp.proprietaire OWNER TO #user_cadastrapp;

COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.proprietaire IS 'Propriétaire';

COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnupro IS 'Compte communal ';


