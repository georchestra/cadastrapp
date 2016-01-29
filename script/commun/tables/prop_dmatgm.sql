CREATE TABLE #schema_cadastrapp.prop_dmatgm (
    code character varying(255) NOT NULL,
    description character varying(255)
);


ALTER TABLE #schema_cadastrapp.prop_dmatgm OWNER TO #user_cadastrapp;


ALTER TABLE ONLY #schema_cadastrapp.prop_dmatgm
    ADD CONSTRAINT code_dmatgm_pkey PRIMARY KEY (code);

INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('00', 'Indeterminé');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('01', 'Pierre');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('12', 'Pierre et meulière');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('13', 'Pierre et béton');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('14', 'Pierre et brique');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('15', 'Pierre et aggloméré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('16', 'Pierre et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('19', 'Pierre et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('02', 'Meulière');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('21', 'Pierre et meulière');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('23', 'Meulière et béton');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('24', 'Meulière et brique');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('25', 'Meulière et aggloméré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('26', 'Meulière et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('29', 'Meulière et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('03', 'Béton');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('31', 'Pierre et béton');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('32', 'Meulière et béton');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('34', 'Béton et brique');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('35', 'Béton et aggloméré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('36', 'Béton et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('39', 'Béton et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('04', 'Briques');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('41', 'Pierre et brique');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('42', 'Meulière et brique');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('43', 'Béton et brique');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('45', 'Brique et aggloméré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('46', 'Brique et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('49', 'Brique et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('05', 'Aggloméré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('51', 'Pierre et agglomeré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('52', 'Meulière et agglomeré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('53', 'Béton et aggloméré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('54', 'Brique et aggloméré');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('56', 'Aggloméré et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('59', 'Aggloméré et autres materiaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('06', 'Bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('61', 'Pierre et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('62', 'Meulière et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('63', 'Béton et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('64', 'Brique et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('65', 'Aggloméré et bois');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('69', 'Bois et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('09', 'Autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('92', 'Meulière et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('93', 'Béton et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('94', 'Brique et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('95', 'Aggloméré et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('96', 'Bois et autres matériaux');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('10', 'Pierre et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('20', 'Meulière et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('30', 'Béton et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('40', 'Briques et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('50', 'Aggloméré et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('60', 'Bois et indéterminé');
INSERT INTO #schema_cadastrapp.prop_dmatgm ( code, description) VALUES ('90', 'Autres matériaux et indéterminé');
