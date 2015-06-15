-- Table: cadastreapp_qgis.groupe_autorisation

-- DROP TABLE cadastrapp_arcopole.groupe_autorisation;

CREATE TABLE cadastrapp_arcopole.groupe_autorisation
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
ALTER TABLE cadastrapp_arcopole.groupe_autorisation
  OWNER TO cadastrapp_arcopole;
COMMENT ON TABLE cadastrapp_arcopole.groupe_autorisation
  IS 'Table de correlation entre les groupes LDAP et les droits géographiques';
COMMENT ON COLUMN cadastrapp_arcopole.groupe_autorisation.id IS 'Id d''indentification de lignes';
COMMENT ON COLUMN cadastrapp_arcopole.groupe_autorisation.idgroup IS 'Identfiant du groupe LDAP devant être filtré';
COMMENT ON COLUMN cadastrapp_arcopole.groupe_autorisation.ccoinsee IS 'Code commune insee sur 6 caractères';
COMMENT ON COLUMN cadastrapp_arcopole.groupe_autorisation.ccodep IS 'Code département à mettre en relation avec le code commune';

