Ext.namespace("GEOR.Addons.Cadastre");

GEOR.Addons.Cadastrapp = Ext.extend(GEOR.Addons.Base, {

    window: null,

    /**
     * Method: init
     * 
     * @param: record - {Ext.data.record} a record with the addon parameters
     */
    init: function(record) {
        
        // Create closure to be able to create windows
        var initThis = this;
        
        // Get information for addons options
        GEOR.Addons.Cadastre.cadastrappWebappUrl = record.data.options.webapp.url+"services/";
       
        GEOR.Addons.Cadastre.WFSLayerSetting = record.data.options.WFSLayerSetting; 
        var WMSSetting = record.data.options.WMSLayer;
        var popupSetting = record.data.options.popup;
        
        // Call the webapp configuration services
        Ext.Ajax.request({
            method: 'GET',
            url: GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getConfiguration',
            success: function(response) {
                var configuration = Ext.decode(response.responseText);
                
                GEOR.Addons.Cadastre.cnil1RoleName = configuration.cnil1RoleName;
                GEOR.Addons.Cadastre.cnil2RoleName = configuration.cnil2RoleName;
                GEOR.Addons.Cadastre.minCharToSearch = configuration.minNbCharForSearch;
                GEOR.Addons.Cadastre.maxRequest = configuration.maxRequest;
                
                GEOR.Addons.Cadastre.WFSLayerSetting.nameFieldIdParcelle = configuration.cadastreLayerIdParcelle;
                GEOR.Addons.Cadastre.WFSLayerSetting.wfsUrl = configuration.cadastreWFSURL;
                GEOR.Addons.Cadastre.WFSLayerSetting.typename = configuration.cadastreWFSLayerName;
                
                WMSSetting.layerNameGeoserver = configuration.cadastreWMSLayerName;
                WMSSetting.url =  configuration.cadastreWMSURL;
                          
                GEOR.Addons.Cadastre.menu = new GEOR.Addons.Cadastre.Menu({
                    map: initThis.map,
                    popupOptions: {
                        unpinnable: false,
                        draggable: true
                    }
                });
        
                GEOR.Addons.Cadastre.isWMSLayerAdded = false;
               
                GEOR.Addons.Cadastre.selection=[];
                GEOR.Addons.Cadastre.selection.state=[];
                GEOR.Addons.Cadastre.selection.state.list = record.data.options.selectedStyle.colorState1;
                GEOR.Addons.Cadastre.selection.state.selected = record.data.options.selectedStyle.colorState2;
                GEOR.Addons.Cadastre.selection.state.details = record.data.options.selectedStyle.colorState3;
                
                GEOR.Addons.Cadastre.relevePropriete=[];
                GEOR.Addons.Cadastre.relevePropriete.maxProprietaire = 25;
                
                // Init gobal variables   
                GEOR.Addons.Cadastre.WFSLayer;
                GEOR.Addons.Cadastre.result=[];
                GEOR.Addons.Cadastre.result.tabs;
                
                // result window for plots and owners
                GEOR.Addons.Cadastre.result.plot=[];
                GEOR.Addons.Cadastre.result.plot.window;
                GEOR.Addons.Cadastre.result.owner=[];
                GEOR.Addons.Cadastre.result.owner.window;
                
                GEOR.Addons.Cadastre.createSelectionControl(record.data.options.defautStyleParcelle , record.data.options.selectedStyle);
                                
                initThis.window = new Ext.Window({
                    title: OpenLayers.i18n('cadastrapp.cadastre_tools'),
                    closable: true,
                    closeAction: "hide",
                    resizable: true,
                    border: false,
                    constrainHeader: true,
                    boxMaxWidth: 650,
                    cls: 'cadastrapp',
                    items: [ {
                        xtype: 'toolbar',
                        border: false,
                        items: GEOR.Addons.Cadastre.menu.items
                    } ],
                    listeners: {
                    	"show": function() {
                    		GEOR.Addons.Cadastre.addWMSLayer(WMSSetting);
                    		GEOR.Addons.Cadastre.addPopupOnhover(popupSetting);                    		
                    	},
                        "hide": function() {
                          
                        	// deactivate all controls
                        	 Ext.each(GEOR.Addons.Cadastre.menu.cadastrappControls, function(control, index) {
                                 control.deactivate();    
                             });
                        	  
                        	 // deactivate popup hover control
                             var mapControls = layer.map.controls[21];
                        	 mapControls.destroy();
                            
                        	 
                        	 
                            // Remove WMS Layer
                            if (GEOR.Addons.Cadastre.isWMSLayerAdded == true && GEOR.Addons.Cadastre.WMSLayer != null) {
                            	
                            	 var layersWMS = [];
                                 layersWMS.push(GEOR.Addons.Cadastre.WMSLayer);
                                 var reader = new GeoExt.data.LayerReader();
                                 var layerData = reader.readRecords(layersWMS);

                                 GeoExt.MapPanel.guess().layers.remove(layerData.records[0]);

                               // this.map.removeLayer(GEOR.Addons.Cadastre.WMSLayer);
                                GEOR.Addons.Cadastre.WMSLayer.destroy();
                                GEOR.Addons.Cadastre.WMSLayer = null;
                                GEOR.Addons.Cadastre.addPopupOnhover = null ; //
                            }
                                                     
                            // Remove all windows
                            if(GEOR.Addons.Cadastre.popup){
                                GEOR.Addons.Cadastre.popup.close();
                                GEOR.Addons.Cadastre.popup=null
                            }
                            if (GEOR.Addons.Cadastre.result.plot.window){
                                GEOR.Addons.Cadastre.result.plot.window.close();
                                GEOR.Addons.Cadastre.result.plot.window=null;
                            }
                            
                            if (GEOR.Addons.Cadastre.result.owner.window){
                                GEOR.Addons.Cadastre.result.owner.window.close();
                                GEOR.Addons.Cadastre.result.owner.window=null;
                            }
                            
                            
                            if ( GEOR.Addons.Cadastre.proprietaireWindow){
                                GEOR.Addons.Cadastre.proprietaireWindow.close();
                                GEOR.Addons.Cadastre.proprietaireWindow = null;
                            }
                            
                            if (GEOR.Addons.Cadastre.rechercheParcelleWindow){
                                GEOR.Addons.Cadastre.rechercheParcelleWindow.close();
                                GEOR.Addons.Cadastre.rechercheParcelleWindow = null;
                            }
                        
                            
                            if( GEOR.Addons.Cadastre.printBordereauParcellaireWindow){
                                GEOR.Addons.Cadastre.printBordereauParcellaireWindow.close();
                                GEOR.Addons.Cadastre.printBordereauParcellaireWindow=null;
                            }       
                            
                            if( GEOR.Addons.Cadastre.request.informationsWindow){
                                GEOR.Addons.Cadastre.request.informationsWindow.close();
                                GEOR.Addons.Cadastre.request.informationsWindow=null;
                            }
                            
                            if(GEOR.Addons.Cadastre.coProprieteWindow){
                                GEOR.Addons.Cadastre.coProprieteWindow.close();
                                GEOR.Addons.Cadastre.coProprieteWindow=null;
                            }
                            
                        },
                        scope: initThis
                    }
                });
        
                if (initThis.target) {
                    // create a button to be inserted in toolbar:
                    initThis.components = initThis.target.insertButton(initThis.position, {
                        xtype: 'button',
                        tooltip: initThis.getTooltip(record),
                        iconCls: "addon-cadastrapp",
                        handler: initThis._onCheckchange,
                        scope: initThis
                    });
                    initThis.target.doLayout();
                    // create a menu item for the "tools" menu:
                    initThis.item = new Ext.menu.CheckItem({
                        text: initThis.getText(record),
                        qtip: initThis.getQtip(record),
                        iconCls: "addon-cadastrapp",
                        checked: false,
                        listeners: {
                            "checkchange": initThis._onCheckchange,
                            scope: initThis
                        }
                    });
                }
            },
            failure: function(result) {
                alert(OpenLayers.i18n('cadastrapp.connection.error'));
            }
        });
    },
    

    /**
     * Method: _onCheckchange Callback on checkbox state changed
     */
    _onCheckchange: function(item, checked) {
    	if (checked && !this.window.isVisible()) {
    		this.window.show();
    		this.window.alignTo(Ext.get(this.map.div), "t-t", [ 0, 5 ], true);
    	} else {
    		this.window.hide();
    	}
    },

    /**
     * Method: destroy Called by GEOR_tools when deselecting this addon
     */
    destroy: function() {
        
        // Remove menu
        // Add test in case add-ons did not load properly
        if(this.windows){
            this.window.hide();
        }
        
        // Remove all windows
        if(GEOR.Addons.Cadastre.popup){
            GEOR.Addons.Cadastre.popup.close();
            GEOR.Addons.Cadastre.popup=null
        }
        if (GEOR.Addons.Cadastre.result.plot.window){
            GEOR.Addons.Cadastre.result.plot.window.close();
            GEOR.Addons.Cadastre.result.plot.window=null;
        }
        
        if (GEOR.Addons.Cadastre.result.owner.window){
            GEOR.Addons.Cadastre.result.owner.window.close();
            GEOR.Addons.Cadastre.result.owner.window=null;
        }
        
        
        if ( GEOR.Addons.Cadastre.proprietaireWindow){
            GEOR.Addons.Cadastre.proprietaireWindow.close();
            GEOR.Addons.Cadastre.proprietaireWindow = null;
        }
        
        if (GEOR.Addons.Cadastre.rechercheParcelleWindow){
            GEOR.Addons.Cadastre.rechercheParcelleWindow.close();
            GEOR.Addons.Cadastre.rechercheParcelleWindow = null;
        }
    
        
        if( GEOR.Addons.Cadastre.printBordereauParcellaireWindow){
            GEOR.Addons.Cadastre.printBordereauParcellaireWindow.close();
            GEOR.Addons.Cadastre.printBordereauParcellaireWindow=null;
        }       
        
        if( GEOR.Addons.Cadastre.request.informationsWindow){
            GEOR.Addons.Cadastre.request.informationsWindow.close();
            GEOR.Addons.Cadastre.request.informationsWindow=null;
        }
        
        if(GEOR.Addons.Cadastre.coProprieteWindow){
            GEOR.Addons.Cadastre.coProprieteWindow.close();
            GEOR.Addons.Cadastre.coProprieteWindow=null;
        }
        
     // Add test in case add-ons did not load properly
        if(GEOR.Addons.Cadastre.menu){
            GEOR.Addons.Cadastre.menu.destroy();
        }
        
        GEOR.Addons.Cadastre.selection=null;
        GEOR.Addons.Cadastre.relevePropriete=null;
        GEOR.Addons.Cadastre.result=null;
        GEOR.Addons.Cadastre.cnil1RoleName=null;
        GEOR.Addons.Cadastre.cnil2RoleName=null;
        GEOR.Addons.Cadastre.minCharToSearch=null;        
        GEOR.Addons.Cadastre.WFSLayerSetting=null;
               
        this.map = null;

        GEOR.Addons.Base.prototype.destroy.call(this);
    }
    
});
