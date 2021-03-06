-- Create view proprietaire based on Qgis Models

COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.proprietaire IS 'Propriétaire';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.id_proprietaire IS 'Identifiant propriétaire';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnupro IS 'Compte communal propriétaire';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.lot IS 'Lot';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnulp IS 'Num libellé partiel';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccocif IS 'Code CDIF';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnuper IS 'Num personne MAJIC';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccodro_c IS 'Code droit réel ou particulier';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccodem_c IS 'Code démembrement/indivision';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.gdesip IS 'Indic destinataire avis imposition';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.gtoper IS 'Indic personne physique/morale ';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccoqua_c IS 'Qualité personne physique';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnatpr_c IS 'Nature personnes physique/morale';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccogrm_c IS 'Groupe personne morale';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dsglpm IS 'Sigle personne morale';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dforme IS 'Forme juridique MAJIC2';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ddenom IS 'Dénomination personne';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.gtyp3 IS 'Type 1ere ligne adresse';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.gtyp4 IS 'Type 2eme ligne adresse';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.gtyp5 IS 'Type 3eme ligne adresse';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.gtyp6 IS 'Type 4eme ligne adresse';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dlign3 IS '1ere ligne adresse';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dlign4 IS '2eme ligne adresse';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dlign5 IS '3eme ligne adresse';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dlign6 IS '4eme ligne adresse';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccopay IS 'Code pays étranger/TOM';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccodep1a2 IS 'Code département';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccodira IS 'Code commune';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccocom_adr IS 'Code INSEE';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccovoi IS 'Code voie MAJIC2';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccoriv IS 'Code voie Rivoli';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnvoiri IS 'Num voirie';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dindic IS 'Répétition voirie';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccopos IS 'Code postal';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnirpp IS '';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dqualp IS 'Qualité abrégée';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnomlp IS 'Nom  d usage';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dprnlp IS 'Prénoms associés au nom usage';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.jdatnss IS 'Date de naissance';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dldnss IS 'Lieu naissance';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.epxnee IS 'Mention du complément';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnomcp IS 'Nom complément';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dprncp IS 'Prénoms associés au nom complément';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnomus IS 'Nom d usage depuis 2015';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dprnus IS 'prénom d usage';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dformjur IS 'Forme juridique';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dsiren IS 'Num SIREN';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.cgocommune IS 'Code commune INSEE';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.comptecommunal IS 'Compte communal';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.app_nom_usage IS '';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.app_nom_naissance IS '';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccodro IS 'Droit réel ou particulier';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccodro_lib IS 'Libellé CCODRO';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccoqua IS 'Qualité personne pysique/morale';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccoqua_lib IS 'Libellé CCOQUA';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccogrm IS 'Groupe personne morale';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccogrm_lib IS 'Libellé CCOGRM';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccodem IS 'Code démembrement/indivision';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.ccodem_lib IS 'Libellé CCODEM';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnatpr IS 'Nature personne physique/morale';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire.dnatpr_lib IS 'Libellé DNATPR';


