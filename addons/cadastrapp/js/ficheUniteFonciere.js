var printMap;
/**
 * Create date as 11072017154030
 */
function formatDate() {
    var d = new Date(),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear(),
        hour = d.getHours(),
        minutes = d.getMinutes(),
        seconds = d.getSeconds();
    if (month.length < 2) {
        month = '0' + month
    };
    if (day.length < 2) {
        day = '0' + day
    };
    var date = day + month + year + hour + minutes + seconds;
    return date;
}

/**
 * Init code after body is loaded
 */
function init() {
    var clones,
        baseLayerPosition,
        openerLayers = [],
        openerMap = new Object();
    
    //activate JQuery UI actions for this element     
    $('#printmap').draggable().resizable();    
    
    //from window opener, take information to clone map, layers and generate graphic elements as legend, north arrow or text     
    if (window.opener) {        

        // get original map and layers from mapfishapp viewer
        openerMap = window.opener.GeoExt.MapPanel.guess().map; 
        openerLayers = openerMap.layers;
        
        // clones layer and get informations to call legend
        clones = [];
        baseLayerPosition = "";        
        openerLayers.forEach(function(layer, index) {            
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
            scales: openerMap.scales
        });
        // create cotrols and add to map
        var scaleCtr = new OpenLayers.Control.ScaleLine();        
        var navCtr = new OpenLayers.Control.Navigation();
        printMap.setOptions({
            controls: [scaleCtr, navCtr]
        });
        // add clones layers
        printMap.addLayers(clones);
        // set base layer
        if (baseLayerPosition !== "") {
            printMap.setBaseLayer(printMap.layers[0]);
        }
        // zoom to original extend
        printMap.setCenter(new OpenLayers.LonLat(openerMap.center.lon, openerMap.center.lat), openerMap.getZoom());
        
        // add information
        var parcelleId = window.opener.GEOR.Addons.Cadastre.UF.parcelleId;
        getUFInformation(parcelleId);
    }
}

/**
 * Download file or image to computer without server
 * @param {String} filename - Text input by user from prompt window
 * @param {String} text - Text that will be insert in file if user download json
 * @param {String} dataURL - Data URI containing a representation of the image in 96 dpi resolution if user download page in PNG
 */
function download(filename, text, dataURL) {
    // create temporary element to trigger download
    var link = document.createElement('a');
    if (text) {
        link.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
    } else {
        link.href = dataURL;
    }
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    link = null;
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
/**
 * Create canvas and download canvas as image
 * @param {e} - not use
 */
function getCanvas(e) {
    // create function to trigger download
    function saveAsFile() {
        var dateStr = formatDate();
        var fileName = prompt("Entrez un nom de fichier:", "Fiche_unite_fonciere" + dateStr + ".png");
        if (fileName !== null && fileName !== "") {
            if (fileName.indexOf(".png") < 0) {
                fileName = fileName + ".png";
            }
            // change style rules to do not display resizable handler
            var l = [printMap.div];
            l.forEach(function(el) {
                handlerVisibility(el, false);
            });
            // fire create canvas by html2canvas lib - return only mainPage as canvas            
            html2canvas(document.getElementById("mainPage"), {
                proxy: window.opener.GEOR.Addons.Cadastre.cadastrappWebappUrl+"/canvaProxy/",
                onrendered: function(canvas) {
                    var dataURL = canvas.toDataURL("image/png");
                    download(fileName, false, dataURL);
                    // change style rules to do display resizable handler
                    l.forEach(function(el) {
                        handlerVisibility(el, true);
                    });
                }
            });
        }
    }
    // fire download
    saveAsFile();
}

/**
 * 
 * @param parcelleId
 * @returns
 */
function getUFInformation(parcelleId){
    
    // get global information
    $.getJSON( window.opener.GEOR.Addons.Cadastre.cadastrappWebappUrl+"getInfoUniteFonciere?parcelle=" +  encodeURIComponent(parcelleId), function(data) {
        
        // get owner name
        $.getJSON( window.opener.GEOR.Addons.Cadastre.cadastrappWebappUrl+"getProprietaire?details=2&comptecommunal=" +  encodeURIComponent(data.comptecommunal), function(prop) {
            var propName = propName=prop[0].app_nom_usage;
           
            document.getElementById('informationgenerale').innerHTML =
            "<div class=\"datauf\"><span class=\"dataufLabel\">Propriétaire : </span>"+propName+"</div>"+
            "<div class=\"datauf\"><span class=\"dataufLabel\"></span>"+data.comptecommunal+"</div>"+
            "<div class=\"datauf\"><span class=\"dataufLabel\">Surface DGFIP de l'UF : </span>"+data.dcntpa_sum.toLocaleString()+" m²</div>"+
            "<div class=\"datauf\"><span class=\"dataufLabel\">Surface calculée : </span>"+data.sigcal_sum.toLocaleString()+" m²</div>"+
            "<div class=\"datauf\"><span class=\"dataufLabel\">Surface bâtie calculée : </span>"+data.sigcalb_sum.toLocaleString()+" m²</div>";
        });

        getParcellesInformation(data.uf);
      }); 
}

/**
 * 
 * @param ufId
 * @returns
 */
function getParcellesInformation(ufId){
    
    $.getJSON( window.opener.GEOR.Addons.Cadastre.cadastrappWebappUrl+"getParcelle?unitefonciere=" + encodeURIComponent(ufId), function( data ) {
        
        var parcelles="";
        var sommeSurf=0;
        
        data.forEach(function(element){
            parcelles=parcelles+ "<div class=\"data\"><span class=\"dataLabel\">"+element.parcelle+"</span>"+element.surfc.toLocaleString()+" m²</div>";
            sommeSurf=sommeSurf+element.surfc;
        });
         
        var content = "<div class=\"info\"><b>Cette unité foncière est composée de "+data.length+" parcelles.</b></div>"+
        "<div class=\"info\">La somme des surfaces DGFiP est égale à "+ sommeSurf.toLocaleString() +" m².</div>";
        
        document.getElementById('composition').innerHTML=content;
        document.getElementById('parcelles').innerHTML=parcelles;
    
      }); 
}

