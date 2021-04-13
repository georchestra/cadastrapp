
CREATE TABLE #schema_cadastrapp.prop_ccodem (
    ccodem character varying(1) NOT NULL,
    ccodem_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_ccodem OWNER TO #user_cadastrapp;


ALTER TABLE ONLY #schema_cadastrapp.prop_ccodem
    ADD CONSTRAINT ccodem_pkey PRIMARY KEY (ccodem);


INSERT INTO #schema_cadastrapp.prop_ccodem VALUES ('C', 'Un des copropriétaires');
INSERT INTO #schema_cadastrapp.prop_ccodem VALUES ('I', 'Indivision simple');
INSERT INTO #schema_cadastrapp.prop_ccodem VALUES ('L', 'Propriété en litige');
INSERT INTO #schema_cadastrapp.prop_ccodem VALUES ('S', 'Succession de');
INSERT INTO #schema_cadastrapp.prop_ccodem VALUES ('V', 'La veuve ou les héritiers de');
