-- Create commune Views based on Qgis Models

COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.commune IS 'Commune';
COMMENT ON COLUMN #schema_cadastrapp.commune.cgocommune IS 'Code commune INSEE';
COMMENT ON COLUMN #schema_cadastrapp.commune.clerivili  IS 'Zone alphabétique MAJIC2';
COMMENT ON COLUMN #schema_cadastrapp.commune.libcom  IS 'Nom de commune';
COMMENT ON COLUMN #schema_cadastrapp.commune.libcom_maj IS 'libcom majuscule';
COMMENT ON COLUMN #schema_cadastrapp.commune.libcom_min IS 'libcom minuscule';
COMMENT ON COLUMN #schema_cadastrapp.commune.typcom IS 'Différencie commune recensées de rurales';


