

CREATE TABLE cadastrapp_arcopole.prop_ccodro (
    ccodro character varying(1) NOT NULL,
    ccodro_lib character varying(150)
);


ALTER TABLE cadastrapp_arcopole.prop_ccodro OWNER TO postgres;


ALTER TABLE ONLY cadastrapp_arcopole.prop_ccodro
    ADD CONSTRAINT ccodro_pkey PRIMARY KEY (ccodro);


