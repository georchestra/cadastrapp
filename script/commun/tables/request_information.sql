-- Sequence: #schema_cadastrapp.hibernate_sequence


CREATE SEQUENCE #schema_cadastrapp.hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 2
  CACHE 1;
ALTER TABLE #schema_cadastrapp.hibernate_sequence
  OWNER TO #user_cadastrapp;


-- Table: #schema_cadastrapp.request_user_information
----------------------------------------------------------------
CREATE TABLE #schema_cadastrapp.request_user_information
(
  userid bigint NOT NULL,
  adress character varying(255),
  cni character varying(255),
  codepostal character varying(255),
  commune character varying(255),
  firstname character varying(255),
  lastname character varying(255),
  mail character varying(255),
  type character varying(255)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE #schema_cadastrapp.request_user_information
  ADD CONSTRAINT request_user_information_pkey PRIMARY KEY(userid );
  
ALTER TABLE #schema_cadastrapp.request_user_information
  OWNER TO #user_cadastrapp;
  
  

-- Table: #schema_cadastrapp.request_information
----------------------------------------------------------------
CREATE TABLE #schema_cadastrapp.request_information
(
  requestid bigint NOT NULL,
  askby integer,
  requestdate timestamp without time zone,
  reponseby integer,
  userid bigint NOT NULL
)
WITH (
  OIDS=FALSE
);

-- Constraint: 

ALTER TABLE #schema_cadastrapp.request_information
  CONSTRAINT request_information_pkey PRIMARY KEY (requestid );
  
 -- Foreign Key: 
ALTER TABLE #schema_cadastrapp.request_information
  ADD CONSTRAINT foreingKeyUserId FOREIGN KEY (userid)
      REFERENCES #schema_cadastrapp.request_user_information (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE #schema_cadastrapp.request_information
  OWNER TO #user_cadastrapp;


-- Table: #schema_cadastrapp.request_parcelles_information
----------------------------------------------------------------
CREATE TABLE #schema_cadastrapp.request_parcelles_information
(
  informationrequest_requestid bigint NOT NULL,
  parcelle character varying(255)
)
WITH (
  OIDS=FALSE
);


ALTER TABLE #schema_cadastrapp.request_parcelles_information
  ADD CONSTRAINT foreingKeyParcellesRequestId FOREIGN KEY (informationrequest_requestid)
      REFERENCES #schema_cadastrapp.request_information (requestid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE #schema_cadastrapp.request_parcelles_information
  OWNER TO #user_cadastrapp;


-- Table: #schema_cadastrapp.request_comptecommunaux_information
----------------------------------------------------------------
CREATE TABLE #schema_cadastrapp.request_comptecommunaux_information
(
  informationrequest_requestid bigint NOT NULL,
  comptecommunaux character varying(255)
)
WITH (
  OIDS=FALSE
);


ALTER TABLE #schema_cadastrapp.request_comptecommunaux_information
  ADD CONSTRAINT foreingKeyCompteCommunauxRequestId FOREIGN KEY (informationrequest_requestid)
      REFERENCES #schema_cadastrapp.request_information (requestid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE #schema_cadastrapp.request_comptecommunaux_information
  OWNER TO cadastrapp;


-- Table: #schema_cadastrapp.request_coproprietes_information
CREATE TABLE #schema_cadastrapp.request_coproprietes_information
(
  informationrequest_requestid bigint NOT NULL,
  coproprietes character varying(255)
)
WITH (
  OIDS=FALSE
);


ALTER TABLE #schema_cadastrapp.request_coproprietes_information
  ADD CONSTRAINT foreingKeyCoProprietesRequestId FOREIGN KEY (informationrequest_requestid)
      REFERENCES cadastrapp_qgis.request_information (requestid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE #schema_cadastrapp.request_coproprietes_information
  OWNER TO cadastrapp;



