-- Create view parcelle, parcelledetails, v_parcelle_surfc based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.v_parcelle_surfc AS
	SELECT
		v_parcelle_surfc.parcelle, -- parcelle
		v_parcelle_surfc.surfc,
		v_parcelle_surfc.surfb 
		FROM 
		(
			SELECT
				distinct gp.geo_parcelle as parcelle,
				round(st_area(gp.geom)) as surfc, 
				sum(round(st_area(gb.geom))) as surfb 
			from #DBSchema_qgis.geo_parcelle as gp
			left join #DBSchema_qgis.geo_batiment_parcelle as gbp on gbp.geo_parcelle = gp.geo_parcelle
			left join #DBSchema_qgis.geo_batiment as gb on gb.geo_batiment = gbp.geo_batiment
			group by gp.geo_parcelle, gp.geom		
		) AS v_parcelle_surfc ;		

ALTER TABLE #schema_cadastrapp.v_parcelle_surfc OWNER TO #user_cadastrapp;



CREATE MATERIALIZED VIEW #schema_cadastrapp.parcelle AS 
	SELECT
		parcelle.parcelle, 
		parcelle.cgocommune, -- Code commune INSEE
		parcelle.dnupla, -- Numéro de plan
		parcelle.dnvoiri, -- Numéro voirie
		parcelle.dindic, -- indice de répétition de voirie 
		parcelle.cconvo, -- Code nature de la voie
		parcelle.dvoilib, --Libellé de la voie
		parcelle.ccopre, -- Préfixe de section
		parcelle.ccosec, -- Section
		parcelle.dcntpa -- Contenance de la parcelle
		FROM 
		(
			SELECT
				COALESCE(parcelle.parcelle,geo_parcelle.geo_parcelle) as parcelle,
				COALESCE(parcelle.ccodep||parcelle.ccodir||parcelle.ccocom,ltrim(substr(geo_parcelle.geo_parcelle,1,6),'0')) as cgocommune,
				COALESCE(ltrim(parcelle.dnupla, '0'),geo_parcelle.tex) as dnupla,
				ltrim(parcelle.dnvoiri, '0') as dnvoiri,
				parcelle.dindic,
				parcelle.cconvo,
				rtrim(parcelle.dvoilib) as dvoilib,
				COALESCE(ltrim(parcelle.ccopre),ltrim(substr(geo_parcelle.geo_parcelle,7,3),'0')) as ccopre,
				COALESCE(ltrim(ccosec),ltrim(substr(geo_parcelle.geo_parcelle,10,2),'0')) as ccosec,
				COALESCE(parcelle.dcntpa,CAST(geo_parcelle.supf AS integer)) as dcntpa
			FROM #DBSchema_qgis.parcelle
			full outer join #DBSchema_qgis.geo_parcelle on parcelle.parcelle = geo_parcelle.geo_parcelle
		) AS parcelle ;	

ALTER TABLE #schema_cadastrapp.parcelle OWNER TO #user_cadastrapp;


-- View: cadastreapp_qgis.parcelle

CREATE MATERIALIZED VIEW #schema_cadastrapp.parcelledetails AS 
	SELECT 
		parcelledetails.parcelle, -- parcelle
		parcelledetails.cgocommune, -- Code commune INSEE
		parcelledetails.dnupla, -- -- Numéro de plan
		parcelledetails.dcntpa, -- Contenance de la parcelle
		parcelledetails.dsrpar, -- not use - MAJ 2018 - suppression champ "DSRPAR"
		parcelledetails.jdatat, -- Date de l'acte
		parcelledetails.dreflf, -- Référence livre foncier
		parcelledetails.gpdl, -- Indice de division en lots -- not use
		parcelledetails.cprsecr, -- Préfixe de la parcelle de référence -- not use
		parcelledetails.ccosecr, -- Section de la parcelle de référence -- not use
		parcelledetails.dnuplar, -- Numéro de plan de la parcelle de référence -- not use
		parcelledetails.dnupdl, -- Numéro d ordre de la pdl -- not use
		parcelledetails.gurbpa, -- Caractère Urbain de la parcelle
		parcelledetails.dparpi, -- Numéro de parcelle primitive
		parcelledetails.ccoarp, -- Indicateur d’arpentage -- not use
		parcelledetails.gparnf, -- Indicateur de parcelle non figurée au plan -- not use
		parcelledetails.gparbat, -- Indicateur de parcelle référençant un bâtiment
		parcelledetails.dnvoiri,
		parcelledetails.dindic, -- Indice de répétition
		parcelledetails.ccovoi, -- Code Majic2 de la voie -- not use
		parcelledetails.ccoriv, -- Code Rivoli de la voie
		parcelledetails.ccocif, -- Code du CDIF -- not use
		parcelledetails.cconvo, -- Code nature de la voie
		parcelledetails.dvoilib, -- Libellé de la voie
		parcelledetails.ccocomm, -- Code INSEE de la commune de la parcelle mère
		parcelledetails.ccoprem, -- Code du préfixe de section de la parcelle mère
		parcelledetails.ccosecm, -- Code section de la parcelle mère
		parcelledetails.dnuplam, -- Numéro de plan de la parcelle mère
		parcelledetails.type_filiation,
		parcelledetails.annee,
		parcelledetails.ccopre, -- Préfixe de section
		parcelledetails.ccosec, -- Section
		parcelledetails.pdl, -- Propriété divisée en lot
		parcelledetails.inspireid , -- Inspireid -- not use
		surfc, -- surface parcelle
		surfb -- surface bâti
		FROM 
		(
			SELECT
				COALESCE(parcelle.parcelle,geo_parcelle.geo_parcelle) as parcelle,
				COALESCE(parcelle.ccodep||parcelle.ccodir||parcelle.ccocom,ltrim(substr(geo_parcelle.geo_parcelle,1,6),'0')) as cgocommune,
				COALESCE(ltrim(parcelle.dnupla, '0'),geo_parcelle.tex) as dnupla,
				COALESCE(parcelle.dcntpa,CAST(geo_parcelle.supf AS integer)) as dcntpa,
				parcelle.dsrpar,
				concat(substr(parcelle.jdatat::text,1,2),'/',substr(parcelle.jdatat::text,3,2),'/',substr(parcelle.jdatat::text,5,4)) as jdatat,
				parcelle.dreflf,
				parcelle.gpdl,
				parcelle.cprsecr,
				parcelle.ccosecr,
				parcelle.dnuplar,
				parcelle.dnupdl,
				parcelle.gurbpa,
				parcelle.dparpi,
				parcelle.ccoarp,
				parcelle.gparnf,
				parcelle.gparbat,
				ltrim(parcelle.dnvoiri, '0') as dnvoiri,
				rtrim(parcelle.dindic) as dindic,
				parcelle.ccovoi,
				parcelle.ccoriv,
				parcelle.ccocif,
				parcelle.cconvo,
				rtrim(parcelle.dvoilib) as dvoilib,
				parcelle.ccocomm,
				parcelle.ccoprem,
				parcelle.ccosecm,
				parcelle.dnuplam,
				parcelle.type_filiation,
				parcelle.annee,
				COALESCE(ltrim(parcelle.ccopre),ltrim(substr(geo_parcelle.geo_parcelle,7,3),'0')) as ccopre,
				COALESCE(ltrim(ccosec),ltrim(substr(geo_parcelle.geo_parcelle,10,2),'0')) as ccosec,
				parcelle.pdl,
				parcelle.inspireid,
				p2.surfc,
				p2.surfb
			from #DBSchema_qgis.parcelle
			full outer join #DBSchema_qgis.geo_parcelle on parcelle.parcelle = geo_parcelle.geo_parcelle
			left join cadastrapp.v_parcelle_surfc p2 on parcelle.parcelle=p2.parcelle
		) AS parcelledetails ;

ALTER TABLE #schema_cadastrapp.parcelleDetails OWNER TO #user_cadastrapp;

