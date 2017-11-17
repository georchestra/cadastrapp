
CREATE TABLE #schema_cadastrapp.prop_ccocac (
    ccocac character varying(4) NOT NULL,
    ccocac_lib character varying(150)
);


ALTER TABLE #schema_cadastrapp.prop_ccocac OWNER TO #user_cadastrapp;


ALTER TABLE ONLY #schema_cadastrapp.prop_ccocac
    ADD CONSTRAINT ccocac_pkey PRIMARY KEY (ccocac);


INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('MAG1', 'Boutiques et magasins sur rue (exemples : commerces, restaurants, cafés ou agences bancaires pour une surface principale inférieure à 400 m²)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('MAG2', 'Commerces sans accès direct sur la rue (surface principale inférieure à 400 m²)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('MAG3', 'Magasins appartenant à un ensemble commercial(surface principale inférieure à 400 m²)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('MAG4', 'Magasins de grande surface(surface principale comprise entre 400 et 2 499 m²)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('MAG5', 'Magasins de très grande surface (surface principale égale ou supérieure à 2 500 m²)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('MAG6', 'Stations - service, stations de lavage et assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('MAG7', 'Marchés');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('BUR1', 'Locaux à usage de bureaux d’agencement ancien');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('BUR2', 'Locaux à usage de bureaux d’agencement récent');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('BUR3', 'Locaux assimilables à des bureaux mais présentant des aménagements spécifiques');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('DEP1', 'Lieux de dépôt à ciel ouvert et terrains à usage commercial ou industriel');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('DEP2', 'Lieux de dépôt couverts');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('DEP3', 'Parcs de stationnement à ciel ouvert');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('DEP4', 'Parcs de stationnement couverts');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('DEP5', 'Installations spécifiques de stockage');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('ATE1', 'Ateliers artisanaux');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('ATE2', 'Locaux utilisés pour une activité de transformation, de manutention ou de maintenance');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('ATE3', 'Chenils, viviers et autres locaux assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('HOT1', 'Hôtels « confort » (4 étoiles et plus, ou confort identique)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('HOT2', 'Hôtels « supérieur » (2 ou 3 étoiles, ou confort identique)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('HOT3', 'Hôtels « standard » (1 étoile, ou confort identique)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('HOT4', 'Foyers d’hébergement, centres d’accueil, auberges de jeunesse');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('HOT5', 'Hôtels clubs, villages de vacances et résidences hôtelières');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('SPE1', 'Salles de spectacles, musées et locaux assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('SPE2', 'Établissements ou terrains affectés à la pratique d’un sport ou à usage de spectacles sportifs');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('SPE3', 'Salles de loisirs diverses');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('SPE4', 'Terrains de camping confortables (3 étoiles et plus, ou confort identique)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('SPE5', 'Terrains de camping ordinaires(1 ou 2 étoiles, ou confort identique)');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('SPE6', 'Établissements de détente et de bien - être');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('SPE7', 'Centres de loisirs, centres de colonies de vacances, maisons de jeunes');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('ENS1', 'Écoles et institutions privées exploitées dans un but non lucratif');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('ENS2', 'Établissements d’enseignement à but lucratif');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('CLI1', 'Cliniques et Établissements hospitaliers');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('CLI2', 'Centres médico-sociaux, centres de soins, crèches, halte - garderies');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('CLI3', 'Maisons de repos, maisons de retraite (médicalisées ou non) et locaux assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('CLI4', 'Centres de rééducation, de thalassothérapie, établissements thermaux');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('IND1', 'Établissements industriels nécessitant un outillage important autres que les carrières et assimilés');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('IND2', 'carrières et Établissements assimilables');
INSERT INTO #schema_cadastrapp.prop_ccocac VALUES ('EXC1', 'Locaux ne relevant d’aucune des catégories précédentes par leurs caractéristiques sortant de l’ordinaire');
