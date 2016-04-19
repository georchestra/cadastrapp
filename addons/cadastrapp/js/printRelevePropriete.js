Ext.namespace("GEOR.Addons.Cadastre");

/**
 * @param compteComunal (could be a list of id user or only one
 */
GEOR.Addons.Cadastre.onClickPrintRelevePropriete = function(compteCommunal) {

    // PARAMS
    var params = {
            compteCommunal : compteCommunal
    }
    var url = GEOR.Addons.Cadastre.cadastrappWebappUrl + 'createRelevePropriete?' + Ext.urlEncode(params);

    Ext.DomHelper.useDom = true;
    
    // Directly download file, without and call service without ogcproxy
    Ext.DomHelper.append(document.body, {
        tag : 'iframe',
        id : 'downloadIframe',
        frameBorder : 0,
        width : 0,
        height : 0,     
        css : 'display:none;visibility:hidden;height:0px;',
        src : url
    });

}
