
CREATE TABLE #schema_cadastrapp.prop_ccogrm (
    ccogrm character varying(2) NOT NULL,
    ccogrm_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_ccogrm OWNER TO #user_cadastrapp;



ALTER TABLE ONLY #schema_cadastrapp.prop_ccogrm
    ADD CONSTRAINT ccogrm_pkey PRIMARY KEY (ccogrm);


