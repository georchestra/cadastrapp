
CREATE TABLE #schema_cadastrapp.prop_ccodro (
    ccodro character varying(1) NOT NULL,
    ccodro_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_ccodro OWNER TO #user_cadastrapp;


ALTER TABLE ONLY #schema_cadastrapp.prop_ccodro
    ADD CONSTRAINT ccodro_pkey PRIMARY KEY (ccodro);


INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('P', 'PROPRIETAIRE');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('U', 'USUFRUITIER (ASSOCIE AVEC N)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('N', 'NU-PROPRIETAIRE (ASSOCIE AVEC U)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('B', 'BAILLEUR A CONSTRUCTION (ASSOCIE AVEC R)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('R', 'PRENEUR A CONSTRUCTION (ASSOCIE AVEC B)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('F', 'FONCIER (ASSOCIE AVEC D OU T)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('T', 'TENUYER (ASSOCIE AVEC F)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('D', 'DOMANIER (ASSOCIE AVEC F)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('W', 'PRENEUR D''UN BAIL A REHABILITATION (ASSOCIE AVEC V)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('A', 'LOCATAIRE-ATTRIBUTAIRE (ASSOCIE AVEC P)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('E', 'EMPHYTEOTE (ASSOCIE AVEC P)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('K', 'ANTICHRESISTE (ASSOCIE AVEC P)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('L', 'FONCTIONNAIRE LOGE');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('G', 'GERANT,MANDATAIRE,GESTIONNAIRE');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('S', 'SYNDIC DE COPROPRIETE');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('H', 'ASSOCIE DANS UNE SOCIETE EN TRANSPARENCE FISCALE(ASSOCIE AVEC P)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('O', 'AUTORISATION D’OCCUPATION TEMPORAIRE (70 ANS)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('J', 'JEUNE AGRICULTEUR');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('Q', 'GESTIONNAIRE TAXE SUR LES BUREAUX (ILE DE FRANCE)');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('X', 'LA POSTE PROPRIETAIRE ET OCCUPANT');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('Y', 'LA POSTE OCCUPANT - NON PROPRIETAIRE');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('C', 'FIDUCIAIRE');
INSERT INTO #schema_cadastrapp.prop_ccodro VALUES ('M', 'OCCUPANT D''UNE PARCELLE APPARTENANT AU DEPARTEMENT DE MAYOTTE OU A L’ETAT (ASSOCIE A P).');

