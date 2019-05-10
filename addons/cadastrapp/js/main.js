Ext.namespace("GEOR.Addons.Cadastre");

GEOR.Addons.Cadastrapp = Ext.extend(GEOR.Addons.Base, {

    window: null,
    /**
     * Method: clone
     * @param objectToClone - {Object} a simple JavaScript object to break pipeline memory effect between variable 
     * @return {Object} - the clone
     * Method use to Fix georcherstra/cadastrapp/#447
     */
    clone: function (objectToclone) {
        return JSON.parse(JSON.stringify(objectToclone))
    },

    /**
     * Method: init
     * 
     * @param: record - {Ext.data.record} a record with the addon parameters
     */
    init: function(record) {
        // Create global variable to access to clone method 
        GEOR.Addons.Cadastre.clone = this.clone;

        // Get information for addons options
        /**
         * @deprecated since version 1.8 : cadastrappWebappUrl          
         */
        GEOR.Addons.Cadastre.cadastrappWebappUrl = this.options.webapp.url+"services/";
        
        // set normalize url
        GEOR.Addons.Cadastre.url= [];
        
        GEOR.Addons.Cadastre.url.serviceConfig = this.options.webapp.url + this.options.webapp.serviceConfig;
        
        GEOR.Addons.Cadastre.url.serviceProprietaire = this.options.webapp.url + this.options.webapp.serviceProprietaire;
        
        GEOR.Addons.Cadastre.url.serviceProprietairesByInfoParcelles = this.options.webapp.url + this.options.webapp.serviceProprietairesByInfoParcelles;
                      
        GEOR.Addons.Cadastre.url.serviceFIC = this.options.webapp.url + this.options.webapp.serviceFIC;
                
        GEOR.Addons.Cadastre.url.serviceProprietairesByParcelles = this.options.webapp.url + this.options.webapp.serviceProprietairesByParcelles;
        
        GEOR.Addons.Cadastre.url.serviceCoproprietaire = this.options.webapp.url + this.options.webapp.serviceCoproprietaire;
        
        GEOR.Addons.Cadastre.url.serviceBatiments = this.options.webapp.url + this.options.webapp.serviceBatiments;        
        
        GEOR.Addons.Cadastre.url.serviceBatimentsByParcelle = this.options.webapp.url + this.options.webapp.serviceBatimentsByParcelle;
        
        GEOR.Addons.Cadastre.url.serviceParcelle = this.options.webapp.url + this.options.webapp.serviceParcelle;        
        
        GEOR.Addons.Cadastre.url.serviceInfoUniteFonciere = this.options.webapp.url + this.options.webapp.serviceInfoUniteFonciere;
        
        GEOR.Addons.Cadastre.url.serviceHabitationDetail = this.options.webapp.url + this.options.webapp.serviceHabitationDetail;
        
        GEOR.Addons.Cadastre.url.serviceInfoBulle = this.options.webapp.url + this.options.webapp.serviceInfoBulle;
        
        GEOR.Addons.Cadastre.url.serviceCheckRequestLimitation = this.options.webapp.url + this.options.webapp.serviceCheckRequestLimitation;
        
        GEOR.Addons.Cadastre.url.serviceSaveInformationRequest = this.options.webapp.url + this.options.webapp.serviceSaveInformationRequest;
        
        GEOR.Addons.Cadastre.url.servicePrintPdfRequest = this.options.webapp.url + this.options.webapp.servicePrintPdfRequest;
                       
        GEOR.Addons.Cadastre.url.serviceCreateDemandeFromObj = this.options.webapp.url + this.options.webapp.serviceCreateDemandeFromObj;
        
        GEOR.Addons.Cadastre.url.serviceCreateBordereauParcellaire = this.options.webapp.url + this.options.webapp.serviceCreateBordereauParcellaire;
        
        GEOR.Addons.Cadastre.url.serviceExportLotsAsPDF = this.options.webapp.url + this.options.webapp.serviceExportLotsAsPDF;
        
        GEOR.Addons.Cadastre.url.serviceExportLotsAsCSV = this.options.webapp.url + this.options.webapp.serviceExportLotsAsCSV;
        
        GEOR.Addons.Cadastre.url.serviceCreateRelevePropriete = this.options.webapp.url + this.options.webapp.serviceCreateRelevePropriete;
        
        GEOR.Addons.Cadastre.url.serviceCreateReleveProprieteAsCSV = this.options.webapp.url + this.options.webapp.serviceCreateReleveProprieteAsCSV;
        
        GEOR.Addons.Cadastre.url.serviceExportProprietaireByParcelles = this.options.webapp.url + this.options.webapp.serviceExportProprietaireByParcelles;
        
        GEOR.Addons.Cadastre.url.serviceExportCoProprietaireByParcelles = this.options.webapp.url + this.options.webapp.serviceExportCoProprietaireByParcelles;
        
        GEOR.Addons.Cadastre.url.serviceExportParcellesAsCSV = this.options.webapp.url + this.options.webapp.serviceExportParcellesAsCSV;
        
        GEOR.Addons.Cadastre.url.serviceExportAsCSV = this.options.webapp.url + this.options.webapp.serviceExportAsCSV;
        
        GEOR.Addons.Cadastre.url.serviceCoProprietaireList = this.options.webapp.url + this.options.webapp.serviceCoProprietaireList;
        
        GEOR.Addons.Cadastre.url.serviceFromProprietairesFile = this.options.webapp.url + this.options.webapp.serviceFromProprietairesFile;
                
        GEOR.Addons.Cadastre.url.serviceVoie = this.options.webapp.url + this.options.webapp.serviceVoie;
        
        GEOR.Addons.Cadastre.url.serviceFromParcellesFile = this.options.webapp.url + this.options.webapp.serviceFromParcellesFile;
        
        GEOR.Addons.Cadastre.url.serviceCommune = this.options.webapp.url + this.options.webapp.serviceCommune;
        
        GEOR.Addons.Cadastre.url.serviceSection = this.options.webapp.url + this.options.webapp.serviceSection;
        
        GEOR.Addons.Cadastre.url.serviceDnuplaList = this.options.webapp.url + this.options.webapp.serviceDnuplaList;                       

        GEOR.Addons.Cadastre.WFSLayerSetting = this.options.WFSLayerSetting;
        // those are only used here to initialize other components
        var WMSSetting = this.options.WMSLayer;
        GEOR.Addons.Cadastre.UF=[];
        GEOR.Addons.Cadastre.UF.WFSLayerSetting=this.options.UFWFSLayerSetting
        GEOR.Addons.Cadastre.UF.isfoncier = this.options.foncier;

        // Call the webapp configuration services
        Ext.Ajax.request({
            method: 'GET',
            url: GEOR.Addons.Cadastre.url.serviceConfig,
            success: function(response) {
                var configuration = Ext.decode(response.responseText);

                GEOR.Addons.Cadastre.cnil1RoleName = configuration.cnil1RoleName;
                GEOR.Addons.Cadastre.cnil2RoleName = configuration.cnil2RoleName;
                GEOR.Addons.Cadastre.minCharToSearch = configuration.minNbCharForSearch;
                GEOR.Addons.Cadastre.minParacelleIdLength = configuration.minParacelleIdLength;
                GEOR.Addons.Cadastre.maxRequest = configuration.maxRequest;

                GEOR.Addons.Cadastre.WFSLayerSetting.nameFieldIdParcelle = configuration.cadastreLayerIdParcelle;
                GEOR.Addons.Cadastre.WFSLayerSetting.wfsUrl = configuration.cadastreWFSURL;
                GEOR.Addons.Cadastre.WFSLayerSetting.typename = configuration.cadastreWFSLayerName;
                
                if(GEOR.Addons.Cadastre.UF.isfoncier){
                    GEOR.Addons.Cadastre.UF.WFSLayerSetting.wfsUrl = configuration.uFWFSURL;
                    GEOR.Addons.Cadastre.UF.WFSLayerSetting.typename = configuration.uFWFSLayerName;
                    GEOR.Addons.Cadastre.UF.organisme = configuration.organisme;
                    GEOR.Addons.Cadastre.UF.dateValiditeMajic = configuration.dateValiditeMajic;
                    GEOR.Addons.Cadastre.UF.dateValiditeEDIGEO = configuration.dateValiditeEDIGEO;
                }

                WMSSetting.layerNameGeoserver = configuration.cadastreWMSLayerName;
                WMSSetting.url =  configuration.cadastreWMSURL;
                GEOR.Addons.Cadastre.pdfbasemaptitles = configuration.pdfbasemaptitles;
                
                if (!GEOR.Addons.Cadastre.isCNIL1() && !GEOR.Addons.Cadastre.isCNIL2()) {
                   Ext.getCmp('owner-selection-submenu').hide();
                   Ext.getCmp('owner-lookup-submenu').hide();
                   Ext.getCmp('owner-lookup-button').hide();
                }
            },
            failure: function(result) {
                alert(OpenLayers.i18n('cadastrapp.connection.error'));
            }
        });

        GEOR.Addons.Cadastre.menu = new GEOR.Addons.Cadastre.Menu({
            map: this.map,
            helpUrl: this.options.helpUrl,
            popupOptions: {
                unpinnable: false,
                draggable: true
            }
        });

        GEOR.Addons.Cadastre.isWMSLayerAdded = false;

        GEOR.Addons.Cadastre.selection=[];
        GEOR.Addons.Cadastre.selection.state=[];
        GEOR.Addons.Cadastre.selection.state.list = "listed";
        GEOR.Addons.Cadastre.selection.state.selected = "selected";

        GEOR.Addons.Cadastre.relevePropriete=[];
        GEOR.Addons.Cadastre.relevePropriete.maxProprietaire = 25;

        // Init gobal variables
        GEOR.Addons.Cadastre.WFSLayer;
        // Init uf feature list
        GEOR.Addons.Cadastre.UF.features=[];
        GEOR.Addons.Cadastre.result=[];
        GEOR.Addons.Cadastre.result.tabs;

        // result window for plots and owners
        GEOR.Addons.Cadastre.result.plot=[];
        GEOR.Addons.Cadastre.result.plot.window;
        GEOR.Addons.Cadastre.result.owner=[];
        GEOR.Addons.Cadastre.result.owner.window;

        GEOR.Addons.Cadastre.defaultStyles=GEOR.Addons.Cadastre.clone(this.options.style);        
        
        // Check if user has style preferences and apply them
        if(GEOR.ls.get("cadastrapp_style_preference") != null){
            try {
                GEOR.Addons.Cadastre.styles=JSON.parse(GEOR.ls.get("cadastrapp_style_preference"));
            } catch(e) {
                // If an error occured when parsing data
                GEOR.Addons.Cadastre.styles=GEOR.Addons.Cadastre.clone(GEOR.Addons.Cadastre.defaultStyles);
                
            }
        }
        //if nothing stored in localstorage
        if(typeof GEOR.Addons.Cadastre.styles === "undefined"){
            GEOR.Addons.Cadastre.styles=GEOR.Addons.Cadastre.clone(GEOR.Addons.Cadastre.defaultStyles);
        }
        // Create vectore Layer
        GEOR.Addons.Cadastre.createLayer(GEOR.Addons.Cadastre.styles);
        
        GEOR.wmc.events.on("aftercontextrestore", GEOR.Addons.Cadastre.restoreLayersOnClear, this);
        GEOR.managelayers.events.on("aftercontextcleared", GEOR.Addons.Cadastre.restoreLayersOnClear, this);

        this.window = new Ext.Window({
            title: OpenLayers.i18n('cadastrapp.cadastre_tools'),
            closable: true,
            closeAction: "hide",
            resizable: true,
            border: false,
            constrainHeader: true,
            boxMaxWidth: 650,
            cls: 'cadastrapp',
            collapsible: true,
            items: [ {
                xtype: 'toolbar',
                border: false,
                items: GEOR.Addons.Cadastre.menu.items
            } ],
            listeners: {
                "show": function() {
                    this.item.setChecked(true, true);
                    GEOR.Addons.Cadastre.visible = true;
                    GEOR.Addons.Cadastre.addWMSLayer(WMSSetting);
                    this.map.addLayer(GEOR.Addons.Cadastre.WFSLayer);
                    GEOR.Addons.Cadastre.addPopupOnhover(this.options.popup);
                    var maxX =  window.innerWidth;
                    this.window.setPagePosition(maxX * 0.3 ,0);
                },
                "hide": function() {
                    this.item.setChecked(false, true);
                    GEOR.Addons.Cadastre.visible = false;
                    // deactivate all controls create by cadastrapp addons
                    // All controls used by cadastrapp are store in the field GEOR.Addons.Cadastre.menu.cadastrappControls
                    Ext.each(GEOR.Addons.Cadastre.menu.cadastrappControls, function(control, index) {
                        control.deactivate();
                    });

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
                    }
                    // Remove WFS layer
                    this.map.removeLayer(GEOR.Addons.Cadastre.WFSLayer);

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

                    if (GEOR.Addons.Cadastre.proprietaireWindow){
                        GEOR.Addons.Cadastre.proprietaireWindow.close();
                        GEOR.Addons.Cadastre.proprietaireWindow = null;
                    }

                    if (GEOR.Addons.Cadastre.rechercheParcelleWindow){
                        GEOR.Addons.Cadastre.rechercheParcelleWindow.close();
                        GEOR.Addons.Cadastre.rechercheParcelleWindow = null;
                    }

                    if(GEOR.Addons.Cadastre.printBordereauParcellaireWindow){
                        GEOR.Addons.Cadastre.printBordereauParcellaireWindow.close();
                        GEOR.Addons.Cadastre.printBordereauParcellaireWindow=null;
                    }

                    if(GEOR.Addons.Cadastre.request.informationsWindow){
                        GEOR.Addons.Cadastre.request.informationsWindow.close();
                        GEOR.Addons.Cadastre.request.informationsWindow=null;
                    }

                    if(GEOR.Addons.Cadastre.coProprieteWindow){
                        GEOR.Addons.Cadastre.coProprieteWindow.close();
                        GEOR.Addons.Cadastre.coProprieteWindow=null;
                    }
                    
                    if(GEOR.Addons.Cadastre.printReleveProprieteWindow){
                    	GEOR.Addons.Cadastre.printReleveProprieteWindow.close();
                    	GEOR.Addons.Cadastre.printReleveProprieteWindow=null;
                    }
                    
                    if(GEOR.Addons.Cadastre.printLotsWindow){
                    	GEOR.Addons.Cadastre.printLotsWindow.close();
                    	GEOR.Addons.Cadastre.printLotsWindow=null;
                    }
                    
                    if(GEOR.Addons.Cadastre.preferencesWindow){
                        GEOR.Addons.Cadastre.preferencesWindow.close();
                        GEOR.Addons.Cadastre.preferencesWindow=null;
                    }
                },
                scope: this
            }
        });

        if (this.target) {
            // create a button to be inserted in toolbar:
            this.components = this.target.insertButton(this.position, {
                xtype: 'button',
                tooltip: this.getTooltip(record),
                iconCls: "addon-cadastrapp",
                handler: this._onCheckchange,
                scope: this,
                listeners: {
                    "afterrender": function() {
                        if (this.options.openToolbarOnLoad) {
                            this._onCheckchange(this.item, true);
                        }
                    },
                    delay: 500,
                    scope: this
                }
            });
            this.target.doLayout();
            // create a menu item for the "tools" menu:
            this.item = new Ext.menu.CheckItem({
                text: this.getText(record),
                qtip: this.getQtip(record),
                iconCls: "addon-cadastrapp",
                checked: false,
                listeners: {
                    "checkchange": this._onCheckchange,
                    scope: this
                }
            });
        }
    },


    /**
     * Method: _onCheckchange Callback on checkbox state changed
     * when called from toolbar button, checked is an mouseevent object
     * when called from tools menu checkitem or manually from openToolbarOnLoad, checked is a boolean
     */
    _onCheckchange: function(item, checked) {
        if (checked && !this.window.isVisible()) {
            this.window.show();
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
        
        if( GEOR.Addons.Cadastre.printReleveProprieteWindow){
            GEOR.Addons.Cadastre.printReleveProprieteWindow.close();
            GEOR.Addons.Cadastre.printReleveProprieteWindow=null;
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
        GEOR.Addons.Cadastre.clone=null;
        
        if(GEOR.Addons.Cadastre.WFSLayer){
            GeoExt.MapPanel.guess().map.removeControl(GEOR.Addons.Cadastre.WFSLayer.selectControl);
            GEOR.Addons.Cadastre.WFSLayer.destroy();
            GEOR.Addons.Cadastre.WFSLayer=null;
        }
       
        GEOR.Addons.Cadastre.WFSLayerSetting=null;

        GEOR.Addons.Cadastre.pdfbasemaptitles=null;
        GEOR.Addons.Cadastre=null;

        this.map = null;

        GEOR.Addons.Base.prototype.destroy.call(this);
    }

});
