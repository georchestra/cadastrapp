-- Role: cadastreapp_qgis

-- DROP USER #user_cadastrapp;

CREATE USER #user_cadastrapp
  INHERIT CREATEDB CREATEROLE;

-- Database: cadastreapp_qgis

-- DROP DATABASE #dbname_qgis;

CREATE DATABASE #dbname_qgis
  WITH OWNER = #user_cadastrapp
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'fr_FR.UTF-8'
       LC_CTYPE = 'fr_FR.UTF-8'
       CONNECTION LIMIT = -1;

\connect #dbname_qgis;

-- Schema: cadastreapp_qgis

-- DROP SCHEMA #schema_cadastrapp;

CREATE SCHEMA #schema_cadastrapp
  AUTHORIZATION #user_cadastrapp;
  
  CREATE EXTENSION dblink;