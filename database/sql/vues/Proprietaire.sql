-- Create view proprietaire based on Qgis Models

--DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.proprietaire ;

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietaire AS 
	SELECT
		proprietaire.id_proprietaire,
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
		proprietaire.ccodro,
		proprietaire.ccodro_lib,
		proprietaire.ccoqua,
		proprietaire.ccoqua_lib,
		proprietaire.ccogrm,
		proprietaire.ccogrm_lib,
		proprietaire.ccodem,
		proprietaire.ccodem_lib,
		proprietaire.dnatpr,
		proprietaire.dnatpr_lib
	FROM
	(
		SELECT 
			pqgis.proprietaire as id_proprietaire,
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
			REPLACE(rtrim(pqgis.ddenom),'/',' ') as ddenom,
			pqgis.gtyp3,
			pqgis.gtyp4,
			pqgis.gtyp5,
			pqgis.gtyp6,
			rtrim(pqgis.dlign3) as dlign3,
			rtrim(ltrim(pqgis.dlign4, '0')) as dlign4,
			rtrim(pqgis.dlign5) as dlign5,
			rtrim(pqgis.dlign6) as dlign6,
			pqgis.ccopay,
			pqgis.ccodep1a2,
			pqgis.ccodira,
			pqgis.ccocom_adr,
			pqgis.ccovoi,
			pqgis.ccoriv,
			ltrim(pqgis.dnvoiri, '0') as dnvoiri,
			pqgis.dindic,
			pqgis.ccopos,
			pqgis.dnirpp,
			pqgis.dqualp,
			rtrim(pqgis.dnomlp) as dnomlp,
			rtrim(pqgis.dprnlp) as dprnlp,
			COALESCE(pqgis.jdatnss, '') as jdatnss,
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
				WHEN gtoper = '1' THEN COALESCE(rtrim(dqualp),'')||' '||COALESCE(rtrim(dnomus),'')||' '||COALESCE(rtrim(dprnus),'')
				WHEN gtoper = '2' THEN trim(ddenom)
			END AS app_nom_usage,
			CASE
				WHEN gtoper = '1' THEN COALESCE(rtrim(dqualp),'')||' '||REPLACE(rtrim(ddenom),'/',' ')
				WHEN gtoper = '2' THEN trim(ddenom)
			END AS app_nom_naissance,
			prop_ccodro.ccodro,
			prop_ccodro.ccodro_lib,
			prop_ccoqua.ccoqua,
			prop_ccoqua.ccoqua_lib,
			prop_ccogrm.ccogrm,
			prop_ccogrm.ccogrm_lib,
			prop_ccodem.ccodem,
			prop_ccodem.ccodem_lib,
			prop_dnatpr.dnatpr,
			prop_dnatpr.dnatpr_lib
		FROM #DBSchema_qgis.proprietaire pqgis
		LEFT JOIN #DBSchema_qgis.ccodro prop_ccodro ON pqgis.ccodro = prop_ccodro.ccodro
		LEFT JOIN #DBSchema_qgis.ccoqua prop_ccoqua ON pqgis.ccoqua = prop_ccoqua.ccoqua
		LEFT JOIN #DBSchema_qgis.ccogrm prop_ccogrm ON pqgis.ccogrm = prop_ccogrm.ccogrm
		LEFT JOIN #DBSchema_qgis.ccodem prop_ccodem ON pqgis.ccodem = prop_ccodem.ccodem
		LEFT JOIN #DBSchema_qgis.dnatpr prop_dnatpr ON pqgis.dnatpr = prop_dnatpr.dnatpr
	) AS proprietaire ;


ALTER TABLE #schema_cadastrapp.proprietaire OWNER TO #user_cadastrapp;

