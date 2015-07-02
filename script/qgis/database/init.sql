-- Role: cadastreapp_qgis

-- DROP ROLE #role_cadastrapp;

CREATE ROLE #role_cadastrapp
  SUPERUSER INHERIT CREATEDB CREATEROLE REPLICATION;

-- Database: cadastreapp_qgis

-- DROP DATABASE #dbname_qgis;

CREATE DATABASE #dbname_qgis
  WITH OWNER = #role_cadastrapp
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'fr_FR.UTF-8'
       LC_CTYPE = 'fr_FR.UTF-8'
       CONNECTION LIMIT = -1;

-- Schema: cadastreapp_qgis

-- DROP SCHEMA #schemaname_qgis;

CREATE SCHEMA #schemaname_qgis
  AUTHORIZATION #role_cadastrapp;