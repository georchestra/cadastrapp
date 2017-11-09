-- Create commune Views based on Arcopole Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.commune AS 
	SELECT 
		commune.cgocommune, 
		commune.annee, 
		commune.libcom, 
		commune.libcom_maj, 
		commune.libcom_min
  	FROM dblink('host=#DBHost_arcopole port=#DBPort_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
  		'select 
  			id_comm as cgocommune,
			annmaj as annee,
			rtrim(upper(nomcomm)) as libcom,
            rtrim(upper(nomcomm))as libcom_maj,
            rtrim(nomcomm) as libcom_min
		from #DBSchema_arcopole.dgi_comm'::text) 
	commune(
		cgocommune character varying(6), 
		annee character varying(4), 
		libcom character varying(30), 
		libcom_maj character varying(30), 
		libcom_min character varying(30));

ALTER TABLE #schema_cadastrapp.commune OWNER TO #user_cadastrapp;

