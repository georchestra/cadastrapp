-- Create view parcelle, parcelledetails, v_parcelle_surfc based on Qgis Models

COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.v_parcelle_surfc IS 'parcelle';
COMMENT ON COLUMN #schema_cadastrapp.v_parcelle_surfc.surfc IS '';
COMMENT ON COLUMN #schema_cadastrapp.v_parcelle_surfc.surfb IS '';


COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.parcelle IS 'Parcelle';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.cgocommune IS 'Code commune INSEE';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.dnupla IS 'Num plan';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.dnvoiri IS 'Num voirie';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.dindic IS 'Indic répétition voie';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.cconvo IS 'Nature voie';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.dvoilib IS 'Libellé voie';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.ccopre IS 'Préfixe';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.ccosec IS 'Section';
COMMENT ON COLUMN #schema_cadastrapp.parcelle.dcntpa IS 'Contenance parcelle';


COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.parcelledetails IS 'Détail parcelle';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.parcelle IS 'Parcelle';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.cgocommune IS 'Code commune INSEE';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dnupla IS 'Num plan';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dcntpa IS 'Contenance parcelle';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dsrpar IS '';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.jdatat IS 'Date de l acte';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dreflf IS 'Ref livre foncier';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.gpdl IS 'Indic divisien parcelle en lots';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.cprsecr IS 'Préfixe parcelle de ref';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccosecr IS 'Section parcelle de ref';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dnuplar IS 'Num plan parcelle de ref';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dnupdl IS 'Num ordre de la pa pdl';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.gurbpa IS 'Caractère urbain parcelle';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dparpi IS 'Num parcelle primitive';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccoarp IS 'Indic arpentage';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.gparnf IS 'Indic parcelle non figurée au plan';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.gparbat IS 'Num voirie';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dnvoiri IS 'Compte communal';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dindic IS 'Indice répétition';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccovoi IS 'Code MAJIC2 voie';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccoriv IS 'Code Rivoli voie';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccocif IS 'Code CDIF';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.cconvo IS 'Nature voie';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dvoilib IS 'Libellé voie';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccocomm IS 'Code INSEE commune';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccoprem IS 'Préfixe parcelle mère';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccosecm IS 'Section parcelle mère';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.dnuplam IS 'Num plan parcelle mère';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.type_filiation IS '';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.annee IS 'Compte communal';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccopre IS 'Compte communal';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.ccosec IS 'Section';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.pdl IS 'Propriété divisé en lot';
COMMENT ON COLUMN #schema_cadastrapp.parcelledetails.inspireid IS 'Identifiant Inspire';
