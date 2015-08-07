-- Create link table beetween parcelle and unite fonciere

CREATE TABLE #schema_cadastrapp.uf_parcelle AS
	SELECT uf_parcelle.parcelle,
		uf_parcelle.uf,
		uf_parcelle.comptecommunal
	FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=#DBpasswd_arcopole'::text, 
  		'select 
  			distinct p.id_parc as parcelle,
  			z.id_uf as uf,
  			z.dnupro as comptecommunal
		from #DBSchema_arcopole.edi_parc AS p
			join cadastre.rm_uf AS z ON  ST_Contains(z.shape, p.shape) 
			order by z.id_uf,z.dnupro'::text) 
	uf_parcelle(
		parcelle character varying(15), 
		uf integer, 
		comptecommunal character varying(12)
);

ALTER TABLE #schema_cadastrapp.uf_parcelle OWNER TO #user_cadastrapp;