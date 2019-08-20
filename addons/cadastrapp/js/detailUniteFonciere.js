Ext.namespace("GEOR.Addons.Cadastre");

/**
 * public: method[onClickdisplayFIUF]
 * 
 * @param parcelleId Identifiant de parcelle ex :8301032610C0012
 * 
 * 
 *  Cette methode  selection une uf sur la carte en fonction d'un id de parcelle
 *  puis récupère l'ensemble des valeurs nécessaire pour 
 *  créer la fichie d'unité foncière dans une nouvelle page html
 *
 */
GEOR.Addons.Cadastre.onClickDisplayFIUF = function(parcelleId) {

    // Stock parcelle Id
    GEOR.Addons.Cadastre.UF.parcelleId=parcelleId;
	
	// Open new window
	window.open("ws/addons/cadastrapp/html/ficheUniteFonciere.html");

};
;
