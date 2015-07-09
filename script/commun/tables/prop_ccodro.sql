

CREATE TABLE #schema_cadastrapp.prop_ccodro (
    ccodro character varying(1) NOT NULL,
    ccodro_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_ccodro OWNER TO #user_cadastrapp;


ALTER TABLE ONLY #schema_cadastrapp.prop_ccodro
    ADD CONSTRAINT ccodro_pkey PRIMARY KEY (ccodro);


