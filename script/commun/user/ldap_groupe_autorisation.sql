CREATE MATERIALIZED VIEW cadastrapp.org_autorisation AS
	SELECT 
		regexp_replace(cn, '[''\[\]]', '', 'g') AS idorg,
		unnest(string_to_array(regexp_replace(description, '[''\[\]]', '', 'g'), ',')) as inseecommune,
		NULL as ccodep
	FROM
		cadastrapp.organisations;

ALTER TABLE cadastrapp.org_autorisation
  OWNER TO cadastrapp;

CREATE TABLE cadastrapp.role_autorisation
(
  idrole character varying(50), -- Identifiant du role LDAP devant être filtré
  cgocommune character varying(6), -- Code commune INSEE
  ccodep character varying(3), -- Code département à mettre en relation avec le code commune
  CONSTRAINT "role_autorisation_UK" UNIQUE (cgocommune , ccodep , idrole )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE cadastrapp.role_autorisation
  OWNER TO cadastrapp;

CREATE VIEW cadastrapp.groupe_autorisation AS
	SELECT
		idorg AS idgroup,
		LEFT(inseecommune,2)||'0'||RIGHT(inseecommune,-2) AS cgocommune,
		ccodep
	FROM
		cadastrapp.org_autorisation
	UNION
	SELECT
		idrole AS idgroup,
		cgocommune,
		ccodep
	FROM
		cadastrapp.role_autorisation;
ALTER TABLE cadastrapp.groupe_autorisation
  OWNER TO cadastrapp;