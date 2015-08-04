
CREATE TABLE #schema_cadastrapp.prop_ccoqua (
    ccoqua character varying(1) NOT NULL,
    ccoqua_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_ccoqua OWNER TO #user_cadastrapp;


ALTER TABLE ONLY #schema_cadastrapp.prop_ccoqua
    ADD CONSTRAINT ccoqua_pkey PRIMARY KEY (ccoqua);



INSERT INTO #schema_cadastrapp.prop_ccoqua VALUES ('1', 'M');
INSERT INTO #schema_cadastrapp.prop_ccoqua VALUES ('2', 'MME');
INSERT INTO #schema_cadastrapp.prop_ccoqua VALUES ('3', 'MLE');
