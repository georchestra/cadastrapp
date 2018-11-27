CREATE TABLE #schema_cadastrapp.prop_bati_detent (
    detent text NOT NULL,
    detent_lib text
);


ALTER TABLE #schema_cadastrapp.prop_bati_detent OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.prop_bati_detent
    ADD CONSTRAINT detent_pkey PRIMARY KEY (detent);
    

INSERT INTO #schema_cadastrapp.prop_bati_detent VALUES ('1','bon');
INSERT INTO #schema_cadastrapp.prop_bati_detent VALUES ('2','assez bon');
INSERT INTO #schema_cadastrapp.prop_bati_detent VALUES ('3','passable');
INSERT INTO #schema_cadastrapp.prop_bati_detent VALUES ('4','médiocre');
INSERT INTO #schema_cadastrapp.prop_bati_detent VALUES ('5','mauvais');