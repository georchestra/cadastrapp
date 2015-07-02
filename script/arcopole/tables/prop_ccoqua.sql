
CREATE TABLE #schema_arcopole.prop_ccoqua (
    ccoqua character varying(1) NOT NULL,
    ccoqua_lib character varying(150)
);


ALTER TABLE #schema_arcopole.prop_ccoqua OWNER TO postgres;


ALTER TABLE ONLY #schema_arcopole.prop_ccoqua
    ADD CONSTRAINT ccoqua_pkey PRIMARY KEY (ccoqua);

