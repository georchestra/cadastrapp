-- Table: cadastreapp_qgis.groupe_autorisation

-- DROP TABLE #schema_cadastrapp.groupe_autorisation;

CREATE TABLE #schema_cadastrapp.groupe_autorisation
(
  id serial NOT NULL, -- Id d'indentification de lignes
  idgroup character varying(30), -- Identfiant du groupe LDAP devant être filtré
  ccoinsee character varying(6), -- Code commune insee sur 6 caractères
  ccodep character varying(3), -- Code département à mettre en relation avec le code commune
  CONSTRAINT groupe_autorisation_pk PRIMARY KEY (id ),
  CONSTRAINT "groupe_autorisation_UK" UNIQUE (ccoinsee , ccodep , idgroup )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE #schema_cadastrapp.groupe_autorisation
  OWNER TO #user_cadastrapp;
COMMENT ON TABLE #schema_cadastrapp.groupe_autorisation
  IS 'Table de correlation entre les groupes LDAP et les droits géographiques';
COMMENT ON COLUMN #schema_cadastrapp.groupe_autorisation.id IS 'Id d''indentification de lignes';
COMMENT ON COLUMN #schema_cadastrapp.groupe_autorisation.idgroup IS 'Identfiant du groupe LDAP devant être filtré';
COMMENT ON COLUMN #schema_cadastrapp.groupe_autorisation.ccoinsee IS 'Code commune insee sur 6 caractères';
COMMENT ON COLUMN #schema_cadastrapp.groupe_autorisation.ccodep IS 'Code département à mettre en relation avec le code commune';

