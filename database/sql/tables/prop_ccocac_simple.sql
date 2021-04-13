
CREATE TABLE #schema_cadastrapp.prop_ccocac_simple (
    ccocac character varying(4) NOT NULL,
    ccocac_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_ccocac_simple OWNER TO #user_cadastrapp;


ALTER TABLE ONLY #schema_cadastrapp.prop_ccocac_simple
    ADD CONSTRAINT ccocac_s_pkey PRIMARY KEY (ccocac);


INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('MAG', 'Magasins et lieux de vente');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('BUR', 'Bureaux et locaux divers assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('DEP', 'Lieux de dépôt ou stockage et parcs de stationnement');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('ATE', 'Ateliers et autres locaux assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('HOT', 'Hôtels et locaux assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('SPE', 'Etablissements de spectacles, de sports et de loisirs et autres locaux assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('ENS', 'Etablissements d''enseignement et locaux assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('CLI', 'Cliniques et Établissements du secteur sanitaire et social');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('IND', 'Établissements industriels n''étant pas évaluées selon la méthode comptable');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('EXC', 'Autres établissements');
