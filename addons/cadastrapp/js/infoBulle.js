Ext.namespace("GEOR.Addons.Cadastre");

/**
 *  Display an infobulle when waiting on a parcelle
 *  
 * @param idParcelle parameter will be use to call webapp and get additional information
 * @param lonlat position where popup should be displayed
 */    
GEOR.Addons.Cadastre.displayInfoBulle = function(idParcelle, lonlat) {
    
    if(GEOR.Addons.Cadastre.popup){
        GEOR.Addons.Cadastre.popup.close();
    }

    // Build url depending on check button Foncier
    urlInfoBulleService =  GEOR.Addons.Cadastre.url.serviceInfoBulle + '?parcelle=' + idParcelle
    
    if (!GEOR.Addons.Cadastre.UF.isfoncier){
        urlInfoBulleService += "&infouf=0";
    }
    // webapp request using parcelleid
    Ext.Ajax.request({
        url : urlInfoBulleService,
        failure : function() {
            alert("Erreur lors de la requete 'getInfoBulle' ");
        },
        method : "GET",
        success : function(response, opts) {
          
            // result from requestion JSON
            var result = Ext.decode(response.responseText);
            
            html = "";
            if (typeof(result) != "undefined"){
                
                    var NBCHARPARCELLEARCOPOLE = 15;
                    var departement, codir, inseecom;
                                       
                    if( idParcelle.length > NBCHARPARCELLEARCOPOLE ){
                        departement = idParcelle.substr(4,2);
                        codir = idParcelle.substr(6,1);
                        inseecom = idParcelle.substr(7,3)
                    }else{
                        departement = idParcelle.substr(0,2);
                        codir = idParcelle.substr(2,1);
                        inseecom = idParcelle.substr(3,3);
                    }
            
                    html = "<div class=\"cadastrapp-infobulle-parcelle\">";
                    
                    html += "<table style=\"width:100%;\">";
                    html += "<thead><tr><th colspan=\"2\" style=\"text-align:center; font-weight: bold; text-transform: uppercase;\">Parcelle</th></tr></thead>";
                    html += "<tbody>";
					html += "<tr><td colspan=\"2\" style=\"text-align:center; font-style: italic;\">" + idParcelle + "</td></tr>";
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.commune') + " : </td><td>" + result.libcom + "</td></tr>";
                    if( idParcelle.length > NBCHARPARCELLEARCOPOLE ){
                        html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.annee') + " : </td><td>" + idParcelle.substr(0,4) + "</td></tr>";
                    }
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.departement') + " : </td><td>" + departement + "</td></tr>";
                    if( codir != "0" ){
                        html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.direction') + " : </td><td>" + codir + "</td></tr>";
                    }
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.inseecom') + " : </td><td>" + inseecom + "</td></tr>";
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.prefix') + " : </td><td>" + result.ccopre + "</td></tr>";
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.section') + " : </td><td>" + result.ccosec + "</td></tr>";
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.nplan') + " : </td><td>" + result.dnupla + "</td></tr>";
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.adresse') + " : </td><td>" + result.dnvoiri + " " + result.dindic + " " + result.cconvo + " " + result.dvoilib + "</td></tr>";
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.contenancedgfip') + " : </td><td>" + result.dcntpa.toLocaleString() + " m²</td></tr>";
                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.sig') + " : </td><td>" + result.surfc.toLocaleString() + " m²</td></tr>";
                    
                    if(GEOR.Addons.Cadastre.isCNIL1() || GEOR.Addons.Cadastre.isCNIL2()){
                        if (typeof(result.proprietaires) != "undefined"){
                            Ext.each(result.proprietaires, function(proprietaire, currentIndex){
                                if(currentIndex==8){
                                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.proprietaire') + " : </td><td>...</td></tr>";
                                }else{
                                    html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.proprietaire') + " : </td><td>" + proprietaire.app_nom_usage + "</td></tr>";
                                }                   
                            });
                        }
                    }
                    html += "</tbody>";
                    html += "</table>";
                    
                    html += "</div>";
				
                if (GEOR.Addons.Cadastre.UF.isfoncier){
					html += "<br/>";
                    html += "<div class=\"cadastrapp-infobulle-unite-fonciere\">";
					
					html += "<table style=\"width:100%;\">";
                    html += "<thead><tr><th colspan=\"2\" style=\"text-align:center; font-weight: bold; text-transform: uppercase;\">Unité Foncière</th></tr></thead>";
					html += "<tbody>";
					if(GEOR.Addons.Cadastre.isCNIL1() || GEOR.Addons.Cadastre.isCNIL2()){
					     html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.infobulle.ccomunal') + " : </td><td>" + result.comptecommunal + "</td></tr>";
					}
					html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.contenancedgfip') + " UF : </td><td>" + result.dcntpa_sum.toLocaleString() + " m²</td></tr>";
					html += "<tr><td class=\"infobulle-label\">" + OpenLayers.i18n('cadastrapp.sig') + " UF : </td><td>" + result.sigcal_sum.toLocaleString() + " m²</td></tr>";
					html += "</tbody>";
                    html += "</table>";
					
                    html += "</div>";
                }
    
               GEOR.Addons.Cadastre.popup = new GeoExt.Popup({
                    map:GeoExt.MapPanel.guess().map,
                    location: lonlat,
                    width: 300,
                    html: html,
                    listeners: {
                        close: function() {
                            // closing a popup destroys it, but our reference is truthy
                            GEOR.Addons.Cadastre.popup = null;
                        }
                    }
                });
                
               GEOR.Addons.Cadastre.popup.show();
                document.body.onmousemove = function(e) {
                    //destroy popup on move
                    if(GEOR.Addons.Cadastre.popup){
                        GEOR.Addons.Cadastre.popup.close();
                    }
                }
            }
        },
        failure : function(response, options){ 
            console.log("Infobulle : Error occured when trying to get information from server, please check server logs")
        }
    }); 
    
                
}

