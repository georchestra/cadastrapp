-- Create view parcelle, parcelledetails, v_parcelle_surfc based on Arcopole Models

-- Create view for parcelle surface
CREATE MATERIALIZED VIEW #schema_cadastrapp.v_parcelle_surfc as select  parcelle, surfc, surfb
  FROM dblink('host=#DBHost_arcopole port=#DBPort_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
    'select 
      distinct ep.id_parc as parcelle,
      round(st_area(ep.shape)) as surfc, 
      sum(round(st_area(eb.shape))) as surfb
    from #DBSchema_arcopole.edi_parc as ep
    left join #DBSchema_arcopole.edi_bati as eb on eb.codparc = ep.id_parc
    group by ep.id_parc, ep.shape		
    '::text)
  parcelle_surfc(
    parcelle character varying(19),
    surfc float, 
    surfb float);
    
  
ALTER TABLE #schema_cadastrapp.v_parcelle_surfc OWNER TO #user_cadastrapp;


-- Create view for parcelle 
CREATE MATERIALIZED VIEW #schema_cadastrapp.parcelle AS
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
    FROM dblink('host=#DBHost_arcopole port=#DBPort_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
    'select
      COALESCE(dgi_nbati.codparc,edi_parc.id_parc) as parcelle,
      COALESCE(dgi_nbati.codcomm,edi_parc.codcomm) as cgocommune,
      COALESCE(dgi_nbati.dnupla,edi_parc.numero) as dnupla,
      ltrim(dgi_nbati.dnvoirie,''0'') as dnvoiri,
      dgi_nbati.dindic,
      dgi_nbati.cconvo,
      rtrim(dgi_nbati.dvoilib) as dvoilib,
      COALESCE(regexp_replace(substr(dgi_nbati.codparc,7,3), ''0{3}'', ''''), regexp_replace(substr(edi_parc.id_parc,7,3), ''0{3}'', '''')) as ccopre,
      COALESCE(ltrim(substr(dgi_nbati.codparc,10,2),''0''), ltrim(substr(edi_parc.id_parc,10,2),''0'')) as ccosec ,
      COALESCE(CAST(dgi_nbati.dcntpa AS integer), CAST(edi_parc.supf AS integer)) as dncpta
    from #DBSchema_arcopole.dgi_nbati
	full outer join #DBSchema_arcopole.edi_parc on dgi_nbati.codparc = edi_parc.id_parc'::text) 
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


-- Create view for parcelle with details
CREATE MATERIALIZED VIEW #schema_cadastrapp.parcelledetails AS
  SELECT parcelledetails.parcelle,  
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
      parcelledetails.inspireid, 
      surfc,
      surfb
    FROM dblink('host=#DBHost_arcopole port=#DBPort_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
    'select
      COALESCE(dgi_nbati.codparc,edi_parc.id_parc) as parcelle,
      COALESCE(dgi_nbati.codcomm,edi_parc.codcomm) as cgocommune,
      COALESCE(dgi_nbati.dnupla,edi_parc.numero) as dnupla,
      COALESCE(CAST(dgi_nbati.dcntpa AS integer), CAST(edi_parc.supf AS integer)) as dncpta,
      dgi_nbati.dsrpar,
      concat(substr(dgi_nbati.jdatat,1,2),''/'',substr(dgi_nbati.jdatat,3,2),''/'',substr(dgi_nbati.jdatat,5,4)) as jdatat,
      dgi_nbati.dreflf,
      dgi_nbati.gpdl,
      dgi_nbati.cprsecr,
      dgi_nbati.ccosecr,
      dgi_nbati.dnuplar,
      dgi_nbati.dnupdl,
      dgi_nbati.gurbpa,
      dgi_nbati.dparpi,
      dgi_nbati.ccoarp,
      dgi_nbati.gparnf,
      dgi_nbati.gparbat,
      ltrim(dgi_nbati.dnvoirie, ''0'') as dnvoiri,
      dgi_nbati.dindic,
      ltrim(dgi_nbati.ccovoi, ''0'') as ccovoi,
      dgi_nbati.ccoriv,
      dgi_nbati.ccocif,
      dgi_nbati.cconvo,
      rtrim(dgi_nbati.dvoilib) as dvoilib,
      dgi_nbati.ccocom as ccocomm,
      dgi_nbati.ccoprem,
      dgi_nbati.ccosecm,
      dgi_nbati.dnuplam,
      dgi_nbati.type as type_filiation,
      substr(dgi_nbati.codlot,1,4) as annee ,
      COALESCE(regexp_replace(substr(dgi_nbati.codparc,7,3), ''0{3}'', ''''), regexp_replace(substr(edi_parc.id_parc,7,3), ''0{3}'', '''')) as ccopre,
      COALESCE(ltrim(substr(dgi_nbati.codparc,10,2),''0''), ltrim(substr(edi_parc.id_parc,10,2),''0'')) as ccosec ,
      ''pdl'' as pdl, 
      ''FR''||dgi_nbati.codparc as inspireId
    from #DBSchema_arcopole.dgi_nbati
	full outer join #DBSchema_arcopole.edi_parc on dgi_nbati.codparc = edi_parc.id_parc'::text) 
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
  
ALTER TABLE #schema_cadastrapp.parcelledetails OWNER TO #user_cadastrapp;
