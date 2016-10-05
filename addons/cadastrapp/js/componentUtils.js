Ext.namespace("GEOR.Addons.Cadastre.Component");


/**
 * liste des communes
 */
GEOR.Addons.Cadastre.Component.getComboCommune = function(id) {
		return new Ext.form.ComboBox({
			id : 'comList' +id,
			hiddenName : 'cgocommune',
			allowBlank : false,
			mode : 'local',
			value : '',
			emptyText:OpenLayers.i18n('cadastrapp.ObjectRequest.parcelle.commune'),
			//anchor : '95%',
			editable : true,
			tabIndex : 0,
			displayField : 'displayname',
			forceSelection :true,
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
				}
			}
		});
	}

/**
 * liste des propriétaire de la communes en paramètre
 */
GEOR.Addons.Cadastre.Component.getComboProprioByCommune = function(id, communeListId) {
		return new Ext.form.ComboBox({
		    id : 'proprioList' + id,
		    hiddenName : 'ddenom',
		    xtype : 'combo',
		    allowBlank : false,
		    mode : 'local',
		    value : '',
		    emptyText:OpenLayers.i18n('cadastrapp.ObjectRequest.parcelle.proprietaire'),
		    forceSelection : true,
		    anchor : '95%',
		    editable : true,
		    displayField : 'app_nom_usage',
		    valueField : 'app_nom_usage',
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
		                            cgocommune : Ext.getCmp(communeListId+id).value,
		                            ddenom : q.query,
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
		            //GEOR.Addons.Cadastre.proprietaireWindow.buttons[0].enable();
		        }
		    }
		});
	}


/**
 * liste des sections par commune 
 */
GEOR.Addons.Cadastre.Component.getComboSectionByCommune = function(id, communeListId) {
	var cgocommune = Ext.getCmp(communeListId+id).value;
	if(!cgocommune){
		cgocommune = null;
	}
	
		return new Ext.form.ComboBox({
		    id : 'sectionList' + id,
		    hiddenName : 'section',
		    xtype : 'combo',
		    cls: 'comboStyle',
		    allowBlank : false,
		    mode : 'local',
		    value : '',
		    emptyText:OpenLayers.i18n('cadastrapp.ObjectRequest.parcelle.section'),
		    forceSelection : false,
		    anchor : '95%',
		    editable : true,
		    forceSelection : true,
		    displayField : 'fullccosec',
		    valueField : 'fullccosec',
		    disabled : true,
		    store : GEOR.Addons.Cadastre.getSectionStore(cgocommune),
		});
	}

/**
 * liste des parcelles
 */
GEOR.Addons.Cadastre.Component.getComboParcelleBySection = function(id) {
	
		return new Ext.form.ComboBox({
		    id : 'parcelleList' + id,
		    hiddenName : 'parcelle',
		    cls:'comboStyle',
		    xtype : 'combo',
		    allowBlank : false,
		    mode : 'local',
		    value : '',
		    emptyText:OpenLayers.i18n('cadastrapp.ObjectRequest.parcelle.numero'),
		    forceSelection : true,
		    anchor : '95%',
		    editable : true,
		    displayField : 'dnupla',
		    valueField : 'dnupla',
		    disabled : true,
		    store : GEOR.Addons.Cadastre.initParcelleStore(),
		    listeners: {
                beforequery: function(q){  
                    if (q.query) {
                        var length = q.query.length;
                        q.query = new RegExp(Ext.escapeRe(q.query), 'i');
                        q.query.length = length;
                    }
                }
            }
		});
	}

/**
 * CheckBox
 */
GEOR.Addons.Cadastre.Component.getCheckBoxGroup = function(bp,rb, id) {
    
	return new Ext.form.CheckboxGroup({
	    allowBlank: false,
        id : 'checkBoxGroup'+id,
        cls:'radio',
        items: [{
            xtype: 'checkbox',
            boxLabel: 'Relevé de propriété',
            id : 'rpBox' + id,
            checked: rb,
            inputValue: 'RP'
    
        },{
            xtype: 'checkbox',
                boxLabel: 'Bordereau parcellaire',
                id : 'bpBox' + id,
                checked: bp,
                inputValue: 'BP'
        }]
	});
}

/**
 * liste des propriétaire de la communes en paramètre
 */
GEOR.Addons.Cadastre.Component.getComboProprioByInfoParcelle = function(id, communeListId, sectionId, numeroId) {
		return new Ext.form.ComboBox({
		    id : 'proprioList' + id,
		    hiddenName : 'ddenom',
		    xtype : 'combo',
		    allowBlank : false,
		    mode : 'local',
		    value : '',
		    emptyText:OpenLayers.i18n('cadastrapp.ObjectRequest.parcelle.proprietaire'),
		    forceSelection : true,
		    anchor : '95%',
		    editable : true,
		    displayField : 'app_nom_usage',
		    valueField : 'app_nom_usage',
		    disabled : true,
		    store : new Ext.data.JsonStore({
		        proxy : new Ext.data.HttpProxy({
		            url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getProprietairesByInfoParcelles',
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
		                            commune : Ext.getCmp(communeListId+id).value,
		                            section : Ext.getCmp(sectionId+id).value,
		                            numero : Ext.getCmp(numeroId+id).value,
		                            ddenom : q.query,
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
		            //GEOR.Addons.Cadastre.proprietaireWindow.buttons[0].enable();
		        }
		    }
		});
	}