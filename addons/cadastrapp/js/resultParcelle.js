Ext.namespace("GEOR.Addons.Cadastre");

/**
 * Create menu list of button to export plots, owner and co-owners
 */
GEOR.Addons.Cadastre.exportAsCsvButton = function() {
    if(GEOR.Addons.Cadastre.isCNIL1() || GEOR.Addons.Cadastre.isCNIL2()){
        var exportCsvItems = [];
               
        // create parcelles button and get parcelles list as CSV
        exportCsvItems.push({
            text:OpenLayers.i18n("cadastrapp.result.csv.button.parcelles"),
            showSeparator:false,
            handler(){
                return GEOR.Addons.Cadastre.exportPlotSelectionAsCSV();
            },scope:this
        });
        
        // create parcelles button and get owners list as CSV
        exportCsvItems.push({
            text:OpenLayers.i18n("cadastrapp.result.csv.button.owner"),
            handler(){
                return GEOR.Addons.Cadastre.exportOwnersAsCSV();
            },scope:this
        });
        
        // create parcelles button and get co-owners list as CSV
        exportCsvItems.push({
            text:OpenLayers.i18n("cadastrapp.result.csv.button.coowner"),
            handler(){
                return GEOR.Addons.Cadastre.exportCoOwnersAsCSV();
            },scope:this
        });
        
        // create menu to set properties at this menu
        var menuCsv = new Ext.menu.Menu({
            items: exportCsvItems,
            showSeparator:false
        });
        
        // create menu with items
        return new Ext.Button({            
            text:OpenLayers.i18n("cadastrapp.result.csv.export"),      
            menu: menuCsv
        });
        
    } else {
        return new Ext.Button({
            text: OpenLayers.i18n("cadastrapp.result.csv.export"),
            disabled:true,
            listeners : {
                click : function(b, e) {                
                    // Export selected plots as csv
                    GEOR.Addons.Cadastre.exportPlotSelectionAsCSV();
                }
            }      
        });
    }
};

/**
 * Init Global windows containing all tabs
 */
GEOR.Addons.Cadastre.initResultParcelle = function() {

    // fenêtre principale
    GEOR.Addons.Cadastre.result.plot.window = new Ext.Window({
        title : OpenLayers.i18n('cadastrapp.parcelle.result.title'),
        frame : true,
        autoScroll : true,
        minimizable : false,
        closable : true,
        resizable : true,
        draggable : true,
        constrainHeader : true,
        border : false,
        width : 630,
        height : 200,
        collapsible: true,
        boxMaxHeight : Ext.getBody().getViewSize().height - 200,
        listeners : {
            // Adding because autoheight and boxMaxHeight to not work together
            // on tabPanel
            afterlayout : function(window) {
                if (this.getHeight() > this.boxMaxHeight) {
                    this.setHeight(this.boxMaxHeight);
                }
            },
            close : function(window) {
                // *********************
                // supprime tous les entités de la couche selection
                GEOR.Addons.Cadastre.clearLayerSelection();
                // ferme les fenêtres cadastrales et foncières
                GEOR.Addons.Cadastre.closeAllWindowFIUC();
                GEOR.Addons.Cadastre.closeAllWindowFIUF();
                // *********************
                GEOR.Addons.Cadastre.result.plot.window = null;
            },
            show : function(window) {
                // lors du changement entre onglets : deselection de toutes les
                // parcelles et selection de celles du nouvel onglet
            }
        },
        items : [ {
            xtype : 'tabpanel',
            layoutOnTabChange : true,
            enableTabScroll : true,
            items : [ {
                xtype : 'panel',
                title : '+',
                border : true,
                closable : false,
                listeners : {
                    activate : function(grid) {
                        // Add void tab
                        GEOR.Addons.Cadastre.addNewResultParcelle(OpenLayers.i18n('cadastrapp.parcelle.result.selection.title'), null, OpenLayers.i18n('cadastrapp.parcelle.result.selection.content'));
                    }
                }
            } ]
        } ],

        buttons : [ {
            text : OpenLayers.i18n('cadastrapp.result.parcelle.zoom.list'),
            listeners : {
                click : function(b, e) {
                    // zoom on plots from the active tab
                    GEOR.Addons.Cadastre.zoomOnFeatures(GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList);
                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.result.parcelle.zoom.selection'),
            disabled:true,
            listeners : {
                click : function(b, e) {
                    // zoom on selected plots from the active tab
                    var selection = GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().getSelections();

                    var features = [];
                    Ext.each(selection, function(item) {
                        var parcelleId = item.data.parcelle;
                        features.push(GEOR.Addons.Cadastre.getFeatureById(parcelleId));
                    });
                    if (features) {
                        GEOR.Addons.Cadastre.zoomOnFeatures(features);
                    }

                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.result.parcelle.delete'),
            disabled:true,
            listeners : {
                click : function(b, e) {
                    // remove selected plots from the active tab
                    // delete from store
                    var selection = GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().getSelections();

                    Ext.each(selection, function(item) {

                        // remove from store
                        GEOR.Addons.Cadastre.result.tabs.getActiveTab().getStore().remove(item);

                        var parcelleId = item.data.parcelle;
                        var feature = GEOR.Addons.Cadastre.getFeatureById(parcelleId);

                        // Close open windows
                        GEOR.Addons.Cadastre.closeFoncierAndCadastre(parcelleId, GEOR.Addons.Cadastre.result.tabs.getActiveTab());

                        // Remove feature
                        if (feature) {
                            var index = GEOR.Addons.Cadastre.indexFeatureSelected(feature);
                            GEOR.Addons.Cadastre.changeStateFeature(feature, index, "reset");
                        }
                    });
                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.result.parcelle.fiche'),
            disabled:true,
            listeners : {
                click : function(b, e) {

                    var selection = GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().getSelections();

                    Ext.each(selection, function(item) {
                        var parcelleId = item.data.parcelle;
                        var feature = GEOR.Addons.Cadastre.getFeatureById(parcelleId);
                        var state = GEOR.Addons.Cadastre.selection.state.list;

                        // Si la fenêtre details cadastre ou foncier est déjà
                        // ouverte
                        if (GEOR.Addons.Cadastre.result.tabs.getActiveTab().idParcellesCOuvertes.indexOf(parcelleId) != -1 || GEOR.Addons.Cadastre.result.tabs.getActiveTab().idParcellesFOuvertes.indexOf(parcelleId) != -1) {
                            GEOR.Addons.Cadastre.closeFoncierAndCadastre(parcelleId, grid);
                        } else {
                            GEOR.Addons.Cadastre.openFoncierOrCadastre(parcelleId, GEOR.Addons.Cadastre.result.tabs.getActiveTab());
                            state = GEOR.Addons.Cadastre.selection.state.details;
                        }

                        // change selection color on map depending on state
                        if (feature) {
                            GEOR.Addons.Cadastre.changeStateFeature(feature, 0, state);
                        }
                    });
                }
            }
        },
        // display simple button or button with menu if cnil1 or cnil2
        GEOR.Addons.Cadastre.exportAsCsvButton(), 
        {
            text : OpenLayers.i18n('cadastrapp.close'),
            listeners : {
                click : function(b, e) {
                    // Close all tab and open windows
                    GEOR.Addons.Cadastre.result.plot.window.close();
                }
            }
        } ]
    });
};

/**
 * public: method[addNewResultParcelle]
 * 
 * Add new parcelle(s) to resultwindow with a default message no result
 * 
 * @param: title -> title of the new tab
 * @param: result -> result to be used in the grid of this tab
 */
GEOR.Addons.Cadastre.addNewResultParcelle = function(title, result) {
    GEOR.Addons.Cadastre.addNewResult(title, result, OpenLayers.i18n('cadastrapp.parcelle.result.nodata'));
};

/**
 * public: method[addNewResult]
 * 
 * Call the initWindows method if windows do not exist then fill one tab with
 * given information or message
 * 
 * @param: title tab title
 * @param: result to be store in a grid
 * @param: message to replace data if not exist
 */
GEOR.Addons.Cadastre.addNewResult = function(title, result, message) {

    // If windows do not exist
    if (GEOR.Addons.Cadastre.result.plot.window == null) {
        GEOR.Addons.Cadastre.initResultParcelle();
    }

    // Get tab list
    GEOR.Addons.Cadastre.result.tabs = GEOR.Addons.Cadastre.result.plot.window.items.items[0];

    // **********
    // Listener
    GEOR.Addons.Cadastre.result.tabs.addListener('beforetabchange', function(tab, newTab, currentTab) {
        var store;
        if (currentTab) { // cad la table de resultats est ouverte et on
            // navigue entre les
            // onglets, sinon toute selection en bleue sur la carte va redevenir
            // jaune
            if (currentTab.store) {
                store = currentTab.store.data.items;
                // deselection des parcelles
                GEOR.Addons.Cadastre.changeStateParcelleOfTab(store, "tmp");
            }

            if (newTab) {
                                                              
                if (newTab.store) {
                    // enable or disable button according to row selection
                    var selTab = newTab.getSelectionModel();
                    selTab.fireEvent("selectionchange",selTab);
                    
                    store = newTab.store.data.items;
                    newTab.featureList

                    // selection en jaune
                    Ext.each(newTab.featuresList, function(feature, currentIndex) {

                        // if exists, change state
                        if (feature) {
                            GEOR.Addons.Cadastre.changeStateFeature(feature, null, GEOR.Addons.Cadastre.selection.state.list);
                        }
                    });

                    // selection en bleue
                    var selectedRows = newTab.getSelectionModel().selections.items;
                    var idField = GEOR.Addons.Cadastre.WFSLayerSetting.nameFieldIdParcelle;

                    // For each element of the store
                    Ext.each(selectedRows, function(element, currentIndex) {

                        // get feature corresponding of the parcelleId
                        Ext.each(newTab.featuresList, function(selectedFeature, currentIndex) {
                            if (selectedFeature.attributes[idField] == element.data.parcelle) {
                                feature = selectedFeature;
                                return false;
                            }
                        });

                        // if exists, change state
                        if (feature) {
                            GEOR.Addons.Cadastre.changeStateFeature(feature, null, GEOR.Addons.Cadastre.selection.state.selected);
                        }
                    });
                }
            }
        }
    });
    // **********

    var currentTabGrid = new Ext.grid.GridPanel({
        title : title,
        autoExpandColumn: 'adresse',
        id : 'resultParcelleWindowTab' + GEOR.Addons.Cadastre.result.tabs.items.length,
        border : true,
        anchor : '100%',
        closable : true,
        store : (result != null) ? result : new Ext.data.Store(),
        colModel : GEOR.Addons.Cadastre.getResultParcelleColModel(),
        autoHeight : true,
        sm : new Ext.grid.RowSelectionModel({
            multiSelect : true,
            listeners : {
                // Add feature and change state when selected
                rowselect : function(grid, rowIndex, record) {
                    var parcelleId = record.data.parcelle;
                    var feature = GEOR.Addons.Cadastre.getFeatureById(parcelleId);
                    if (feature) {
                        GEOR.Addons.Cadastre.changeStateFeature(feature, 0, GEOR.Addons.Cadastre.selection.state.selected);

                    }
                },
                // Remove feature and change state when deselected
                rowdeselect : function(grid, rowIndex, record) {
                    var parcelleId = record.data.parcelle;
                    var feature = GEOR.Addons.Cadastre.getFeatureById(parcelleId);
                    if (feature) {
                        GEOR.Addons.Cadastre.changeStateFeature(feature, 0, GEOR.Addons.Cadastre.selection.state.list);

                    }
                },
                selectionchange : function(grid, rowIndx, record){
                    Ext.each(GEOR.Addons.Cadastre.result.plot.window.buttons, function(btn, idx){
                        
                        if(grid.selections.length == 0){ // no selection, we deactive some buttons
                            switch (btn.text){
                            case OpenLayers.i18n('cadastrapp.result.parcelle.zoom.selection'):
                                btn.disable();
                                break;
                            case OpenLayers.i18n('cadastrapp.result.parcelle.delete'):
                                btn.disable();
                                break;
                            case OpenLayers.i18n('cadastrapp.result.parcelle.fiche'):
                                btn.disable();
                                break;
                            default:
                                if(!btn.menu && btn.text == OpenLayers.i18n("cadastrapp.result.csv.export")){
                                    btn.disable();
                                }
                            }                                   
                        } else {
                            btn.enable();
                        }
                    });
                }
            }
        }),
        viewConfig : {
            deferEmptyText : false,
            emptyText : message
        },
        listeners : {
            close : function(grid) {
                // on ferme toutes les fenetres filles : detail parcelle
                Ext.each(grid.detailParcelles, function(element, currentIndex) {
                    if (element) {
                        element.close()
                    }
                });

                // on ferme la fenetre si c'est le dernier onglet
                if (GEOR.Addons.Cadastre.result.tabs.items.length == 2) {
                    // si il ne reste que cet onglet et l'onglet '+', fermer la
                    // fenetre
                    GEOR.Addons.Cadastre.result.plot.window.close();
                } else {
                    // on selectionne manuellement le nouvel onglet à
                    // activer, pour eviter de tomber sur le '+' (qui va
                    // tenter de refaire un onglet et ça va faire
                    // nimporte quoi)
                    var index = GEOR.Addons.Cadastre.result.tabs.items.findIndex('id', grid.id);
               
                    // *************
                    // quand on ferme l'onglet on vire toutes les
                    // parcelles dependantes
                    store = grid.store.data.items;
                    GEOR.Addons.Cadastre.changeStateParcelleOfTab(store, "reset");
                    // *************
                    GEOR.Addons.Cadastre.result.tabs.setActiveTab((index == 0) ? 1 : (index - 1));
                }
            },
            rowdblclick: function ( grid, rowIndex, e ) {
                var row = grid.store.getAt(rowIndex);
                var parcelleId = row.data.parcelle;
                var feature = GEOR.Addons.Cadastre.getFeatureById(parcelleId);
                var state = GEOR.Addons.Cadastre.selection.state.list;

                // Si la fenêtre details cadastre ou foncier est déjà
                // ouverte
                if (GEOR.Addons.Cadastre.result.tabs.getActiveTab().idParcellesCOuvertes.indexOf(parcelleId) != -1 || GEOR.Addons.Cadastre.result.tabs.getActiveTab().idParcellesFOuvertes.indexOf(parcelleId) != -1) {
                    GEOR.Addons.Cadastre.closeFoncierAndCadastre(parcelleId, grid);
                } else {
                    GEOR.Addons.Cadastre.openFoncierOrCadastre(parcelleId, GEOR.Addons.Cadastre.result.tabs.getActiveTab());
                    state = GEOR.Addons.Cadastre.selection.state.details;
                }

                // change selection color on map depending on state
                if (feature) {
                    GEOR.Addons.Cadastre.changeStateFeature(feature, 0, state);
                }
            }
        }
    });

    currentTabGrid.detailParcelles = new Array();
    currentTabGrid.fichesCOuvertes = new Array();
    currentTabGrid.idParcellesCOuvertes = new Array();
    currentTabGrid.fichesFOuvertes = new Array();
    currentTabGrid.idParcellesFOuvertes = new Array();
    currentTabGrid.featuresList = new Array();

    // Add new panel at the end just before + and activate it
    var currentTabIndex = GEOR.Addons.Cadastre.result.tabs.items.length - 1;
    GEOR.Addons.Cadastre.result.tabs.insert(currentTabIndex, currentTabGrid);
    GEOR.Addons.Cadastre.result.tabs.setActiveTab(currentTabIndex);

    // lors d'une recherche de parcelle on envoie une requête attributtaire pour
    // stocker les features
    currentTabGrid.getStore().each(function(record) {
        GEOR.Addons.Cadastre.getFeaturesWFSAttribute(record.data.parcelle);
    });

    GEOR.Addons.Cadastre.result.plot.window.show();
}

/**
 * Method: showTabSelection
 * 
 * Affiche le tableau de résultat ou le met à jour
 * 
 * @param: parcelsIds
 * @param: selectRows
 */
GEOR.Addons.Cadastre.showTabSelection = function(parcelsIds) {

    // si il existe au moins une parcelle
    if (parcelsIds.length > 0) {

        // Vérifie si la parcelle n'est pas déjà dans le store
        if (GEOR.Addons.Cadastre.result.tabs.getActiveTab() && GEOR.Addons.Cadastre.result.tabs.getActiveTab().getStore()) {

            GEOR.Addons.Cadastre.result.tabs.getActiveTab().getStore().each(function(item) {

                // Si la parcelle est déja dans le store on la supprime de la
                // liste
                // et on la change l'état de selection
                var index = parcelsIds.indexOf(item.data.parcelle);
                if (index > -1) {
                    parcelsIds.splice(index);
                    var rowIndex = GEOR.Addons.Cadastre.indexRowParcelle(item.data.parcelle);
                    if (GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().isSelected(rowIndex)) {
                        GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().deselectRow(rowIndex, false);
                    } else {
                        GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().selectRow(rowIndex, true);
                    }
                }
            });
        }

        // all parcels could have been removed
        if (parcelsIds.length > 0) {
            // paramètres pour le getParcelle
            var params = {};
            params.parcelle = new Array();
            Ext.each(parcelsIds, function(parcelleId, currentIndex) {
                params.parcelle.push(parcelleId);
            });

            // envoi la liste de resultat
            Ext.Ajax.request({
                method : 'POST',
                url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getParcelle',
                params : params,
                success : function(response) {

                    // si la fenetre de recherche n'est pas ouverte
                    if (!GEOR.Addons.Cadastre.result.plot.window || !GEOR.Addons.Cadastre.result.tabs || !GEOR.Addons.Cadastre.result.tabs.getActiveTab()) {

                        GEOR.Addons.Cadastre.addNewResultParcelle("Selection (" + parcelsIds.length + ")", GEOR.Addons.Cadastre.getResultParcelleStore(response.responseText, false));

                    // si la fenêtre est ouverte on ajoute les lignes
                    } else {
                        GEOR.Addons.Cadastre.addResultToTab(response.responseText);
                    }
                },
                failure : function(result) {
                    alert('ERROR-');
                }
            });
        }
    }
}

/**
 * met à jour l'état des parcelles en fonction de l'évènement sur l'onglet
 * 
 * @param: store
 * @param: typeSelector
 * 
 */
GEOR.Addons.Cadastre.changeStateParcelleOfTab = function(store, typeSelector) {

    // For each element of the store
    Ext.each(store, function(element, currentIndex) {
        // get feature corresponding of the parcelleId
        var feature = GEOR.Addons.Cadastre.getFeatureById(element.data.parcelle);

        // if exists, change state
        if (feature) {
            var index = GEOR.Addons.Cadastre.indexFeatureSelected(feature);
            GEOR.Addons.Cadastre.changeStateFeature(feature, index, typeSelector);
        }
    });
}

/**
 * en fonction des cases à cocher on ouvre la fenêtre cadastrale et/ou foncière
 * 
 * @param: id
 * @param: grid
 * 
 */
GEOR.Addons.Cadastre.openFoncierOrCadastre = function(id, grid) {

    var cadastreExiste = (grid.idParcellesCOuvertes.indexOf(id) != -1)
    var foncierExiste = (grid.idParcellesFOuvertes.indexOf(id) != -1)

    if (!foncierExiste && GEOR.Addons.Cadastre.isFoncier()) {
        grid.detailParcelles.push(GEOR.Addons.Cadastre.onClickDisplayFIUF(id));
    }
    if (!cadastreExiste) {
        grid.detailParcelles.push(GEOR.Addons.Cadastre.displayFIUC(id));
    }
}

/**
 * fermeture d'une fenêtre cadastre et foncière donnée par un id
 * 
 * @param: idParcelle
 * @param: grid
 */
GEOR.Addons.Cadastre.closeFoncierAndCadastre = function(idParcelle, grid) {

    cadastreExiste = (grid.idParcellesCOuvertes.indexOf(idParcelle) != -1)
    foncierExiste = (grid.idParcellesFOuvertes.indexOf(idParcelle) != -1)

    if (cadastreExiste) {
        GEOR.Addons.Cadastre.closeWindowFIUC(idParcelle, grid);
    }
    if (foncierExiste) {
        GEOR.Addons.Cadastre.closeWindowFIUF(idParcelle, grid);
    }
}

/**
 * Method: closeWindowFIUC
 * 
 * Ferme la fenetre de fiche cadastrale
 * 
 * @param: idParcelle
 * @param: grid
 */
GEOR.Addons.Cadastre.closeWindowFIUC = function(idParcelle, grid) {
    var index = grid.idParcellesCOuvertes.indexOf(idParcelle);
    var ficheCourante = grid.fichesCOuvertes[index];
    if (ficheCourante) {
        ficheCourante.close();
    }
}

/**
 * Method: closeWindowFIUF
 * 
 * Ferme la fenetre de fiche foncière
 * 
 * @param: idParcelle
 * @param: grid
 */
GEOR.Addons.Cadastre.closeWindowFIUF = function(idParcelle, grid) {
    var index = grid.idParcellesFOuvertes.indexOf(idParcelle);
    var ficheCourante = grid.fichesFOuvertes[index];
    if (ficheCourante) {
        ficheCourante.close();
    }
}

/**
 * Method: closeAllWindowFIUC
 * 
 * Ferme toutes les fenêtres de fiches cadastrales
 * 
 */
GEOR.Addons.Cadastre.closeAllWindowFIUC = function() {
    // for each tabs
    Ext.each(GEOR.Addons.Cadastre.result.tabs.items.items, function(tab, currentIndex) {
        if (tab && tab.idParcellesCOuvertes) {
            // create a temp array
            var parcellesList = tab.idParcellesCOuvertes.slice(0);          
            // for each fiche
            Ext.each(parcellesList, function(idParcellesCOuverte, currentIndex) {
                GEOR.Addons.Cadastre.closeWindowFIUC(idParcellesCOuverte, tab);
            });
            tab.fichesCOuvertes = [];
            tab.idParcellesCOuvertes = [];
        }
    });

}

/**
 * Method: closeAllWindowFIUF
 * 
 * Ferme toutes les fenêtres de fiches foncière
 * 
 */
GEOR.Addons.Cadastre.closeAllWindowFIUF = function() {
    // for each tabs
    Ext.each(GEOR.Addons.Cadastre.result.tabs.items.items, function(tab, currentIndex) {
        if (tab && tab.idParcellesFOuvertes) {
            // create a temp array
            var parcellesList = tab.idParcellesFOuvertes.slice(0);
            // for each fiche
            Ext.each(parcellesList, function(idParcellesFOuverte, currentIndex) {
                GEOR.Addons.Cadastre.closeWindowFIUF(idParcellesFOuverte, tab);
            });
            tab.fichesFOuvertes = [];
            tab.idParcellesFOuvertes = [];
        }
    });
}


/**
 * get CSV from co-owners
 */
GEOR.Addons.Cadastre.exportCoOwnersAsCSV = function() {
    if (GEOR.Addons.Cadastre.result.tabs && GEOR.Addons.Cadastre.result.tabs.getActiveTab()) {
        var selection = GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().getSelections();
        
        // verify selected information
        if (selection && selection.length > 0) {

            // Create array to store parcelles id
            var parcellesId = [];
            
            // Add each parcelle ID in one array
            Ext.each(selection, function(item) {
                parcellesId.push(item.data.parcelle);
            });
            
            // create POST request
            var url = GEOR.Addons.Cadastre.cadastrappWebappUrl + "exportCoProprietaireByParcelles";
            var params = parcellesId.join();
            GEOR.Addons.Cadastre.getCsvByPost(url, params); 
        }else {
            Ext.Msg.alert('Status', OpenLayers.i18n('cadastrapp.result.no.selection'));
        }
    } else {
        Ext.Msg.alert('Status', OpenLayers.i18n('cadastrapp.result.no.search'));
    }  
};

/**
 * get CSV from owners
 */

GEOR.Addons.Cadastre.exportOwnersAsCSV = function (){
    if (GEOR.Addons.Cadastre.result.tabs && GEOR.Addons.Cadastre.result.tabs.getActiveTab()) {
        var selection = GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().getSelections();
        
        // verify selected information
        if (selection && selection.length > 0) {

            // Create array to store parcelles id
            var parcellesId = [];
            
            // Add each parcelle ID in one array
            Ext.each(selection, function(item) {
                parcellesId.push(item.data.parcelle);
            });
            
            // create POST request
            var url = GEOR.Addons.Cadastre.cadastrappWebappUrl + "exportProprietaireByParcelles";
            var params = parcellesId.join();
            GEOR.Addons.Cadastre.getCsvByPost(url, params); 
        }else {
            Ext.Msg.alert('Status', OpenLayers.i18n('cadastrapp.result.no.selection'));
        }
    } else {
        Ext.Msg.alert('Status', OpenLayers.i18n('cadastrapp.result.no.search'));
    } 
};



/**
 * Create new request to get and download CSV file
 */
GEOR.Addons.Cadastre.getCsvByPost = function(url, params){
    // create Iframe
    var iframe = document.createElement("iframe");            
    iframe.setAttribute("style", "display:none;");
    
    // create form with attributes and request informations
    var form = document.createElement("form");            
    form.setAttribute("method", "post");
    form.setAttribute("action", url);
    
    // create input with
    var hiddenField = document.createElement("input");
    hiddenField.setAttribute("type", "hidden");
    hiddenField.setAttribute("name", "parcelles");
    
    // insert values to input to set POST params
    hiddenField.value = params;
    
    // insert input to form
    form.appendChild(hiddenField);
    
    // insert form to iframe to only refresh hidden iframe
    iframe.appendChild(form);
    
    // insert iframe to document body
    document.body.appendChild(iframe);
    
    // submit form to launch request and get CSV
    form.submit();
};

/**
 * Export selection of currentTab as CSV using webapp service
 */
GEOR.Addons.Cadastre.exportPlotSelectionAsCSV = function() {

    if (GEOR.Addons.Cadastre.result.tabs && GEOR.Addons.Cadastre.result.tabs.getActiveTab()) {
        var selection = GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().getSelections();
        
        // verify selected information
        if (selection && selection.length > 0) {

            // Create array to store parcelles id
            var parcellesId = [];
                
            // Add each parcelle ID in one array
            Ext.each(selection, function(item) {
                parcellesId.push(item.data.parcelle);
            });
                
            // create POST request
            var url = GEOR.Addons.Cadastre.cadastrappWebappUrl + "exportParcellesAsCSV";
            var params = parcellesId.join();
            GEOR.Addons.Cadastre.getCsvByPost(url, params); 
        } else {
            Ext.Msg.alert('Status', OpenLayers.i18n('cadastrapp.result.no.selection'));
        }
    } else {
        Ext.Msg.alert('Status', OpenLayers.i18n('cadastrapp.result.no.search'));
    }

}

/**
 * Print bordereau parcellaire of selected plots
 */
GEOR.Addons.Cadastre.printSelectedBordereauParcellaire = function() {

    if (GEOR.Addons.Cadastre.result.tabs && GEOR.Addons.Cadastre.result.tabs.getActiveTab()) {
        var selection = GEOR.Addons.Cadastre.result.tabs.getActiveTab().getSelectionModel().getSelections();

        if (selection && selection.length > 0) {
            var parcelleIds = [];
            Ext.each(selection, function(item) {
                parcelleIds.push(item.data.parcelle);
            });

            GEOR.Addons.Cadastre.onClickPrintBordereauParcellaireWindow(parcelleIds);
        } else {
            Ext.Msg.alert('Status', OpenLayers.i18n('cadastrapp.result.no.selection'));
        }
    } else {
        Ext.Msg.alert('Status', OpenLayers.i18n('cadastrapp.result.no.search'));

    }

}

/**
 * Add JSON information to current tab and to feature list If Plots already
 * exist in store it will not be added twice
 * 
 * @param result
 *            JSON information for getParcelle service
 * 
 * If result is empty nothing is added
 * 
 */
GEOR.Addons.Cadastre.addResultToTab = function(result) {

    if (result && GEOR.Addons.Cadastre.result.tabs.getActiveTab() && GEOR.Addons.Cadastre.result.tabs.getActiveTab().getStore()) {
        
        var jsonStore = GEOR.Addons.Cadastre.getResultParcelleStore(result, false);
        
        var parcelleList = Ext.decode(result);
        Ext.each(parcelleList, function(element, currentIndex) {
            
            // Add parcelle only if not already in
            if (GEOR.Addons.Cadastre.indexRowParcelle(element.parcelle) == -1) {

                // ajout de la ligne
                GEOR.Addons.Cadastre.result.tabs.getActiveTab().getStore().add(jsonStore.getAt(currentIndex));
                // Ajout à la selection
                GEOR.Addons.Cadastre.getFeaturesWFSAttribute(element.parcelle);
            }
        });
    }
}
