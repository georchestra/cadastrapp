<?xml version="1.0" encoding="UTF-8"?>
<!--
  
  nom du SLD : edi_ptdiv
  
  auteur :   Arnaud LECLERE
  date :     11/09/2014
  
  couche principale cible du style :  bdu.cad_arcopole:edi_ptdiv

  objet : Style relatif aux symboles de mitoyenneté et points divers du cadastre des communes de Rennes Métropole (point) - millésime mars 2014

-->

<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
<NamedLayer>
    <Name>Symboles de mitoyenneté et points divers du cadastre de Rennes Métropole</Name>
    <UserStyle>
      
      <FeatureTypeStyle>
        
<Rule>        
     
                <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                        <ogc:PropertyIsEqualTo>
                              <ogc:PropertyName>symbol</ogc:PropertyName>
                              <ogc:Literal>46</ogc:Literal>
                        </ogc:PropertyIsEqualTo>
                </ogc:Filter>
             
                <!-- Affichage des numéros (sans extension et sans batiment) <![CDATA[ ]]> -->
            <TextSymbolizer>
                <Label><![CDATA[ ]]> </Label> 
                <LabelPlacement>
                     <PointPlacement>
                           <AnchorPoint>
                                     <AnchorPointX>0</AnchorPointX>
                                     <AnchorPointY>0</AnchorPointY>
                           </AnchorPoint>

                           <Rotation>
                                <ogc:Sub>
                                  <ogc:Literal>180</ogc:Literal>
                                  <ogc:PropertyName>angle</ogc:PropertyName>
                                </ogc:Sub>
                          </Rotation>
                     </PointPlacement>
                </LabelPlacement>

                <Graphic>
                       <Mark>
                           <WellKnownName>ttf://Equipements_PVI#168</WellKnownName>
                           <Fill>
                               <CssParameter name="fill">#6e6e6e</CssParameter>
                           </Fill>
                       </Mark>
                </Graphic> 
                                 
                  
        </TextSymbolizer>        

 
<!--                      <PointSymbolizer>
                        <Graphic>
                            <Mark>
                                <WellKnownName>ttf://Equipements_PVI#168</WellKnownName>

                                <Fill>
                                    <CssParameter name="fill">
                                        <ogc:Literal>#6e6e6e</ogc:Literal>
                                    </CssParameter>
                                </Fill>
                            </Mark>
                            <Size>14</Size>
                            <Rotation>
                                      <ogc:PropertyName>angle</ogc:PropertyName>
                                      <ogc:Literal>180</ogc:Literal>
                            </Rotation>
                        </Graphic>
                    </PointSymbolizer>
-->
        </Rule>
  

        
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>