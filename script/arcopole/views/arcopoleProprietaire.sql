create view #schema_cadastrapp.proprietaire as 
SELECT *
   FROM dblink('host=#DBHost_arcopole dbname=#DBName_arcopole user=#DBUser_arcopole password=c'::text, 'select 
    id_prop as     proprietaire,
id_prop as dnupro,
codlot as lot,
dnulp,
ccocif,
dnuper,
ccodro as ccodro_c,
ccodem as ccodem_c,
gdesip,
gtoper,
ccoqua as ccoqua_c,
dnatpr as dnatpr_c,
ccogrm as ccogrm_c,
dsglpm,
dforme,
ddenom,
gtyp3,
gtyp4,
gtyp5,
gtyp6,
dlign3,
dlign4,
dlign5,
dlign6,
ccopay,
ccodepla2 as ccodep1a2,
ccodira,
ccomadr as ccocom_adr,
ccovoi,
ccoriv,
dnvoiri,
dindic,
ccopos,
dnirpp,
dqualp,
dnomlp,
dprnlp,
jdatnss,
dldnss,
epxnee,
dnomcp,
dprncp,
dformjur,
''dsiren'' as dsiren,
substr(id_prop,1,2) as ccodep,
substr(id_prop,3,1) as ccodir ,
substr(id_prop,4,3) as ccocom,
id_prop as  comptecommunal from #DBSchema_arcopole.dgi_prop'::text) dgi_prop(
id_proprietaire character varying(20), dnupro character varying(12), lot character varying, dnulp character varying(2), 
ccocif character varying(4), dnuper character varying(6), ccodro_c character varying(1), ccodem_c character varying(1),
 gdesip character varying(1), gtoper character varying(1), ccoqua_c character varying(1), dnatpr_c character varying(3),
  ccogrm_c character varying(2), dsglpm character varying(10), dforme character varying(7), ddenom character varying(60),
   gtyp3 character varying(1), gtyp4 character varying(1), gtyp5 character varying(1), gtyp6 character varying(1), 
   dlign3 character varying(30), dlign4 character varying(36), dlign5 character varying(30), dlign6 character varying(32), 
   ccopay character varying(3), ccodep1a2 character varying(2), ccodira character varying(1), ccocom_adr character varying(3), 
   ccovoi character varying(5), ccoriv character varying(4), dnvoiri character varying(4), dindic character varying(1), 
   ccopos character varying(5), dnirpp character varying(10), dqualp character varying(3), dnomlp character varying(30), 
   dprnlp character varying(15), jdatnss character varying(10), dldnss character varying(58), epxnee character varying(3),
    dnomcp character varying(30), dprncp character varying(15), dformjur character varying(4), dsiren character varying(10),
     ccodep character varying(2), ccodir character varying(1), ccocom character varying(3), comptecommunal character varying(15))
   LEFT JOIN #schema_cadastrapp.prop_ccodro ON dgi_prop.ccodro_c::text = prop_ccodro.ccodro::text
   LEFT JOIN #schema_cadastrapp.prop_ccoqua ON dgi_prop.ccoqua_c::text = prop_ccoqua.ccoqua::text
   LEFT JOIN #schema_cadastrapp.prop_ccogrm ON dgi_prop.ccogrm_c::text = prop_ccogrm.ccogrm::text
   LEFT JOIN #schema_cadastrapp.prop_ccodem ON dgi_prop.ccodem_c::text = prop_ccodem.ccodem::text
   LEFT JOIN #schema_cadastrapp.prop_dnatpr ON dgi_prop.dnatpr_c::text = prop_dnatpr.dnatpr::text

ALTER TABLE #schema_cadastrapp.proprietaire OWNER TO #user_cadastrapp;








