
CREATE TABLE #schema_cadastrapp.request_user_information (
	CNI character varying(25) NOT NULL,
    nom character varying(25) NOT NULL,
    prenom character varying(25) NOT NULL,
    ville character varying(25) NOT NULL,
    adresse character varying(25) NOT NULL,
);


ALTER TABLE #schema_cadastrapp.request_user_information OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.request_user_information
    ADD CONSTRAINT request_information_pkey PRIMARY KEY (CNI);


CREATE TABLE #schema_cadastrapp.request_information (
	request_id character varying(25) NOT NULL,
    user_id character varying(25) NOT NULL,
    date character varying(25) NOT NULL,
    parcelle_id character varying(25) NOT NULL;
    comptecommunal character varying(25) NOT NULL;
);


ALTER TABLE #schema_cadastrapp.request_information OWNER TO #user_cadastrapp;

ALTER TABLE ONLY #schema_cadastrapp.request_information
    ADD CONSTRAINT request_information_pkey PRIMARY KEY (request_id);
