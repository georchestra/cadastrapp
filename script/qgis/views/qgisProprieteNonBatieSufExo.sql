-- Create view proprietenonbatiesufexo based on Qgis Models

CREATE OR REPLACE VIEW #schema_cadastrapp.proprietenonbatiesufexo AS 
	SELECT 
		proprietenonbatiesufexo.parcelle, 
		proprietenonbatiesufexo.id_local, 
		proprietenonbatiesufexo.comptecommunal, 
		proprietenonbatiesufexo.cgocommune,  
		proprietenonbatiesufexo.ccolloc, 
		proprietenonbatiesufexo.gnexts, 
		proprietenonbatiesufexo.jandeb, 
		proprietenonbatiesufexo.jfinex, 
		proprietenonbatiesufexo.fcexb, 
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
			sufex.fcexn as fcexb,
			sufex.pexn	
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
 		janimp character varying(4), 
 		fcexb character varying(10), 
 		pexn integer);


ALTER TABLE #schema_cadastrapp.proprietenonbatiesufexo OWNER TO #user_cadastrapp;
