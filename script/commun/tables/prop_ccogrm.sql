
CREATE TABLE #schema_cadastrapp.prop_ccogrm (
    ccogrm character varying(2) NOT NULL,
    ccogrm_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_ccogrm OWNER TO #user_cadastrapp;



ALTER TABLE ONLY #schema_cadastrapp.prop_ccogrm
    ADD CONSTRAINT ccogrm_pkey PRIMARY KEY (ccogrm);

INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('0', 'PERSONNES MORALES NON REMARQUABLES');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('0A', 'CAAA en Alsace-Moselle - PERSONNES MORALES NON REMARQUABLES');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('1', 'ETAT');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('1A', 'CAAA en Alsace-Moselle - ETAT');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('2', 'REGION');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('2A', 'CAAA en Alsace-Moselle - REGION');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('3', 'DEPARTEMENT');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('3A', 'CAAA en Alsace-Moselle - DEPARTEMENT');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('4', 'COMMUNE');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('4A', 'CAAA en Alsace-Moselle - COMMUNE');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('5', 'OFFICE HLM');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('5A', 'CAAA en Alsace-Moselle - OFFICE HLM');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('6', 'PERSONNES MORALES REPRESENTANT DES SOCIETES');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('6A', 'CAAA en Alsace-Moselle - PERSONNES MORALES REPRESENTANT DES SOCIETES');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('7', 'COPROPRIETAIRE');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('7A', 'CAAA en Alsace-Moselle - COPROPRIETAIRE');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('8', 'ASSOCIE');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('8A', 'CAAA en Alsace-Moselle - ASSOCIE');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('9', 'ETABLISSEMENTS PUBLICS OU ARGANISMES ASSIMILES');
INSERT INTO #schema_cadastrapp.prop_ccogrm VALUES ('9A', 'CAAA en Alsace-Moselle - ETABLISSEMENTS PUBLICS OU ARGANISMES ASSIMILES');


