
CREATE TABLE #schema_cadastrapp.prop_dnatpr (
    dnatpr character varying(3) NOT NULL,
    dnatpr_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_dnatpr OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.prop_dnatpr
    ADD CONSTRAINT dnatpr_pkey PRIMARY KEY (dnatpr);



INSERT INTO #schema_cadastrapp.prop_dnatpr VALUES ('CAA', 'Pers. Morale : CAISSE ASSURANCE AGRICOLE');
INSERT INTO #schema_cadastrapp.prop_dnatpr VALUES ('CLL', 'Pers. Morale : COLLECTIVITE LOCALE');
INSERT INTO #schema_cadastrapp.prop_dnatpr VALUES ('DOM', 'Pers. physique : PROPRIETAIRE OCCUPANT DOM');
INSERT INTO #schema_cadastrapp.prop_dnatpr VALUES ('ECF', 'Pers. physique : ECONOMIQUEMENT FAIBLE (NON SERVI)');
INSERT INTO #schema_cadastrapp.prop_dnatpr VALUES ('FNL', 'Pers. physique : FONCTIONNAIRE LOGE');
INSERT INTO #schema_cadastrapp.prop_dnatpr VALUES ('HLM', 'Pers. Morale : OFFICE HLM');
INSERT INTO #schema_cadastrapp.prop_dnatpr VALUES ('RFF', 'Pers. Morale : RESEAU FERRE DE FRANCE');
INSERT INTO #schema_cadastrapp.prop_dnatpr VALUES ('SEM', 'Pers. Morale : SOCIETE D ECONOMIE MIXTE');

