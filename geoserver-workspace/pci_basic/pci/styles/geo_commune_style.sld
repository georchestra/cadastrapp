<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
 xmlns="http://www.opengis.net/sld" 
 xmlns:ogc="http://www.opengis.net/ogc" 
 xmlns:xlink="http://www.w3.org/1999/xlink" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <!-- a Named Layer is the basic building block of an SLD document -->
  <NamedLayer>
    <Name>geo_commune</Name>
    <UserStyle>
    <!-- Styles can have names, titles and abstracts -->
      <Title>geo_commune</Title>
      <Abstract>Rendu des communes du PCI vecteur</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- A FeatureTypeStyle for rendering polygons -->
      <FeatureTypeStyle>
        <Rule>
          <Name>rule1</Name>
          <Title>Commune</Title>
          <Abstract>Commune du PCI vecteur</Abstract>
          <PolygonSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#ff6501</CssParameter>
              <CssParameter name="stroke-width">3</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
        <Rule>
          <Name>rule2</Name>
          <Title>Nom de commune entre 1:20000 et 1:50000</Title>
          <MinScaleDenominator>20000</MinScaleDenominator>
          <MaxScaleDenominator>50000</MaxScaleDenominator>
          <TextSymbolizer>
             <Label>
               <ogc:PropertyName>tex2</ogc:PropertyName>
             </Label>
             <LabelPlacement>
               <PointPlacement>
                 <AnchorPoint>
                   <AnchorPointX>0.5</AnchorPointX>
                   <AnchorPointY>0.5</AnchorPointY>
                 </AnchorPoint>
               </PointPlacement>
             </LabelPlacement>
            <Font>
               <CssParameter name="font-family">Arial</CssParameter>
               <CssParameter name="font-size">12</CssParameter>
               <CssParameter name="font-style">normal</CssParameter>
             </Font>
             <VendorOption name="autoWrap">60</VendorOption>
           </TextSymbolizer>
         </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>