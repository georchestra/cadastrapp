
-- CREATE INDEX ONLY FOR MATERIALIZED VIEW ONLY

-- Commune
CREATE INDEX idxcommunelibcom ON #schema_cadastrapp.commune (libcom);
CREATE INDEX idxcommunecgocommune ON #schema_cadastrapp.commune (cgocommune);

-- DesHabitation
CREATE INDEX idxdeschabitationinvar ON #schema_cadastrapp.deschabitation (invar, annee);

-- Proprietaire
CREATE INDEX idxproprietairecgocommune ON #schema_cadastrapp.proprietaire (cgocommune);
CREATE INDEX idxproprietaireddenom ON #schema_cadastrapp.proprietaire (UPPER(rtrim(ddenom)));
CREATE INDEX idxproprietaireupperdnomlp ON #schema_cadastrapp.proprietaire (UPPER(dnomlp));

-- Proprietaire_parcelle
CREATE INDEX idxproprietaireparcellecomptecommunal ON #schema_cadastrapp.proprietaire_parcelle (comptecommunal);
CREATE INDEX idxproprietaireparcelleparcelle ON #schema_cadastrapp.proprietaire_parcelle (parcelle);

-- Parcelle
CREATE INDEX idxparcellecgocommune ON #schema_cadastrapp.parcelle (cgocommune);
CREATE INDEX idxparcelleparcelle ON #schema_cadastrapp.parcelle (parcelle);
CREATE INDEX idxparcelleccosec ON #schema_cadastrapp.parcelle (ccosec);
CREATE INDEX idxparcellednupla ON #schema_cadastrapp.parcelle (dnupla);
CREATE INDEX idxparcelleccopre ON #schema_cadastrapp.parcelle (ccopre);
CREATE INDEX idxparcelledvoilib ON #schema_cadastrapp.parcelle (UPPER(dvoilib));

-- ParcelleDetails
CREATE INDEX idxparcelledetailscgocommune ON #schema_cadastrapp.parcelledetails (cgocommune);
CREATE INDEX idxparcelledetailsparcelle ON #schema_cadastrapp.parcelledetails (parcelle);
CREATE INDEX idxparcelledetailsccosec ON #schema_cadastrapp.parcelledetails (ccosec);
CREATE INDEX idxparcelledetailsdnupla ON #schema_cadastrapp.parcelledetails (dnupla);
CREATE INDEX idxparcelledetailsccopre ON #schema_cadastrapp.parcelledetails (ccopre);
CREATE INDEX idxparcelledetailsdvoilib ON #schema_cadastrapp.parcelledetails (dvoilib);
CREATE INDEX idxparcelledetailscgocommuneparcelle ON #schema_cadastrapp.parcelledetails (cgocommune, parcelle);

-- ProprieteBatie
CREATE INDEX idxproprietebatiecomptecommunal ON #schema_cadastrapp.proprietebatie (comptecommunal);
CREATE INDEX idxproprietebatiecgocommune ON #schema_cadastrapp.proprietebatie (cgocommune);
CREATE INDEX idxproprietebatiednubat ON #schema_cadastrapp.proprietebatie (dnubat);

-- ProprieteNonbatie
CREATE INDEX idxproprietenonbatieparcelle ON #schema_cadastrapp.proprietenonbatie (parcelle);
CREATE INDEX idxproprietenonbatiecgocommune ON #schema_cadastrapp.proprietenonbatie (cgocommune);

-- ProprieteNonbatieSufExo
CREATE INDEX idxproprietenonbatiesufexoparcelle ON #schema_cadastrapp.proprietenonbatiesufexo (parcelle);
CREATE INDEX idxproprietenonbatiesufexocgocommune ON #schema_cadastrapp.proprietenonbatiesufexo (cgocommune);

-- Section
CREATE INDEX idxsectioncgocommune ON #schema_cadastrapp.section (cgocommune);
CREATE INDEX idxsectioncgocommuneccosec ON #schema_cadastrapp.section (cgocommune, ccosec);


