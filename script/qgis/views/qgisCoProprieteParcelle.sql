-- Create view making link beetween parcelle, owners and lots based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.co_propriete_parcelle AS 
	SELECT co_propriete_parcelle.lots, 
		co_propriete_parcelle.parcelle,
		co_propriete_parcelle.comptecommunal -- Compte communal du propriétaire
  	FROM dblink('host=#DBHost_qgis port=#DBPort_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text,
  		'select  
  			lo.lots,
  			lo.parcelle,
  			lo.comptecommunal
		from #DBSchema_qgis.lots lo'::text) 
	co_propriete_parcelle (
		lots character varying(29), 
		parcelle  character varying(19), 
		comptecommunal character varying(15));

ALTER TABLE #schema_cadastrapp.co_propriete_parcelle OWNER TO #user_cadastrapp;
