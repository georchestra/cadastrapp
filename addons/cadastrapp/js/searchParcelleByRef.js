Ext.namespace("GEOR.Addons.Cadastre");

// Parcelle windows with four tabs
// Ref ; Adresse cadastrale, Ref cadastrale
GEOR.Addons.Cadastre.rechercheParcelleWindow;

/**
 * Init windows if it does not exist, and select first tab
 * 
 * @param: tab 0, 1, 2 or 3
 *      0 -> tab search by ref
 *      1 -> tab search by adresse
 *      2 -> tab search by id
 *      3 -> tab search by lot
 */
GEOR.Addons.Cadastre.onClickRechercheParcelle = function(tabIndex) {

    // If Window does not exist, init it
    if (GEOR.Addons.Cadastre.rechercheParcelleWindow == null) {
        GEOR.Addons.Cadastre.initRechercheParcelle();
    }
    // Update current tab on rerender windows
    GEOR.Addons.Cadastre.rechercheParcelleWindow.items.items[0].setActiveTab(tabIndex);
    GEOR.Addons.Cadastre.rechercheParcelleWindow.doLayout();

    GEOR.Addons.Cadastre.rechercheParcelleWindow.show();
}

/**
 *  Create search windows
 */
GEOR.Addons.Cadastre.initRechercheParcelle = function() {

    var parcBisStore = GEOR.Addons.Cadastre.getBisStore();

    // combobox "villes"
    var parcCityCombo1 = new Ext.form.ComboBox({
        id : 'searchById',
        fieldLabel : OpenLayers.i18n('cadastrapp.parcelle.city'),
        hiddenName : 'cgocommune',
        mode : 'local',
        value : ' ',
        anchor : '95%',
        editable : true,
        selectOnFocus : true,
        tabIndex : 0,
        displayField : 'displayname',
        valueField : 'cgocommune',
        minLength : GEOR.Addons.Cadastre.minCharToSearch,
        store : GEOR.Addons.Cadastre.getPartialCityStore(),
        listeners : {
            beforequery : function(q) {
                if (q.query) {
                    var length = q.query.length;
                    if (length >= GEOR.Addons.Cadastre.minCharToSearch) {
                        if (isNaN(q.query)) {
                            // recherche par nom de ville
                            q.combo.getStore().load({
                                params : {
                                    libcom : q.query
                                }
                            });
                        } else {
                            // recherche par code insee
                            q.combo.getStore().load({
                                params : {
                                    cgocommune : q.query
                                }
                            });
                        }
                    } else if (length < GEOR.Addons.Cadastre.minCharToSearch) {
                        q.combo.getStore().loadData([], false);
                    }
                    q.query = new RegExp(Ext.escapeRe(q.query), 'i');
                    q.query.length = length;
                }
            },
            change : function(combo, newValue, oldValue) {
                console.log("Update section information");
                if( newValue.match(/([0-9]{6})/g) != undefined && newValue.match(/([0-9]{6})/g).length == 1 ) {
                    var insee = newValue.match(/([0-9]{6})/g)[0];
                    // refaire le section store pour cette ville
                    parcelleGrid.reconfigure(GEOR.Addons.Cadastre.getVoidRefStore(), GEOR.Addons.Cadastre.getRefColModel(insee));
                    // permettre l'édition automatique du premier champ de section
                    parcelleGrid.startEditing(0, 0);
                }
            },
            valid : function(combo) {
                parcelleGrid.enable();
                GEOR.ls.set("default_cadastrapp_city",combo.getValue());
            },
            afterrender: function(combo) {
                if(GEOR.ls.get("default_cadastrapp_city") != undefined 
                        && GEOR.ls.get("default_cadastrapp_city").match(/([0-9]{6})/g) != undefined
                        && GEOR.ls.get("default_cadastrapp_city").match(/([0-9]{6})/g).length == 1 ) {
                    var insee = GEOR.ls.get("default_cadastrapp_city").match(/([0-9]{6})/g)[0];
                    combo.store.on('load', function() {
                        combo.setValue(insee);
                        combo.fireEvent('change',combo,insee,'');
                    }, this, {single: true});
                    combo.doQuery(insee);
                }
            }
        }
    });

    var parcCityCombo2 = new Ext.form.ComboBox({
        fieldLabel : OpenLayers.i18n('cadastrapp.parcelle.city'),
        id : 'searchByAdress',
        hiddenName : 'cgocommune',
        allowBlank : false,
        mode : 'local',
        value : '',
        anchor : '95%',
        editable : true,
        tabIndex : 0,
        displayField : 'displayname',
        valueField : 'cgocommune',
        minLength : GEOR.Addons.Cadastre.minCharToSearch,
        store : GEOR.Addons.Cadastre.getPartialCityStore(),
        listeners : {
            beforequery : function(q) {
                if (q.query) {
                    var length = q.query.length;
                    if (length >= GEOR.Addons.Cadastre.minCharToSearch) {
                        if (isNaN(q.query)) {
                            // recherche par nom de ville
                            q.combo.getStore().load({
                                params : {
                                    libcom : q.query
                                }
                            });
                        } else {
                            // recherche par code commune
                            q.combo.getStore().load({
                                params : {
                                    cgocommune : q.query
                                }
                            });
                        }
                    } else if (length < GEOR.Addons.Cadastre.minCharToSearch) {
                        q.combo.getStore().loadData([], false);
                    }
                    q.query = new RegExp(Ext.escapeRe(q.query), 'i');
                    q.query.length = length;
                }
            },
            valid : function(combo) {
                Ext.getCmp('cfCadSearchByRef').enable();
                GEOR.ls.set("default_cadastrapp_city",combo.getValue());
            },
            afterrender: function(combo) {
                if( GEOR.ls.get("default_cadastrapp_city") != undefined 
                        && GEOR.ls.get("default_cadastrapp_city").match(/([0-9]{6})/g) != undefined 
                        && GEOR.ls.get("default_cadastrapp_city").match(/([0-9]{6})/g).length == 1 ) {
                    var insee = GEOR.ls.get("default_cadastrapp_city").match(/([0-9]{6})/g)[0];
                    combo.store.on('load', function() {
                        combo.setValue(insee);
                        combo.fireEvent('change',combo,insee,'');
                    }, this, {single: true});
                    combo.doQuery(insee);
                }
            }
        }
    });

    // grille "références"
    var parcelleGrid = new Ext.grid.EditorGridPanel({
        fieldLabel : OpenLayers.i18n('cadastrapp.parcelle.references'),
        xtype : 'editorgrid',
        ds : GEOR.Addons.Cadastre.getVoidRefStore(),
        cm : GEOR.Addons.Cadastre.getRefColModel(null),
        clicksToEdit : 1,
        tabIndex : 1,
        autoHeight : true,
        anchor : '95%',
        disabled : true,
        viewConfig : {
            forceFit : true,
        },
        listeners : {
            beforeedit : function(e) {
                if (e.column == 0) {
                    // pas d'edition de section si aucune ville selectionnée
                    if (parcCityCombo1.value == '') {
                        console.log("La ville doit être choisie d'abord")
                        return false;
                    }
                }
                if (e.column == 1) {
                    // pas d'edition de parcelle si aucune section selectionnée
                    if (e.record.data.section == '') {
                        console.log("La section et pre-section doit être choisie d'abord")
                        return false;
                    }
                    // on remplace le contenu du store des parcelles selon la section selectionnée
                    GEOR.Addons.Cadastre.loadParcelleStore(e.grid.getColumnModel().getColumnById(e.field).editor.getStore(), parcCityCombo1.value, e.record.data.section);
                    GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].enable();
                }
            },
            afteredit : function(e) {
                // on ajoute un champ vide, si le dernier champ est complet
                var lastIndex = e.grid.store.getCount() - 1;
                var lastData = e.grid.store.getAt(e.grid.store.getCount() - 1).data;

                if (lastData.section != '' && lastData.parcelle != '') {
                    e.grid.stopEditing();
                    var p = new e.grid.store.recordType({
                        section : '',
                        parcelle : ''
                    }); // create new record
                    e.grid.store.add(p);
                }
            }
        }
    });

    // fenêtre principale
    GEOR.Addons.Cadastre.rechercheParcelleWindow = new Ext.Window({
        title : OpenLayers.i18n('cadastrapp.parcelle.title'),
        minimizable : false,
        closable : true,
        resizable : true,
        draggable : true,
        constrainHeader : true,
        border : false,
        layout : 'fit',
        labelWidth : 100,
        width : 450,
        collapsible: true,
        defaults : {
            bodyStyle : 'padding:10px',
        },
        listeners : {
            close : function(window) {
                GEOR.Addons.Cadastre.rechercheParcelleWindow = null;
            }
        },
        items : [ {
            xtype : 'tabpanel',
            defaults : {
                anchor : '95%',
                height : 150,
                layoutOnTabChange : true,
                autoScroll : true
            },
            items : [ {
                // ONGLET 1
                title : OpenLayers.i18n('cadastrapp.parcelle.title.tab1'),
                xtype : 'form',
                defaultType : 'displayfield',
                id : 'parcFirstForm',
                items : [ parcCityCombo1, // combobox "villes"
                {
                    value : OpenLayers.i18n('cadastrapp.parcelle.city.exemple'),
                    fieldClass : 'displayfieldGray'
                }, parcelleGrid // grille "références"
                ]
            }, {
                // ONGLET 2
                title : OpenLayers.i18n('cadastrapp.parcelle.title.tab2'),
                xtype : 'form',
                defaultType : 'displayfield',
                id : 'parcSecondForm',
                items : [ parcCityCombo2, // combobox "villes"
                {
                    value : OpenLayers.i18n('cadastrapp.parcelle.city.exemple'),
                    fieldClass : 'displayfieldGray'
                }, {
                    // Add auto completion on dvoilib using getVoie service
                    fieldLabel : OpenLayers.i18n('cadastrapp.parcelle.town'),
                    allowBlank : false,
                    hiddenName : 'dvoilib',
                    xtype : 'combo',
                    mode : 'local',
                    value : '',
                    anchor : '95%',
                    forceSelection : false,
                    editable : true,
                    displayField : 'libellevoie',
                    valueField : 'dvoilib',
                    minLength : GEOR.Addons.Cadastre.minCharToSearch,
                    store : new Ext.data.JsonStore({
                        proxy : new Ext.data.HttpProxy({
                            url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getVoie',
                            method : 'GET',
                            autoload : true
                        }),
                        fields : [ 'dvoilib', {
                            name : 'libellevoie',
                            convert : function(v, rec) {
                                return rec.cconvo + ' ' + rec.dvoilib
                            }
                        } ]
                    }),
                    listeners : {
                        beforequery : function(q) {
                            if (q.query) {
                                var length = q.query.length;
                                var cgocommune = GEOR.Addons.Cadastre.rechercheParcelleWindow.items.items[0].getActiveTab().getForm().findField('cgocommune').value;
                                if (length >= GEOR.Addons.Cadastre.minCharToSearch && cgocommune.match(/([0-9]{6})/g).length == 1) {
                                    var insee = cgocommune.match(/([0-9]{6})/g)[0];
                                    q.combo.getStore().load({
                                        params : {
                                            cgocommune : insee,
                                            dvoilib : q.query
                                        }
                                    });
                                }
                            } else if (length < GEOR.Addons.Cadastre.minCharToSearch) {
                                q.combo.getStore().loadData([], false);
                            }
                            q.query = new RegExp(Ext.escapeRe(q.query), 'i');
                            q.query.length = length;
                        },
                        valid : function(element) {
                            GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].enable();
                        }
                    },
                }, {
                    value : OpenLayers.i18n('cadastrapp.parcelle.town.exemple'),
                    fieldClass : 'displayfieldGray'
                }, {
                    id : 'cfCadSearchByRef',
                    xtype : 'compositefield',
                    anchor : '95%',
                    fieldLabel : OpenLayers.i18n('cadastrapp.parcelle.street'),
                    disabled : true,
                    defaults : {
                        flex : 1
                    },
                    items : [ {
                        name : 'dnvoiri',
                        xtype : 'textfield',
                        anchor : '50%'
                    }, {
                        hiddenName : 'dindic',
                        xtype : 'combo',
                        mode : 'local',
                        value : '',
                        anchor : '50%',
                        triggerAction : 'all',
                        forceSelection : true,
                        editable : false,
                        displayField : 'name',
                        valueField : 'value',
                        store : parcBisStore
                    }, {
                        xtype : 'displayfield',
                        value : OpenLayers.i18n('cadastrapp.parcelle.street.exemple'),
                        fieldClass : 'displayfieldGray',
                    } ]
                } ]
            }, {
                // ONGLET 3
                title : OpenLayers.i18n('cadastrapp.parcelle.title.tab3'),
                xtype : 'form',
                defaultType : 'displayfield',
                id : 'parcThirdForm',
                items : [ {
                    xtype : 'textfield',
                    id : 'searchByRef',
                    allowBlank : false,
                    anchor : '95%',
                    fieldLabel : OpenLayers.i18n('cadastrapp.parcelle.ident'),
                    name : 'parcelle',
                    validator : function(value) {
                        if (!value || value.length < 14) {
                            return OpenLayers.i18n('cadastrapp.parcelle.ident.control');
                        } else {
                            return true;
                        }
                    },
                    listeners : {
                        valid : function(element) {
                            GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].enable();
                        }
                    }
                }, {
                    value : OpenLayers.i18n('cadastrapp.parcelle.ident.exemple'),
                    fieldClass : 'displayfieldGray'
                } ]
            }, {
                // ONGLET 4
                title : OpenLayers.i18n('cadastrapp.parcelle.title.tab4'),
                xtype : 'form',
                defaultType : 'displayfield',
                id : 'parcForthForm',
                fileUpload : true,
                items : [ {
                    xtype : 'textarea',
                    id : 'searchByLot',
                    fieldLabel : OpenLayers.i18n('cadastrapp.parcelle.lot'),
                    name : 'parcelle',
                    anchor : '95%',
                    validator : function(value) {
                        if (Ext.getCmp('searchByLotPath').getValue().length < 2 && value.length < 14) {
                            GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].disable();
                            return OpenLayers.i18n('cadastrapp.parcelle.lot.control');
                        } else {
                            return true;
                        }
                    },
                    listeners : {
                        focus : function(element) {
                            Ext.getCmp('searchByLotPath').reset();
                        },
                        valid : function(element) {
                            GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].enable();
                        }
                    }
                }, {
                    value : OpenLayers.i18n('cadastrapp.parcelle.lot.exemple'),
                    fieldClass : 'displayfieldGray'
                }, {
                    value : OpenLayers.i18n('cadastrapp.parcelle.or'),
                    fieldClass : 'displayfieldCenter'
                }, {
                    fieldLabel : OpenLayers.i18n('cadastrapp.parcelle.file.path'),
                    name : 'filePath',
                    xtype : 'fileuploadfield',
                    id : 'searchByLotPath',
                    anchor : '95%',
                    emptyText : OpenLayers.i18n('cadastrapp.parcelle.file.exemple'),
                    buttonText : OpenLayers.i18n('cadastrapp.parcelle.file.open'),
                    validator : function(value) {
                        if (Ext.getCmp('searchByLot').getValue().length < 2 && value.length < 2) {
                            GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].disable();
                            return false;
                        } else {
                            GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].enable();
                            return true;
                        }
                    },
                    listeners : {
                        fileselected : function(element) {
                            Ext.getCmp('searchByLot').reset();
                        }
                    }
                } ]
            } ],
            listeners : {
                tabchange : function(panel, newTab) {
                    GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].disable();
                }
            }
        } ],

        buttons : [ {
            id : 'butSearchParcByRef',
            text : OpenLayers.i18n('cadastrapp.search'),
            disabled : true,
            listeners : {
                click : function(b, e) {
                    var currentForm = GEOR.Addons.Cadastre.rechercheParcelleWindow.items.items[0].getActiveTab();

                    if (currentForm.getForm().isValid()) {
                        if (currentForm.id == 'parcFirstForm') {

                            // TITRE de l'onglet resultat
                            var resultTitle = currentForm.getForm().findField('cgocommune').lastSelectionText;

                            // PARAMS
                            var params = {};
                            params.cgocommune = currentForm.getForm().findField('cgocommune').value;

                            // Create new tab
                            GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, null);

                            parcelleGrid.getStore().each(function(record) {

                                if (record.data.parcelle != undefined && record.data.parcelle > 0 && record.data.section != undefined && record.data.section.length > 0) {

                                    params.dnupla = record.data.parcelle;
                                    params.ccopre = record.data.section.substring(0, record.data.section.length - 2);
                                    params.ccosec = record.data.section.substring(record.data.section.length - 2, record.data.section.length);

                                    //envoi la liste de resultat
                                    Ext.Ajax.request({
                                        method : 'POST',
                                        url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getParcelle',
                                        params : params,
                                        success : function(result) {
                                            GEOR.Addons.Cadastre.addResultToTab(result.responseText);
                                        },
                                        failure : function(result) {
                                            alert('ERROR');
                                        }
                                    });
                                } else {
                                    console.log("Not enough data to call the webservice ");
                                }
                            });
                        } else if (currentForm.id == 'parcSecondForm') {
                            //TITRE de l'onglet resultat
                            var resultTitle = currentForm.getForm().findField('cgocommune').lastSelectionText;

                            //PARAMS
                            var params = currentForm.getForm().getValues();
                            params.cgocommune = currentForm.getForm().findField('cgocommune').value;

                            //envoi des données d'une form
                            Ext.Ajax.request({
                                method : 'POST',
                                url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getParcelle',
                                params : params,
                                success : function(result) {
                                    GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, GEOR.Addons.Cadastre.getResultParcelleStore(result.responseText, false));
                                },
                                failure : function(result) {
                                    alert('ERROR');
                                }
                            });
                        } else if (currentForm.id == 'parcThirdForm') {

                            //TITRE de l'onglet resultat
                            var resultTitle = OpenLayers.i18n('cadastrapp.result.title.ids');

                            //envoi des données d'une form
                            Ext.Ajax.request({
                                method : 'POST',
                                url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getParcelle',
                                params : currentForm.getForm().getValues(),
                                success : function(result) {
                                    GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, GEOR.Addons.Cadastre.getResultParcelleStore(result.responseText, false));
                                },
                                failure : function(result) {
                                    alert('ERROR');
                                }
                            });
                        } else if (currentForm.id == 'parcForthForm') {
                            // Recherche parcelle par fichier
                            if (currentForm.getForm().findField('filePath').value != undefined && currentForm.getForm().findField('filePath').value.length > 2) {

                                var resultTitle = OpenLayers.i18n('cadastrapp.result.title.fichier');

                                // sousmet le form (pour envoyer le fichier)
                                currentForm.getForm().submit({
                                    url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'fromParcellesFile',
                                    params : {},
                                    success : function(form, action) {
                                        GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, GEOR.Addons.Cadastre.getResultParcelleStore(action.response.responseText, true));
                                    },
                                    failure : function(form, action) {
                                        alert('ERROR');
                                    }
                                });
                            } else {
                                //TITRE de l'onglet resultat
                                var resultTitle = "Recherche par lot";

                                //envoi des données d'une form
                                Ext.Ajax.request({
                                    method : 'POST',
                                    url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getParcelle',
                                    params : currentForm.getForm().getValues(),
                                    success : function(result) {
                                        GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, GEOR.Addons.Cadastre.getResultParcelleStore(result.responseText, false));
                                    },
                                    failure : function(result) {
                                        alert('ERROR');
                                    }
                                });

                            }
                        }
                    }
                }
            }
        }, {
            text: OpenLayers.i18n('cadastrapp.clear'),
            listeners: {
                click: function(b, e) {
                    var currentForm = GEOR.Addons.Cadastre.rechercheParcelleWindow.items.items[0].getActiveTab();
                    GEOR.ls.set("default_cadastrapp_city"," ");
                    if (currentForm.id == 'parcFirstForm') {
                        parcelleGrid.getStore().removeAll();
                    }
                    currentForm.form.reset();
                    GEOR.Addons.Cadastre.rechercheParcelleWindow.buttons[0].disable();
                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.close'),
            listeners : {
                click : function(b, e) {
                    GEOR.Addons.Cadastre.rechercheParcelleWindow.close();
                }
            }
        } ]
    });

};
