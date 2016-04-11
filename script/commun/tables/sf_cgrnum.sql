CREATE TABLE #schema_cadastrapp.sf_cgrnum (
    cgrnum character varying(2) NOT NULL,
    cgrnum_lib character varying
);

ALTER TABLE #schema_cadastrapp.sf_cgrnum OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.sf_cgrnum
    ADD CONSTRAINT cgrnum_pkey PRIMARY KEY (cgrnum);

INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('01', 'Terres');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('02', 'Prés');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('03', 'Vergers');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('04', 'Vignes');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('05', 'Bois');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('06', 'Landes');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('07', 'Carrières');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('08', 'Eaux');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('09', 'Jardins');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('10', 'Terrains à bâtir');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('11', 'Terrains d''agrément');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('12', 'Chemin de fer');
INSERT INTO #schema_cadastrapp.sf_cgrnum VALUES ('13', 'Sol');
