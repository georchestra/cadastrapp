Ext.namespace("GEOR.Addons.Cadastre");

/**
 * public: method[onClickdisplayFIUF]
 * 
 * @param parcelleId Identifiant de parcelle ex :20148301032610C0012
 * 
 * 
 *  Cette methode construit la fiche d'information foncière pour une parcelle donnée dont d'identifiant est parcelleId
 *   Elle  permet également l'export au format pdf de la fiche d'information foncière
 *
 */
GEOR.Addons.Cadastre.onClickDisplayFIUF = function(parcelleId) {

	var windowFIUF, parcelleGrid;

	// ArrayStore to display information on vertical panel
	var fiufGlobalInfosStore = new Ext.data.ArrayStore({
		fields : [ {
			name : 'uniteFonciere'
		}, {
			name : 'surface',
			type : 'float'
		}, ]
	});

	// Store contenant les co propriétaires de la parcelle et de l'unité foncière
	var fiufProprietaireStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({
			url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getProprietaire',
			method : 'GET',
			autoLoad : false
		}),
		fields : [ 'comptecommunal', 'app_nom_usage' ]
	});

	// Store contenant les informations des différentes parcelles
	var fiufParcelleListStore = new Ext.data.JsonStore({
		proxy : new Ext.data.HttpProxy({
			url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getParcelle',
			autoLoad : false,
			method : 'POST'
		}),
		fields : [ 'parcelle', 'dcntpa', 'surfc', {
			name : 'adresse',
			convert : function(v, rec) {
				return rec.dnvoiri + rec.dindic + rec.cconvo + rec.dvoilib
			}
		} ],
		listeners : {
		    'datachanged' : function (){
                // if data are load in the store
                if(this.data.length > 0){
                    var featuresId = [];
                    Ext.each(this.data.items, function(item, currentIndex){
                        featuresId.push(item.data.parcelle);
                    });
                    GEOR.Addons.Cadastre.getFeaturesWFSAttribute(featuresId);
                }
            }
		}

	});

	// Requete Ajax pour chercher dans la webapp les donnees relative a la parcelle
	Ext.Ajax.request({
		url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getInfoUniteFonciere?parcelle=' + parcelleId,
		method : 'GET',
		success : function(response) {

			var result = Ext.decode(response.responseText);

			// Preparation du tableau de donnees
			var fiufGlobalInfosData = [ [ 'Surface DGFiP', result.dcntpa_sum ], [ 'Surface SIG', result.sigcal_sum ], [ 'Surface Batie', result.sigcalb_sum ] ];

			// Chargement dans le store correspondant
			fiufGlobalInfosStore.loadData(fiufGlobalInfosData, false);

			// If UF exist
			if (result.uf) {
				// Load information about parcelle
				fiufParcelleListStore.load({
					params : {
						'unitefonciere' : result.uf
					}
				});

				// Load proprietaire information only if CNIL Level is ok
				if (GEOR.Addons.Cadastre.isCNIL1() || GEOR.Addons.Cadastre.isCNIL2()) {
					fiufProprietaireStore.load({
						params : {
							'comptecommunal' : result.comptecommunal,
							'details' : 2
						}
					});
				}
			}
		}
	});

	// ---------- Definition des Grids 

	// Declaration et construction du tableau informations globales de la
	// parcelle
	// Tableau composé des colonnes 'unité foncière' et 'surface'
	var fiufGlobalInfosGrid = new Ext.grid.GridPanel({
		store : fiufGlobalInfosStore,
		name : 'FIUF_globalInformations',
		xtype : 'editorgrid',
		autoExpandColumn : 'surface',
		colModel : new Ext.grid.ColumnModel({
			defaults : {
				sortable : false,
			},
			columns : [ {
				header : OpenLayers.i18n('cadastrapp.uniteFonciere'),
				width : 100,
				dataIndex : 'uniteFonciere'
			}, {
				header : OpenLayers.i18n('cadastrapp.surface'),
				width : 100,
				renderer : Ext.util.Format.numberRenderer('0,000.00 m²'),
				dataIndex : 'surface',
				id : 'surface'
			}, ],
		}),
		columnWidth : .4,
		border : true,
		autoHeight : true,
	});

	// Declaration et construction du tableau des coproprietaires
	// Composé d'une seule colonne: co-proprietaires
	var fiufProprietaireGrid = new Ext.grid.GridPanel({
		store : fiufProprietaireStore,
		name : 'Fiuf_Proprietaire',
		xtype : 'editorgrid ',
		autoExpandColumn : 'app_nom_usage',
		colModel : new Ext.grid.ColumnModel({
			columns : [ {
				header : 'Id propriétaire',
				width : 110,
				dataIndex : 'comptecommunal'
			}, {
				id : 'app_nom_usage',
				header : 'Nom',
				dataIndex : 'app_nom_usage'
			}, ],
		}),
		columnWidth : .6,
		border : true,
		autoHeight : true,
	});

	// Declaration et construction du tableau de liste de parcelles
	// Comprend les colonnes 'parcelle', 'surface DGFiP','Surface SIG' et
	// 'Adresse postale'
	var fiufParcelleListGrid = new Ext.grid.GridPanel({
		store : fiufParcelleListStore,
		name : 'Fiuf_ParcelleList',
		xtype : 'editorgrid',
		height: 300,
		autoExpandColumn : 'adresse',
		disableSelection: true,
		anchor : '100%',
		colModel : new Ext.grid.ColumnModel({
			defaults : {
				sortable : true,
			},
			columns : [ {
				// colonne parcelle
				header : OpenLayers.i18n('cadastrapp.parcelle'),
				width : 130,
				dataIndex : 'parcelle'
			}, {
				// colonne surface DGFiP
				header : OpenLayers.i18n('cadastrapp.surface') + " " + OpenLayers.i18n('cadastrapp.contenancedgfip'),
				width : 90,
				dataIndex : 'dcntpa',
				renderer : Ext.util.Format.numberRenderer('0,000.00 m²'),
			}, {
				// colonne surface SIG
				header : OpenLayers.i18n('cadastrapp.surface') + " " + OpenLayers.i18n('cadastrapp.sig'),
				width : 90,
				dataIndex : 'surfc',
				renderer : Ext.util.Format.numberRenderer('0,000.00 m²'),
			}, {
				// colonne adresse postale
				id : 'adresse',
				header : OpenLayers.i18n('cadastrapp.parcelle.adresse.postale'),
				dataIndex : 'adresse'
			} ],
		})
	});

	var upCompositeField = new Ext.Container({
		id : "cadastrappUfComposite",
		layout : "column",
		anchor : '100%',
		items : [ fiufGlobalInfosGrid, fiufProprietaireGrid ]
	})

	// Declaration et creation de la fenetre principale
	// Comprend les tableaux 'unite fonciere', 'coproprietaires' et 'liste des
	// parcelles
	windowFIUF = new Ext.Window({
		title : parcelleId,
		frame : true,
		bodyPadding : 10,
		width : 500,
		layout : 'anchor',
		closable : true,
		resizable : true,
		draggable : true,
		autoScroll : true,
		constrainHeader : true,
		items : [ upCompositeField, fiufParcelleListGrid ],
		listeners : {
			close : function(window) {
				// deselection de la ligne
				var rowIndex = GEOR.Addons.Cadastre.indexRowParcelle(parcelleId);
				GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().deselectRow(rowIndex);
				// mise à jour des tableau de fenêtres ouvertes
				var index = GEOR.Addons.Cadastre.result.tabs.getActiveTab().idParcellesFOuvertes.indexOf(parcelleId);
				GEOR.Addons.Cadastre.result.tabs.getActiveTab().idParcellesFOuvertes.splice(index, 1);
				GEOR.Addons.Cadastre.result.tabs.getActiveTab().fichesFOuvertes.splice(index, 1);
				var feature = GEOR.Addons.Cadastre.getFeatureById(parcelleId);
				if (feature) {
					GEOR.Addons.Cadastre.changeStateFeature(feature, -1, GEOR.Addons.Cadastre.selection.state.list);
				}
				GEOR.Addons.Cadastre.closeWindowFIUC(parcelleId, GEOR.Addons.Cadastre.result.tabs.getActiveTab()); // on ferme la fenêtre
				// cadastrale si ouverte
				windowFIUF = null;
			}
		},
		buttons : [ {
			id : "selectParcelleButton",
			text : OpenLayers.i18n('cadastrapp.uniteFonciere.select.all'),
			disabled : true,
			listeners : {
				click : function(b, e) {
				    var listId = [];

                    fiufParcelleListStore.each(function(record) {
                        listId.push(record.data.parcelle);
                    });
                   
                    if(listId.length > 0 ){
                        GEOR.Addons.Cadastre.addNewResultParcelle ("UF Sélection", null);
                        GEOR.Addons.Cadastre.showTabSelection(listId);
                    }                                  
				}
			}
		} ]

	});

	GEOR.Addons.Cadastre.result.tabs.getActiveTab().fichesFOuvertes.push(windowFIUF);
	GEOR.Addons.Cadastre.result.tabs.getActiveTab().idParcellesFOuvertes.push(parcelleId);
	windowFIUF.show();
};
;
