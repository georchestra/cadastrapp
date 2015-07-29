-- HabitationDetails
-- Create views deschabitation, descproffessionnel, descdependance based on Arcopole Models


CREATE OR REPLACE VIEW  #schema_cadastrapp.descproffessionnel AS 
	SELECT *
		FROM dblink('host=GD-CMS-CRAI-001.fasgfi.fr dbname=cadastreArcopole user=cadastreapp password=c'::text, '
			select 
				id_pev as pev,invar,
				substr(codlot,1,4) as annee,dnudes,
				vsurzt 
			from #DBSchema_arcopole.dgi_PPROF  '::text) 
	descproffessionnel(
			pev character varying(19),
			invar character varying(16), 
			annee character varying(4), 
			dnudes character varying(3), 
			vsurzt character varying(9));
			
ALTER TABLE #schema_cadastrapp.descproffessionnel OWNER TO #user_cadastrapp;


CREATE OR REPLACE VIEW #schema_cadastrapp.deschabitation AS 
	SELECT *
		FROM dblink('host=GD-CMS-CRAI-001.fasgfi.fr dbname=cadastreArcopole user=cadastreapp password=c'::text, 
		'select 
			pev.id_pev as pev,
			substr(pev.codlot,1,4) as annee,pev.invar,
 			dnupev,pev.ccoaff,
 			''Partie principale d''''habitation'' as dnupev_lib,
			ccoaff.description as ccoaff_lib, 
 			pevp.dnudes,
 			pevp.detent,
 			pevp.dsupdc,
 			pevp.dnbniv,
 			pevp.dnbpdc,
			dnbppr,
			dnbsam,
			dnbcha,
			dnbcu8,
			dnbcu9,
			dnbsea,
			dnbann,
			dnbbai,
			dnbdou,
			dnblav,
			dnbwc,
			geaulc,
			gelelc,
			ggazlc,
			gchclc,
			gteglc,
			gesclc,
			gasclc,
			gvorlc,
			cconad1.description as cconad_ga,
			cconad2.description as cconad_cv,
			cconad3.description as cconad_gr,
			cconad4.description as cconav_tr,
			dsueic_ga,
			dsueic_cv,
			dsueic_gr,
			dsueic_tr
		from #DBSchema_arcopole.DGI_PEV  as pev 
  			left join #DBSchema_arcopole.dgi_phab pevp on pev.id_pev=pevp.id_pev
			left join #DBSchema_arcopole.dom_ccoaff as ccoaff on pev.ccoaff=ccoaff.code  
			left join #DBSchema_arcopole.dom_cconad cconad1 on pevp.cconad_ga=cconad1.code 
			left join #DBSchema_arcopole.dom_cconad cconad2 on pevp.cconad_cv=cconad2.code 
			left join #DBSchema_arcopole.dom_cconad cconad3 on pevp.cconad_gr=cconad3.code 
			left join #DBSchema_arcopole.dom_cconad cconad4 on pevp.cconad_tr=cconad4.code '::text) 
	deschabitation(
			pev character varying(19), 
			annee character varying(4), 
			invar character varying(16), 
			dnupev character varying(3), 
			ccoaff character varying(1), 
			dnupev_lib character varying(50), 
			ccoaff_lib character varying(150), 
			dnudes character varying(3), 
			detent character varying(1),
			dsupdc integer, 
			dnbniv character varying(2), 
			dnbpdc character varying(2), 
			dnbppr character varying(2), 
			dnbsam character varying(2), 
			dnbcha character varying(2),
			dnbcu8 character varying(2), 
			dnbcu9 character varying(2), 
			dnbsea character varying(2), 
			dnbann character varying(2), 
			dnbbai character varying(2), 
			dnbdou character varying(2), 
			dnblav character varying(2), 
			dnbwc character varying(2), 
			geaulc character varying(1), 
			gelelc character varying(1), 
			ggazlc character varying(1), 
			gchclc character varying(1),
			gteglc character varying(1), 
			gesclc character varying(1), 
			gasclc character varying(1),
			gvorlc character varying(1), 
			cconad_ga character varying(150), 
			cconav_cv character varying(150),
			cconad_gr character varying(150), 
			cconav_tr character varying(150), 
			dsueic_ga numeric, 
			dsueic_cv numeric, 
			dsueic_gr numeric, 
			dsueic_tr numeric);

ALTER TABLE #schema_cadastrapp.deschabitation OWNER TO #user_cadastrapp;

CREATE OR REPLACE VIEW #schema_cadastrapp.descdependance AS 
	select *
		FROM dblink('host=GD-CMS-CRAI-001.fasgfi.fr dbname=cadastreArcopole user=cadastreapp password=c'::text, 
		'select 
			id_pev as pev,
			invar,
			substr(codlot,1,4) as annee,
			dnudes,
			description as cconad_lib,
			dsudep,
			dnbbai,
			dnbdou,
			dnblav,
			dnbwc,
			geaulc,
			GELECL as gelelc,
			gchclc
		from #DBSchema_arcopole.dgi_dep as pevdependances
			left join #DBSchema_arcopole.dom_cconad as cconad on pevdependances.cconad=cconad.code'::text) 
	descdependance(
		pev character varying(19), 
		invar character varying(16), 
		annee character varying(4), 
		dnudes character varying(6), 
		cconad_lib character varying(150),
		dsudep character varying(6), 
		dnbbai character varying(2), 
		dnbdou character varying(2), 
		dnblav character varying(2), 
		dnbwc character varying(2), 
		geaulc character varying(1), 
		gelelc character varying(1), 
		gchclc character varying(1));

ALTER TABLE #schema_cadastrapp.descdependance OWNER TO #user_cadastrapp;

