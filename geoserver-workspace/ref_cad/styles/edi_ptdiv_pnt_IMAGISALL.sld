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
       <!-- Filtre sur les VRAIS Calvaires -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:And>
                                <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>12</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           
                                <ogc:PropertyIsLike  wildCard="*" singleChar="." escape="!">
                                    <ogc:PropertyName>texte</ogc:PropertyName>
                                    <ogc:Literal>Calvaire*</ogc:Literal>
                             </ogc:PropertyIsLike>
                              </ogc:And>  
                             </ogc:Filter>
                                                  
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation du symbole calvaire -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0058'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>8</Size>
                             </Graphic>
                           </PointSymbolizer>        
</Rule>        
<Rule>        
       <!-- Filtre sur les PUITS renseignes en Calvaires -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:And>
                                <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>12</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                          
                                <ogc:PropertyIsLike  wildCard="*" singleChar="." escape="!">
                                    <ogc:PropertyName>texte</ogc:PropertyName>
                                    <ogc:Literal>Puits</ogc:Literal>
                             </ogc:PropertyIsLike>
                              </ogc:And>  
                             </ogc:Filter>
                                                 
 
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation du symbole calvaire -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+00FA'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>12</Size>
                             </Graphic>
                           </PointSymbolizer>        
</Rule>  
<Rule>        
       <!-- Filtre sur les flèche de cours d'eau -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>30</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation du symbole flèche de cours d'eau -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0076'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>33</Size>
                                 
       <!-- rotation selon champ angle -->                               
                         <!--       <Rotation>
                                      <ogc:Sub>
                                          <ogc:Literal>270</ogc:Literal>
                                          <ogc:PropertyName>angle</ogc:PropertyName>
                                      </ogc:Sub>
                                   </Rotation>
                           -->
                             </Graphic>                        
                           </PointSymbolizer>        
</Rule>  

<Rule>        
       <!-- Filtre sur les fossés mitoyens -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>41</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation des fossés mitoyens -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0067'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>8</Size>
                                 
       <!-- rotation selon champ angle -->                               
                           <!--
                                 <Rotation>
                                      <ogc:Sub>
                                          <ogc:Literal>360</ogc:Literal>
                                          <ogc:PropertyName>angle</ogc:PropertyName>
                                      </ogc:Sub>
                                   </Rotation>
                            -->
                             </Graphic>                        
                           </PointSymbolizer>        
</Rule>
<Rule>        
       <!-- Filtre sur les fossés non mitoyens -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>42</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation des fossés non mitoyens -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0065'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>8</Size>
                                 
       <!-- rotation selon champ angle -->                               
                          <!--
                                 <Rotation>
                                      <ogc:Sub>
                                          <ogc:Literal>360</ogc:Literal>
                                          <ogc:PropertyName>angle</ogc:PropertyName>
                                      </ogc:Sub>
                                   </Rotation>
                             -->
                             </Graphic>                        
                           </PointSymbolizer>        
</Rule>  
<Rule>        
       <!-- Filtre sur les haies mitoyennes -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>45</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation des haies mitoyennes -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0056'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>8</Size>
                                 
       <!-- rotation selon champ angle -->                               
                             <!--
                                 <Rotation>
                                      <ogc:Sub>
                                          <ogc:Literal>360</ogc:Literal>
                                          <ogc:PropertyName>angle</ogc:PropertyName>
                                      </ogc:Sub>
                                   </Rotation>
                               --> 
                             </Graphic>                        
                           </PointSymbolizer>        
</Rule>
<Rule>        
  <!-- Filtre sur les haies non mitoyennes -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>46</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation des haies non mitoyennes -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0052'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>12</Size>
                                  <!-- rotation selon champ angle -->                              
                            <!--     
                                 <Rotation>
                                      <ogc:Sub>
                                          <ogc:Literal>-90</ogc:Literal>
                                          <ogc:PropertyName>angle</ogc:PropertyName>
                                      </ogc:Sub>
                                   </Rotation>
                             -->
                             </Graphic>                        
                           </PointSymbolizer>        
</Rule>     
  
  
  
  
<Rule>        
       <!-- Filtre sur les murs mitoyens -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>39</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation des murs mitoyens -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0054'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>8</Size>
                                 
       <!-- rotation selon champ angle -->                               
                             <!--   
                                 <Rotation>
                                      <ogc:Sub>  
                                          <ogc:Literal>270</ogc:Literal>
                                          <ogc:PropertyName>angle</ogc:PropertyName>
                                      </ogc:Sub>
                                   </Rotation>
                               -->          
                             </Graphic>                        
                           </PointSymbolizer>        
</Rule>
<Rule>        
       <!-- Filtre sur les murs non mitoyens -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>40</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation des murs non mitoyens -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0050'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>8</Size>
                                 
       <!-- rotation selon champ angle -->                               
                   <!--              <Rotation>
                                      <ogc:Sub>
                                          <ogc:Literal>270</ogc:Literal>
                                         <ogc:PropertyName>angle</ogc:PropertyName>
                                      </ogc:Sub>
                                   </Rotation>
                      -->
                             </Graphic>                        
                           </PointSymbolizer>        
</Rule>  

<Rule>        
       <!-- Filtre sur les Pylones -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>50</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation du symbole pylones -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0059'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>8</Size>
                             </Graphic>
                           </PointSymbolizer>        
</Rule>
<Rule>        
       <!-- Filtre sur les Puits -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>63</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation du symbole puits -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+00FA'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>12</Size>        
                                </Graphic>                        
                           </PointSymbolizer>  
  
</Rule>
<Rule>        
       <!-- Filtre sur les clotures mitoyennes -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>43</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation des clotures mitoyennes -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0057'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>9</Size>
                                 
       <!-- rotation selon champ angle -->                               
                       <!--          <Rotation>
                                      <ogc:Sub>
                                          <ogc:Literal>270</ogc:Literal>
                                          <ogc:PropertyName>angle</ogc:PropertyName>
                                      </ogc:Sub>
                                   </Rotation>
                        -->
                             </Graphic>                        
                           </PointSymbolizer>        
</Rule>
<Rule>        
       <!-- Filtre sur les clotures non mitoyennes -->          

                           <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                              <ogc:PropertyIsEqualTo>
                                  <ogc:PropertyName>symbol</ogc:PropertyName>
                                  <ogc:Literal>44</ogc:Literal>
                              </ogc:PropertyIsEqualTo>
                           </ogc:Filter>
  
       <!-- Echelle d'affichage -->
                            <MinScaleDenominator>500</MinScaleDenominator>
                            <MaxScaleDenominator>1500</MaxScaleDenominator>
     
       <!-- représentation des clotures non mitoyennes -->      
                           <PointSymbolizer>
                               <Graphic>
                                 <Mark>
                                   <WellKnownName>ttf://ImagisALL#${'U+0053'}</WellKnownName>
                                     <Fill>
                                       <CssParameter name="fill">#6e6e6e</CssParameter>
                                     </Fill>                    
                                 </Mark>
                                 <Size>9</Size>
                                 
       <!-- rotation selon champ angle -->                               
                                 <Rotation>
                                      <ogc:Add>
                                        <ogc:PropertyName>angle</ogc:PropertyName>
                                        <ogc:Literal>270</ogc:Literal>
                                      </ogc:Add>
                                   </Rotation>
                                 </Graphic>                        
                           </PointSymbolizer>      
</Rule>  


      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>