
-- ces requêtes suppriment les vues matérialisées existantes

REFRESH MATERIALIZED VIEW #schema_cadastrapp.commune;

REFRESH MATERIALIZED VIEW #schema_cadastrapp.section;

REFRESH MATERIALIZED VIEW #schema_cadastrapp.parcelle;
REFRESH MATERIALIZED VIEW #schema_cadastrapp.parcelledetails;
REFRESH MATERIALIZED VIEW #schema_cadastrapp.v_parcelle_surfc;

REFRESH MATERIALIZED VIEW #schema_cadastrapp.proprietaire;

REFRESH MATERIALIZED VIEW #schema_cadastrapp.proprietaire_parcelle;
REFRESH MATERIALIZED VIEW #schema_cadastrapp.co_propriete_parcelle;

REFRESH MATERIALIZED VIEW #schema_cadastrapp.proprietebatie;
REFRESH MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatie;
REFRESH MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatiesufexo;

REFRESH MATERIALIZED VIEW #schema_cadastrapp.lot;

REFRESH MATERIALIZED VIEW #schema_cadastrapp.descproffessionnel;
REFRESH MATERIALIZED VIEW #schema_cadastrapp.descdependance;
REFRESH MATERIALIZED VIEW #schema_cadastrapp.deschabitation;

REFRESH MATERIALIZED VIEW #schema_cadastrapp.uf_parcelle ;
