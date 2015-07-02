-- Role: cadastrapp_arcopole

-- DROP ROLE #role_arcopole;

CREATE ROLE #role_arcopole
  SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;

-- Database: cadastrapp_arcopole

-- DROP DATABASE #dbname_arcopole;

CREATE DATABASE #dbname_arcopole
  WITH OWNER = #role_arcopole
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'fr_FR.UTF-8'
       LC_CTYPE = 'fr_FR.UTF-8'
       CONNECTION LIMIT = -1;

-- Schema: cadastrapp_arcopole

-- DROP SCHEMA cadastrapp_arcopole;

CREATE SCHEMA #schemaname_arcopole
  AUTHORIZATION #role_arcopole;
  
  