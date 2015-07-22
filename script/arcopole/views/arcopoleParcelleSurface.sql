-- View: cadastrapp_arcopole.parcelle

-- DROP VIEW #schema_cadastrapp.v_parcelle_surfc;

create view  #schema_cadastrapp.v_parcelle_surfc as select  * 
	FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
		'select distinct id_parc as parcelle,st_area(shape) as surfc from #DBSchema_arcopole.edi_parc'::text)
			parcelle_surfc(parcelle character varying(19),surfc float);
	
ALTER TABLE #schema_cadastrapp.v_parcelle_surfc OWNER TO #user_cadastrapp;





