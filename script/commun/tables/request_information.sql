
-- Table: #schema_cadastrapp.request_information

CREATE TABLE #schema_cadastrapp.request_information
(
  requestid bigint NOT NULL,
  comptecommunal character varying(255),
  requestdate timestamp without time zone,
  cni character varying(255) NOT NULL
)
WITH (
  OIDS=FALSE
);

-- Constraint: 

ALTER TABLE #schema_cadastrapp.request_information
  ADD CONSTRAINT request_information_pkey PRIMARY KEY(requestid );
  
 -- Foreign Key: 

ALTER TABLE #schema_cadastrapp.request_information
  ADD CONSTRAINT fk23e7677c28a26364 FOREIGN KEY (cni)
      REFERENCES #schema_cadastrapp.request_user_information (cni) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE #schema_cadastrapp.request_information
  OWNER TO #user_cadastrapp;



-- Table: #schema_cadastrapp.request_parcelles_information
CREATE TABLE #schema_cadastrapp.request_parcelles_information
(
  informationrequest_requestid bigint NOT NULL,
  parcellesid character varying(255)
)
WITH (
  OIDS=FALSE
);


ALTER TABLE #schema_cadastrapp.request_parcelles_information
  ADD CONSTRAINT fkcb4839aeb73d664f FOREIGN KEY (informationrequest_requestid)
      REFERENCES #schema_cadastrapp.request_information (requestid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
      
ALTER TABLE #schema_cadastrapp.request_parcelles_information
  OWNER TO #user_cadastrapp;


-- Table: #schema_cadastrapp.request_user_information
CREATE TABLE #schema_cadastrapp.request_user_information
(
  cni character varying(255) NOT NULL,
  adress character varying(255),
  codepostal character varying(255),
  commune character varying(255),
  firstname character varying(255),
  lastname character varying(255)
)
WITH (
  OIDS=FALSE
);

ALTER TABLE cadastrapp_qgis.request_user_information
  ADD CONSTRAINT request_user_information_pkey PRIMARY KEY(cni );
  
ALTER TABLE #schema_cadastrapp.request_user_information
  OWNER TO #user_cadastrapp;


