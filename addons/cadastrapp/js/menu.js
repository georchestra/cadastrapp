Ext.namespace("GEOR.Addons.Cadastre");

/**
 * 
 * Create a menu main controler for cadastrapp
 */
GEOR.Addons.Cadastre.Menu = Ext.extend(Ext.util.Observable, {

    map : null,

    cadastrappControls : null,

    items : null,

    /**
     * api: config[layerOptions] ``Object`` Options to be passed to the
     * OpenLayers.Layer.Vector constructor.
     */
    layerOptions : {},

    /**
     * api: property[toggleGroup] ``String`` The name of the group used for the
     * buttons created. If none is provided, it's set to this.map.id.
     */
    toggleGroup : null,

    /**
     * private: method[constructor] Private constructor override.
     */
    constructor : function(config) {
        Ext.apply(this, config);

        this.cadastrappControls = [];
        this.items = [];

        this.initMap();
        // if set, automatically creates a "cadastrapp" layer
        var style = this.style || OpenLayers.Util.applyDefaults(this.defaultStyle, OpenLayers.Feature.Vector.style["default"]);

        var styleMap = new OpenLayers.StyleMap({
            'default' : style,
            'vertices' : new OpenLayers.Style({
                pointRadius : 5,
                graphicName : "square",
                fillColor : "white",
                fillOpacity : 0.6,
                strokeWidth : 1,
                strokeOpacity : 1,
                strokeColor : "#333333"
            })
        });
        var layerOptions = OpenLayers.Util.applyDefaults(this.layerOptions, {
            styleMap : styleMap,
            displayInLayerSwitcher : false
        });
        var cadastreLayer = new OpenLayers.Layer.Vector("__georchestra_cadastrapps", layerOptions);
        this.map.addLayer(cadastreLayer);

        this.initZoomControls();
        this.items.push('-');
        this.initSelectionControls(cadastreLayer);
        this.items.push('-');
        
        //init UF button only if option foncier is true in manifest
        if (GEOR.Addons.Cadastre.UF.isfoncier){
            this.initUFControls(cadastreLayer);
            this.items.push('-');
        }
        this.initRechercheControls();
        this.items.push('-');
        this.initDemandeControl();
        this.items.push('-');
        this.initHelpControl();

        GEOR.Addons.Cadastre.Menu.superclass.constructor.apply(this, arguments);
    },

    /**
     * private: method[initMap] Convenience method to make sure that the map
     * object is correctly set.
     */
    initMap : function() {
        if (this.map instanceof GeoExt.MapPanel) {
            this.map = this.map.map;
        }

        if (!this.map) {
            this.map = GeoExt.MapPanel.guess().map;
        }

        // if no toggleGroup was defined, set to this.map.id
        if (!this.toggleGroup) {
            this.toggleGroup = this.map.id;
        }

    },

    /**
     * private: method[initZoomControls]
     * 
     * 
     * init action on zoom button
     */
    initZoomControls : function() {
        var action, iconCls, actionOptions, tooltip, handler;

        handler = OpenLayers.Handler.Path;
        iconCls = "gx-featureediting-zoom";
        tooltip = OpenLayers.i18n("Zoom to results extent");
        actionOptions = {
            allowDepress : true,
            pressed : false,
            tooltip : tooltip,
            iconCls : iconCls,
            text : OpenLayers.i18n("cadastrapp.zoom"),
            iconAlign : 'top',
            // check item options
            checked : false,
            handler : function() {
                // Zoom on all element from all parcelle from each tab of
                // resultParcellet tabpanel
                if (GEOR.Addons.Cadastre.result.tabs && GEOR.Addons.Cadastre.result.tabs.items) {
                    var allfeatures = [];
                    Ext.each(GEOR.Addons.Cadastre.result.tabs.items.items, function(tab, currentIndex) {
                        if (tab.featuresList) {
                            Ext.each(tab.featuresList, function(feature) {
                                allfeatures.push(feature);
                            })
                        }
                    })
                    GEOR.Addons.Cadastre.zoomOnFeatures(allfeatures);
                }
            }
        };

        action = new Ext.Button(actionOptions);
        this.items.push(action);

    },
    /**
     * private: method[initSelectionControls]
     * 
     * @param layer:
     *            ``OpenLayers.Layer.Vector`` init action on selection button
     */
    initSelectionControls : function(layer) {
        var control, handler, geometryTypes, geometryType, options, action, iconCls, actionOptions, tooltip;

        geometryTypes = [ "Point", "LineString", "Polygon" ];

        for (var i = 0; i < geometryTypes.length; i++) {
            options = {
                handlerOptions : {
                    stopDown : true,
                    stopUp : true
                }
            };
            geometryType = geometryTypes[i];

            var isButtonPressed = false;

            switch (geometryType) {
                case "LineString":
                    handler = OpenLayers.Handler.Path;
                    iconCls = "gx-featureediting-cadastrapp-line";
                    tooltip = OpenLayers.i18n("cadastrapp.create_line");
                    break;
                case "Point":
                    handler = OpenLayers.Handler.Point;
                    iconCls = "gx-featureediting-cadastrapp-point";
                    tooltip = OpenLayers.i18n("cadastrapp.create_point");
                    break;
                case "Polygon":
                    handler = OpenLayers.Handler.Polygon;
                    iconCls = "gx-featureediting-cadastrapp-polygon";
                    tooltip = OpenLayers.i18n("cadastrapp.create_polygon");
                    break;
            }

            control = new OpenLayers.Control.DrawFeature(layer, handler, options);

            this.cadastrappControls.push(control);

            control.events.on({
                "featureadded" : this.onFeatureAdded,
                scope : this
            });

            actionOptions = {
                control : control,
                map : this.map,
                // button options
                toggleGroup : 'map',
                allowDepress : true,
                pressed : isButtonPressed,
                tooltip : tooltip,
                iconCls : iconCls,
                text : OpenLayers.i18n("cadastrapp." + geometryType.toLowerCase()),
                iconAlign : 'top',
                // check item options
                group : this.toggleGroup,
                checked : false
            };

            action = new GeoExt.Action(actionOptions);

            this.items.push(action);
        }

    },

    /**
     * private: method[initUFControls] 
     * 
     * Init action for UF controls
     */
    initUFControls : function(layer) {

        var control, handler,  options, action,actionOptions;

        options = {
                handlerOptions : {
                stopDown : true,
                stopUp : true
                }
        };

        handler = OpenLayers.Handler.Point;
        control = new OpenLayers.Control.DrawFeature(layer, handler, options);
        this.cadastrappControls.push(control);
        control.events.on({
            "featureadded" : this.onUFClick,
            scope : this
        });

        actionOptions = {
                control : control,
                map : this.map,
                // button options
                toggleGroup : 'map',
                allowDepress : true,
                tooltip : OpenLayers.i18n("cadastrapp.menu.tooltips.foncier"),
                iconCls : "gx-featureediting-cadastrapp-parcelle",
                text : OpenLayers.i18n("cadastrapp.foncier"),
                iconAlign : 'top',
                toggleHandler : function(btn, pressed) {
                    if (pressed) {
                        
                        // Close all windows and clean existing feature
                        GEOR.Addons.Cadastre.WFSLayer.removeAllFeatures();
                        GEOR.Addons.Cadastre.UF.features=[];
                        if(GEOR.Addons.Cadastre.result.plot.window){
                            GEOR.Addons.Cadastre.result.plot.window.close();
                        }
                        if(GEOR.Addons.Cadastre.rechercheParcelleWindow){
                            GEOR.Addons.Cadastre.rechercheParcelleWindow.close();
                        }
                        if(GEOR.Addons.Cadastre.proprietaireWindow){
                            GEOR.Addons.Cadastre.proprietaireWindow.close();
                        }
                        if(GEOR.Addons.Cadastre.coProprieteWindow){
                            GEOR.Addons.Cadastre.coProprieteWindow.close();
                        }
                    }
                    else{
                        // Clean existing feature
                        GEOR.Addons.Cadastre.WFSLayer.removeAllFeatures();
                        GEOR.Addons.Cadastre.UF.features=[];
                    }
                }
        };
        action = new GeoExt.Action(actionOptions);
        this.items.push(action);
    },
    /**
     * private: method[initRechercheControls] 
     *  Init action on search parcelle menu and button
     */
    initRechercheControls : function() {
        // menu : recherche parcelle
        var configRechercheParcelle = {
            tooltip : OpenLayers.i18n("cadastrapp.parcelle"),
            iconCls : "gx-featureediting-cadastrapp-parcelle",
            iconAlign : 'top',
            text : OpenLayers.i18n("cadastrapp.parcelle"),
            handler : function() {
                GEOR.Addons.Cadastre.onClickRechercheParcelle(0)
            }
        };
        this.items.push(new Ext.Button(configRechercheParcelle));

        // menu : recherche propriétaire
            var configRechercheProprietaire = {
                id : 'owner-lookup-button',
                tooltip : OpenLayers.i18n("cadastrapp.proprietaire"),
                iconCls : "gx-featureediting-cadastrapp-parcelle",
                iconAlign : 'top',
                text : OpenLayers.i18n("cadastrapp.proprietaire"),
                handler : function() {
                    GEOR.Addons.Cadastre.onClickRechercheProprietaire(0)
                }
            };
            this.items.push(new Ext.Button(configRechercheProprietaire));

        // menu : recherche avancée
        var scrollMenu = new Ext.menu.Menu();
        var configRechercheAvancee = {
            tooltip : OpenLayers.i18n("cadastrapp.recherches"),
            iconCls : "gx-featureediting-cadastrapp-line",
            iconAlign : 'top',
            text : OpenLayers.i18n("cadastrapp.recherches"),
            menu : scrollMenu
        };
        this.items.push(new Ext.Button(configRechercheAvancee));

        // sous-menu : recherche parcelle
        var scrollMenuRechercheParcelle = new Ext.menu.Menu();
        var buttonRechercheParcelle = scrollMenu.add({
            tooltip : OpenLayers.i18n("cadastrapp.parcelle"),
            text : OpenLayers.i18n("cadastrapp.parcelle"),
            menu : scrollMenuRechercheParcelle
        });
        // sous-sous-menu : recherche parcelle - par référence
        var buttonRechercheParcelleIdentifiant = scrollMenuRechercheParcelle.add({
            tooltip : OpenLayers.i18n("cadastrapp.menu.parcelle.refer"),
            text : OpenLayers.i18n("cadastrapp.menu.parcelle.refer")
        });
        buttonRechercheParcelleIdentifiant.on('click', function() {
            GEOR.Addons.Cadastre.onClickRechercheParcelle(0)
        });

        // sous-sous-menu : recherche parcelle - par adresse
        var buttonRechercheParcelleAdresse = scrollMenuRechercheParcelle.add({
            tooltip : OpenLayers.i18n("cadastrapp.menu.parcelle.adresse"),
            text : OpenLayers.i18n("cadastrapp.menu.parcelle.adresse")
        });
        buttonRechercheParcelleAdresse.on('click', function() {
            GEOR.Addons.Cadastre.onClickRechercheParcelle(1)
        });

        // sous-sous-menu : recherche parcelle - par identifiant cadastral
        var buttonRechercheParcelleAdresse = scrollMenuRechercheParcelle.add({
            tooltip : OpenLayers.i18n("cadastrapp.menu.parcelle.identifiant"),
            text : OpenLayers.i18n("cadastrapp.menu.parcelle.identifiant")
        });
        buttonRechercheParcelleAdresse.on('click', function() {
            GEOR.Addons.Cadastre.onClickRechercheParcelle(2)
        });

        // sous-sous-menu : recherche parcelle - par identifiant cadastral
        var buttonRechercheParcelleLot = scrollMenuRechercheParcelle.add({
            tooltip : OpenLayers.i18n("cadastrapp.menu.parcelle.lot"),
            text : OpenLayers.i18n("cadastrapp.menu.parcelle.lot")
        });
        buttonRechercheParcelleLot.on('click', function() {
            GEOR.Addons.Cadastre.onClickRechercheParcelle(3)
        });

            // sous-menu : recherche proprietaire
            var scrollMenuRechercheProprietaire = new Ext.menu.Menu();
            var buttonRechercheProprietaire = scrollMenu.add({
                id : 'owner-lookup-submenu',
                tooltip : OpenLayers.i18n("cadastrapp.proprietaire"),
                text : OpenLayers.i18n("cadastrapp.proprietaire"),
                menu : scrollMenuRechercheProprietaire
            });
            // sous-sous-menu : recherche proprietaire - par nom
            var buttonRechercheProprietaireNom = scrollMenuRechercheProprietaire.add({
                tooltip : OpenLayers.i18n("cadastrapp.proprietaire.nom"),
                text : OpenLayers.i18n("cadastrapp.proprietaire.nom")
            });
            buttonRechercheProprietaireNom.on('click', function() {
                GEOR.Addons.Cadastre.onClickRechercheProprietaire(0)
            });

            // sous-sous-menu : recherche proprietaire - par compte
            var buttonRechercheProprietaireCompte = scrollMenuRechercheProprietaire.add({
                tooltip : OpenLayers.i18n("cadastrapp.proprietaire.compte"),
                text : OpenLayers.i18n("cadastrapp.proprietaire.compte")
            });
            buttonRechercheProprietaireCompte.on('click', function() {
                GEOR.Addons.Cadastre.onClickRechercheProprietaire(1)
            });

            // sous-sous-menu : recherche proprietaire - par compte
            var buttonRechercheProprietaireCompte = scrollMenuRechercheProprietaire.add({
                tooltip : OpenLayers.i18n("cadastrapp.proprietaire.lot"),
                text : OpenLayers.i18n("cadastrapp.proprietaire.lot")
            });
            buttonRechercheProprietaireCompte.on('click', function() {
                GEOR.Addons.Cadastre.onClickRechercheProprietaire(2)
            });

            // sous-menu : recherche copropriété
            var buttonRechercheCopropriete = scrollMenu.add({
                tooltip : OpenLayers.i18n("cadastrapp.copropriete"),
                text : OpenLayers.i18n("cadastrapp.copropriete")
            });
            buttonRechercheCopropriete.on('click', function() {
                GEOR.Addons.Cadastre.onClickRechercheCoPropriete()
            });

        // sous-menu : traitement sélection
        var scrollMenuTraitementSelection = new Ext.menu.Menu();
        var buttonTraitementSelection = scrollMenu.add({
            tooltip : OpenLayers.i18n("cadastrapp.selection"),
            text : OpenLayers.i18n("cadastrapp.selection"),
            menu : scrollMenuTraitementSelection
        });
        // sous-sous-menu : traitement sélection - parcelles
        var scrollMenuTraitementSelectionParcelles = new Ext.menu.Menu();
        var buttonTraitementSelectionParcelles = scrollMenuTraitementSelection.add({
            tooltip : OpenLayers.i18n("cadastrapp.selection.parcelles"),
            text : OpenLayers.i18n("cadastrapp.selection.parcelles"),
            menu : scrollMenuTraitementSelectionParcelles
        });
        // sous-sous-sous-menu : traitement sélection - parcelles - export
        var buttonTraitementSelectionParcellesExport = scrollMenuTraitementSelectionParcelles.add({
            tooltip : OpenLayers.i18n("cadastrapp.selection.parcelles.export"),
            text : OpenLayers.i18n("cadastrapp.selection.parcelles.export")
        });
        buttonTraitementSelectionParcellesExport.on('click', function() {
            GEOR.Addons.Cadastre.exportPlotSelectionAsCSV()
        });

        // sous-sous-sous-menu : traitement sélection - parcelles - bordereau
        var buttonTraitementSelectionParcellesBordereau = scrollMenuTraitementSelectionParcelles.add({
            tooltip : OpenLayers.i18n("cadastrapp.selection.parcelles.bordereau"),
            text : OpenLayers.i18n("cadastrapp.selection.parcelles.bordereau")
        });
        buttonTraitementSelectionParcellesBordereau.on('click', function() {
            GEOR.Addons.Cadastre.printSelectedBordereauParcellaire()
        });

            // sous-sous-menu : traitement sélection - proprietaires
            var scrollMenuTraitementSelectionProprietaires = new Ext.menu.Menu();
            var buttonTraitementSelectionProprietaires = scrollMenuTraitementSelection.add({
                id : 'owner-selection-submenu',
                tooltip : OpenLayers.i18n("cadastrapp.selection.proprietaires"),
                text : OpenLayers.i18n("cadastrapp.selection.proprietaires"),
                menu : scrollMenuTraitementSelectionProprietaires
            });

            // sous-sous-sous-menu : traitement sélection - proprietaire -
            // export
            var buttonTraitementSelectionProprietairesExport = scrollMenuTraitementSelectionProprietaires.add({
                tooltip : OpenLayers.i18n("cadastrapp.selection.proprietaires.export"),
                text : OpenLayers.i18n("cadastrapp.selection.proprietaires.export")
            });

            buttonTraitementSelectionProprietairesExport.on('click', function() {
                GEOR.Addons.Cadastre.exportOwnerSelectionAsCSV()
            });

    },

    /**
     * private: method[initDemandeControl]
     */
    initDemandeControl : function() {

        // menu : recherche parcelle
        var configDemande = {
            tooltip : OpenLayers.i18n("cadastrapp.demande"),
            iconCls : "gx-featureediting-cadastrapp-demande",
            iconAlign : 'top',
            text : OpenLayers.i18n("cadastrapp.demande"),
            handler : GEOR.Addons.Cadastre.onClickAskInformations
        };
        this.items.push(new Ext.Button(configDemande));
    },

    /**
     * private: method[initHelpControl]
     */
    initHelpControl : function() {

        var configHelp = {
            tooltip : OpenLayers.i18n("cadastrapp.help"),
            iconCls : "help-button",
            iconAlign : 'top',
            helpUrl: this.helpUrl,
            text : OpenLayers.i18n("cadastrapp.help"),
            handler: function() {
                if (Ext.isIE) {
                    window.open(this.helpUrl);
                } else {
                    window.open(this.helpUrl, OpenLayers.i18n("cadastrapp.help"), "menubar=no,status=no,scrollbars=yes");
                }
            }
        };
        this.items.push(new Ext.Button(configHelp));
    },

    /**
     * private: method[onFeatureAdded]
     * 
     * @param event:
     *            ``event`` Called when a new feature is added to the layer.
     *            Change the state of the feature to INSERT and select it.
     * 
     */
    onFeatureAdded : function(event) {
        var feature;

        feature = event.feature;
        feature.state = OpenLayers.State.INSERT;
       
        GEOR.Addons.Cadastre.selectFeatureIntersection(feature);
        // erase point, line or polygones
        feature.layer.removeAllFeatures();
    },

    /**
     * private: method[onUFClick]
     * 
     * @param event:
     *            ``event`` Called when a new feature is added to the layer using unite fonciere button
     * 
     */
    onUFClick : function(event) {
        // Select feature in point intersection
        GEOR.Addons.Cadastre.selectFeatureIntersection(event.feature, "uniteFonciere");
        // erase point
        event.feature.layer.removeAllFeatures();
    },
    
    /**
     * 
     */
    destroy : function() {

        // deactivate each button, so more element from cadastrapp is toogled
        Ext.each(this.cadastrappControls, function(control, index) {
            control.deactivate();
            this.map.removeControl(control);
        });
        this.cadastrappControls = null, this.items = null;

        // Remove WMS Layer
        if (GEOR.Addons.Cadastre.isWMSLayerAdded == true && GEOR.Addons.Cadastre.WMSLayer != null) {

            this.map.removeLayer(GEOR.Addons.Cadastre.WMSLayer);
            GEOR.Addons.Cadastre.WMSLayer.destroy();
            GEOR.Addons.Cadastre.WMSLayer = null;
        }

        GEOR.Addons.Cadastre.isLayerAdded = null;

        // Remove WFSLayer
        GEOR.Addons.Cadastre.WFSLayer.removeAllFeatures();
        this.map.removeLayer(GEOR.Addons.Cadastre.WFSLayer);
        GEOR.Addons.Cadastre.WFSLayer.destroy();
        GEOR.Addons.Cadastre.WFSLayer = null;

        this.map = null;
    },

    CLASS_NAME : "Cadastrapp"
});
