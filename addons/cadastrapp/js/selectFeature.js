Ext.namespace("GEOR.Addons.Cadastre");

/**
 * Method: createSelectionControl
 * 
 * Créé le controle de sélection et la couche des parcelles à partir du wfs
 * 
 * @param: style
 * @param:selectedStyle
 * 
 */
GEOR.Addons.Cadastre.createSelectionControl = function(style, selectedStyle) {

    var styleFeatures = new OpenLayers.StyleMap(new OpenLayers.Style({ // param
        // config
        fillColor : "${getFillColor}", // style des entités en fonction de
        // l'état
        strokeColor : "${getStrokeColor}",
        strokeWidth : "${getstrokeWidth}",
        fillOpacity : "${getFillOpacity}",
        pointRadius : style.pointRadius,
        pointerEvents : style.pointerEvents,
        fontSize : style.fontSize,
        graphicZIndex : 1000
    }, {
        context : {
            getFillColor : function(feature) {
                var fill = selectedStyle.defautColor;
                if (feature.state == GEOR.Addons.Cadastre.selection.state.list) {
                    fill = selectedStyle.colorState1;
                }
                if (feature.state == GEOR.Addons.Cadastre.selection.state.selected) {
                    fill = selectedStyle.colorState2;
                }
                if (feature.state == GEOR.Addons.Cadastre.selection.state.details) {
                    fill = selectedStyle.colorState3;
                }
                return fill;
            },
            getFillOpacity : function(feature) {
                var opacity = 1;
                if (!feature.state) {
                    opacity = 0;
                }
                if (feature.state == GEOR.Addons.Cadastre.selection.state.list || feature.state == GEOR.Addons.Cadastre.selection.state.selected || feature.state == GEOR.Addons.Cadastre.selection.state.details) {
                    opacity = selectedStyle.opacity;
                }
                return opacity;
            },
            getStrokeColor : function(feature) {
                var color = style.strokeColor;
                if (feature.state == GEOR.Addons.Cadastre.selection.state.list) {
                    color = selectedStyle.colorState1;
                }
                if (feature.state == GEOR.Addons.Cadastre.selection.state.selected) {
                    color = selectedStyle.colorState2;
                }
                if (feature.state == GEOR.Addons.Cadastre.selection.state.details) {
                    color = selectedStyle.colorState3;
                }
                return color;
            },
            getstrokeWidth : function(feature) {
                var width = style.strokeWidth;
                if (feature.state == GEOR.Addons.Cadastre.selection.state.list || feature.state == GEOR.Addons.Cadastre.selection.state.selected || feature.state == GEOR.Addons.Cadastre.selection.state.details) {
                    width = selectedStyle.strokeWidth;
                }
                return width;
            },
        }
    }));

    // création de la couche des entités selectionnées
    GEOR.Addons.Cadastre.WFSLayer = new OpenLayers.Layer.Vector("selection", {
        displayInLayerSwitcher : false
    });
    GEOR.Addons.Cadastre.WFSLayer.styleMap = styleFeatures;

    // ajout de la couche à la carte
    layer.map.addLayer(GEOR.Addons.Cadastre.WFSLayer);
    GEOR.Addons.Cadastre.WFSLayer.setZIndex(1001);

    // création de la classe de l'écouteur clique
    OpenLayers.Control.Click = OpenLayers.Class(OpenLayers.Control, {
        defaultHandlerOptions : {
            'single' : true,
            'double' : false,
            'pixelTolerance' : 0,
            'stopSingle' : false,
            'stopDouble' : false
        },
        initialize : function(options) {
            this.handlerOptions = OpenLayers.Util.extend({}, this.defaultHandlerOptions);
            OpenLayers.Control.prototype.initialize.apply(this, arguments);
            this.handler = new OpenLayers.Handler.Click(this, {
                'click' : this.trigger
            }, this.handlerOptions);
        },
        trigger : function(e) {
            // récupération de la longitude et latitude à partir du clique
            lonlat = map.getLonLatFromPixel(e.xy);
            GEOR.Addons.Cadastre.getFeaturesWFSSpatial("Point", lonlat.lon + "," + lonlat.lat, "clickSelector");
        }
    });
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
    var map = layer.map;

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
                var coords = lonlat.lon + "," + lonlat.lat;
                GEOR.Addons.Cadastre.getFeaturesWFSSpatial("Point", coords, "infoBulle");
            }
        },
        onMove : function(evt) {
            // if this control sent an Ajax request (e.g. GetFeatureInfo) when
            // the mouse pauses the onMove callback could be used to abort that
            // request.
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
 * @param: typeGeom
 * @param: coords
 * @param: typeSelector
 * 
 * 
 */
GEOR.Addons.Cadastre.getFeaturesWFSSpatial = function(typeGeom, coords, typeSelector) {

    var filter;
    var selectRows = false; // ligne dans le resultat de recherche doit être
    // sélectionnée si etat =2
    var polygoneElements = "";
    var endPolygoneElements = "";

    if (typeGeom == "Polygon") {
        polygoneElements = "<gml:outerBoundaryIs><gml:LinearRing>";
        endPolygoneElements = "</gml:LinearRing></gml:outerBoundaryIs>";
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

            // dans cette variable on peut avoir plusieurs résultat pour la même
            // parcelle
            var result = geojson_format.read(response.responseText);

            // !! récupère de façon unique les parcelles résultat
            resultSelection = result.filter(function(itm, i, result) {
                return i == getIndex(result, itm.attributes[idField]);
            });

            if (typeSelector != "infoBulle") {

                // If result windows is not opened, create it
                if (!GEOR.Addons.Cadastre.result.plot.window) {
                    GEOR.Addons.Cadastre.addNewResultParcelle("Sélection", null);
                }

                var feature, state;
                var parcelsIds = [], codComm = null;

                // Create parcelsList
                Ext.each(resultSelection, function(feature, currentIndexI) {
                    if (feature) {
                        var exist = false;
                        index = -1;

                        // on teste si l'entité est déja selectionnée
                        Ext.each(GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList, function(selectedFeature, currentIndexJ) {
                            if (selectedFeature.fid == feature.fid) {
                                exist = true;
                                feature = selectedFeature;
                                index = currentIndexJ;
                                return false; // this breaks out of the 'each'
                                // loop
                            }
                        });

                        // on l'ajoute à la selection si elle n'est pas trouvée
                        if (!exist) {
                            GEOR.Addons.Cadastre.WFSLayer.addFeatures(feature);
                            GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList.push(feature);
                        }

                        // on met à jour son état
                        state = GEOR.Addons.Cadastre.changeStateFeature(feature, index - 1, typeSelector);
                        var id = feature.attributes[idField];

                        // si la parcelle est selectionnée on récupère son id
                        // pour le getParcelle pour le tableau
                        if (state == GEOR.Addons.Cadastre.selection.state.list || state == GEOR.Addons.Cadastre.selection.state.selected || GEOR.Addons.Cadastre.selection.state.details) {
                            parcelsIds.push(id);
                        } else {
                            // sinon on la supprime du tableau et on ferme les
                            // fenêtres de détail
                            GEOR.Addons.Cadastre.result.tabs.getActiveTab().getStore().removeAt(GEOR.Addons.Cadastre.indexRowParcelle(id));
                            GEOR.Addons.Cadastre.closeWindowFIUC(id, GEOR.Addons.Cadastre.result.tabs.getActiveTab());
                            GEOR.Addons.Cadastre.closeWindowFIUF(id, GEOR.Addons.Cadastre.result.tabs.getActiveTab());
                        }
                    }
                });

                GEOR.Addons.Cadastre.showTabSelection(parcelsIds);
                // si la méthode est appelée pour afficher l'infobulle
            } else {
                // si on survole la couche et pas le fond de carte pour avoir
                // l'infobulle
                if (resultSelection.length > 0) {
                    var map = GeoExt.MapPanel.guess().map;
                    var idParcelle = resultSelection[0].attributes[idField];
                    var lonlat = new OpenLayers.LonLat(coords.split(",")[0], coords.split(",")[1])
                    GEOR.Addons.Cadastre.displayInfoBulle(map, idParcelle, lonlat);
                }
            }
        },
        failure : function(response) {
            console.log("Error ", response.responseText);
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

    var filter = "" + GEOR.Addons.Cadastre.WFSLayerSetting.nameFieldIdParcelle + "='" + idParcelle + "'";
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
            var feature = geojson_format.read(featureJson)[0];
            if (feature) {
                if (GEOR.Addons.Cadastre.indexFeatureSelected(feature) == -1) {
                    GEOR.Addons.Cadastre.WFSLayer.addFeatures(feature);
                    GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList.push(feature);
                    GEOR.Addons.Cadastre.changeStateFeature(feature, null, GEOR.Addons.Cadastre.selection.state.list);
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
 * Method: setState
 * 
 * Change l'etat sur la carte et mis à jour le dessin
 * 
 * @param: feature
 * @param: state
 * 
 */
GEOR.Addons.Cadastre.setState = function(feature, state) {
    feature.state = state;
    GEOR.Addons.Cadastre.WFSLayer.drawFeature(feature);
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
            } else if (feature.state == GEOR.Addons.Cadastre.selection.state.selected) {
                state = GEOR.Addons.Cadastre.selection.state.list;
            } else if (feature.state == GEOR.Addons.Cadastre.selection.state.details) {
                console.log("no action from maps are available when details panel are open")
            } else {
                state = GEOR.Addons.Cadastre.selection.state.list;
            }
            break;
        case GEOR.Addons.Cadastre.selection.state.selected:
            state = GEOR.Addons.Cadastre.selection.state.selected;
            break;
        case GEOR.Addons.Cadastre.selection.state.details:
            state = GEOR.Addons.Cadastre.selection.state.details;
            break;
        case GEOR.Addons.Cadastre.selection.state.list:
            state = GEOR.Addons.Cadastre.selection.state.list;
            break;

        case "reset":
            GEOR.Addons.Cadastre.result.tabs.getActiveTab().featuresList.splice(index, 1);
            GEOR.Addons.Cadastre.WFSLayer.destroyFeatures([ feature ]);
            break;

        case "tmp":
            state = null;
            break;

        }
        GEOR.Addons.Cadastre.setState(feature, state);
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
 * Method: selectFeatureIntersection
 * 
 * Récupère les coordonnées et la géométrie de l'entité dessinée et envoie une
 * requête au serveur
 * 
 * @param: feature
 */
GEOR.Addons.Cadastre.selectFeatureIntersection = function(feature) {

    // get geometry type
    var typeGeom = feature.geometry.id.split('_')[2];
    var coords = "";

    if (typeGeom == "Point") {
        coords = feature.geometry.x + "," + feature.geometry.y;
    } else {
        var components = feature.geometry.components;
        if (typeGeom == "Polygon")
            components = components[0].components;
        coords = components[0].x + "," + components[0].y;
        Ext.each(components, function(component, currentIndex) {
            coords += " " + component.x + "," + component.y;
        });
    }
    GEOR.Addons.Cadastre.getFeaturesWFSSpatial(typeGeom, coords, "clickSelector");
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

    // zoom sur les entités selectionnées etat 2
    if (features.length > 0 && features[0] != null) {
        // récupération des bordure de l'enveloppe des entités selectionnées
        var minLeft = features[0].geometry.bounds.left;
        var maxRight = features[0].geometry.bounds.right;
        var minBottom = features[0].geometry.bounds.bottom;
        var maxTop = features[0].geometry.bounds.top;
        // on calcule l'enveloppe maximale des entités de la couche slection
        Ext.each(features, function(selectedFeature, currentIndex) {
            minLeft = Math.min(minLeft, selectedFeature.geometry.bounds.left)
            maxRight = Math.max(maxRight, selectedFeature.geometry.bounds.right)
            minBottom = Math.min(minBottom, selectedFeature.geometry.bounds.bottom)
            maxTop = Math.max(maxTop, selectedFeature.geometry.bounds.top)
        });
        var map = GeoExt.MapPanel.guess().map;
        map.zoomToExtent([ minLeft, minBottom, maxRight, maxTop ]); // zoom sur
        // l'emprise
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
            // take URL of layer
            var resultURL = items.grid[0][0].url;
            // if URL contain cadastrapp layer name, add URL to array
            if(resultURL.indexOf(searchName) !== -1 ){
                urlFind.push(resultURL);
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
