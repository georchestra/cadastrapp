-- Create view section based on Qgis Models

COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.uf_parcelle IS 'Unité foncière';

COMMENT ON COLUMN #schema_cadastrapp.uf_parcelle.parcelle IS 'identifiant de la parcelle cadastrale';
COMMENT ON COLUMN #schema_cadastrapp.uf_parcelle.uf IS 'identifiant de l''unité foncière';
COMMENT ON COLUMN #schema_cadastrapp.uf_parcelle.comptecommunal IS 'identifiant du compte communal propriétaire de l''unité foncière';
