
CREATE TABLE #schema_cadastrapp.prop_type_filiation (
    filiation text NOT NULL,
    filiation_lib text
);


ALTER TABLE #schema_cadastrapp.prop_type_filiation OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.prop_type_filiation
    ADD CONSTRAINT filiation_pkey PRIMARY KEY (filiation);


INSERT INTO #schema_cadastrapp.prop_type_filiation VALUES ('D','Division');
INSERT INTO #schema_cadastrapp.prop_type_filiation VALUES ('R','Réunion');
INSERT INTO #schema_cadastrapp.prop_type_filiation VALUES ('T','Transfert');
