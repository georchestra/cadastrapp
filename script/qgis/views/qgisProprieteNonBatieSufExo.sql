-- Create view proprietenonbatiesufexo based on Qgis Models

CREATE MATERIALIZED VIEW #schema_cadastrapp.proprietenonbatiesufexo AS 
	SELECT 
		proprietenonbatiesufexo.parcelle, 
		proprietenonbatiesufexo.id_local, 
		proprietenonbatiesufexo.comptecommunal, 
		proprietenonbatiesufexo.cgocommune,  
		proprietenonbatiesufexo.ccolloc, 
		proprietenonbatiesufexo.gnexts, 
		proprietenonbatiesufexo.jandeb, 
		proprietenonbatiesufexo.jfinex, 
		proprietenonbatiesufexo.rcexnba, 
		proprietenonbatiesufexo.fcexn, 
		proprietenonbatiesufexo.pexn
	FROM dblink('host=#DBHost_qgis dbname=#DBName_qgis user=#DBUser_qgis password=#DBpasswd_qgis'::text, 
		'select 
			p.parcelle,
			sufex.suf as id_local,
			p.comptecommunal,
			p.ccodep || p.ccodir ||	p.ccocom as cgocommune,
			sufex.ccolloc,
			sufex.gnexts,
			sufex.jandeb,
			sufex.jfinex,
			CAST (sufex.rcexnba AS INTEGER) as rcexnba,
			sufex.fcexn,
			CAST (sufex.pexn AS INTEGER) as pexn
		from #DBSchema_qgis.parcelle p
			left join #DBSchema_qgis.suf on suf.comptecommunal=p.comptecommunal and p.parcelle=suf.parcelle
			left join #DBSchema_qgis.sufexoneration as sufex on sufex.suf=suf.suf'::text)
	proprietenonbatiesufexo(
		parcelle character varying(19),
		id_local character varying(21),  
		comptecommunal character varying(15), 
		cgocommune character varying(6), 
		ccolloc character varying(2), 
		gnexts character varying(2), 
		jandeb character varying(4),
		jfinex character varying(4), 
		rcexnba integer, 
		fcexn character varying(10),
		pexn integer);


ALTER TABLE #schema_cadastrapp.proprietenonbatiesufexo OWNER TO #user_cadastrapp;
