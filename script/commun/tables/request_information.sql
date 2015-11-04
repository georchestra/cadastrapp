-- Sequence: #schema_cadastrapp.hibernate_sequence


CREATE SEQUENCE #schema_cadastrapp.hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
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
  ADD CONSTRAINT request_information_pkey PRIMARY KEY (requestid );
  
 -- Foreign Key: 
ALTER TABLE #schema_cadastrapp.request_information
  ADD CONSTRAINT foreingKeyUserId FOREIGN KEY (userid)
      REFERENCES #schema_cadastrapp.request_user_information (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
  
ALTER TABLE #schema_cadastrapp.request_information
  OWNER TO #user_cadastrapp;


-- Table: #schema_cadastrapp.object_request
CREATE TABLE #schema_cadastrapp.object_request
(
  objectid bigint NOT NULL,
  type integer,
  value character varying(255)
)
WITH (
  OIDS=FALSE
);

-- Constraint: 
ALTER TABLE #schema_cadastrapp.object_request
  ADD CONSTRAINT object_request_pkey PRIMARY KEY (objectid );
  
ALTER TABLE #schema_cadastrapp.object_request
  OWNER TO #user_cadastrapp;
  
  
-- Table: #schema_cadastrapp.object_request
CREATE TABLE #schema_cadastrapp.request_information_object_request
(
  request_information_requestid bigint NOT NULL,
  objectsrequest_objectid bigint NOT NULL  
)
WITH (
  OIDS=FALSE
);

-- Constraint: 
ALTER TABLE #schema_cadastrapp.request_information_object_request
   ADD CONSTRAINT request_information_object_request_pkey PRIMARY KEY (request_information_requestid , objectsrequest_objectid );

ALTER TABLE #schema_cadastrapp.request_information_object_request
   ADD CONSTRAINT foreingKeyRequestObjectRequestId FOREIGN KEY (request_information_requestid)
      REFERENCES #schema_cadastrapp.request_information (requestid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;

ALTER TABLE #schema_cadastrapp.request_information_object_request
   ADD CONSTRAINT foreingKeyRequestObjectRequestObjectId FOREIGN KEY (objectsrequest_objectid)
      REFERENCES #schema_cadastrapp.object_request (objectid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
 
 ALTER TABLE #schema_cadastrapp.request_information_object_request     
    ADD CONSTRAINT request_information_object_request_objectsrequest_objectid_key UNIQUE (objectsrequest_objectid );

ALTER TABLE #schema_cadastrapp.request_information_object_request
  OWNER TO #user_cadastrapp;
  




