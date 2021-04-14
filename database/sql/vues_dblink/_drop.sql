
-- ces requêtes suppriment les vues matérialisées existantes

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.commune;

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.section;

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.parcelle;
DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.parcelledetails;
DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.v_parcelle_surfc;

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.proprietaire;

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.proprietaire_parcelle;
DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.co_propriete_parcelle;

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.proprietebatie;
DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.proprietenonbatie;
DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.proprietenonbatiesufexo;

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.lot;

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.descproffessionnel;
DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.descdependance;
DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.deschabitation;

DROP MATERIALIZED VIEW IF EXISTS #schema_cadastrapp.uf_parcelle CASCADE ;
