-- View: cadastrapp_qgis.parcelle

DROP VIEW #schema_cadastrapp.parcelle;

CREATE OR REPLACE VIEW #schema_cadastrapp.parcelle AS 
 SELECT parcelle.parcelle, parcelle.ccoinsee, parcelle.dnupla, parcelle.dnvoiri, parcelle.dindic, parcelle.cconvo, parcelle.dvoilib, parcelle.ccopre, parcelle.ccosec, parcelle.dcntpa
   FROM dblink('host=localhost dbname=qadastre user=cadastre password=cadastre'::text, 
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
	parcelle(parcelle character varying(19), 
	ccoinsee character varying(6), 
	dnupla character varying(4),
	dnvoiri character varying(4),
	dindic character varying(1), 
	cconvo character varying(4), 
	dvoilib character varying(26), 
	ccopre character varying(3), 
	ccosec character varying(2),
	dcntpa integer);

ALTER TABLE #schema_cadastrapp.parcelle  OWNER TO cadastrapp;


-- View: cadastreapp_qgis.parcelle

-- DROP VIEW #schema_cadastrapp.parcelleDetails;

CREATE OR REPLACE VIEW #schema_cadastrapp.parcelleDetails AS 
 SELECT 
 	parcelle.parcelle,
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
	inspireid ,
	surfc
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
		from parcelle 	
		'::text) 
	parcelle(parcelle character varying(19), 
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
		left join #schema_cadastrapp.v_parcelle_surfc p2 on parcelle.parcelle=p2.parcelle;

ALTER TABLE #schema_cadastrapp.parcelleDetails OWNER TO #user_cadastrapp;






