-- Create view parcelle, parcelledetails, v_parcelle_surfc based on Qgis Models

CREATE OR REPLACE VIEW #schema_cadastrapp.v_parcelle_surfc AS
	SELECT  v_parcelle_surfc.parcelle,
			v_parcelle_surfc.surfc 
		FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
			'select distinct 
				geo_parcelle as parcelle,
				st_area(geom) as surfc 
			from geo_parcelle'::text)
	v_parcelle_surfc(
		parcelle character varying(19),
		surfc float);

ALTER TABLE #schema_cadastrapp.v_parcelle_surfc OWNER TO #user_cadastrapp;


CREATE OR REPLACE VIEW #schema_cadastrapp.parcelle AS 
	SELECT parcelle.parcelle, 
		parcelle.ccoinsee, 
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
			ccodep||ccodir||ccocom as ccoinsee,
			dnupla,
			dnvoiri,
			dindic,
			cconvo,
			dvoilib,
			ccopre,
			ccosec,
			dcntpa
		from parcelle'::text) 
	parcelle(
		parcelle character varying(19), 
		ccoinsee character varying(6), 
		dnupla character varying(4),
		dnvoiri character varying(4),
		dindic character varying(1), 
		cconvo character varying(4), 
		dvoilib character varying(26), 
		ccopre character varying(3), 
		ccosec character varying(2),
		dcntpa integer);

ALTER TABLE #schema_cadastrapp.parcelle  OWNER TO #user_cadastrapp;


-- View: cadastreapp_qgis.parcelle

CREATE OR REPLACE VIEW #schema_cadastrapp.parcelleDetails AS 
	SELECT 
		parcelleDetails.parcelle,
		parcelleDetails.lot,
		parcelleDetails.ccocom,
		parcelleDetails.dnupla,
		parcelleDetails.dcntpa,
		parcelleDetails.dsrpar,
		parcelleDetails.dnupro,
		parcelleDetails.jdatat,
		parcelleDetails.dreflf,
		parcelleDetails.gpdl,
		parcelleDetails.cprsecr,
		parcelleDetails.ccosecr,
		parcelleDetails.dnuplar,
		parcelleDetails.dnupdl,
		parcelleDetails.gurbpa,
		parcelleDetails.dparpi,
		parcelleDetails.ccoarp,
		parcelleDetails.gparnf,
		parcelleDetails.gparbat,
		parcelleDetails.dnvoiri,
		parcelleDetails.dindic,
		parcelleDetails.ccovoi,
		parcelleDetails.ccoriv,
		parcelleDetails.ccocif,
		parcelleDetails.cconvo,
		parcelleDetails.dvoilib,
		parcelleDetails.ccocomm,
		parcelleDetails.ccoprem,
		parcelleDetails.ccosecm,
		parcelleDetails.dnuplam,
		parcelleDetails.type_filiation,
		parcelleDetails.annee,
		parcelleDetails.ccodep,
		parcelleDetails.ccodir,
		parcelleDetails.ccopre,
		parcelleDetails.ccosec,
		parcelleDetails.comptecommunal,
		parcelleDetails.pdl,
		parcelleDetails.inspireid ,
		parcelleDetails.surfc
   	FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
   		'select 
			parcelle,
			lot,
			ccocom,
			dnupla,
			dcntpa,
			dsrpar,
			dnupro,
			jdatat,
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
			dnvoiri,
			dindic,
			ccovoi,
			ccoriv,
			ccocif,
			cconvo,
			dvoilib,
			ccocomm,
			ccoprem,
			ccosecm,
			dnuplam,
			type_filiation,
			annee,
			ccodep,
			ccodir,
			ccopre,
			ccosec,
			comptecommunal,
			pdl,
			inspireid 
		from parcelle'::text) 
	parcelleDetails(parcelle character varying(19), 
		lot character varying, 
		ccocom character varying(3), 
		dnupla character varying(4),
		dcntpa integer, 
		dsrpar character varying(1), 
		dnupro character varying(6), 
		jdatat date,
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
		ccodep character varying(2), 
		ccodir character varying(1), 
		ccopre character varying(3),
		ccosec character varying(2),
		comptecommunal character varying(15), 
		pdl character varying(22),
		inspireid character varying(16))
		left join #schema_cadastrapp.v_parcelle_surfc p2 on parcelleDetails.parcelle=p2.parcelle;

ALTER TABLE #schema_cadastrapp.parcelleDetails OWNER TO #user_cadastrapp;

