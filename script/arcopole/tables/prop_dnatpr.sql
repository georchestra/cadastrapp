
CREATE TABLE #schema_arcopole.prop_dnatpr (
    dnatpr character varying(3) NOT NULL,
    dnatpr_lib character varying(150)
);


ALTER TABLE #schema_arcopole.prop_dnatpr OWNER TO postgres;

ALTER TABLE ONLY #schema_arcopole.prop_dnatpr
    ADD CONSTRAINT dnatpr_pkey PRIMARY KEY (dnatpr);

