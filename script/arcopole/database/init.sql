-- Role: cadastrapp_arcopole

-- DROP ROLE #role_cadastrapp;

CREATE ROLE #role_cadastrapp
  SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;

-- Database: cadastrapp_arcopole

-- DROP DATABASE #dbname_arcopole;

CREATE DATABASE #dbname_arcopole
  WITH OWNER = #role_cadastrapp
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'fr_FR.UTF-8'
       LC_CTYPE = 'fr_FR.UTF-8'
       CONNECTION LIMIT = -1;

\connect #dbname_arcopole;

-- Schema: cadastrapp_arcopole

-- DROP SCHEMA #schema_cadastrapp;

CREATE SCHEMA #dbname_arcopole.#schema_cadastrapp
  AUTHORIZATION #role_cadastrapp;
  
CREATE EXTENSION dblink;
