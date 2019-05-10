Ext.namespace("GEOR.Addons.Cadastre");

/**
 * onClickPrintLotsWindow
 * 
 * @param parcelleId String
 * @param batiments Array of building String id
 * 
 * @returns Ext.Window 
 */
GEOR.Addons.Cadastre.onClickPrintLotsWindow = function(parcelleId, batiments) {
    // Close previous windows if already opened
    if (GEOR.Addons.Cadastre.printLotsWindow != null) {
        GEOR.Addons.Cadastre.printLotsWindow.close();
    }

    GEOR.Addons.Cadastre.initPrintLotsWindow(parcelleId, batiments);
    GEOR.Addons.Cadastre.printLotsWindow.show();
}

/**
 * initPrintLotsWindow
 * 
 * @param parcelleId String
 * @param batiments Array of building String id
 * 
 * @returns Ext.Window 
 */
GEOR.Addons.Cadastre.initPrintLotsWindow = function(parcelleId, batiments) {

    // combobox building list with buildind array
    var comboDnubat = new Ext.form.ComboBox({
        width : 150,
        mode : 'local',
        forceSelection : true,
        displayField : 'dnubat',
        valueField : 'dnubat',
        store : batiments,
        value : batiments[0],
        listeners : {
            expand : function() {
                this.store.removeAll();
                this.store.loadData(batiments);
                this.show();
            }
        }
    });

    // new windows
    GEOR.Addons.Cadastre.printLotsWindow = new Ext.Window({
        title : OpenLayers.i18n('cadastrapp.lots.title') + ' - ' + parcelleId,
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
                GEOR.Addons.Cadastre.printLotsWindow = null;
            }
        },

        items : [ {
            xtype : 'form',
            id : 'lotForm',
            height : 200,
            labelWidth : 1,
            autoHeight : true,
            items : [ {
                xtype : 'fieldset',
                title : OpenLayers.i18n('cadastrapp.lots.batiments'),
                autoHeight : true,
                hidden : (batiments.length==1),
                items : [ comboDnubat ]
            }, {
                xtype : 'fieldset',
                title : OpenLayers.i18n('cadastrapp.lots.type'),
                autoHeight : true,
                items : [ {
                    xtype : 'radio',
                    boxLabel : OpenLayers.i18n('cadastrapp.lots.type.pdf'),
                    checked : true,
                    name : 'exportType',
                    id : 'radiolotspdf'
                }, {
                    xtype : 'radio',
                    boxLabel : OpenLayers.i18n('cadastrapp.lots.type.csv'),
                    name : 'exportType',
                    id : 'radiolotscsv'
                }]
            } ]
        } ],

        buttons : [ {
            text : OpenLayers.i18n('cadastrapp.generate'),
            listeners : {
                click : function(b, e) {

                    // pdf by default
                    var url = GEOR.Addons.Cadastre.url.serviceExportLotsAsPDF;
                    // check csv radio button value
                    if (Ext.getCmp('radiolotscsv').getValue()) {
                        url = GEOR.Addons.Cadastre.url.serviceExportLotsAsCSV;
                    }
                    // create Iframe
                    var iframe = document.createElement("iframe");
                    iframe.setAttribute("style", "display:none;");

                    // create form with attributes and request informations
                    var form = document.createElement("form");
                    form.setAttribute("method", "post");
                    form.setAttribute("action", url);

                    // create input with
                    var parcelleField = document.createElement("input");
                    parcelleField.setAttribute("type", "hidden");
                    parcelleField.setAttribute("name", "parcelle");

                    // insert values to input to set POST params
                    parcelleField.value = parcelleId;

                    // insert input to form
                    form.appendChild(parcelleField);

                    // create input with
                    var dnubatField = document.createElement("input");
                    dnubatField.setAttribute("type", "hidden");
                    dnubatField.setAttribute("name", "dnubat");

                    // insert values to input to set POST params
                    dnubatField.value = comboDnubat.getRawValue();
                   
                    // insert input to form
                    form.appendChild(dnubatField);

                    // insert form to iframe to only refresh hidden iframe
                    iframe.appendChild(form);

                    // insert iframe to document body
                    document.body.appendChild(iframe);

                    // submit form to launch request and get CSV
                    form.submit();

                    GEOR.Addons.Cadastre.printLotsWindow.close();
                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.close'),
            listeners : {
                click : function(b, e) {
                    GEOR.Addons.Cadastre.printLotsWindow.close();
                }
            }
        } ]
    });
}
