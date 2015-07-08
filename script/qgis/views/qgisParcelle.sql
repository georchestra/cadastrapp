-- View: cadastreapp_qgis.parcelle

-- DROP VIEW #schema_cadastrapp.parcelle;

CREATE OR REPLACE VIEW #schema_cadastrapp.parcelle AS 
 SELECT parcelle.parcelle, parcelle.lot, parcelle.ccocom, parcelle.dnupla, parcelle.dcntpa, parcelle.dsrpar, parcelle.dnupro, parcelle.jdatat, parcelle.dreflf, parcelle.gpdl, parcelle.cprsecr, parcelle.ccosecr, parcelle.dnuplar, parcelle.dnupdl, parcelle.gurbpa, parcelle.dparpi, parcelle.ccoarp, parcelle.gparnf, parcelle.gparbat, parcelle.dnvoiri, parcelle.dindic, parcelle.ccovoi, parcelle.ccoriv, parcelle.ccocif, parcelle.cconvo, parcelle.dvoilib, parcelle.ccocomm, parcelle.ccoprem, parcelle.ccosecm, parcelle.dnuplam, parcelle.type_filiation, parcelle.annee, parcelle.ccodep, parcelle.ccodir, parcelle.ccopre, parcelle.ccosec, parcelle.comptecommunal, parcelle.pdl, parcelle.inspireid
   FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=c'::text, 'select 
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
	inspireid from parcelle'::text) parcelle(parcelle character varying(19), lot character varying, ccocom character varying(3), dnupla character varying(4), dcntpa integer, dsrpar character varying(1), dnupro character varying(6), jdatat date, dreflf character varying(5), gpdl character varying(1), cprsecr character varying(3), ccosecr character varying(2), dnuplar character varying(4), dnupdl character varying(3), gurbpa character varying(1), dparpi character varying(4), ccoarp character varying(1), gparnf character varying(1), gparbat character varying(1), dnvoiri character varying(4), dindic character varying(1), ccovoi character varying(5), ccoriv character varying(4), ccocif character varying(4), cconvo character varying(4), dvoilib character varying(26), ccocomm character varying(3), ccoprem character varying(3), ccosecm character varying(2), dnuplam character varying(4), type_filiation character varying(1), annee character varying(4), ccodep character varying(2), ccodir character varying(1), ccopre character varying(3), ccosec character varying(2), comptecommunal character varying(15), pdl character varying(22), inspireid character varying(16));

ALTER TABLE #schema_cadastrapp.parcelle OWNER TO #user_cadastrapp;

