Ext.namespace("GEOR.Addons.Cadastre");


/**
 * Method: createLayerStyle
 * 
 * Create style for vector layer using params
 * 
 * @param:styleParams
 * 
 */
GEOR.Addons.Cadastre.createLayerStyle = function(styleParams) {
    var defaultStyle = new OpenLayers.Style({
        fillColor : styleParams.listed.fillColor, 
        strokeColor : styleParams.listed.strokeColor,
        strokeWidth : styleParams.listed.strokeWidth,
        fillOpacity : styleParams.listed.opacity,
        strokeOpacity : styleParams.listed.opacity,
    });
    
    var selectStyle = new OpenLayers.Style({
        fillColor : styleParams.selected.fillColor, 
        strokeColor : styleParams.selected.strokeColor,
        strokeWidth : styleParams.selected.strokeWidth,
        fillOpacity : styleParams.selected.opacity,
        strokeOpacity : styleParams.selected.opacity,
    });
    
    var globalStyle = new OpenLayers.StyleMap({
        'default': defaultStyle,
        'select': selectStyle
    });
    
    return globalStyle;
}

/**
 * Method: createLayer
 * 
 * Create vector layer and associate style for plots
 * 
 * @param:styleParams
 * 
 */
GEOR.Addons.Cadastre.createLayer = function(styleParams) {
    
   
    // création de la couche des entités selectionnées
    GEOR.Addons.Cadastre.WFSLayer = new OpenLayers.Layer.Vector("__georchestra_cadastrapps_plots", {
        displayInLayerSwitcher : false,
        styleMap:GEOR.Addons.Cadastre.createLayerStyle(styleParams)
    });
    
    GEOR.Addons.Cadastre.WFSLayer.selectControl = new OpenLayers.Control.SelectFeature([GEOR.Addons.Cadastre.WFSLayer]);

    // This control have to be remove when closing cadastrapp
    GeoExt.MapPanel.guess().map.addControl(GEOR.Addons.Cadastre.WFSLayer.selectControl);
       
    // This is done to make sure layer is always on top even if we had some more layer
    // TODO change this by using mapfishapp event
    GEOR.Addons.Cadastre.WFSLayer.setZIndex(1001);
}

/**
 * Method: addPopupOnhover
 * 
 * Créé un popup lorsque l'on survole la map
 * 
 * @param: popuConfig
 * 
 */
GEOR.Addons.Cadastre.addPopupOnhover = function(popupConfig) {
    var map = GeoExt.MapPanel.guess().map;

    // comme pour le clique , on crée la classe du controleur hover
    OpenLayers.Control.Hover = OpenLayers.Class(OpenLayers.Control, {
        defaultHandlerOptions : {
            'delay' : 200,
            'pixelTolerance' : null,
            'stopMove' : false
        },
        initialize : function(options) {
            this.handlerOptions = OpenLayers.Util.extend({}, this.defaultHandlerOptions);
            OpenLayers.Control.prototype.initialize.apply(this, arguments);
            this.handler = new OpenLayers.Handler.Hover(this, {
                'pause' : this.onPause,
                'move' : this.onMove
            }, this.handlerOptions);
        },
        onPause : function(evt) {
            var zoom = map.getZoom();
            // affichage si niveau de zoom convenable
            if (zoom > popupConfig.minZoom) {
                var lonlat = map.getLonLatFromPixel(evt.xy);
                var point = new OpenLayers.Geometry.Point(lonlat.lon, lonlat.lat);
                GEOR.Addons.Cadastre.getFeaturesWFSSpatial(point, "infoBulle");
            }
        }
    });
    // création du controleur de survol
    var infoControls = {
        'hover' : new OpenLayers.Control.Hover({
            handlerOptions : {
                // delai d'affichage du popup
                'delay' : popupConfig.timeToShow
            }
        })
    };
    // Add controls to control list created by cadastrapp
    GEOR.Addons.Cadastre.menu.cadastrappControls.push(infoControls.hover);
    map.addControl(infoControls["hover"]);
    infoControls["hover"].activate();
}

/**
 * Method: getFeaturesWFSSpatial
 * 
 * Envoie une requête au geoserveur pour faire une intersection de la couche wms
 * avec la géométrie donnée en paramètres
 * 
 * @param: geometry
 * @param: typeSelector
 */
GEOR.Addons.Cadastre.getFeaturesWFSSpatial = function(geometry, typeSelector) {
    
    if(!typeSelector){
        typeSelector="clickSelector";
    }
    
    var filter;
    var selectRows = false; // ligne dans le resultat de recherche doit être sélectionnée si etat =2
    var polygoneElements = "";
    var endPolygoneElements = "";
    
    var coords = GEOR.Addons.Cadastre.getFeatureCoord(geometry);
    var typeGeom = geometry.id.split('_')[2];
    
    if (typeGeom == "Polygon") {
        polygoneElements = "<gml:outerBoundaryIs><gml:LinearRing>";
        endPolygoneElements = "</gml:LinearRing></gml:outerBoundaryIs>";
    }else if (typeGeom == "MultiPolygon"){
        polygoneElements = "<gml:polygonMember><gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>";
        endPolygoneElements = "</gml:LinearRing></gml:outerBoundaryIs></gml:Polygon></gml:polygonMember>";
    }

    filter = '<Filter xmlns:gml="http://www.opengis.net/gml"><Intersects><PropertyName>' + GEOR.Addons.Cadastre.WFSLayerSetting.geometryField + '</PropertyName><gml:' + typeGeom + '>' + polygoneElements + '<gml:coordinates>' + coords + '</gml:coordinates>' + endPolygoneElements + '</gml:' + typeGeom + '></Intersects></Filter>';

    Ext.Ajax.request({
        async : false,
        url : GEOR.Addons.Cadastre.WFSLayerSetting.wfsUrl,
        method : 'GET',
        headers : {
            'Content-Type' : 'application/json'
        },
        params : {
            "request" : GEOR.Addons.Cadastre.WFSLayerSetting.request,
            "version" : GEOR.Addons.Cadastre.WFSLayerSetting.version,
            "service" : GEOR.Addons.Cadastre.WFSLayerSetting.service,
            "typename" : GEOR.Addons.Cadastre.WFSLayerSetting.typename,
            "outputFormat" : GEOR.Addons.Cadastre.WFSLayerSetting.outputFormat,
            "filter" : filter
        },
        success : function(response) {

            // champ identifiant de parcelle dans geoserver
            var idField = GEOR.Addons.Cadastre.WFSLayerSetting.nameFieldIdParcelle;
            var resultSelection;

            var getIndex = function(result, str) {
                var index = -1;
                Ext.each(result, function(element, currentIndex) {
                    if (element.attributes[idField] == str) {
                        index = currentIndex;
                        return false; // this breaks out of the 'each' loop
                    }
                });
                return index;
            }

            var geojson_format = new OpenLayers.Format.GeoJSON();

            // dans cette variable on peut avoir plusieurs résultat pour la même parcelle
            var result = geojson_format.read(response.responseText);

            // !! récupère de façon unique les parcelles résultat
            resultSelection = result.filter(function(itm, i, result) {
                return i == getIndex(result, itm.attributes[idField]);
            });
            
            if (typeSelector != "infoBulle" && typeSelector != "uniteFonciere") {

                var feature, state;
                var parcelsIds = [], codComm = null;

                // Create plotsList
                Ext.each(resultSelection, function(feature, currentIndexI) {
                    if (feature) {
                        var exist = false;
                        index = -1;

                        // si il y a déja une fenêtre de parcelle
                        if (GEOR.Addons.Cadastre.result.plot.window) {
                            // on teste si l'entité est déja selectionnée
                            Ext.each(GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList, function(selectedFeature, currentIndexJ) {
                                if (selectedFeature.fid == feature.fid) {
                                    exist = true;
                                    feature = selectedFeature;
                                    index = currentIndexJ;
                                    return false; // this breaks out of the 'each' loop
                                }
                            });
                        }

                        // on met la feature a l'état list
                        if (!exist) {
                            state = GEOR.Addons.Cadastre.selection.state.list;
                        }else{
                            // on met à jour son état
                            state = GEOR.Addons.Cadastre.changeStateFeature(feature, index - 1, typeSelector);
                        }

                        var id = feature.attributes[idField];

                        // si la parcelle est selectionnée on récupère son id
                        // pour le getParcelle pour le tableau
                        if (state == GEOR.Addons.Cadastre.selection.state.list || state == GEOR.Addons.Cadastre.selection.state.selected) {
                            parcelsIds.push(id);
                        } else {
                            // sinon on la supprime du tableau et on ferme les fenêtres de détails
                            GEOR.Addons.Cadastre.result.tabs.getActiveTab().getStore().removeAt(GEOR.Addons.Cadastre.indexRowParcelle(id));
                            GEOR.Addons.Cadastre.closeWindowFIUC(id, GEOR.Addons.Cadastre.result.tabs.getActiveTab());
                        }
                    }
                });

                GEOR.Addons.Cadastre.showTabSelection(parcelsIds);             
            } else if(typeSelector == "infoBulle") { // si la méthode est appelée pour afficher l'infobulle
                // si on survole la couche et pas le fond de carte pour avoir l'infobulle
                if (resultSelection.length > 0) {
                    var idParcelle = resultSelection[0].attributes[idField];
                    // if infoBulle geometry is a point
                    var lonlat = new OpenLayers.LonLat(geometry.x, geometry.y)
                    GEOR.Addons.Cadastre.displayInfoBulle(idParcelle, lonlat);
                }
            }else{ // unitefonciere
                if (resultSelection.length > 0) {
                    
                    //get Parcelle Id
                    var idParcelle = resultSelection[0].attributes[idField];
                    GEOR.Addons.Cadastre.searchUFbyParcelle(idParcelle,geometry);               
                }
            }
        },
        failure : function(response) {
            console.log("Error ", response.responseText);
        }
    });
}

/**
 *  Search and display UF of the corresponding plots and geom
 */
GEOR.Addons.Cadastre.searchUFbyParcelle=  function(idParcelle, geometry){
    
    var polygoneElements = "";
    var endPolygoneElements = "";               
    var coords = GEOR.Addons.Cadastre.getFeatureCoord(geometry);
    var typeGeom = geometry.id.split('_')[2];
    
    if (typeGeom == "Polygon") {
        polygoneElements = "<gml:outerBoundaryIs><gml:LinearRing>";
        endPolygoneElements = "</gml:LinearRing></gml:outerBoundaryIs>";
    }else if (typeGeom == "MultiPolygon"){
        polygoneElements = "<gml:polygonMember><gml:Polygon><gml:outerBoundaryIs><gml:LinearRing>";
        endPolygoneElements = "</gml:LinearRing></gml:outerBoundaryIs></gml:Polygon></gml:polygonMember>";
    }

    var filterUF = '<Filter xmlns:gml="http://www.opengis.net/gml"><Contains><PropertyName>' + GEOR.Addons.Cadastre.UF.WFSLayerSetting.geometryField + '</PropertyName><gml:' + typeGeom + '>' + polygoneElements + '<gml:coordinates>' + coords + '</gml:coordinates>' + endPolygoneElements + '</gml:' + typeGeom + '></Contains></Filter>';

    var postData = 
        '<wfs:GetFeature service="' + GEOR.Addons.Cadastre.UF.WFSLayerSetting.service + '" version="' + GEOR.Addons.Cadastre.UF.WFSLayerSetting.version + '" outputFormat="' + GEOR.Addons.Cadastre.UF.WFSLayerSetting.outputFormat + '" xmlns:wfs="http://www.opengis.net/wfs" xmlns:ogc="http://www.opengis.net/ogc" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://www.opengis.net/wfs http://schemas.opengis.net/wfs/1.0.0/WFS-basic.xsd">' +
            '<wfs:Query typeName="' + GEOR.Addons.Cadastre.UF.WFSLayerSetting.typename + '">' +
                filterUF +
            '</wfs:Query>' +
        '</wfs:GetFeature>';

    Ext.Ajax.request({
        async : false,
        url : GEOR.Addons.Cadastre.UF.WFSLayerSetting.wfsUrl,
        method : 'POST',
        headers : {
            'Content-Type' : 'application/json'
        },
        params : postData,
        success : function(response) {
            var geojson_format = new OpenLayers.Format.GeoJSON();
            var result = geojson_format.read(response.responseText);
            
            // If no result display message erreur, no UF at this point
            
            // Else display UF and launch new windows
            Ext.each(result, function(feature, index) {
                if (feature) {
                    // For each existing uf feature remove it
                    Ext.each(GEOR.Addons.Cadastre.UF.features, function(feature) {
                        GEOR.Addons.Cadastre.WFSLayer.removeFeatures(feature);
                    });
                    GEOR.Addons.Cadastre.UF.features=[];
                    GEOR.Addons.Cadastre.UF.features.push(feature);
                    // set selected style on feature to keep style in new windows
                    feature.style=GEOR.Addons.Cadastre.WFSLayer.styleMap.styles.select.defaultStyle;
                    // Add new feature
                    GEOR.Addons.Cadastre.WFSLayer.addFeatures(feature);
                    GEOR.Addons.Cadastre.changeStateFeature(feature,index,GEOR.Addons.Cadastre.selection.state.selected);
                    GEOR.Addons.Cadastre.zoomOnFeatures(GEOR.Addons.Cadastre.UF.features);
                }
            });

            GEOR.Addons.Cadastre.onClickDisplayFIUF(idParcelle);
        }
    });
    
}

/**
 * Method: indexFeatureSelected
 * 
 * Récupère l'index de l'entité selectionnée dans la couche selection
 * 
 * @param: feature
 * 
 */
GEOR.Addons.Cadastre.indexFeatureSelected = function(feature) {

    var idField = GEOR.Addons.Cadastre.WFSLayerSetting.nameFieldIdParcelle;
    var index = -1;

    Ext.each(GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList, function(selectedFeature, currentIndex) {
        if (selectedFeature.attributes[idField] == feature.attributes[idField]) {
            index = currentIndex;
            return false; // this breaks out of the 'each' loop
        }
    });
    return index;
}

/**
 * Method: indexRowParcelle
 * 
 * Récupère l'index de la ligne d'une parcelle dans le tableau
 * 
 * @param: idParcelle
 */
GEOR.Addons.Cadastre.indexRowParcelle = function(idParcelle) {
    var rowIndex = GEOR.Addons.Cadastre.result.tabs.getActiveTab().getStore().find('parcelle', idParcelle);
    return rowIndex;
}

/**
 * Method: getFeaturesWFSAttribute
 * 
 * Envoie une requete selon un filtre attributaire
 * 
 * @param: idParcelle
 */
GEOR.Addons.Cadastre.getFeaturesWFSAttribute = function(idParcelle) {
   
    var filter = "";
    var idField = GEOR.Addons.Cadastre.WFSLayerSetting.nameFieldIdParcelle;
    if(Array.isArray(idParcelle)){
        var lastValue = idParcelle[idParcelle.length-1];
        Ext.each(idParcelle, function(id, currentIndex){
            var filterArg = " "+ idField +" LIKE " + "'" + "%" + id + "%" + "'";
            var operator = (id == lastValue) ? "" : " or ";
            filter = filter + filterArg + operator ;
        });
    } else {
        filter = "" + idField + "='" + idParcelle + "'";
    }

    var featureJson = "";

    Ext.Ajax.request({
        url : GEOR.Addons.Cadastre.WFSLayerSetting.wfsUrl,
        method : 'GET',
        headers : {
            'Content-Type' : 'application/json'
        },
        params : {
            "request" : GEOR.Addons.Cadastre.WFSLayerSetting.request,
            "version" : GEOR.Addons.Cadastre.WFSLayerSetting.version,
            "service" : GEOR.Addons.Cadastre.WFSLayerSetting.service,
            "typename" : GEOR.Addons.Cadastre.WFSLayerSetting.typename,
            "outputFormat" : GEOR.Addons.Cadastre.WFSLayerSetting.outputFormat,
            "cql_filter" : filter
        },
        success : function(response) {
            featureJson = response.responseText;
            var geojson_format = new OpenLayers.Format.GeoJSON();
            var resultSelection = geojson_format.read(featureJson);
            if(resultSelection && resultSelection.length > 0){                
                Ext.each(resultSelection, function(item, currentIndex){
                    var feature = item;
                    if (feature) {
                        if (GEOR.Addons.Cadastre.indexFeatureSelected(feature) == -1) {
                            GEOR.Addons.Cadastre.WFSLayer.addFeatures(feature);
                            GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList.push(feature);
                            GEOR.Addons.Cadastre.changeStateFeature(feature, null, GEOR.Addons.Cadastre.selection.state.list);
                        }
                    }
                });
                
                var enableBtn = Ext.getCmp('selectParcelleButton') ? Ext.getCmp('selectParcelleButton') : false;
                if(enableBtn){
                    enableBtn.enable();
                }                
            }
            
        },
        failure : function(response) {
            console.log("Error ", response.responseText);
        }
    });
}

/**
 * Method: getFeatureById
 * 
 * Retourne une entité en prenant son id
 * 
 * @param: idParcelle
 * @return feature with id parcell = idParcelle
 */
GEOR.Addons.Cadastre.getFeatureById = function(idParcelle) {

    var idField = GEOR.Addons.Cadastre.WFSLayerSetting.nameFieldIdParcelle;
    var feature;

    Ext.each(GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList, function(selectedFeature, currentIndex) {
        if (selectedFeature.attributes[idField] == idParcelle) {
            feature = selectedFeature;
            return false;
        }
    });
    return feature;
}

/**
 * Method: changeStateFeature
 * 
 * Gestion du changement d'état lors du click sur l'entitée
 * 
 * @param: feature
 * @param: index
 * @param: typeSelector
 * 
 * @return null if feature is null
 * 
 */
GEOR.Addons.Cadastre.changeStateFeature = function(feature, index, typeSelector) {

    var state = null;

    if (feature) {
        switch (typeSelector) {
        // changement par selection sur la carte
        case "clickSelector":
            if (feature.state == GEOR.Addons.Cadastre.selection.state.list) {
                state = GEOR.Addons.Cadastre.selection.state.selected;
                GEOR.Addons.Cadastre.WFSLayer.selectControl.select(feature);
            } else {
                state = GEOR.Addons.Cadastre.selection.state.list;
                GEOR.Addons.Cadastre.WFSLayer.selectControl.unselect(feature);
            }
            break;
        case GEOR.Addons.Cadastre.selection.state.selected:
            state = GEOR.Addons.Cadastre.selection.state.selected;
            GEOR.Addons.Cadastre.WFSLayer.selectControl.select(feature);
            break;
        case GEOR.Addons.Cadastre.selection.state.list:
            state = GEOR.Addons.Cadastre.selection.state.list;
            GEOR.Addons.Cadastre.WFSLayer.selectControl.unselect(feature);
            break;
        case "reset":
            GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList.splice(index, 1);
            GEOR.Addons.Cadastre.WFSLayer.destroyFeatures([ feature ]);
            break;
        case "tmp":
            state = null;
            GEOR.Addons.Cadastre.WFSLayer.eraseFeatures([feature]);
            break;
        }
        feature.state = state;
    }
    return state;
}

/**
 * Method: clearLayerSelection
 * 
 * Vide le tableau des entités selectionnées
 * 
 */
GEOR.Addons.Cadastre.clearLayerSelection = function() {
    GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList = [];
    GEOR.Addons.Cadastre.WFSLayer.removeAllFeatures();
}

/**
 * Calculate coordinates for given geometry
 */
GEOR.Addons.Cadastre.getFeatureCoord = function(geometry){
    
    var typeGeom = geometry.id.split('_')[2];
    var coords = "";

    if (typeGeom == "Point") {
        coords = geometry.x + "," + geometry.y;
    } else {
        var components = geometry.components;
        // TODO change this way of getting feature.
        // geo_parcelle and unite fonciere are multipolygon in qgis model
        if (typeGeom == "Polygon"){
            components = components[0].components;
        }else if(typeGeom == "MultiPolygon"){
            //get only first Polygon as coords
            components = components[0].components[0].components
        }
        coords = components[0].x + "," + components[0].y;
        Ext.each(components, function(component, currentIndex) {
            coords += " " + component.x + "," + component.y;
        });
    }
    return coords;
}

/**
 * Method: getLayerByName
 * 
 * retourne la couche en prenant le nom
 * 
 * @param: layerName
 */
GEOR.Addons.Cadastre.getLayerByName = function(layerName) {
    var map = GeoExt.MapPanel.guess().map;
    var layer1 = map.getLayersByName(layerName)[0];
    return layer1;
}

/**
 * Method: zoomOnFeatures
 * 
 * Zoom on given features
 * 
 * @param: features Array
 */
GEOR.Addons.Cadastre.zoomOnFeatures = function(features) {

    // zoom sur les entités données en paramètre
    if (features.length > 0 && features[0] != null) {
       
        // récupération de l'emprise de la première entité
        var minLeft = features[0].geometry.bounds.left;
        var maxRight = features[0].geometry.bounds.right;
        var minBottom = features[0].geometry.bounds.bottom;
        var maxTop = features[0].geometry.bounds.top;
        
        // On compare aux autres entités pour avoir l'emprise maximale
        Ext.each(features, function(selectedFeature, currentIndex) {
            minLeft = Math.min(minLeft, selectedFeature.geometry.bounds.left)
            maxRight = Math.max(maxRight, selectedFeature.geometry.bounds.right)
            minBottom = Math.min(minBottom, selectedFeature.geometry.bounds.bottom)
            maxTop = Math.max(maxTop, selectedFeature.geometry.bounds.top)
        });
        
        // récupération de la map et zoom sur l'emprise maximal
        var map = GeoExt.MapPanel.guess().map;
        map.zoomToExtent([ minLeft, minBottom, maxRight, maxTop ]); 
        
        // Récupération du Scale après zoom sur l'extent
        // attention map.getScale peut arrondir le scale qui ne correspondra plus à une valeur de GEOR.config.MAP_SCALES[]
        // Il faut bien utiliser >= et non === pour le scale et arroundir à l'unité pour la comparaison
        var currentScale = Math.round(map.getScale());
        
        // Modification du niveau de zoom pour permettre de voir la parcelle dans son environnement 
        // On recule d'un niveau de zoom pour obtenir un aperçu des parcelles alentours
        // Si le zoom résultant est plus important que GEOR.config.MAP_SCALES[2] , alors on met GEOR.config.MAP_SCALES[2] comme zoom
        Ext.each(GEOR.config.MAP_SCALES, function(scale, index){
            // Si la valeur correspond au scale actuelle
            // et qu'il y a un niveau de zoom inferieur de définit
            if(Math.round(scale) >= currentScale && GEOR.config.MAP_SCALES[index + 1]){
                map.zoomToScale(Math.max(GEOR.config.MAP_SCALES[index + 1], GEOR.config.MAP_SCALES[2]) , true)
                return false;
            }
        });
        
    } else {
		alert(OpenLayers.i18n('cadastrapp.no_feature'));
        console.log("No feature in input, could not zoom on it");
    }
}

/**
 * Method: addWMSLayer
 * 
 * Add layer to map and layerswitcher only if same WMS url does not exist
 * 
 * If layerNameInPanel is empty, do not display layer in layerswitcher
 * 
 * @param: wmsSetting[]
 * 
 *      wmsSetting.layerNameInPanel - Name in layer switcher
 *      wmsSetting.url
 *      wmsSetting.layerNameGeoserver
 *      wmsSetting.transparent
 *      wmsSetting.format
 * 
 */
GEOR.Addons.Cadastre.addWMSLayer = function(wmsSetting) {
          
    // Search layer with same wms URL if already add cadastrapp layer

    // encode special caracter in geoserver layer name param
    var searchName = escape(wmsSetting.layerNameGeoserver);      
    var urlFind = []; // if empty after loop, any layer has been already add 
    
    // for all url in layerPanel, search geoserver's layer param 
    Ext.each(GeoExt.MapPanel.guess().map.layers,function(items, index){
        if(items.grid){
            if(items.grid.length>0 && items.grid[0][0].url){
                // take URL of layer
		var resultURL = items.grid[0][0].url;
		// if URL contain cadastrapp layer name, add URL to array
		if(resultURL.indexOf(searchName) !== -1 ){
		    urlFind.push(resultURL);
		}
            }
        }        
    });   

    // if layer not already present add cadastrapp addons layer
    if(urlFind.length == 0){
		
		GEOR.Addons.Cadastre.isWMSLayerAdded = true;
		
        // Show layer in switcher only if it has a name set by administrator
        var isDisplayInLayerSwitcher = false;
        if (wmsSetting.layerNameInPanel.length > 0) {
            isDisplayInLayerSwitcher = true;
        }

        // Modify WMS information to be added to layer
        GEOR.Addons.Cadastre.WMSLayer = new OpenLayers.Layer.WMS(
            // Layer name in switch panel
            wmsSetting.layerNameInPanel, 
            wmsSetting.url, {
                layers : wmsSetting.layerNameGeoserver,
                transparent : wmsSetting.transparent,
                format : wmsSetting.format,
                type : "WMS",
                queryable : true
            }, {
                // Map option
                displayInLayerSwitcher : isDisplayInLayerSwitcher,
                isBaseLayer : false
            }
         );
        
        // Create LayerStore from OpenLayer 
       var layersWMS = [];
       layersWMS.push(GEOR.Addons.Cadastre.WMSLayer);
       var reader = new GeoExt.data.LayerReader();
       var layerData = reader.readRecords(layersWMS);

       GeoExt.MapPanel.guess().layers.add(layerData.records[0]);

    }
}

/**
 * Method: restoreLayersOnClear
 * 
 * Add WFS/WMS layer to map and layerswitcher when the map layers/context are cleared
 * 
 */
GEOR.Addons.Cadastre.restoreLayersOnClear = function() {
    var layersList = [];
    layersList.push(GEOR.Addons.Cadastre.WFSLayer);
    if( GEOR.Addons.Cadastre.visible ) {
        GEOR.Addons.Cadastre.addWMSLayer(this.options.WMSLayer);
    }
    var reader = new GeoExt.data.LayerReader();
    var layerData = reader.readRecords(layersList);
    layerData.records.forEach(function(value){
        GeoExt.MapPanel.guess().layers.add(value);
    });
}
