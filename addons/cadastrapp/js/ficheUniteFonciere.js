var printMap;


/**
*  Get plots list from cadastrapp service and display then in special div
* @param ufId the uf id that will be used to call services
*/
function getParcellesInformation(ufId){
   
   $.getJSON( window.opener.GEOR.Addons.Cadastre.cadastrappWebappUrl+"getParcelle?unitefonciere=" + encodeURIComponent(ufId), function( data ) {
       
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
   
     }); 
}

/**
*  Get unite fonciere information and owners information from cadastrapp service and display then in special div
* @param parcelleId parcelleId that will be used to call services
*/
function getUFInformation(parcelleId){
   
   // get global information
   $.getJSON( window.opener.GEOR.Addons.Cadastre.cadastrappWebappUrl+"getInfoUniteFonciere?parcelle=" +  encodeURIComponent(parcelleId), function(data) {
       
       // Only if we get a response
       if(data){
           // get owner name
           $.getJSON( window.opener.GEOR.Addons.Cadastre.cadastrappWebappUrl+"getProprietaire?details=2&comptecommunal=" +  encodeURIComponent(data.comptecommunal), function(prop) {
              
               var propName = "";
               prop.forEach(function(element){
                   propName = propName +"<div>"+element.app_nom_usage+"</div>";
               });
                      
               var propDisplay = "Propriétaire";
               if(prop.length>1){
                   propDisplay = "Propriétaires";
               }
               
               document.getElementById('informationgenerale').innerHTML =
               "<div class=\"proprieteaire\"><div>"+propDisplay+" : </div>"+propName+
               "<div>"+data.comptecommunal+"</div></div>"+
               "<div class=\"datauf\"><span class=\"dataufLabel\">Surface DGFIP de l'UF : </span>"+((data.dcntpa_sum === null) ? 0 : data.dcntpa_sum.toLocaleString())+" m²</div>"+
               "<div class=\"datauf\"><span class=\"dataufLabel\">Surface calculée : </span>"+((data.sigcal_sum === null) ? 0 : data.sigcal_sum.toLocaleString())+" m²</div>"+
               "<div class=\"datauf\"><span class=\"dataufLabel\">Surface bâtie calculée : </span>"+((data.sigcalb_sum === null) ? 0 : data.sigcalb_sum.toLocaleString())+" m²</div>";
           });
   
           getParcellesInformation(data.uf);
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
