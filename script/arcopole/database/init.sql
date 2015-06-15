-- Role: cadastrapp_arcopole

-- DROP ROLE cadastrapp_arcopole;

CREATE ROLE cadastrapp_arcopole
  SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;

-- Database: cadastrapp_arcopole

-- DROP DATABASE cadastrapp_arcopole;

CREATE DATABASE cadastrapp_arcopole
  WITH OWNER = cadastrapp_arcopole
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'fr_FR.UTF-8'
       LC_CTYPE = 'fr_FR.UTF-8'
       CONNECTION LIMIT = -1;

-- Schema: cadastrapp_arcopole

-- DROP SCHEMA cadastrapp_arcopole;

CREATE SCHEMA cadastrapp_arcopole
  AUTHORIZATION cadastrapp_arcopole;