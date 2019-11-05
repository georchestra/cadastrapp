-- Create view parcelle, parcelledetails, v_parcelle_surfc based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.v_parcelle_surfc AS
	SELECT  v_parcelle_surfc.parcelle, -- parcelle
			v_parcelle_surfc.surfc,
			v_parcelle_surfc.surfb 
		FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
			'select distinct 
				gp.geo_parcelle as parcelle,
				round(st_area(gp.geom)) as surfc, 
				sum(round(st_area(gb.geom))) as surfb 
			from #DBSchema_qgis.geo_parcelle as gp
			left join #DBSchema_qgis.geo_batiment_parcelle as gbp on gbp.geo_parcelle = gp.geo_parcelle
			left join #DBSchema_qgis.geo_batiment as gb on gb.geo_batiment = gbp.geo_batiment
			group by gp.geo_parcelle, gp.geom		
			'::text)
	v_parcelle_surfc(
		parcelle character varying(19),
		surfc float,
		surfb float);
		

ALTER TABLE #schema_cadastrapp.v_parcelle_surfc OWNER TO #user_cadastrapp;


CREATE MATERIALIZED VIEW #schema_cadastrapp.parcelle AS 
	SELECT parcelle.parcelle, 
		parcelle.cgocommune, -- Code commune INSEE
		parcelle.dnupla, -- Numéro de plan
		parcelle.dnvoiri, -- Numéro voirie
		parcelle.dindic, -- indice de répétition de voirie 
		parcelle.cconvo, -- Code nature de la voie
		parcelle.dvoilib, --Libellé de la voie
		parcelle.ccopre, -- Préfixe de section
		parcelle.ccosec, -- Section
		parcelle.dcntpa -- Contenance de la parcelle
 	 FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
 		'select 
			COALESCE(parcelle.parcelle,geo_parcelle.geo_parcelle) as parcelle,
			COALESCE(parcelle.ccodep||parcelle.ccodir||parcelle.ccocom,ltrim(substr(geo_parcelle.geo_parcelle,1,6),''0'')) as cgocommune,
			COALESCE(ltrim(parcelle.dnupla, ''0''),geo_parcelle.tex) as dnupla,
			ltrim(parcelle.dnvoiri, ''0'') as dnvoiri,
			parcelle.dindic,
			parcelle.cconvo,
			rtrim(parcelle.dvoilib),
			COALESCE(ltrim(parcelle.ccopre),ltrim(substr(geo_parcelle.geo_parcelle,7,3),''0'')) as ccopre,
			COALESCE(ltrim(ccosec),ltrim(substr(geo_parcelle.geo_parcelle,10,2),''0'')) as ccosec,
			COALESCE(parcelle.dcntpa,CAST(geo_parcelle.supf AS integer)) as dcntpa
		from #DBSchema_qgis.parcelle
		full outer join #DBSchema_qgis.geo_parcelle on parcelle.parcelle = geo_parcelle.geo_parcelle'::text)
	parcelle(
		parcelle character varying(19), 
		cgocommune character varying(6), 
		dnupla character varying(4),
		dnvoiri character varying(4),
		dindic character varying(1), 
		cconvo character varying(4), 
		dvoilib character varying(26), 
		ccopre character varying(3), 
		ccosec character varying(2),
		dcntpa integer);

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
   	FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
   		'select 
			COALESCE(parcelle.parcelle,geo_parcelle.geo_parcelle) as parcelle,
			COALESCE(parcelle.ccodep||parcelle.ccodir||parcelle.ccocom,ltrim(substr(geo_parcelle.geo_parcelle,1,6),''0'')) as cgocommune,
			COALESCE(ltrim(parcelle.dnupla, ''0''),geo_parcelle.tex) as dnupla,
			COALESCE(parcelle.dcntpa,CAST(geo_parcelle.supf AS integer)) as dcntpa,
			parcelle.dsrpar,
			concat(substr(parcelle.jdatat::text,1,2),''/'',substr(parcelle.jdatat::text,3,2),''/'',substr(parcelle.jdatat::text,5,4)) as jdatat,
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
			ltrim(parcelle.dnvoiri, ''0'') as dnvoiri,
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
			COALESCE(ltrim(parcelle.ccopre),ltrim(substr(geo_parcelle.geo_parcelle,7,3),''0'')) as ccopre,
			COALESCE(ltrim(ccosec),ltrim(substr(geo_parcelle.geo_parcelle,10,2),''0'')) as ccosec,
			parcelle.pdl,
			parcelle.inspireid 
		from #DBSchema_qgis.parcelle
		full outer join #DBSchema_qgis.geo_parcelle on parcelle.parcelle = geo_parcelle.geo_parcelle'::text)
	parcelledetails(
		parcelle character varying(19), 
		cgocommune character varying(6), 
		dnupla character varying(4),
		dcntpa integer, 
		dsrpar character varying(1), 
		jdatat character varying(10),
		dreflf character varying(5), 
		gpdl character varying(1), 
		cprsecr character varying(3), 
		ccosecr character varying(2), 
		dnuplar character varying(4), 
		dnupdl character varying(3), 
		gurbpa character varying(1), 
		dparpi character varying(4), 
		ccoarp character varying(1),
		gparnf character varying(1), 
		gparbat character varying(1), 
		dnvoiri character varying(4),
		dindic character varying(1), 
		ccovoi character varying(5), 
		ccoriv character varying(4),
		ccocif character varying(4), 
		cconvo character varying(4), 
		dvoilib character varying(26), 
		ccocomm character varying(3), 
		ccoprem character varying(3), 
		ccosecm character varying(2),
		dnuplam character varying(4), 
		type_filiation character varying(1), 
		annee character varying(4), 
		ccopre character varying(3),
		ccosec character varying(2), 
		pdl character varying(22),
		inspireid character varying(17))
	left join #schema_cadastrapp.v_parcelle_surfc p2 on parcelledetails.parcelle=p2.parcelle;

ALTER TABLE #schema_cadastrapp.parcelleDetails OWNER TO #user_cadastrapp;

