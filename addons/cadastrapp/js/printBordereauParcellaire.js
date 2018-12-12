Ext.namespace("GEOR.Addons.Cadastre");


/**
 * 
 * @param parcelleId
 * 
 * @returns Ext.Window 
 */
GEOR.Addons.Cadastre.onClickPrintBordereauParcellaireWindow = function(parcelleId) {
    
    if (GEOR.Addons.Cadastre.printBordereauParcellaireWindow != null) {
        GEOR.Addons.Cadastre.printBordereauParcellaireWindow.close();
    }
    
    // Test if user have CNIL level 1 or 2
    // ask if user want with or without personnal data
    // If more than one basemap available propose them
    if (GEOR.Addons.Cadastre.isCNIL1() || GEOR.Addons.Cadastre.isCNIL2() || GEOR.Addons.Cadastre.pdfbasemaptitles.length>1){
        GEOR.Addons.Cadastre.initPrintBordereauParcellaireWindow(parcelleId);
        GEOR.Addons.Cadastre.printBordereauParcellaireWindow.show();
    }else{
        // PARAMS
        var params = {
            parcelle: parcelleId, 
            personaldata: 0,
            fillcolor: GEOR.Addons.Cadastre.styles.selected.fillColor,
            opacity: GEOR.Addons.Cadastre.styles.selected.opacity,
            strokecolor: GEOR.Addons.Cadastre.styles.selected.strokeColor,
            strokewidth: GEOR.Addons.Cadastre.styles.selected.strokeWidth,
        } 
        var url = GEOR.Addons.Cadastre.cadastrappWebappUrl + 'createBordereauParcellaire?' + Ext.urlEncode(params);

        Ext.DomHelper.useDom = true;
        
        // Directly download file, without and call service without ogcproxy
        Ext.DomHelper.append(document.body, {
            tag: 'iframe',
            id: 'downloadIframe',
            frameBorder: 0,
            width: 0,
            height: 0,
            css: 'display:none;visibility:hidden;height:0px;',
            src: url
        });
        
        // windows might not have been open
        if (GEOR.Addons.Cadastre.printBordereauParcellaireWindow != null) {
            GEOR.Addons.Cadastre.printBordereauParcellaireWindow.close();
        }
    }
}

/**
 * init new windows
 * 
 * @param parcelleId
 */
GEOR.Addons.Cadastre.initPrintBordereauParcellaireWindow = function(parcelleId) {
    
    var itemsInPanel = [{
        xtype: 'hidden',
        name: 'parcelle',
        value: parcelleId
    }];
    
    // Show only personaldata information if user have the goog Cnil level
    if (GEOR.Addons.Cadastre.isCNIL1() || GEOR.Addons.Cadastre.isCNIL2()){
        var fiedSetPersonnalData = new Ext.form.FieldSet({
            title: OpenLayers.i18n('cadastrapp.bordereauparcellaire.data'),
            autoHeight: true,
            hide: true,
            items: [{
                xtype: 'radio',
                boxLabel: OpenLayers.i18n('cadastrapp.bordereauparcellaire.data.without'),
                checked: true,
                name: 'personaldata',
                inputValue: 0
    
            }, {
                xtype: 'radio',
                boxLabel: OpenLayers.i18n('cadastrapp.bordereauparcellaire.data.with'),
                name: 'personaldata',
                inputValue: 1
            } ]
        });

        itemsInPanel.push(fiedSetPersonnalData);
    }
    
    // Show only base map combobox if there is more than one option
    if (GEOR.Addons.Cadastre.pdfbasemaptitles.length>1){
            
        var storeBaseMap = new Ext.data.JsonStore({
            storeId: 'cadstorebasemap',
            fields: [{
                // keep only index from key information
                // Key is formatted like pdf.baseMap.0.title
                // We want only index
                name: 'index',
                convert: function(v, rec) {
                    // Split in array
                    var data  = rec.key.split(".");   
                    // remove element title
                    data.pop();
                    // get index information
                    return data.pop();
                }}, 'value'],
            sortInfo:{
                field: 'value', 
                direction: "ASC"
                    }            
        });
             
        var comboBaseMap = new Ext.form.ComboBox({
            id: 'cadbasemapindex',
            hiddenName: 'basemapindex',
            width: 230,
            displayField: 'value',
            valueField: 'index',
            store: storeBaseMap,
            mode: 'local',
            allowBlank: false,
            selectOnFocus: true,
            typeAhead: true,
            forceSelection: true,
            triggerAction: 'all',
            editable: false
       	});
        
        // Load first value
        comboBaseMap.store.on('load', function(ds,records,o){
            comboBaseMap.setValue(0);
        });
        
        storeBaseMap.loadData(GEOR.Addons.Cadastre.pdfbasemaptitles);
        
        var fiedSetBaseMap = new Ext.form.FieldSet({
            title: OpenLayers.i18n('cadastrapp.bordereauparcellaire.basemap'),
            autoHeight: true,
            hide: true,
            items: [comboBaseMap]
        });
    
        itemsInPanel.push(fiedSetBaseMap);
    }
    

    // fenêtre principale
    GEOR.Addons.Cadastre.printBordereauParcellaireWindow = new Ext.Window({
        title: OpenLayers.i18n('cadastrapp.bordereauparcellaire.title') + ' - ' + parcelleId,
        frame: true,
        autoScroll: true,
        minimizable: false,
        closable: true,
        resizable: false,
        draggable: true,
        constrainHeader: true,
        border: false,
        width: 300,
        defaults: {
            autoHeight: true,
            bodyStyle: 'padding:10px',
            flex: 1
        },
        listeners: {
            close: function(window) {
                GEOR.Addons.Cadastre.printBordereauParcellaireWindow = null;
            }
        },
        items: [ {
            xtype: 'form',
            id: 'bordereauForm',
            height: 200,
            labelWidth: 1,
            autoHeight: true,
            items: itemsInPanel          
        } ],

        buttons: [ {
            text: OpenLayers.i18n('cadastrapp.generate'),
            listeners: {
                click: function(b, e) {

                    // PARAMS
                    var params = GEOR.Addons.Cadastre.printBordereauParcellaireWindow.items.items[0].getForm().getValues();
                    if(Ext.getCmp("cadbasemapindex")){
                        params.basemapindex = Ext.getCmp("cadbasemapindex").getValue() == '' ? "0" : Ext.getCmp("cadbasemapindex").getValue();
                    }
                    // Add style information
                    // remove # to avoid URL problems on server side (XSL template doesnot manage url-encode)
                    params.fillcolor=GEOR.Addons.Cadastre.styles.selected.fillColor.substring(1);;
                    params.opacity=GEOR.Addons.Cadastre.styles.selected.opacity;
                    params.strokecolor=GEOR.Addons.Cadastre.styles.selected.strokeColor.substring(1);;
                    params.strokewidth=GEOR.Addons.Cadastre.styles.selected.strokeWidth;
                    
                    var url = GEOR.Addons.Cadastre.cadastrappWebappUrl + 'createBordereauParcellaire?' + Ext.urlEncode(params);

                    // Needed for IE
                    //Ext.DomHelper.useDom = true;

                    // Directly download file, without and call service without ogcproxy
                    Ext.DomHelper.append(document.body, {
                        tag: 'iframe',
                        id: 'downloadIframe',
                        frameBorder: 0,
                        width: 0,
                        height: 0,
                        css: 'display:none;visibility:hidden;height:0px;',
                        src: url
                    });
                    
                    //TODO add waiting panel
                    // Cannot not manage to stop waiting panel in every case, Ext.js does not see end of Iframe loading in specific case
                    GEOR.Addons.Cadastre.printBordereauParcellaireWindow.close();
                }
            }
        }, {
            text : OpenLayers.i18n('cadastrapp.close'),
            listeners : {
                click : function(b, e) {
                    GEOR.Addons.Cadastre.printBordereauParcellaireWindow.close();
                }
            }
        } ]
    });
}