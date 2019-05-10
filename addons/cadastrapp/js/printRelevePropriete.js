Ext.namespace("GEOR.Addons.Cadastre");

/**
 * @param compteComunal (could be a list of id user or only one
 * @param parcelleId The targeted plot ID
 */
GEOR.Addons.Cadastre.onClickPrintRelevePropriete = function(compteCommunal, parcelleId) {
    if (GEOR.Addons.Cadastre.printReleveProprieteWindow != null) {
        GEOR.Addons.Cadastre.printReleveProprieteWindow.close();
    }
    
    GEOR.Addons.Cadastre.initPrintReleveProprieteWindow(compteCommunal, parcelleId);
    GEOR.Addons.Cadastre.printReleveProprieteWindow.show();
}

/**
 * init new windows
 * 
 * @param parcelleId
 */
GEOR.Addons.Cadastre.initPrintReleveProprieteWindow = function(compteCommunal, parcelleId) {
    parcelleId = (typeof parcelleId !== 'undefined') ? parcelleId : null;

    // fenêtre principale
    GEOR.Addons.Cadastre.printReleveProprieteWindow = new Ext.Window({
        title : OpenLayers.i18n('cadastrapp.relevepropriete.title') + ' - ' + parcelleId,
        frame : true,
        autoScroll : true,
        minimizable : false,
        closable : true,
        resizable : false,
        draggable : true,
        constrainHeader : true,
        border : false,
        width : 300,
        defaults : {
            autoHeight : true,
            bodyStyle : 'padding:10px',
            flex : 1
        },

        listeners : {
            close : function(window) {
                GEOR.Addons.Cadastre.printReleveProprieteWindow = null;
            }
        },

        items : [ {
            xtype : 'form',
            id : 'releveproprieteForm',
            height : 200,
            labelWidth : 1,
            autoHeight : true,

            items : [ {
                xtype : 'fieldset',
                title : OpenLayers.i18n('cadastrapp.relevepropriete.data'),
                autoHeight : true,

                items : [ {
                    xtype : 'hidden',
                    name : 'compteCommunal',
                    value : compteCommunal
                }, {
                    xtype : 'radio',
                    boxLabel : OpenLayers.i18n('cadastrapp.relevepropriete.partial'),
                    checked : true,
                    name : 'parcelleId',
                    inputValue : parcelleId

                }, {
                    xtype : 'radio',
                    boxLabel : OpenLayers.i18n('cadastrapp.relevepropriete.full'),
                    name : 'parcelleId',
                    inputValue : ''
                } ]
            }, {
                xtype : 'fieldset',
                title : OpenLayers.i18n('cadastrapp.relevepropriete.type'),
                autoHeight : true,

                items : [  {
                    xtype : 'radio',
                    boxLabel : OpenLayers.i18n('cadastrapp.relevepropriete.type.pdf'),
                    checked : true,
                    name : 'exportType',
                    id : 'radioRPpdf'
                },{
                    xtype : 'radio',
                    boxLabel : OpenLayers.i18n('cadastrapp.relevepropriete.type.csv'),                 
                    name : 'exportType',
                    id : 'radioRPcsv'
                }]
            }  ]
        } ],

        buttons : [ {
            text : OpenLayers.i18n('cadastrapp.generate'),
            listeners : {
                click : function(b, e) {

                    // PARAMS
                    var params = GEOR.Addons.Cadastre.printReleveProprieteWindow.items.items[0].getForm().getValues();
                    
                    // pdf by default
                    var url = GEOR.Addons.Cadastre.url.serviceCreateRelevePropriete + '?';
                    // check csv radio button value
                    if (Ext.getCmp('radioRPcsv').getValue()) {
                        url = GEOR.Addons.Cadastre.url.serviceCreateReleveProprieteAsCSV + '?';
                    }

                    var url = url + Ext.urlEncode(params);

					Ext.DomHelper.useDom = true;
					
					// Directly download file, without and call service without ogcproxy
					Ext.DomHelper.append(document.body, {
						tag : 'iframe',
						id : 'downloadIframe',
						frameBorder : 0,
						width : 0,
						height : 0,     
						css : 'display:none;visibility:hidden;height:0px;',
						src : url
					});
					
                    //TODO add waiting panel
                    GEOR.Addons.Cadastre.printReleveProprieteWindow.close();
                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.close'),
            listeners : {
                click : function(b, e) {
                    GEOR.Addons.Cadastre.printReleveProprieteWindow.close();
                }
            }
        } ]
    });
}
