-- Create view proprietenonbatiesufexo based on Qgis Models

-- DROP MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatiesufexo ;

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatiesufexo AS 
	SELECT 
		proprietenonbatiesufexo.parcelle,
		proprietenonbatiesufexo.id_local, -- Identifiant du local 
		proprietenonbatiesufexo.comptecommunal,  -- Compte communal
		proprietenonbatiesufexo.cgocommune,  -- Code INSEE commune
		proprietenonbatiesufexo.ccolloc, -- Code collectivité locale
		proprietenonbatiesufexo.gnexts, -- Nature d’exonération temporaire
		proprietenonbatiesufexo.jandeb, -- Année de début d’exonération 
		proprietenonbatiesufexo.jfinex, -- Année de retour à imposition
		proprietenonbatiesufexo.rcexnba, -- Revenu cadastral exonéré, en valeur de l’année
		proprietenonbatiesufexo.fcexn, 
		proprietenonbatiesufexo.pexn -- Pourcentage d’exonération
	FROM 
	(
		SELECT 
			p.parcelle,
			sufex.suf as id_local,
			p.comptecommunal,
			p.ccodep || p.ccodir ||	p.ccocom as cgocommune,
			sufex.ccolloc,
			sufex.gnexts,
			sufex.jandeb,
			sufex.jfinex,
			sufex.rcexnba as rcexnba,
			sufex.fcexn,
			sufex.pexn/100 as pexn
		FROM #DBSchema_qgis.parcelle p
			LEFT JOIN #DBSchema_qgis.suf on suf.comptecommunal=p.comptecommunal and p.parcelle=suf.parcelle
			LEFT JOIN #DBSchema_qgis.sufexoneration as sufex on sufex.suf=suf.suf
	) AS proprietenonbatiesufexo ;


ALTER TABLE #schema_cadastrapp.proprietenonbatiesufexo OWNER TO #user_cadastrapp;
