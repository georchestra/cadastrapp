CREATE OR REPLACE VIEW cadastreapp_qgis.proprietenonbatie AS 
 SELECT proprietenonbatie.jdatat, proprietenonbatie.comptecommunal, proprietenonbatie.dnupro, proprietenonbatie.ccodep, proprietenonbatie.ccodir, proprietenonbatie.ccocom, proprietenonbatie.ccopre, proprietenonbatie.dnupla, proprietenonbatie.parcelle, proprietenonbatie.ccovoi, proprietenonbatie.voie, proprietenonbatie.ccoriv, proprietenonbatie.dnvoiri, proprietenonbatie.dindic, proprietenonbatie.cconvo, proprietenonbatie.dvoilib, proprietenonbatie.dparpi, proprietenonbatie.gpafpd, proprietenonbatie.ccostn, proprietenonbatie.ccosub, proprietenonbatie.cgrnum, proprietenonbatie.dsgrpf, proprietenonbatie.dclssf, proprietenonbatie.cnatsp, proprietenonbatie.dcntsf, proprietenonbatie.drcsuba, proprietenonbatie.pdl, proprietenonbatie.dnulot, proprietenonbatie.ccolloc, proprietenonbatie.gnexts, proprietenonbatie.jandeb, proprietenonbatie.jfinex, proprietenonbatie.fcexn, proprietenonbatie.pexn, proprietenonbatie.dreflf
   FROM dblink('host=MQ-CMS-CRAI-001.fasgfi.fr dbname=qadastre user=cadastreapp password=c'::text, 'select p.jdatat,p.comptecommunal,p.dnupro,p.ccodep, p.ccodir,p.ccocom,p.ccopre,p.dnupla,p.parcelle,p.ccovoi,p.voie,p.ccoriv,
p.dnvoiri,p.dindic,p.cconvo,p.dvoilib,p.dparpi,p.gpafpd,suf.ccostn,suf.ccosub,suf.cgrnum,suf.dsgrpf,
suf.dclssf,suf.cnatsp,suf.dcntsf,suf.drcsuba,suf.pdl,suf.dnulot,sufex.ccolloc,sufex.gnexts,sufex.jandeb,
sufex.jfinex,sufex.fcexn,sufex.pexn,p.dreflf
from parcelle p 
left join voie v on v.voie=p.voie
left join suf on suf.comptecommunal=p.comptecommunal and p.parcelle=suf.parcelle
left join sufexoneration as sufex on sufex.suf=suf.suf
'::text) proprietenonbatie(jdatat date, comptecommunal character varying(15), dnupro character varying(6),
 ccodep character varying(2), ccodir character varying(1), ccocom character varying(3),
 ccopre character varying(3), dnupla character varying(4), parcelle character varying(19),
 ccovoi character varying(5), voie character varying(19), ccoriv character varying(4), 
 dnvoiri character varying(4), dindic character varying(1), cconvo character varying(4), 
 dvoilib character varying(26), dparpi character varying(4), gpafpd character varying(1),
 ccostn character varying(1), ccosub character varying(2), cgrnum character varying(2),
 dsgrpf character varying(2), dclssf character varying(2), cnatsp character varying(5),
 dcntsf integer, drcsuba numeric(10,2), pdl character varying(22), dnulot character varying(7), 
 ccolloc character varying(2), gnexts character varying(2), jandeb character varying(4),
 jfinex character varying(4), fcexn character varying(10), pexn 
integer, dreflf character varying(5));