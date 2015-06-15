
CREATE TABLE cadastrapp_arcopole.prop_dnatpr (
    dnatpr character varying(3) NOT NULL,
    dnatpr_lib character varying(150)
);


ALTER TABLE cadastrapp_arcopole.prop_dnatpr OWNER TO postgres;

ALTER TABLE ONLY cadastrapp_arcopole.prop_dnatpr
    ADD CONSTRAINT dnatpr_pkey PRIMARY KEY (dnatpr);

