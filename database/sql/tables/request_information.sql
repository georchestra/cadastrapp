
-- ici sont créées les tables pour gérer les demandes d'information foncière


-- Sequence: #schema_cadastrapp.hibernate_sequence
CREATE SEQUENCE #schema_cadastrapp.hibernate_sequence
  INCREMENT 1
  MINVALUE 1
  MAXVALUE 9223372036854775807
  START 1
  CACHE 1;
ALTER TABLE #schema_cadastrapp.hibernate_sequence
  OWNER TO #user_cadastrapp;



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
);

ALTER TABLE #schema_cadastrapp.request_user_information
  ADD CONSTRAINT request_user_information_pkey PRIMARY KEY(userid );
  
ALTER TABLE #schema_cadastrapp.request_user_information
  OWNER TO #user_cadastrapp;
  
  

CREATE TABLE #schema_cadastrapp.request_information
(
  requestid bigint NOT NULL,
  askby integer,
  objectnumber integer,
  requestdate timestamp without time zone,
  reponseby integer,
  userid bigint NOT NULL
);

ALTER TABLE #schema_cadastrapp.request_information
  ADD CONSTRAINT request_information_pkey PRIMARY KEY (requestid );
  
ALTER TABLE #schema_cadastrapp.request_information
  ADD CONSTRAINT foreingKeyUserId FOREIGN KEY (userid)
      REFERENCES #schema_cadastrapp.request_user_information (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
  
ALTER TABLE #schema_cadastrapp.request_information
  OWNER TO #user_cadastrapp;



CREATE TABLE #schema_cadastrapp.object_request
(
  objectid bigint NOT NULL,
  requestid bigint NOT NULL,
  type integer,
  comptecommunal character varying(255),
  parcelle character varying(255),
  commune character varying(255),
  section character varying(255),
  numero character varying(255),
  proprietaire character varying(255),
  bp character varying(5),
  rp character varying(5)
);

ALTER TABLE #schema_cadastrapp.object_request
  ADD CONSTRAINT object_request_pkey PRIMARY KEY (objectid );
  
ALTER TABLE #schema_cadastrapp.object_request
  ADD CONSTRAINT foreingKeyRequestId FOREIGN KEY (requestid)
      REFERENCES #schema_cadastrapp.request_information (requestid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;
  
ALTER TABLE #schema_cadastrapp.object_request
  OWNER TO #user_cadastrapp;




