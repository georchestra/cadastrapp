-- Create views deschabitation, descproffessionnel, descdependance based on Qgis Models

--DROP MATERIALIZED VIEW #schema_cadastrapp.deschabitation ;
--DROP MATERIALIZED VIEW #schema_cadastrapp.descproffessionnel ;
--DROP MATERIALIZED VIEW #schema_cadastrapp.descdependance ;


CREATE MATERIALIZED VIEW #schema_cadastrapp.deschabitation AS
	SELECT 
		deschabitation.pev, -- -- Partie d'évaluation
		deschabitation.annee, -- Année
		deschabitation.invar, -- Numéro invariant du local
		deschabitation.dnupev, -- Numéro de PEV - not use
		deschabitation.ccoaff, -- Affectation de la PEV 
		deschabitation.dnupev_lib, -- Libellé numéro PEV - not use
		deschabitation.ccoaff_lib, -- Libellé affectation PEV
		deschabitation.dnudes, -- Numéro d’ordre de descriptif
		deschabitation.detent, -- Etat d'entretien
		deschabitation.dsupdc, -- Supérficie des pièces
		deschabitation.dnbniv, -- Nombre de niveaux de la construction
		deschabitation.dnbpdc, -- Nombre de pièces
		deschabitation.dnbppr, -- Nombre de pièces principales
		deschabitation.dnbsam, -- Nombre de salles à manger
		deschabitation.dnbcha, -- Nombre de chambres
		deschabitation.dnbcu8, -- Nombre cuisines de moins de 9m2
		deschabitation.dnbcu9, -- Nombre de cuisines d'au moins 9m2
		deschabitation.dnbsea, -- nombre de salle d'eau
		deschabitation.dnbann, -- Nombre de pièces annexes
		deschabitation.dnbbai, -- Nombre de baignoires
		deschabitation.dnbdou, -- Nombre de douches
		deschabitation.dnblav, -- Nombre de lavabo
		deschabitation.dnbwc, -- Nombre de WC
		deschabitation.geaulc, -- Présence eau
		deschabitation.gelelc, -- Présence éléctricité
		deschabitation.ggazlc, -- Présence gaz
		deschabitation.gchclc, -- Présence chauffage central
		deschabitation.gteglc, -- Présence tout à l'égout
		deschabitation.gesclc, -- Présence escalier de service
		deschabitation.gasclc, -- Présence ascenseur
		deschabitation.gvorlc, -- Présence vide-ordures
		deschabitation.cconad_ga, -- not use
		deschabitation.cconav_cv, -- not use
		deschabitation.cconad_gr, -- not use
		deschabitation.cconav_tr, -- not use
		deschabitation.dsueic_ga, -- not use
		deschabitation.dsueic_cv, -- not use
		deschabitation.dsueic_gr, -- not use
		deschabitation.dsueic_tr, -- not use
		deschabitation.dmatgm, -- Matériaux des gros murs
		deschabitation.dmatto -- Matériaux des toitures
	FROM 
	(
		SELECT 
			pev.pev,
			pev.annee,
			pev.invar,
			pev.dnupev,
			pev.ccoaff,
			'Partie principale d''habitation' as dnupev_lib,
			ccoaff.ccoaff_lib,
			pevp.dnudes,
			pevp.detent,
			pevp.dsupdc,
			pevp.dnbniv,
			pevp.dnbpdc,
			dnbppr,
			dnbsam,
			dnbcha,
			dnbcu8,
			dnbcu9,
			dnbsea,
			dnbann,
			dnbbai,
			dnbdou,
			dnblav,
			dnbwc,
			geaulc,
			gelelc,
			ggazlc,
			gchclc,
			gteglc,
			gesclc,
			gasclc,
			gvorlc,
			cconad1.cconad_lib as cconad_ga,
			cconad2.cconad_lib as cconav_cv,
			cconad3.cconad_lib as cconad_gr,
			cconad4.cconad_lib as cconav_tr,
			dep1_dsueic  as dsueic_ga,
			dep2_dsueic  as dsueic_cv,
			dep3_dsueic  
			as dsueic_gr,
			dep4_dsueic  as dsueic_tr,
			dmatgm,
			dmatto
		FROM #DBSchema_qgis.pev
			LEFT JOIN #DBSchema_qgis.pevprincipale pevp on pevp.pev=pev.pev
			LEFT JOIN #DBSchema_qgis.ccoaff on pev.ccoaff=ccoaff.ccoaff
			LEFT JOIN #DBSchema_qgis.cconad cconad1 on pevp.dep1_cconad=cconad1.cconad
			LEFT JOIN #DBSchema_qgis.cconad cconad2 on pevp.dep2_cconad=cconad2.cconad
			LEFT JOIN #DBSchema_qgis.cconad cconad3 on pevp.dep3_cconad=cconad3.cconad
			LEFT JOIN #DBSchema_qgis.cconad cconad4 on pevp.dep4_cconad=cconad4.cconad
	) AS deschabitation ;

ALTER TABLE #schema_cadastrapp.deschabitation OWNER TO #user_cadastrapp;


CREATE MATERIALIZED VIEW #schema_cadastrapp.descproffessionnel AS
	SELECT
		descproffessionnel.pev, -- Partie d'évaluation
		descproffessionnel.invar, -- Numéro invariant du local
		descproffessionnel.annee, -- Année
		descproffessionnel.dsupot, -- Surface pondérée
		descproffessionnel.dsup1, -- Surface des parties principales
		descproffessionnel.dsup2, -- Surface des parties secondaires couvertes
		descproffessionnel.dsup3, -- Surface des parties secondaires non couvertes
		descproffessionnel.dsupk1, -- Surface des stationnements couverts
		descproffessionnel.dsupk2, -- Surface des stationnements non couverts
		descproffessionnel.dnudes, --Numéro d’ordre de descriptif
		descproffessionnel.vsurzt, -- Surface réelle totale
		descproffessionnel.ccocac, -- Code catégorie du local
		descproffessionnel.dnutrf, -- Secteur révisé -- not use
		descproffessionnel.dcfloc, -- Coefficient de localisation -- not use
		descproffessionnel.ccortar, -- Code commune origine du tarif -- not use
		descproffessionnel.ccorvl, -- Code réduction du local -- not use
		descproffessionnel.dtaurv, -- Taux de réduction -- not use
		descproffessionnel.dcmloc -- Coefficient de modulation du locals -- not use
	FROM 
	(
		SELECT 
			pvep.pev,
			pvep.invar,
			pvep.annee,
			CAST(pvep.dsupot AS integer),
			CAST(pvep.dsup1 AS integer),
			CAST(pvep.dsup2 AS integer),
			CAST(pvep.dsup3 AS integer),
			CAST(pvep.dsupk1 AS integer),
			CAST(pvep.dsupk2 AS integer),
			'' as dnudes,
			'0' as vsurzt,
			gpev.ccocac,
			gpev.dnutrf,
			gpev.dcfloc,
			gpev.ccortar,
			gpev.ccorvl,
			gpev.dtaurv,
			gpev.dcmloc
		FROM #DBSchema_qgis.pevprofessionnelle as pvep
			LEFT JOIN #DBSchema_qgis.pev as gpev on pvep.pev=gpev.pev
	) AS descproffessionnel ;

ALTER TABLE #schema_cadastrapp.descproffessionnel OWNER TO #user_cadastrapp;



CREATE MATERIALIZED VIEW #schema_cadastrapp.descdependance AS
	SELECT
		descdependance.pev, -- partie d'évaluation
		descdependance.invar, -- numéro invariant du local 
		descdependance.annee, -- année
		descdependance.dnudes, -- Numéro d’ordre de descriptif
		descdependance.cconad_lib, -- Nature de dépendance
		descdependance.dsudep, -- Surface réelle de la dépendance
		descdependance.dnbbai,-- Nombre de baignoires
		descdependance.dnbdou,-- Nombre de douches
		descdependance.dnblav, -- Nombre de lavabos
		descdependance.dnbwc, -- Nombre de WC
		descdependance.geaulc, -- Présence d’eau
		descdependance.gelelc, -- Présence d’électricité
		descdependance.gchclc, -- Présence du chauffage central
		descdependance.dmatgm, -- Matériaux des gros murs
		descdependance.dmatto -- Matériaux des toitures
	FROM 
	(
		SELECT 
			pev,
			invar,
			annee,
			dnudes,
			cconad_lib,
			dsudep,
			dnbbai,
			dnbdou,
			dnblav,
			dnbwc,
			geaulc,
			gelelc,
			gchclc,
			dmatgm,
			dmatto
		FROM #DBSchema_qgis.pevdependances
			LEFT JOIN #DBSchema_qgis.cconad on pevdependances.cconad=cconad.cconad
	) AS descdependance ;

ALTER TABLE #schema_cadastrapp.descdependance OWNER TO #user_cadastrapp;


