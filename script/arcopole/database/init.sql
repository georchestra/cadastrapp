-- Role: cadastrapp_arcopole

-- DROP USER #user_cadastrapp;

CREATE USER #user_cadastrapp
  INHERIT CREATEDB PASSWORD '#pwd_cadastrapp';

-- Database: cadastrapp_arcopole

-- DROP DATABASE #dbname_arcopole;

CREATE DATABASE #dbname_arcopole
  WITH OWNER = #user_cadastrapp
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'fr_FR.UTF-8'
       LC_CTYPE = 'fr_FR.UTF-8'
       CONNECTION LIMIT = -1;

\connect #dbname_arcopole;

-- Schema: cadastrapp_arcopole

-- DROP SCHEMA #schema_cadastrapp;

CREATE SCHEMA #schema_cadastrapp AUTHORIZATION #user_cadastrapp;
  
CREATE EXTENSION dblink;
