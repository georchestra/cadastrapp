
CREATE TABLE cadastrapp_arcopole.prop_ccogrm (
    ccogrm character varying(2) NOT NULL,
    ccogrm_lib character varying(150)
);


ALTER TABLE cadastrapp_arcopole.prop_ccogrm OWNER TO postgres;



ALTER TABLE ONLY cadastrapp_arcopole.prop_ccogrm
    ADD CONSTRAINT ccogrm_pkey PRIMARY KEY (ccogrm);


