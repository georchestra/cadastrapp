
CREATE TABLE #schema_cadastrapp.prop_dnatpr (
    dnatpr character varying(3) NOT NULL,
    dnatpr_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_dnatpr OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.prop_dnatpr
    ADD CONSTRAINT dnatpr_pkey PRIMARY KEY (dnatpr);

