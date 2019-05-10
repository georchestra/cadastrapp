Ext.namespace("GEOR.Addons.Cadastre");

/**
 * Init windows if not existing
 */
GEOR.Addons.Cadastre.onClickRechercheProprietaire = function(tab) {

    if (GEOR.Addons.Cadastre.proprietaireWindow == null) {
        GEOR.Addons.Cadastre.initRechercheProprietaire();
    }
    GEOR.Addons.Cadastre.proprietaireWindow.show();
    GEOR.Addons.Cadastre.proprietaireWindow.items.items[0].setActiveTab(tab);
}

/**
 * Create search windows to find list of plots using owners information Town,
 * name or id
 */
GEOR.Addons.Cadastre.initRechercheProprietaire = function() {

    // comboboxe "villes" de l'onglet "Nom usage ou Naissance"
    var propCityCombo1 = new Ext.form.ComboBox({
        fieldLabel : OpenLayers.i18n('cadastrapp.proprietaire.city'),
        hiddenName : 'cgocommune',
        allowBlank : false,
        mode : 'local',
        value : '',
        forceSelection : true,
        editable : true,
        displayField : 'displayname',
        valueField : 'cgocommune',
        anchor : '95%',
        store : GEOR.Addons.Cadastre.getPartialCityStore(),
        listeners : {
            beforequery : function(q) {
                // Check not null querry and if enough chars
                if (q.query) {
                    var length = q.query.length;
                    // If enough chars in query
                    if (length >= GEOR.Addons.Cadastre.minCharToSearch && q.combo.getStore().getCount() == 0) {

                        // if not a number request by town name
                        if (isNaN(q.query)) {
                            q.combo.getStore().load({
                                params : {
                                    libcom : q.query
                                }
                            });
                        } else {
                            // if not a number request by town code
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
                Ext.getCmp('comboDnomlpSearchByOwners').enable();
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

    // Combobox "Villes" de l'onglet "Compte propriétaire"
    var propCityCombo2 = new Ext.form.ComboBox({
        fieldLabel : OpenLayers.i18n('cadastrapp.proprietaire.city'),
        hiddenName : 'cgocommune',
        allowBlank : false,
        mode : 'local',
        value : '',
        forceSelection : true,
        editable : true,
        displayField : 'displayname',
        valueField : 'cgocommune',
        anchor : '95%',
        store : GEOR.Addons.Cadastre.getPartialCityStore(),
        // Recherche de la ville demandée avec gestion de l'autocomplétion
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
            valid : function(combo) {
                Ext.getCmp('gripOwnerSearchByOwners').enable();
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

    // grille "proprietaires"
    var proprietaireGrid = new Ext.grid.EditorGridPanel({
        id : 'gripOwnerSearchByOwners',
        fieldLabel : OpenLayers.i18n('cadastrapp.proprietaire.proprietaires'),
        name : 'proprietaires',
        xtype : 'editorgrid',
        clicksToEdit : 1,
        ds : GEOR.Addons.Cadastre.getVoidProprietaireStore(),
        cm : GEOR.Addons.Cadastre.getProprietaireColModel(),
        autoHeight : true,
        viewConfig : {
            forceFit : true,
        },
        anchor : '95%',
        border : true,
        disabled : true,
        listeners : {
            beforeedit : function(e) {
                if (e.column == 0 && propCityCombo2.value == '') {
                    return false;
                }
            },
            afteredit : function(e) {
                // on ajoute un champ vide, si le dernier champ est complet
                var lastIndex = e.grid.store.getCount() - 1;
                var lastData = e.grid.store.getAt(e.grid.store.getCount() - 1).data;

                if (lastData.proprietaire != '') {
                    var p = new e.grid.store.recordType({
                        proprietaire : ''
                    }); // create new record
                    e.grid.stopEditing();
                    e.grid.store.add(p); // insert a new record into the
                    // store (also see add)
                    this.startEditing(e.row + 1, 0);
                }
                GEOR.Addons.Cadastre.proprietaireWindow.buttons[0].enable();
            }
        }
    });

    // Construction de la fenêtre principale
    // constituée de deux onglets et des boutons d'ouverture de fichier .csv, de
    // recherche
    // et de fermeture de la fenetre
    GEOR.Addons.Cadastre.proprietaireWindow = new Ext.Window({
        title : OpenLayers.i18n('cadastrapp.proprietaire.title'),
        frame : true,
        minimizable : false,
        closable : true,
        resizable : true,
        draggable : true,
        constrainHeader : true,
        border : false,
        layout : 'fit',
        autoHeight: true,
        labelWidth : 100,
        width : 450,
        collapsible: true,
        defaults : {
            bodyStyle : 'padding:10px',
        },
        listeners : {
            close : function(window) {
                GEOR.Addons.Cadastre.proprietaireWindow = null;
            },
            show : function(window){
                window.alignTo(GeoExt.MapPanel.guess().map.div,"tl",[0,150],false);
            }
        },
        items : [ {
            xtype : 'tabpanel',
            activeTab : 0,
            defaults : {
                anchor : '95%',
                layoutOnTabChange : true,
                autoScroll : true
            },
            items : [ {
                // ONGLET "Nom Usage ou Naissance"
                id : 'propFirstForm',
                xtype : 'form',
                title : OpenLayers.i18n('cadastrapp.proprietaire.title.tab1'),
                defaultType : 'displayfield',
                labelWidth : 110,
                items : [ propCityCombo1, {
                    value : OpenLayers.i18n('cadastrapp.proprietaire.city.exemple'),
                    fieldClass : 'displayfieldGray'
                }, {
                    id : 'comboDnomlpSearchByOwners',
                    hiddenName : 'ddenom',
                    fieldLabel : OpenLayers.i18n('cadastrapp.proprietaire.name'),
                    xtype : 'combo',
                    allowBlank : false,
                    mode : 'local',
                    value : '',
                    forceSelection : false,
                    anchor : '95%',
                    editable : true,
                    displayField : 'nom',
                    valueField : 'nom',
                    disabled : true,
                    store : new Ext.data.JsonStore({
                        proxy : new Ext.data.HttpProxy({
                            url : GEOR.Addons.Cadastre.url.serviceProprietaire,
                            method : 'GET',
                            autoload : true
                        }),
                        fields : [ { name : 'nom',
                            convert : function(v, rec) {
                                if(Ext.getCmp('checkBoxSearchByBirthNames').getValue()){
                                    return rec.app_nom_naissance 
                                }
                                else{
                                    return rec.app_nom_usage 
                                } 
                            }
                        }]
                    }),
                    listeners : {
                        beforequery : function(q) {
                            if (q.query) {
                                var length = q.query.length;
                                if (length >= GEOR.Addons.Cadastre.minCharToSearch) {

                                    var birthsearch = false;
                                    // If check, change JSON store attribut to search in additional birth names
                                    if(Ext.getCmp('checkBoxSearchByBirthNames').getValue()){
                                        birthsearch = true
                                    }
                                
                                    q.combo.getStore().load({
                                        params : {
                                            cgocommune : GEOR.Addons.Cadastre.proprietaireWindow.items.items[0].getActiveTab().getForm().findField('cgocommune').value,
                                            ddenom : q.query,
                                            birthsearch : birthsearch
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
                            GEOR.Addons.Cadastre.proprietaireWindow.buttons[0].enable();
                        }
                    }
                }, {
                    id : 'checkBoxSearchByBirthNames',
                    xtype : 'checkbox',
                    hiddenName : 'birthName',
                    labelSeparator: '',
                    fieldLabel : '',
                    boxLabel: OpenLayers.i18n('cadastrapp.proprietaire.search.birth'),               
                },
                {
                    value : OpenLayers.i18n('cadastrapp.proprietaire.name.exemple'),
                    fieldClass : 'displayfieldGray'
                }, {
                    value : OpenLayers.i18n('cadastrapp.proprietaire.name.tooltip'),
                    fieldClass : 'displayfieldGray'
                } ],
                layout : 'form',
                autoHeight: true
            }, {
                // ONGLET "Compte proprietaire"
                id : 'propSecondForm',
                xtype : 'form',
                title : OpenLayers.i18n('cadastrapp.proprietaire.title.tab2'),
                defaultType : 'displayfield',
                items : [ 
                    propCityCombo2, 
                    {
                        value : OpenLayers.i18n('cadastrapp.proprietaire.city.exemple'),
                        fieldClass : 'displayfieldGray'
                    }, 
                    proprietaireGrid, // grille "proprietaires"
                ],
                layout : 'form',
                autoHeight: true
            }, {
                // ONGLET "par lot"
                id : 'propThirdForm',
                xtype : 'form',
                title : OpenLayers.i18n('cadastrapp.proprietaire.title.tab3'),
                defaultType : 'displayfield',
                fileUpload : true,
                items : [ {
                    fieldLabel : OpenLayers.i18n('cadastrapp.proprietaire.file.path'),
                    name : 'filePath',
                    xtype : 'fileuploadfield',
                    anchor : '95%',
                    emptyText : OpenLayers.i18n('cadastrapp.proprietaire.file.exemple'),
                    buttonText : OpenLayers.i18n('cadastrapp.proprietaire.file.open'),
                    validator : function(value) {
                        if (value.length < 2) {
                            GEOR.Addons.Cadastre.proprietaireWindow.buttons[0].disable();
                            return false;
                        } else {
                            GEOR.Addons.Cadastre.proprietaireWindow.buttons[0].enable();
                            return true;
                        }
                    },
                }, {
                    value : OpenLayers.i18n('cadastrapp.proprietaire.file.explanation'),
                    fieldClass : 'displayfieldGray'
                } ]
            } ],
            listeners : {
                tabchange : function(panel, newTab) {
                    if (GEOR.Addons.Cadastre.proprietaireWindow) {
                        GEOR.Addons.Cadastre.proprietaireWindow.buttons[0].disable();
                    }
                }
            }
        } ],
        buttons : [ {
            text : OpenLayers.i18n('cadastrapp.search'),
            disabled : true,
            listeners : {
                click : function(b, e) {
                    var currentForm = GEOR.Addons.Cadastre.proprietaireWindow.items.items[0].getActiveTab();
                    if (currentForm.id == 'propFirstForm') {
                        if (currentForm.getForm().isValid()) {
                            // TITRE de l'onglet resultat
                            var resultTitle = currentForm.getForm().findField('cgocommune').lastSelectionText;

                            // PARAMS
                            var params = currentForm.getForm().getValues();
                            params.details = 2;
                            
                            // search by birthname
                            if(Ext.getCmp('checkBoxSearchByBirthNames').getValue()){
                                params.birthsearch = true;
                            }

                            // envoi des données d'une form
                            Ext.Ajax.request({
                                method : 'GET',
                                url : GEOR.Addons.Cadastre.url.serviceProprietaire,
                                params : params,
                                success : function(response) {

                                    var comptecommunalArray = [];
                                    var result = Ext.decode(response.responseText);
                                    for (var i = 0; i < result.length; i++) {
                                        comptecommunalArray.push(result[i].comptecommunal);
                                    }
                                    if (result.length > 1) {
                                        GEOR.Addons.Cadastre.addNewResultProprietaire(resultTitle, result, null);
                                    } else {
                                        // envoi des données d'une form
                                        Ext.Ajax.request({
                                            method : 'POST',
                                            url : GEOR.Addons.Cadastre.url.serviceParcelle,
                                            params : {comptecommunal : comptecommunalArray},
                                            success : function(result) {
                                                GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, GEOR.Addons.Cadastre.getResultParcelleStore(result.responseText, false));
                                            },
                                            failure : function(result) {
                                                console.log('Error when getting parcelle information, check server side');
                                            }
                                        });
                                    }
                                },
                                failure : function(result) {
                                    alert('Error when getting proprietaire information, check server side');
                                }
                            });
                        }

                    } else {
                        if (currentForm.getForm().isValid()) {

                            // Gestion du form import par fichier
                            if (currentForm.getForm().findField('filePath') && currentForm.getForm().findField('filePath').value != undefined) {
     
                                // Message de chargement
                                var box = Ext.MessageBox.wait("Lecture du document...",OpenLayers.i18n('Chargement')); 
                                
                                // Titre de l'onglet resultat
                                var resultTitle = OpenLayers.i18n('cadastrapp.result.title.fichier');
    
                                // LECTURE CSV
                                var files = document.getElementById("propThirdForm").querySelectorAll("input[type=file]")[0].files; 
                                
                                function readFile (blob, callback){                            
                                    var reader = new FileReader();
                                    reader.onload = callback;
                                    reader.readAsText(blob);                                 
                                }

                                // read content
                                readFile(files[0], function(e){
                                    
                                    if (typeof(e.target.result) == "string"){
                                       
                                        var content = JSON.stringify(e.target.result);   
                                        content = JSON.parse(content);
                                        // Separateurs: virgule, point virgule, pipe, espace et retour à la ligne
                                        content = content.split(/\r\n|\n|,|;|\|/);                     
                                       
                                        // Filtre pour ne garder que ce qui ressemble a un compte communal
                                        // et ne garder qu'une occurence de chaque compte communal
                                        var compteCommunaux = content.filter(function (value, index, self) {
                                            return (self.indexOf(value) === index && value.length >= 8 && value.match("^[0-9]{5,}.*"));
                                        });
     
                                       // Envoie la liste de compte communal extraite du fichier                                      
                                       Ext.Ajax.request({
                                           method : 'POST',
                                           url : GEOR.Addons.Cadastre.url.serviceParcelle,
                                           params : { comptecommunal : compteCommunaux},
                                           success : function(result) {
                                               box.hide();
                                               GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, GEOR.Addons.Cadastre.getResultParcelleStore(result.responseText, false));
                                           },
                                           failure : function(form, action) {
                                               box.hide();
                                               alert('ERROR');
                                           }
                                       });
                                   }else{
                                       box.hide();
                                       alert('Mauvais type de fichier');
                                   }                           
                                });

                            } else {

                                // TITRE de l'onglet resultat
                                var resultTitle = currentForm.getForm().findField('cgocommune').lastSelectionText;

                                // PARAMS
                                var params = currentForm.getForm().getValues();
                                params.details = 2;

                                // liste des proprietaires
                                var dnuproList = new Array();
                                proprietaireGrid.getStore().each(function(record) {
                                    dnuproList.push(record.data.proprietaire);
                                });
                                params.dnupro = dnuproList;
                                // PAR LISTE
                                // envoi des données d'une form
                                Ext.Ajax.request({
                                    method : 'GET',
                                    url : GEOR.Addons.Cadastre.url.serviceProprietaire,
                                    params : params,
                                    success : function(response) {

                                        var comptecommunalArray = [];
                                        var result = Ext.decode(response.responseText);
                                        for (var i = 0; i < result.length; i++) {
                                            comptecommunalArray.push(result[i].comptecommunal);
                                        }
                                        if (result.length > 1) {
                                            GEOR.Addons.Cadastre.addNewResultProprietaire(resultTitle, result, null);
                                        } else {
                                            var paramsGetParcelle = {};
                                            paramsGetParcelle.comptecommunal = comptecommunalArray;
                                            // envoi des données d'une form
                                            Ext.Ajax.request({
                                                method : 'POST',
                                                url : GEOR.Addons.Cadastre.url.serviceParcelle,
                                                params : paramsGetParcelle,
                                                success : function(result) {
                                                    GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, GEOR.Addons.Cadastre.getResultParcelleStore(result.responseText, false));
                                                },
                                                failure : function(result) {
                                                    console.log('Error when getting parcelle information, check server side');
                                                }
                                            });
                                        }
                                    },
                                    failure : function(result) {
                                        alert('Error when getting proprietaire information, check server side');
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
                    var currentForm = GEOR.Addons.Cadastre.proprietaireWindow.items.items[0].getActiveTab();
                    GEOR.ls.set("default_cadastrapp_city"," ");
                    if (currentForm.id == 'propSecondForm') {
                        proprietaireGrid.getStore().removeAll();
                    }
                    currentForm.form.reset();
                    GEOR.Addons.Cadastre.proprietaireWindow.buttons[0].disable();
                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.close'),
            listeners : {
                click : function(b, e) {
                    GEOR.Addons.Cadastre.proprietaireWindow.close();
                }
            }
        } ]
    });

};
