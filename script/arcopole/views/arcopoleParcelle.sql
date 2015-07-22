
-- DROP VIEW #schema_cadastrapp.parcelle;

CREATE OR REPLACE VIEW #schema_cadastrapp.parcelle AS
 SELECT dgi_nbati.parcelle, dgi_nbati.ccoinsee, dgi_nbati.dnupla, dgi_nbati.dnvoiri, dgi_nbati.dindic, dgi_nbati.cconvo, dgi_nbati.dvoilib, dgi_nbati.ccopre, dgi_nbati.ccosec, dgi_nbati.dcntpa
   FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
   	'select
   		codparc as parcelle,
        substr(codparc,1,3)||substr(codcomm,4,3) as ccoinsee,
        dnupla,
        dnvoirie as dnvoiri,
        dindic,
	    cconvo,
        dvoilib,
    	substr(codparc,7,3) as ccopre,
   		substr(codparc,10,2) ccosec ,
   		dcntpa
       from #DBSchema_arcopole.dgi_nbati'::text) 
           dgi_nbati(
                parcelle character varying(19), 
                ccoinsee character varying(6),
                dnupla character varying(4), 
                dnvoiri character varying(4),
				dindic character varying(1), 
                cconvo character varying(4), 
                dvoilib character varying(26),  
                ccopre character varying(3), 
                ccosec character varying(2),
                dcntpa numeric(38,8) );
                
ALTER TABLE #schema_cadastrapp.parcelle OWNER TO #user_cadastrapp;


-- DROP VIEW #schema_cadastrapp.parcelledetails;

CREATE OR REPLACE VIEW #schema_cadastrapp.parcelledetails AS
 SELECT dgi_nbati.parcelle, dgi_nbati.lot, dgi_nbati.ccocom, dgi_nbati.dnupla, dgi_nbati.dcntpa, dgi_nbati.dsrpar, dgi_nbati.dnupro, dgi_nbati.jdatat, dgi_nbati.dreflf, dgi_nbati.gpdl, dgi_nbati.cprsecr, dgi_nbati.ccosecr, dgi_nbati.dnuplar, dgi_nbati.dnupdl, dgi_nbati.gurbpa, dgi_nbati.dparpi, dgi_nbati.ccoarp, dgi_nbati.gparnf, dgi_nbati.gparbat, dgi_nbati.dnvoiri, dgi_nbati.dindic, dgi_nbati.ccovoi, dgi_nbati.ccoriv, dgi_nbati.ccocif, dgi_nbati.cconvo, dgi_nbati.dvoilib, dgi_nbati.ccocomm, dgi_nbati.ccoprem, dgi_nbati.ccosecm, dgi_nbati.dnuplam, dgi_nbati.type_filiation, dgi_nbati.annee, dgi_nbati.ccodep, dgi_nbati.ccodir, dgi_nbati.ccopre, dgi_nbati.ccosec, dgi_nbati.comptecommunal, dgi_nbati.pdl, dgi_nbati.inspireid, surfc
   FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, '
   select
   codparc as parcelle,
                codlot as lot,
                substr(codcomm,4,3) as ccocom,
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
                FROM #DBSchema_arcopole.dgi_nbati'::text) dgi_nbati(parcelle character varying(19), lot character varying, ccocom character varying(3),
                dnupla character varying(4), dcntpa numeric(38,8), dsrpar character varying(1), dnupro character varying(12), jdatat character varying(8),
                dreflf character varying(5), gpdl character varying(1), cprsecr character varying(3), ccosecr character varying(2), dnuplar character varying(4),
                dnupdl character varying(3), gurbpa character varying(1), dparpi character varying(4), ccoarp character varying(1), gparnf character varying(1),
                gparbat character varying(1), dnvoiri character varying(4), dindic character varying(1), ccovoi character varying(5), ccoriv character varying(4),
                ccocif character varying(4), cconvo character varying(4), dvoilib character varying(26), ccocomm character varying(3), ccoprem character varying(3),
                ccosecm character varying(2), dnuplam character varying(4), type_filiation character varying(1), annee character varying(4), ccodep character varying(2), 
                ccodir character varying(1), ccopre character varying(3), ccosec character varying(2), 
                comptecommunal character varying(15), pdl character varying(22), inspireid character varying(17))
                left join #schema_cadastrapp.v_parcelle_surfc p2 on parcelle.parcelle=p2.parcelle;
                
ALTER TABLE #schema_cadastrapp.parcelledetails OWNER TO #user_cadastrapp;