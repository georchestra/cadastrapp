Ext.namespace("GEOR.Addons.Cadastre");

/**
 * 
 */
GEOR.Addons.Cadastre.onClickLoadPreferencePanel= function(){
    
    // Close previous windows if already opened
    if (GEOR.Addons.Cadastre.preferencesWindow != null) {
        GEOR.Addons.Cadastre.preferencesWindow.close();
    }

    GEOR.Addons.Cadastre.initPreferencesWindow();
    GEOR.Addons.Cadastre.preferencesWindow.show();
}

/**
 * Refresh Cadastrapp vector layers with current style
 */
GEOR.Addons.Cadastre.refreshLayerStyle = function(){
    if(GEOR.Addons.Cadastre.WFSLayer){
        // use localStorage to keep style preferences
        GEOR.ls.set("cadastrapp_style_preference",JSON.stringify(GEOR.Addons.Cadastre.styles));
        GEOR.Addons.Cadastre.WFSLayer.styleMap = GEOR.Addons.Cadastre.createLayerStyle(GEOR.Addons.Cadastre.styles);
        GEOR.Addons.Cadastre.WFSLayer.redraw();
    }
}

/**
 * initPreferencesWindow
 * 
 * @returns Ext.Window 
 */
GEOR.Addons.Cadastre.initPreferencesWindow = function() {
    
    // TabPanel to be include in main windows panel
    var preferenceTabPanel = new Ext.TabPanel({
        activeTab: 0,
        items : [{
            title: OpenLayers.i18n('cadastrapp.preferences.default'),
            xtype : 'form',
            items : [{
                xtype: "colorpickerfield",
                name: "cadaFillListColor",
                id: "cadaFillListColor",
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.fillColor'),
                value: GEOR.Addons.Cadastre.styles.listed.fillColor,
                listeners: {
                    focus: function(field){
                        GEOR.Addons.Cadastre.styles.listed.fillColor = field.getValue();
                        GEOR.Addons.Cadastre.refreshLayerStyle();
                    },
                    scope: this
                }
            },{
                xtype: 'sliderfield',
                name: 'cadaFillListOpacity',
                id: 'cadaFillListOpacity',
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.opacity'),
                value: GEOR.Addons.Cadastre.styles.listed.opacity || 1,
                width: 100,
                minValue: 0,
                maxValue: 1,
                decimalPrecision: 2,
                increment: 0.01,
                tipText: function(thumb){
                    var valOpacity = thumb.value;
                    return String(Math.round(valOpacity*100)) + '%';
                },
                listeners : {
                    valid: function(slider) {
                        GEOR.Addons.Cadastre.styles.listed.opacity = slider.value;
                        GEOR.Addons.Cadastre.refreshLayerStyle();
                    },
                }
            },{
                xtype: "colorpickerfield",
                name: "cadaStroListColor",
                id: "cadaStroListColor",
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.strokeColor'),
                value: GEOR.Addons.Cadastre.styles.listed.strokeColor,
                listeners: {
                    focus: function(field){
                        GEOR.Addons.Cadastre.styles.listed.strokeColor = field.getValue();
                        GEOR.Addons.Cadastre.refreshLayerStyle();
                    },
                    scope: this
                }
            },{
                xtype: "textfield",
                name: "cadaStroListWidth",
                id: "cadaStroListWidth",
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.strokeWidth'),
                value: GEOR.Addons.Cadastre.styles.listed.strokeWidth,
                listeners: {
                    change: function(field, value) {
                        GEOR.Addons.Cadastre.styles.listed.strokeWidth = value;
                        GEOR.Addons.Cadastre.refreshLayerStyle();
                    },
                    scope: this
                }
            }],
            autoHeight: true
            
        },{
            title: OpenLayers.i18n('cadastrapp.preferences.selected'),
            xtype : 'form',
            items : [ {
                xtype: "colorpickerfield",
                name: "cadaFillSelColor",
                id: "cadaFillSelColor",
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.fillColor'),
                value: GEOR.Addons.Cadastre.styles.selected.fillColor,
                listeners: {
                    focus: function(field){
                        GEOR.Addons.Cadastre.styles.selected.fillColor = field.getValue();
                        GEOR.Addons.Cadastre.refreshLayerStyle();
                    },
                    scope: this
                }
            },{
                xtype: 'sliderfield',
                name: 'cadaFillSelOpacity',
                id: 'cadaFillSelOpacity',
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.opacity'),
                value: GEOR.Addons.Cadastre.styles.selected.opacity || 1,
                width: 100,
                minValue: 0,
                maxValue: 1,
                decimalPrecision: 2,
                increment: 0.01,
                tipText: function(thumb){
                    var valOpacity = thumb.value;
                    return String(Math.round(valOpacity*100)) + '%';
                },
                listeners : {
                    valid: function(slider) {
                        GEOR.Addons.Cadastre.styles.selected.opacity = slider.value;
                        GEOR.Addons.Cadastre.refreshLayerStyle();
                    },
                }
            },{
                xtype: "colorpickerfield",
                name: "cadaStroSelColor",
                id: "cadaStroSelColor",
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.strokeColor'),
                value: GEOR.Addons.Cadastre.styles.selected.strokeColor,
                listeners: {
                    focus: function(field){
                        GEOR.Addons.Cadastre.styles.selected.strokeColor = field.getValue();
                        GEOR.Addons.Cadastre.refreshLayerStyle();
                    },
                    scope: this
                }
            },{
                xtype: "textfield",
                name: "cadaStroSelWidth",
                id: "cadaStroSelWidth",
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.strokeWidth'),
                value: GEOR.Addons.Cadastre.styles.selected.strokeWidth,
                listeners: {
                    change: function(field, value) {
                        GEOR.Addons.Cadastre.styles.selected.strokeWidth = value;
                        GEOR.Addons.Cadastre.refreshLayerStyle();
                    },
                    scope: this
                }
            }],
            autoHeight: true            
        }],
    });
        
    // new windows
    GEOR.Addons.Cadastre.preferencesWindow = new Ext.Window({
        title : OpenLayers.i18n('cadastrapp.preferences.title'),
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
                GEOR.Addons.Cadastre.preferencesWindow = null;
            }
        },
        items : preferenceTabPanel,
        buttons : [ {
            text : OpenLayers.i18n('cadastrapp.preferences.default.style'),
            listeners : {
                click : function(b, e) {
                    GEOR.Addons.Cadastre.styles=GEOR.Addons.Cadastre.defaultStyles;
                    GEOR.Addons.Cadastre.refreshLayerStyle();
                    // reset default value to element
                    Ext.getCmp("cadaFillListColor").setValue(GEOR.Addons.Cadastre.styles.listed.fillColor);
                    Ext.getCmp("cadaFillSelColor").setValue(GEOR.Addons.Cadastre.styles.selected.fillColor);
                    Ext.getCmp("cadaFillListOpacity").setValue(GEOR.Addons.Cadastre.styles.listed.opacity);
                    Ext.getCmp("cadaFillSelOpacity").setValue(GEOR.Addons.Cadastre.styles.selected.opacity);
                    Ext.getCmp("cadaStroListColor").setValue(GEOR.Addons.Cadastre.styles.listed.strokeColor);
                    Ext.getCmp("cadaStroSelColor").setValue(GEOR.Addons.Cadastre.styles.selected.strokeColor);
                    Ext.getCmp("cadaStroListWidth").setValue(GEOR.Addons.Cadastre.styles.listed.strokeWidth);
                    Ext.getCmp("cadaStroSelWidth").setValue(GEOR.Addons.Cadastre.styles.selected.strokeWidth);
                }
            }
        },{
            text : OpenLayers.i18n('cadastrapp.close'),
            listeners : {
                click : function(b, e) {
                    GEOR.Addons.Cadastre.preferencesWindow.close();
                }
            }
        }]
    });

}