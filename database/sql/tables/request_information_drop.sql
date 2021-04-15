
-- pour supprimer les tables pour gérer les demandes d'information foncière

DROP TABLE cadastrapp.request_user_information CASCADE ;
DROP TABLE cadastrapp.request_information CASCADE ;
DROP TABLE cadastrapp.object_request CASCADE ;
DROP TABLE cadastrapp.request_information_object_request CASCADE ;
DROP SEQUENCE cadastrapp.hibernate_sequence;

