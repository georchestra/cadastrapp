-- Create view proprietenonbatiesufexo based on Qgis Models

		
COMMENT ON MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatiesufexo IS 'Exonération porpriétés non bâtis';
COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.cgocommune IS 'Code commune INSEE';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.parcelle IS '';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.id_local IS 'Identifiant du local';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.comptecommunal IS '';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.cgocommune IS 'Code commune INSEE';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.ccolloc IS 'Code collectivité locale';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.gnexts IS 'Nature d''éxonération temporaire';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.jandeb IS 'Année de début d''éxonération';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.jfinex IS 'Année de retour à l''imposition';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.rcexnba IS 'Revenu cadastral exonéré';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.fcexn IS '';
		COMMENT ON COLUMN #schema_cadastrapp.proprietenonbatiesufexo.pexn IS 'Pourcentage d''éxonération';
