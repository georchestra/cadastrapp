ALTER TABLE #schema_cadastrapp.request_information
  DROP CONSTRAINT foreingKeyUserId;

ALTER TABLE #schema_cadastrapp.request_information
  ADD CONSTRAINT foreingKeyUserId FOREIGN KEY (userid)
      REFERENCES #schema_cadastrapp.request_user_information (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE #schema_cadastrapp.object_request
  ADD COLUMN requestid bigint;

UPDATE #schema_cadastrapp.object_request
  SET requestid = rior.request_information_requestid
  FROM #schema_cadastrapp.request_information_object_request rior
  WHERE rior.objectsrequest_objectid = object_request.objectid;

ALTER TABLE #schema_cadastrapp.object_request
  ADD CONSTRAINT foreingKeyRequestId FOREIGN KEY (requestid)
      REFERENCES #schema_cadastrapp.request_information (requestid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE; 

ALTER TABLE #schema_cadastrapp.object_request
  ALTER COLUMN requestid SET NOT NULL;

DROP TABLE #schema_cadastrapp.request_information_object_request;
