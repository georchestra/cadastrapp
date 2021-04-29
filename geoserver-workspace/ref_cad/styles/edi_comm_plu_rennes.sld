<?xml version="1.0" encoding="UTF-8"?>
<!--
  nom du SLD : edi_comm_plu_rennes
  
  couche source dans la base :  cadastre.edi_comm
  layer cible du style       :  ref_cad:edi_comm_plg
  
  objet :
  Style relatif aux contour des communes pour le PLU de de Rennes.

  Historique des versions :
  date        |  auteur              |  description
  05/02/2019  |  S GELIN             |  version initiale
  
-->
<StyledLayerDescriptor version="1.1.0" 
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
                       xmlns="http://www.opengis.net/sld" 
                       xmlns:ogc="http://www.opengis.net/ogc" 
                       xmlns:se="http://www.opengis.net/se" 
                       xmlns:xlink="http://www.w3.org/1999/xlink" 
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <se:Name>edi_comm_plu_rennes</se:Name>
    <UserStyle>
      <se:Name>Contour des communes du cadastre de Rennes Métropole</se:Name>
      <se:FeatureTypeStyle>
      
        <se:Rule>
          <se:Name>Contour des communes de Rennes Métropole</se:Name>
          <se:MinScaleDenominator>0</se:MinScaleDenominator>
          <se:MaxScaleDenominator>100000</se:MaxScaleDenominator>

          <se:PolygonSymbolizer>   
            <se:Stroke>
              <se:SvgParameter name="stroke">#ffffff</se:SvgParameter>
              <se:SvgParameter name="stroke-width">4</se:SvgParameter>   
            </se:Stroke>
          </se:PolygonSymbolizer> 

          <se:PolygonSymbolizer>         
            <se:Stroke>
              <se:SvgParameter name="stroke">#000000</se:SvgParameter>
              <se:SvgParameter name="stroke-width">4</se:SvgParameter>
              <se:SvgParameter name="stroke-dasharray">30 5 5 5</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>

        </se:Rule>

      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>