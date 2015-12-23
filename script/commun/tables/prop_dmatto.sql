CREATE TABLE #schema_cadastrapp.prop_dmatto (
    code character varying(255) NOT NULL,
    description character varying(255)
);


ALTER TABLE #schema_cadastrapp.prop_dmatto OWNER TO #user_cadastrapp;


ALTER TABLE ONLY #schema_cadastrapp.prop_dmatto
    ADD CONSTRAINT code_dmatto_pkey PRIMARY KEY (code);
    
    
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('10', 'Tuiles et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('20', 'Ardoises et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('30', 'Zinc - aluminium et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('90', 'Autres materiaux et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('03', 'Zinc - aluminium');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('00', 'Indeterminé');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('01', 'Tuiles');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('12', 'Tuiles et ardoises');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('13', 'Tuiles et zinc - aluminium');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('14', 'Tuiles et béton');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('19', 'Tuiles et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('21', 'Tuiles et ardoises');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('23', 'Ardoises et zinc - aluminium');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('24', 'Ardoises et béton');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('29', 'Ardoises et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('31', 'Tuiles et zinc - aluminium');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('32', 'Ardoises et zinc - aluminium');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('34', 'Zinc - aluminium et beton');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('39', 'Zinc - aluminium et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('41', 'Tuiles et béton');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('42', 'Ardoises et béton');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('43', 'Zinc - aluminium et béton');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('49', 'Beton et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('09', 'Autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('91', 'Tuiles et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('92', 'Ardoises et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('93', 'Zinc - aluminium et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('94', 'Béton et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('02', 'Ardoises');
INSERT INTO #schema_cadastrapp.prop_dmatto (code, description) VALUES ('04', 'Béton');

