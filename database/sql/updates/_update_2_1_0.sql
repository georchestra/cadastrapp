
ALTER TABLE #schema_cadastrapp.request_information
  DROP CONSTRAINT foreingKeyUserId;

ALTER TABLE #schema_cadastrapp.request_information
  ADD CONSTRAINT foreingKeyUserId FOREIGN KEY (userid)
      REFERENCES #schema_cadastrapp.request_user_information (userid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE #schema_cadastrapp.request_information_object_request
   DROP CONSTRAINT foreingKeyRequestObjectRequestId;

ALTER TABLE #schema_cadastrapp.request_information_object_request
   ADD CONSTRAINT foreingKeyRequestObjectRequestId FOREIGN KEY (request_information_requestid)
      REFERENCES #schema_cadastrapp.request_information (requestid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE #schema_cadastrapp.request_information_object_request
   DROP CONSTRAINT foreingKeyRequestObjectRequestObjectId;

ALTER TABLE #schema_cadastrapp.request_information_object_request
   ADD CONSTRAINT foreingKeyRequestObjectRequestObjectId FOREIGN KEY (objectsrequest_objectid)
      REFERENCES #schema_cadastrapp.object_request (objectid) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE CASCADE;      