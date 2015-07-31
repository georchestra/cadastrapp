-- Create view parcelle, parcelledetails, v_parcelle_surfc based on Arcopole Models

-- Create view for parcelle surface
CREATE OR REPLACE VIEW #schema_cadastrapp.v_parcelle_surfc as select  parcelle, surfc
	FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select 
			distinct id_parc as parcelle,
			st_area(shape) as surfc 
		from #DBSchema_arcopole.edi_parc'::text)
	parcelle_surfc(
		parcelle character varying(19),
		surfc float);
	
ALTER TABLE #schema_cadastrapp.v_parcelle_surfc OWNER TO #user_cadastrapp;


-- Create view for parcelle 
CREATE OR REPLACE VIEW #schema_cadastrapp.parcelle AS
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
		FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select
			codparc as parcelle,
			codcomm as cgocommune,
			dnupla,
			dnvoirie as dnvoiri,
			dindic,
			cconvo,
			dvoilib,
			substr(codparc,7,3) as ccopre,
			substr(codparc,10,2) ccosec ,
			dcntpa
		from #DBSchema_arcopole.dgi_nbati'::text) 
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
		dcntpa numeric(38,8));
                
ALTER TABLE #schema_cadastrapp.parcelle OWNER TO #user_cadastrapp;


-- Create view for parcelle with details
CREATE OR REPLACE VIEW #schema_cadastrapp.parcelledetails AS
	SELECT parcelledetails.parcelle, 
			parcelledetails.lot, 
			parcelledetails.cgocommune, 
			parcelledetails.dnupla, 
			parcelledetails.dcntpa, 
			parcelledetails.dsrpar, 
			parcelledetails.dnupro, 
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
			parcelledetails.comptecommunal, 
			parcelledetails.pdl, 
			parcelledetails.inspireid, 
			surfc
		FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select
			codparc as parcelle,
			codlot as lot,
			codcomm as cgocommune,
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
            dnvoirie as dnvoiri,
            dindic,
            ccovoi,
            ccoriv,
            ccocif,
            cconvo,
            dvoilib,
            ccocom as ccocomm,
            ccoprem,
            ccosecm,
            dnuplam,
            type as type_filiation,
            substr(codlot,1,4) as annee ,
            substr(codparc,1,2) as ccodep,
            substr(codparc,3,1) as codir,     
    		substr(codparc,7,3) as ccopre,
   			substr(codparc,10,2) ccosec ,
            dnupro as comptecommunal,
            ''pdl'' as pdl, ''FR''||codparc as inspireId
		from #DBSchema_arcopole.dgi_nbati'::text) 
	parcelledetails(
		parcelle character varying(19), 
		lot character varying, 
		cgocommune character varying(6),
		dnupla character varying(4), 
		dcntpa numeric(38,8), 
		dsrpar character varying(1), 
		dnupro character varying(12), 
		jdatat character varying(8),
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
		comptecommunal character varying(15), 
		pdl character varying(22), 
		inspireid character varying(17))
	left join #schema_cadastrapp.v_parcelle_surfc p2 on parcelledetails.parcelle=p2.parcelle;
                
ALTER TABLE #schema_cadastrapp.parcelledetails OWNER TO #user_cadastrapp;