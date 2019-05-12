var printMap;


/**
*  Get plots list from cadastrapp service and display then in special div
* @param ufId the uf id that will be used to call services
*/
function getParcellesInformation(ufId){
   
   $.getJSON( window.opener.GEOR.Addons.Cadastre.url.serviceParcelle+ "?unitefonciere=" + encodeURIComponent(ufId), function( data ) {
       
       var parcelles="";
       var sommeSurf=0;
       
       data.forEach(function(element){
           parcelles=parcelles+ "<div class=\"data\"><span class=\"dataLabel\">"+element.parcelle+"</span>"+((element.surfc === null) ? 0 : element.surfc.toLocaleString())+" m²</div>";
           sommeSurf=sommeSurf+element.surfc;
       });
        
       var parcelleDisplay = " parcelle";
       if(data.length>1){
           parcelleDisplay = " parcelles";
       }
       var content = "<div class=\"info\"><b>Cette unité foncière est composée de "+data.length+parcelleDisplay+".</b></div>"+
       "<div class=\"info\">La somme des surfaces DGFiP est égale à "+ sommeSurf.toLocaleString() +" m².</div>";
       
       document.getElementById('composition').innerHTML=content;
       document.getElementById('parcelles').innerHTML=parcelles;
       
       setGloblainformation();
   
     }); 
}

/**
*  Get and set globla information in DOM
*/
function setGloblainformation(){
    document.getElementById('services').innerHTML = "<div>Données foncières valides au "+window.opener.GEOR.Addons.Cadastre.UF.dateValiditeMajic+" - "+
    "Données cartographiques valides au "+window.opener.GEOR.Addons.Cadastre.UF.dateValiditeEDIGEO+"</div>"+
    "<div>"+window.opener.GEOR.Addons.Cadastre.UF.organisme+"</div>"
}

/**
 * Calculate ratio of building surface area / plots surface area
 * 
 * @param surfaceTotal Contenance DGFIP
 * @param surfaceBatie Surface Batie calculée
 * @returns 0 if one of the value is empty or null
 */
function getSufBatCalcPourcentage(surfaceTotal, surfaceBatie){
    var percent = 0;
    if(surfaceTotal && surfaceBatie && surfaceTotal != 0 && surfaceBatie != 0){
        percent = ((surfaceBatie/surfaceTotal)*100).toFixed(1);
    }
    return percent;  
}

/**
*  Get unite fonciere information and owners information from cadastrapp service and display then in special div
* @param parcelleId parcelleId that will be used to call services
*/
function getUFInformation(parcelleId){
   
   // get global information
   $.getJSON( window.opener.GEOR.Addons.Cadastre.url.serviceInfoUniteFonciere + "?parcelle=" +  encodeURIComponent(parcelleId), function(data) {
       
       // Only if we get a response
       if(data){
           // get owner name
           $.getJSON( window.opener.GEOR.Addons.Cadastre.url.serviceProprietaire + "?details=2&comptecommunal=" +  encodeURIComponent(data.comptecommunal), function(prop) {
              
               var propName = "";
               prop.forEach(function(element){
                   propName = propName +"<div>"+element.app_nom_usage+"</div>";
               });
                      
               var propDisplay = "Propriétaire";
               if(prop.length>1){
                   propDisplay = "Propriétaires";
               }
               
               document.getElementById('informationgenerale').innerHTML =
               "<div class=\"proprieteaire\"><div class=\"propTitle\">"+propDisplay+" ( "+data.comptecommunal+" ) : </div>"+
               "<div class=\"propList\">"+propName+"</div>"+
               "<div class=\"datauflist\">"+
               "<div class=\"datauf\"><span class=\"dataufLabel\">Contenance DGFIP de l'UF : </span>"+((data.dcntpa_sum === null) ? 0 : data.dcntpa_sum.toLocaleString())+" m²</div>"+
               "<div class=\"datauf\"><span class=\"dataufLabel\">Surface calculée : </span>"+((data.sigcal_sum === null) ? 0 : data.sigcal_sum.toLocaleString())+" m²</div>"+
               "<div class=\"datauf\"><span class=\"dataufLabel\">Surface bâtie calculée : </span>"+((data.sigcalb_sum === null) ? 0 : data.sigcalb_sum.toLocaleString())+" m²</div>"+
               "<div class=\"datauf\"><span class=\"dataufLabel\">Pourcentage surface bâtie calculée : </span>"+getSufBatCalcPourcentage(data.dcntpa_sum, data.sigcalb_sum)+" %</div>"+
               "</div>";
           });
   
           getParcellesInformation(data.uf);
       }
       
     }); 
}

/**
 * Force refresh layers by add and remove features
 */
function updateFeatures() {
    // remove and add feature for each layers
    var lyrWithFeatures = {}
    printMap.layers.forEach(function(lyr) {
        if (lyr.features) {
            var lyrFeatures = lyr.features
            lyr.removeAllFeatures();
            lyr.addFeatures(lyrFeatures);
        } else {
            // refresh others type of layer
            if (lyr.refresh) {
                lyr.refresh({
                    force: true
                });
            }
        }
    });
}

/**
 * Init code after body is loaded
 */
function init() {
    var clones,
        baseLayerPosition,
        openerMap;
    
    //activate JQuery UI actions for this element     
    $('#printmap').draggable().resizable();    
    
    //from window opener, take information to clone map, layers and generate graphic elements as legend, north arrow or text     
    if (window.opener) {        

        // get original map and layers from mapfishapp viewer
        openerMap = window.opener.GeoExt.MapPanel.guess().map; 
        
        // clones layer and get informations to call legend
        clones = [];
        baseLayerPosition = "";        
        openerMap.layers.forEach(function(layer, index) {            
            if (layer.clone) {                        
                if(layer.attribution){ // to not display source on map
                    delete layer["attribution"];
                }
               
                clones.push(layer.clone());
                // get and set base layer from original viewer
                // This name is specific to mapfishapp.
                if (layer.name === "__georchestra_base_layer") { 
                    baseLayerPosition = index;
                }
            }
        });
        // new map    
        printMap = new OpenLayers.Map("printmap", {            
            units: "m",
            scales: openerMap.scales,
            controls: [
                new OpenLayers.Control.ScaleLine()
                ] 
        });
        
        // add clones layers
        printMap.addLayers(clones);
        // set base layer
        if (baseLayerPosition !== "") {
            printMap.setBaseLayer(printMap.layers[baseLayerPosition]);
        }
        // zoom to original extend
        printMap.setCenter(new OpenLayers.LonLat(openerMap.center.lon, openerMap.center.lat), openerMap.getZoom());
        // detect window resize action and update layers by remove and add manipulation to force layer refresh
        window.addEventListener("resize", function() {
            if (printMap && printMap.layers && printMap.layers.length > 0) {
                updateFeatures();
            }
        }, false);
        // add information
        var parcelleId = window.opener.GEOR.Addons.Cadastre.UF.parcelleId;
        getUFInformation(parcelleId);
    }
}

/**
 * Hide or show JQuery UI handler use to resize given elements
 * @param {Object} el - HTML element
 * @param {String} bool - True tue show and false to hide
 */
function handlerVisibility(el, bool) {
    var div = el.getElementsByTagName("div");
    var n = Object.keys(div);
    if (n.length > 0) {
        n.forEach(function(key) {
            if (div[key].className.indexOf("ui") > -1) {
                div[key].style.display = bool ? "block" : "none";
            }
        });
    }
}
