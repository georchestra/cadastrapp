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
 * initPreferencesWindow
 * 
 * @returns Ext.Window 
 */
GEOR.Addons.Cadastre.initPreferencesWindow = function() {
    
    // TabPanel to be include in main windows panel
    var preferenceTabPanel = new Ext.TabPanel({
        items : [{
            title: OpenLayers.i18n('cadastrapp.preferences.default'),
            xtype : 'form',
            items : [ {
                xtype: 'compositefield',
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.fillColor'),
                items: [{
                    xtype: 'button',
                    text: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
                    menu: {
                        xtype: 'colormenu',
                        value:  GEOR.Addons.Cadastre.styles.listed.fillColor.replace('#', ''),
                        listeners: {
                            select: function(menu, color) {
                                color = "#" + color;
                                menu.ownerCt.ownerCt.btnEl.setStyle("background", color);
                                GEOR.Addons.Cadastre.styles.listed.fillColor = color;
                            },
                            scope: this
                        }
                    },
                    listeners: {
                    }
                }]
            }, {
                xtype: 'compositefield',
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.strokeColor'),
                items: [{
                    xtype: 'button',
                    text: '&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;',
                    menu: {
                        xtype: 'colormenu',
                        value:  GEOR.Addons.Cadastre.styles.listed.strokeColor.replace('#', ''),
                        listeners: {
                            select: function(menu, color) {
                                color = "#" + color;
                                menu.ownerCt.ownerCt.btnEl.setStyle("background", color);
                                GEOR.Addons.Cadastre.styles.listed.strokeColor = color;
                            },
                            scope: this
                        }
                    },
                    listeners: {
                    }
                }]
            },{
                id: 'strokeSlider',
                xtype: 'sliderfield',
                name: 'strokeWidhtSlider',
                fieldLabel: OpenLayers.i18n('cadastrapp.preferences.strokeWidth'),
                value: GEOR.Addons.Cadastre.styles.listed.strokeWidth || 1,
                width: 100,
                minValue: 0,
                maxValue: 36,
                decimalPrecision: 0,
                increment: 1,
                tipText: function(thumb){
                    var valOpacity = thumb.value;
                    return String(valOpacity);
                },
                // Get default opacity value, give it to the feature style properties
                // and draw feature if value change
                listeners : {
                    valid: function(slider) {
                        GEOR.Addons.Cadastre.styles.listed.strokeWidth = slider.value;
                    },
                }
            },{
                id: 'opacitySlider',
                xtype: 'sliderfield',
                name: 'Opacity',
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
                // Get default opacity value, give it to the feature style properties
                // and draw feature if value change
                listeners : {
                    valid: function(slider) {
                        GEOR.Addons.Cadastre.styles.listed.opacity = slider.value;
                    },
                }
            }],
            layout : 'fit',
            autoHeight: true
            
        },{
            title: OpenLayers.i18n('cadastrapp.preferences.selected'),
            html: "Selected"
            
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
        items : preferenceTabPanel
    });

}