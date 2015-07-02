
INSERT INTO #schema_arcopole.prop_ccodem VALUES ('C', 'Un des copropriétaires');
INSERT INTO #schema_arcopole.prop_ccodem VALUES ('I', 'Indivision simple');
INSERT INTO #schema_arcopole.prop_ccodem VALUES ('L', 'Propriété en litige');
INSERT INTO #schema_arcopole.prop_ccodem VALUES ('S', 'Succession de');
INSERT INTO #schema_arcopole.prop_ccodem VALUES ('V', 'La veuve ou les héritiers de');


INSERT INTO #schema_arcopole.prop_ccodro VALUES ('P', 'PROPRIETAIRE');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('U', 'USUFRUITIER (ASSOCIE AVEC N)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('N', 'NU-PROPRIETAIRE (ASSOCIE AVEC U)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('B', 'BAILLEUR A CONSTRUCTION (ASSOCIE AVEC R)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('R', 'PRENEUR A CONSTRUCTION (ASSOCIE AVEC B)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('F', 'FONCIER (ASSOCIE AVEC D OU T)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('T', 'TENUYER (ASSOCIE AVEC F)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('D', 'DOMANIER (ASSOCIE AVEC F)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('W', 'PRENEUR D''UN BAIL A REHABILITATION (ASSOCIE AVEC V)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('A', 'LOCATAIRE-ATTRIBUTAIRE (ASSOCIE AVEC P)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('E', 'EMPHYTEOTE (ASSOCIE AVEC P)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('K', 'ANTICHRESISTE (ASSOCIE AVEC P)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('L', 'FONCTIONNAIRE LOGE');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('G', 'GERANT,MANDATAIRE,GESTIONNAIRE');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('S', 'SYNDIC DE COPROPRIETE');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('H', 'ASSOCIE DANS UNE SOCIETE EN TRANSPARENCE FISCALE(ASSOCIE AVEC P)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('O', 'AUTORISATION D’OCCUPATION TEMPORAIRE (70 ANS)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('J', 'JEUNE AGRICULTEUR');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('Q', 'GESTIONNAIRE TAXE SUR LES BUREAUX (ILE DE FRANCE)');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('X', 'LA POSTE PROPRIETAIRE ET OCCUPANT');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('Y', 'LA POSTE OCCUPANT - NON PROPRIETAIRE');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('C', 'FIDUCIAIRE');
INSERT INTO #schema_arcopole.prop_ccodro VALUES ('M', 'OCCUPANT D''UNE PARCELLE APPARTENANT AU DEPARTEMENT DE MAYOTTE OU A L’ETAT (ASSOCIE A P).');

INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('0', 'PERSONNES MORALES NON REMARQUABLES');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('0A', 'CAAA en Alsace-Moselle - PERSONNES MORALES NON REMARQUABLES');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('1', 'ETAT');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('1A', 'CAAA en Alsace-Moselle - ETAT');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('2', 'REGION');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('2A', 'CAAA en Alsace-Moselle - REGION');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('3', 'DEPARTEMENT');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('3A', 'CAAA en Alsace-Moselle - DEPARTEMENT');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('4', 'COMMUNE');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('4A', 'CAAA en Alsace-Moselle - COMMUNE');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('5', 'OFFICE HLM');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('5A', 'CAAA en Alsace-Moselle - OFFICE HLM');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('6', 'PERSONNES MORALES REPRESENTANT DES SOCIETES');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('6A', 'CAAA en Alsace-Moselle - PERSONNES MORALES REPRESENTANT DES SOCIETES');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('7', 'COPROPRIETAIRE');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('7A', 'CAAA en Alsace-Moselle - COPROPRIETAIRE');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('8', 'ASSOCIE');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('8A', 'CAAA en Alsace-Moselle - ASSOCIE');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('9', 'ETABLISSEMENTS PUBLICS OU ARGANISMES ASSIMILES');
INSERT INTO #schema_arcopole.prop_ccogrm VALUES ('9A', 'CAAA en Alsace-Moselle - ETABLISSEMENTS PUBLICS OU ARGANISMES ASSIMILES');

INSERT INTO #schema_arcopole.prop_ccoqua VALUES ('1', 'M');
INSERT INTO #schema_arcopole.prop_ccoqua VALUES ('2', 'MME');
INSERT INTO #schema_arcopole.prop_ccoqua VALUES ('3', 'MLE');

INSERT INTO #schema_arcopole.prop_dnatpr VALUES ('CAA', 'Pers. Morale : CAISSE ASSURANCE AGRICOLE');
INSERT INTO #schema_arcopole.prop_dnatpr VALUES ('CLL', 'Pers. Morale : COLLECTIVITE LOCALE');
INSERT INTO #schema_arcopole.prop_dnatpr VALUES ('DOM', 'Pers. physique : PROPRIETAIRE OCCUPANT DOM');
INSERT INTO #schema_arcopole.prop_dnatpr VALUES ('ECF', 'Pers. physique : ECONOMIQUEMENT FAIBLE (NON SERVI)');
INSERT INTO #schema_arcopole.prop_dnatpr VALUES ('FNL', 'Pers. physique : FONCTIONNAIRE LOGE');
INSERT INTO #schema_arcopole.prop_dnatpr VALUES ('HLM', 'Pers. Morale : OFFICE HLM');
INSERT INTO #schema_arcopole.prop_dnatpr VALUES ('RFF', 'Pers. Morale : RESEAU FERRE DE FRANCE');
INSERT INTO #schema_arcopole.prop_dnatpr VALUES ('SEM', 'Pers. Morale : SOCIETE D ECONOMIE MIXTE');
