-- Create view making link beetween parcelle and owners based on Qgis Models

COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.proprietaire_parcelle IS 'parcelle';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire_parcelle.parcelle IS 'Parcelle';
COMMENT ON COLUMN #schema_cadastrapp.proprietaire_parcelle.comptecommunal IS 'Compte communal';
