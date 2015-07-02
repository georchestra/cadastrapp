CREATE TABLE #schema_arcopole.prop_ccodem (
    ccodem character varying(1) NOT NULL,
    ccodem_lib character varying(150)
);


ALTER TABLE #schema_arcopole.prop_ccodem OWNER TO postgres;


ALTER TABLE ONLY #schema_arcopole.prop_ccodem
    ADD CONSTRAINT ccodem_pkey PRIMARY KEY (ccodem);



