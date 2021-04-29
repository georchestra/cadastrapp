<?xml version="1.0" encoding="UTF-8"?>
<!--
  nom du SLD : voie_priv_plg
  
  auteur :   Arnaud LECLERE
  date :     20/08/2014
  
  couche principale cible du style :  bdu.cad_arcopole:voie_priv
  
  objet : Style relatif aux parcelles privées des communes de Rennes Métropole.
  
-->
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
            <Name>Parcelles privées des communes de Rennes Métropole</Name>
            <UserStyle xmlns="http://www.opengis.net/sld">
                <FeatureTypeStyle>
                    <Rule>
                      
                      <PolygonSymbolizer>
                             <Fill>
                                   <CssParameter name="fill">#dcdcdc</CssParameter>
                             </Fill>
                      </PolygonSymbolizer>
                      
                      <PolygonSymbolizer>
                         <Fill>
                           <GraphicFill>
                              
                             <Graphic>
                                   <Mark>
                                       <WellKnownName>shape://vertline</WellKnownName>
                                     <Stroke>
                                       <CssParameter name="stroke">#969696</CssParameter>
                                       <CssParameter name="stroke-width">0.5</CssParameter>
                                    
                                    </Stroke>
                                    </Mark>
                                 <Size>20</Size>
                               <Rotation>                                    
                                            <ogc:Literal>45</ogc:Literal>
                               </Rotation>
                             </Graphic>
                             
                           </GraphicFill>
                         </Fill>
                      </PolygonSymbolizer>
                                           
                    </Rule>
                </FeatureTypeStyle>
            </UserStyle>
        </NamedLayer>
    </StyledLayerDescriptor>