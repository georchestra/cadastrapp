CREATE MATERIALIZED VIEW #schema_cadastrapp.org_autorisation AS
	SELECT 
		regexp_replace(cn, '[''\[\]]', '', 'g') AS idorg,
		unnest(string_to_array(regexp_replace(description, '[''\[\]]', '', 'g'), ',')) as inseecommune,
		NULL as ccodep
	FROM
		#schema_cadastrapp.organisations;

ALTER TABLE #schema_cadastrapp.org_autorisation
  OWNER TO #user_cadastrapp;

CREATE TABLE #schema_cadastrapp.role_autorisation
(
  idrole character varying(50), -- Identifiant du role LDAP devant être filtré
  cgocommune character varying(6), -- Code commune INSEE
  ccodep character varying(3), -- Code département à mettre en relation avec le code commune
  CONSTRAINT "role_autorisation_UK" UNIQUE (cgocommune , ccodep , idrole )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE #schema_cadastrapp.role_autorisation
  OWNER TO #user_cadastrapp;

COMMENT ON TABLE #schema_cadastrapp.role_autorisation IS 'Table de correlation entre les roles LDAP et les droits géographiques';

COMMENT ON COLUMN #schema_cadastrapp.role_autorisation.idrole IS 'Identfiant du role LDAP devant être filtré';
COMMENT ON COLUMN #schema_cadastrapp.role_autorisation.cgocommune IS 'Code commune INSEE version cadastre (6 char ->  made with ccodpe + ccodir + ccocom)';
COMMENT ON COLUMN #schema_cadastrapp.role_autorisation.ccodep IS 'Code département à mettre en relation avec le code commune';


CREATE VIEW #schema_cadastrapp.groupe_autorisation AS
	SELECT
		idorg AS idgroup,
		LEFT(inseecommune,2)||'0'||RIGHT(inseecommune,-2) AS cgocommune,
		ccodep
	FROM
		#schema_cadastrapp.org_autorisation
	UNION
	SELECT
		idrole AS idgroup,
		cgocommune,
		ccodep
	FROM
		#schema_cadastrapp.role_autorisation;
ALTER TABLE #schema_cadastrapp.groupe_autorisation
  OWNER TO #user_cadastrapp;
