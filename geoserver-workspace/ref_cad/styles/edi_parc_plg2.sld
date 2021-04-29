<?xml version="1.0" encoding="UTF-8"?>
<!--
  
  nom du SLD : edi_parc_plg2
  
  auteur :   Arnaud LECLERE
  date :     17/09/2014
  
  couche principale cible du style :  bdu.cad_arcopole:edi_parc
  
  objet :  Style relatif aux bordures des parcelles du cadastre de Rennes Métropole (pour cacher les subdivisions fiscales).
 
  
-->
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
      
  <NamedLayer>
    <Name>edi_parc_plg</Name>
    <UserStyle>
      <Title>Bordures des parcelles du cadastre de Rennes Métropole (pour cacher les subdivisions fiscales)</Title>
      <FeatureTypeStyle>
        
        <Rule>
          <Name>Grand</Name>
          
          <!--<MinScaleDenominator>500</MinScaleDenominator>-->
          <MaxScaleDenominator>5000</MaxScaleDenominator>
          
          <PolygonSymbolizer>
                                       
                <Stroke>
                    <CssParameter name="stroke">#666666</CssParameter>
                    <CssParameter name="stroke-width">0.2</CssParameter>
                </Stroke>
            
          </PolygonSymbolizer>

        </Rule>
      
        <Rule>
          <Name>Petit</Name>
          
          <MinScaleDenominator>5000</MinScaleDenominator>
          <MaxScaleDenominator>40000</MaxScaleDenominator>
          
          <PolygonSymbolizer>
      
              <Stroke>
                  <CssParameter name="stroke">#666666</CssParameter>
                  <CssParameter name="stroke-width">0.2</CssParameter>
              </Stroke>
            
          </PolygonSymbolizer>
        </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>