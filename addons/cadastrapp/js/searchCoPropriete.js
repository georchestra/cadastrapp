Ext.namespace("GEOR.Addons.Cadastre");

/**
 * Init windows if not existing
 */
GEOR.Addons.Cadastre.onClickRechercheCoPropriete = function(tab) {

    if (GEOR.Addons.Cadastre.coProprieteWindow == null) {
        GEOR.Addons.Cadastre.initRechercheCoPropriete();
    }
    GEOR.Addons.Cadastre.coProprieteWindow.show();

}

/**
 * Create search windows to find list of plots using owners information Town,
 * name or id
 */
GEOR.Addons.Cadastre.initRechercheCoPropriete = function() {

    // combobox "villes" de l'onglet "Nom usage ou Naissance"
    var propCityCombo1 = new Ext.form.ComboBox({
        fieldLabel : OpenLayers.i18n('cadastrapp.proprietaire.city'),
        hiddenName : 'cgocommune',
        width : 300,
        mode : 'local',
        allowBlank: false,
        value:"",
        forceSelection : true,
        editable : true,
        displayField : 'displayname',
        valueField : 'cgocommune',
        store : GEOR.Addons.Cadastre.getPartialCityStore(),
        listeners : {
            beforequery : function(q) {
                // Check not null querry and if enough chars
                if (q.query) {
                    var length = q.query.length;
                    // If enough chars in query
                    if (length >= GEOR.Addons.Cadastre.minCharToSearch) {

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
            valid : function(element) {
                Ext.getCmp('cadastrappCoProprieteSearchDdenomcombo').enable();
                Ext.getCmp('cadastrappCoProprieteSearchTexfieldParcelle').enable();
                Ext.getCmp('cadastrappCoProprieteSearchTexfieldComptecommunal').enable();
            }
        }
    });

    // Construction de la fenêtre principale
    // constituée de deux onglets et des boutons d'ouverture de fichier .csv, de
    // recherche
    // et de fermeture de la fenetre
    GEOR.Addons.Cadastre.coProprieteWindow = new Ext.Window({
        title : OpenLayers.i18n('cadastrapp.search.copropriete.title'),
        frame : true,
        autoScroll : true,
        minimizable : false,
        closable : true,
        resizable : true,
        draggable : true,
        constrainHeader : true,
        border : false,
        width : 480,
        defaults : {
            autoHeight : true,
            bodyStyle : 'padding:10px',
            flex : 1
        },
        listeners : {
            close : function(window) {
                GEOR.Addons.Cadastre.coProprieteWindow = null;
            }
        },
        items : [ {
            // Form
            id : 'cadastrappCoProprieteSearchForm',
            xtype : 'form',
            defaultType : 'displayfield',
            labelWidth : 130,
            items : [ propCityCombo1, {
                value : OpenLayers.i18n('cadastrapp.proprietaire.city.exemple'),
                fieldClass : 'displayfieldGray'
            }, {
                id : 'cadastrappCoProprieteSearchDdenomcombo',
                hiddenName : 'ddenom',
                fieldLabel : OpenLayers.i18n('cadastrapp.proprietaire.name'),
                xtype : 'combo',
                width : 300,
                mode : 'local',
                value : '',
                forceSelection : false,
                editable : true,
                displayField : 'app_nom_usage',
                valueField : 'app_nom_usage',
                minLength : 12,
                minLengthText : OpenLayers.i18n('cadastrapp.search.copropriete.ddenom.control'),
                disabled : true,
                store : new Ext.data.JsonStore({
                    proxy : new Ext.data.HttpProxy({
                        url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getProprietaire',
                        method : 'GET',
                        autoload : true
                    }),
                    fields : [ 'app_nom_usage' ]
                }),
                listeners : {
                    beforequery : function(q) {
                        if (q.query) {
                            var length = q.query.length;
                            if (length >= GEOR.Addons.Cadastre.minCharToSearch) {
                                q.combo.getStore().load({
                                    params : {
                                        cgocommune : GEOR.Addons.Cadastre.coProprieteWindow.items.items[0].getForm().findField('cgocommune').value,
                                        ddenom : q.query,
                                    }
                                });
                            }
                        } else if (length < GEOR.Addons.Cadastre.minCharToSearch) {
                            q.combo.getStore().loadData([], false);
                        }
                        q.query = new RegExp(Ext.escapeRe(q.query), 'i');
                        q.query.length = length;
                    }
                }
            }, {
                value : OpenLayers.i18n('cadastrapp.proprietaire.name.exemple'),
                fieldClass : 'displayfieldGray'
            }, {
                xtype : 'textfield',
                id : 'cadastrappCoProprieteSearchTexfieldParcelle',
                fieldLabel : OpenLayers.i18n('cadastrapp.search.copropriete.parcelle.ident'),
                name : 'parcelle',
                width : 300,
                minLength : 12,
                minLengthText : OpenLayers.i18n('cadastrapp.parcelle.ident.control'),
            }, {
                value : OpenLayers.i18n('cadastrapp.parcelle.ident.exemple'),
                fieldClass : 'displayfieldGray'
            }, {
                xtype : 'textfield',
                id : 'cadastrappCoProprieteSearchTexfieldComptecommunal',
                fieldLabel : OpenLayers.i18n('cadastrapp.search.copropriete.comptecommunal.ident'),
                name : 'comptecommunal',
                disabled: true,
                width : 300,
                minLength : 12,
                minLengthText : OpenLayers.i18n('cadastrapp.search.copropriete.comptecommunal.control')
            }, {
                value : OpenLayers.i18n('cadastrapp.search.copropriete.comptecommunal.exemple'),
                fieldClass : 'displayfieldGray'
            } ]
        } ],
        buttons : [ {
            text : OpenLayers.i18n('cadastrapp.search'),
            disabled : false,
            listeners : {
                click : function(b, e) {
                    
                    var currentForm = GEOR.Addons.Cadastre.coProprieteWindow.items.items[0];

                    // Form value to check which service to call
                    var values = currentForm.getForm().getValues();
                    
                    if (values.parcelle && values.parcelle.length > 0 && Ext.getCmp("cadastrappCoProprieteSearchTexfieldParcelle").isValid()) {

                        var resultTitle = values.parcelle;
                        var requestparam = {};
                        requestparam.parcelle = values.parcelle;
                        Ext.Ajax.request({
                            method : 'POST',
                            url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getParcelle',
                            params : requestparam,
                            success : function(result) {
                                GEOR.Addons.Cadastre.addNewResultParcelle(resultTitle, GEOR.Addons.Cadastre.getResultParcelleStore(result.responseText, false));
                            },
                            failure : function(result) {
                                console.log('Error when getting parcelle information, check server side');
                            }
                        });
                    } else if (values.cgocommune && values.cgocommune.length > 0 &&
                               ((values.comptecommunal && values.comptecommunal.length > 0 && Ext.getCmp('cadastrappCoProprieteSearchTexfieldComptecommunal').isValid())
                                 || (values.ddenom && values.ddenom.length >0 && Ext.getCmp('cadastrappCoProprieteSearchDdenomcombo').isValid()))) {
                        
                        var resultTitle = currentForm.getForm().findField('cgocommune').lastSelectionText;
                        if(resultTitle.length == 0){
                            resultTitle = values.comptecommunal;
                        }
                        var requestparam = {};
                        requestparam.comptecommunal = values.comptecommunal;
                        requestparam.cgocommune = values.cgocommune;
                        requestparam.ddenom = values.ddenom;
                        requestparam.details=1;

                        // envoi des données d'une form
                        Ext.Ajax.request({
                            method : 'GET',
                            url : GEOR.Addons.Cadastre.cadastrappWebappUrl + '/getCoProprietaireList',
                            params : requestparam,
                            success : function(response) {

                                var comptecommunalArray = [];
                                var result = Ext.decode(response.responseText);
                                Ext.each(result, function(value, index) {
                                    comptecommunalArray.push(value.comptecommunal);
                                });

                                if (result.length > 1) {
                                    GEOR.Addons.Cadastre.addNewResultProprietaire(resultTitle, result, null);
                                } else {
                                    var paramsGetParcelle = {};
                                    paramsGetParcelle.comptecommunal = comptecommunalArray;
                                    // envoi des données d'une form
                                    Ext.Ajax.request({
                                        method : 'POST',
                                        url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getParcelle',
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
                    } else{
                        // no require parameter to make a call
                        Ext.MessageBox.alert(OpenLayers.i18n('cadastrapp.search.copropriete.alert.title'), OpenLayers.i18n('cadastrapp.search.copropriete.alert.message'));        
                    }
                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.close'),
            listeners : {
                click : function(b, e) {
                    GEOR.Addons.Cadastre.coProprieteWindow.close();
                }
            }
        } ]
    });

};