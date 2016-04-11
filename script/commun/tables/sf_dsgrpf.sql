CREATE TABLE #schema_cadastrapp.sf_dsgrpf (
    dsgrpf character varying(2) NOT NULL,
    dsgrpf_lib character varying
);

ALTER TABLE #schema_cadastrapp.sf_dsgrpf OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.sf_dsgrpf
    ADD CONSTRAINT dsgrpf_pkey PRIMARY KEY (dsgrpf);

INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('AB', 'Terrains à bâtir');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('AG', 'Terrains d’agrément');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('B', 'Bois');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('BF', 'Futaies Feuillues');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('BM', 'Futaies Mixtes');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('BO', 'Oseraies');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('BP', 'Peupleraies');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('BR', 'Futaies résineuses');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('BS', 'Taillis sous Futaies');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('BT', 'Taillis simples');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('CA', 'Carrières');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('CH', 'Chemins de fer, Canaux de Navigation');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('E', 'Eaux');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('J', 'Jardins');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('L', 'Landes');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('LB', 'Landes Boisées');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('P', 'Prés');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('PA', 'Pâtures ou Pâturages');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('PC', 'Pacages ou Pâtis');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('PE', 'Prés d''embouche');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('PH', 'Herbages');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('PP', 'Prés, Pâtures ou Herbages plantes');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('S', 'Sols');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('T', 'Terre');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('TP', 'Terres plantées');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('VE', 'Vergers');
INSERT INTO #schema_cadastrapp.sf_dsgrpf VALUES ('VI', 'Vignes');
