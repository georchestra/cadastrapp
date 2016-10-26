-- Create view parcelle, parcelledetails, v_parcelle_surfc based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.v_parcelle_surfc AS
	SELECT  v_parcelle_surfc.parcelle,
			v_parcelle_surfc.surfc, 
			v_parcelle_surfc.surfb 
		FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
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

-- view parcelle_union
-- this view consists in showing all parcels : from the matrix data and from the geographic data
CREATE MATERIALIZED VIEW #schema_cadastrapp.parcelle_union AS
  SELECT
    parcelle_union.cgocommune,
    parcelle_union.parcelle,
    parcelle_union.ccopre,
    parcelle_union.ccosec,
    parcelle_union.dnupla,
    parcelle_union.dcntpa
  FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, '
    (
      -- LEFT JOIN
      SELECT
        ccodep||ccodir||ccocom as cgocommune,
        parcelle,
        ltrim(ccopre)  AS ccopre,
        ltrim(ccosec) AS ccosec,
        ltrim(dnupla, ''0'') as dnupla,
		dcntpa
      FROM #DBSchema_qgis.parcelle as p
        LEFT JOIN #DBSchema_qgis.geo_parcelle AS gp ON p.parcelle = gp.geo_parcelle 
      )
      UNION
      (
      -- RIGHT JOIN
      SELECT
        ltrim(substr(gp.geo_parcelle,5,6),''0'') AS cgocommune,
        gp.geo_parcelle AS parcelle,
        ltrim(substr(gp.geo_parcelle,11,3),''0'')  AS ccopre,
        ltrim(substr(gp.geo_parcelle,14,2),''0'')  AS ccosec,
        gp.tex AS dnupla,
        CAST(supf AS integer) AS dcntpa
      FROM #DBSchema_qgis.parcelle AS p
        RIGHT JOIN #DBSchema_qgis.geo_parcelle AS gp ON p.parcelle = gp.geo_parcelle 
    )'::text)
  parcelle_union(
  cgocommune character varying(6),
  parcelle character varying(19),
  ccopre character varying(3),
  ccosec character varying(2),
  dnupla character varying(4),
  dcntpa integer);

-- rights
ALTER TABLE #schema_cadastrapp.parcelle_union OWNER TO #user_cadastrapp;

CREATE MATERIALIZED VIEW #schema_cadastrapp.parcelle AS 
	SELECT u.parcelle, 
		u.cgocommune, 
		u.dnupla, 
		p.dnvoiri, 
		p.dindic, 
		p.cconvo, 
		p.dvoilib, 
		u.ccopre, 
		u.ccosec, 
		u.dcntpa
	FROM (
		#schema_cadastrapp.parcelle_union AS u
		LEFT JOIN (
			SELECT parcelle.parcelle,
				parcelle.cgocommune,
				parcelle.dnupla,
				parcelle.dnvoiri,
				parcelle.dindic,
				parcelle.cconvo,
				parcelle.dvoilib,
				parcelle.ccopre,
				parcelle.ccosec,
				parcelle.dcntpa
			FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
				'select
					parcelle,
					ccodep||ccodir||ccocom as cgocommune,
					ltrim(dnupla, ''0'') as dnupla,
					ltrim(dnvoiri, ''0'') as dnvoiri,
					dindic,
					cconvo,
					rtrim(dvoilib),
					ltrim(ccopre),
					ltrim(ccosec),
					dcntpa
				from #DBSchema_qgis.parcelle'::text)
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
				dcntpa integer)
		) AS p
		ON u.parcelle = p.parcelle
	);

ALTER TABLE #schema_cadastrapp.parcelle OWNER TO #user_cadastrapp;


-- View: cadastreapp_qgis.parcelle

CREATE MATERIALIZED VIEW #schema_cadastrapp.parcelledetails AS 
	SELECT 
		parcelledetails.parcelle,
		parcelledetails.cgocommune,
		parcelledetails.dnupla,
		parcelledetails.dcntpa,
		parcelledetails.dsrpar,
		parcelledetails.jdatat,
		parcelledetails.dreflf,
		parcelledetails.gpdl,
		parcelledetails.cprsecr,
		parcelledetails.ccosecr,
		parcelledetails.dnuplar,
		parcelledetails.dnupdl,
		parcelledetails.gurbpa,
		parcelledetails.dparpi,
		parcelledetails.ccoarp,
		parcelledetails.gparnf,
		parcelledetails.gparbat,
		parcelledetails.dnvoiri,
		parcelledetails.dindic,
		parcelledetails.ccovoi,
		parcelledetails.ccoriv,
		parcelledetails.ccocif,
		parcelledetails.cconvo,
		parcelledetails.dvoilib,
		parcelledetails.ccocomm,
		parcelledetails.ccoprem,
		parcelledetails.ccosecm,
		parcelledetails.dnuplam,
		parcelledetails.type_filiation,
		parcelledetails.annee,
		parcelledetails.ccopre,
		parcelledetails.ccosec,
		parcelledetails.pdl,
		parcelledetails.inspireid ,
		surfc,
		surfb
   	FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
   		'select 
			parcelle,
			ccodep||ccodir||ccocom as cgocommune,
			ltrim(dnupla, ''0'') as dnupla,
			dcntpa,
			dsrpar,
			concat(substr(jdatat::text,9,2),''/'',substr(jdatat::text,6,2),''/'',substr(jdatat::text,1,4)) as jdatat,
			dreflf,
			gpdl,
			cprsecr,
			ccosecr,
			dnuplar,
			dnupdl,
			gurbpa,
			dparpi,
			ccoarp,
			gparnf,
			gparbat,
			ltrim(dnvoiri, ''0'') as dnvoiri,
			rtrim(dindic) as dindic,
			ccovoi,
			ccoriv,
			ccocif,
			cconvo,
			rtrim(dvoilib) as dvoilib,
			ccocomm,
			ccoprem,
			ccosecm,
			dnuplam,
			type_filiation,
			annee,
			ltrim(ccopre),
			ltrim(ccosec),
			pdl,
			inspireid 
		from #DBSchema_qgis.parcelle'::text)
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




