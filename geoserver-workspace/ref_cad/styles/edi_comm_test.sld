<?xml version="1.0" encoding="UTF-8"?>
<!--
  
  nom du SLD : edi_comm_plg
  
  auteur :   Arnaud LECLERE
  date :     19/08/2014
  
  couche principale cible du style :  bdu.cad_arcopole:edi_comm
  
  objet : Style relatif aux 43 limites communales du cadastre de Rennes Métropole.
  Il reprend la représentation/style de réseau carto.
  
-->
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>vgs_edi_comm_plg</Name>
    <UserStyle>
      <Name>Limites communales du cadastre de Rennes Métropole </Name>
      <FeatureTypeStyle>
        
               
        <Rule>
          <Name>ACIGNE</Name>

            <ogc:Filter>
                  <ogc:PropertyIsEqualTo>
                      <ogc:PropertyName>nomcom</ogc:PropertyName>
                      <ogc:Literal>ACIGNE</ogc:Literal>
                  </ogc:PropertyIsEqualTo>
            </ogc:Filter> 
    

                   
                  <!-- représentation de la commune -->
           <PolygonSymbolizer>
                <Fill>
                      <CssParameter name="fill">#ccccff</CssParameter>
                </Fill>
                <Stroke>
                      <CssParameter name="stroke">#ffaa00</CssParameter>
                      <CssParameter name="stroke-width">0.46</CssParameter>
                </Stroke>
           </PolygonSymbolizer>  
          
<!--                    affichage nom de la commune 
           <TextSymbolizer>
                <Label>
                     <ogc:PropertyName>nomcom</ogc:PropertyName>
                </Label>        
           <Font>
                    <CssParameter name="font-family">DejaVu Sans</CssParameter>
                    <CssParameter name="font-size">11</CssParameter>
                    <CssParameter name="font-style">normal</CssParameter>
                    <CssParameter name="font-weight">bold</CssParameter>
           </Font>
          <LabelPlacement>
                   <PointPlacement>
                       <AnchorPoint>
                           <AnchorPointX>0.5</AnchorPointX>
                           <AnchorPointY>0.5</AnchorPointY>
                       </AnchorPoint>
                     </PointPlacement>
          </LabelPlacement>
             
             <Halo>
                   <Radius>1</Radius>
                   <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                   </Fill>
            </Halo>
          </TextSymbolizer>  
      -->      </Rule>

     
        
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>