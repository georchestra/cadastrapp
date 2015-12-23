
-- Drop all view
DROP VIEW #schema_cadastrapp.commune;

DROP VIEW #schema_cadastrapp.parcelledetails;
DROP VIEW #schema_cadastrapp.parcelle;
DROP VIEW #schema_cadastrapp.v_parcelle_surfc;

DROP VIEW #schema_cadastrapp.proprietaire;

DROP VIEW #schema_cadastrapp.proprietaire_parcelle;
DROP VIEW #schema_cadastrapp.co_propriete_parcelle;

DROP VIEW #schema_cadastrapp.proprietebatie;
DROP VIEW #schema_cadastrapp.proprietenonbatie;
DROP VIEW #schema_cadastrapp.proprietenonbatiesufexo;

DROP VIEW #schema_cadastrapp.descproffessionnel;
DROP VIEW #schema_cadastrapp.descdependance;
DROP VIEW #schema_cadastrapp.deschabitation;

DROP VIEW #schema_cadastrapp.section;

-- Drop all tables (not user autorisation tables)

DROP TABLE #schema_cadastrapp.uf_parcelle;

DROP TABLE #schema_cadastrapp.prop_ccodem;
DROP TABLE #schema_cadastrapp.prop_ccodro;
DROP TABLE #schema_cadastrapp.prop_ccogrm;
DROP TABLE #schema_cadastrapp.prop_ccoqua;
DROP TABLE #schema_cadastrapp.prop_dnatpr;
DROP TABLE #schema_cadastrapp.prop_dmatto;
DROP TABLE #schema_cadastrapp.prop_dmatgm;


DROP TABLE #schema_cadastrapp.request_information CASCADE;
DROP TABLE #schema_cadastrapp.request_user_information CASCADE;
DROP TABLE #schema_cadastrapp.request_information_object_request CASCADE;
DROP TABLE #schema_cadastrapp.object_request CASCADE;



