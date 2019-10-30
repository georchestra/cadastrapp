-- Create lot Views based on Qgis Models

COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.lot IS 'lot';
COMMENT ON COLUMN #schema_cadastrapp.lot.id_local IS 'Identifiant du local';
COMMENT ON COLUMN #schema_cadastrapp.lot.dnulot IS 'Numéro du lot - 00Axxxx';
COMMENT ON COLUMN #schema_cadastrapp.lot.dnumql IS 'Numérateur du lot';
COMMENT ON COLUMN #schema_cadastrapp.lot.ddenql IS 'Dénominateur du lot';
