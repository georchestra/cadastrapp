
CREATE TABLE #schema_cadastrapp.request_user_information (
	userId character varying(25) NOT NULL,
    nom character varying(25) NOT NULL,
    prenom character varying(25) NOT NULL,
    ville character varying(25) NOT NULL,
    adresse character varying(25) NOT NULL,
    CNI varying(25)
);


ALTER TABLE #schema_cadastrapp.request_information OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.request_information
    ADD CONSTRAINT request_information_pkey PRIMARY KEY (id);


CREATE TABLE #schema_cadastrapp.request (
	request_id character varying(25) NOT NULL,
    user_id character varying(25) NOT NULL,
    date character varying(25) NOT NULL,
    parcelle_id character varying(25) NOT NULL
);

