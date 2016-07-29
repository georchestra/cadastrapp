<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
 xmlns="http://www.opengis.net/sld" 
 xmlns:ogc="http://www.opengis.net/ogc" 
 xmlns:xlink="http://www.w3.org/1999/xlink" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <!-- a Named Layer is the basic building block of an SLD document -->
  <NamedLayer>
    <Name>geo_section</Name>
    <UserStyle>
    <!-- Styles can have names, titles and abstracts -->
      <Title>geo_section</Title>
      <Abstract>Rendu des sections du PCI vecteur</Abstract>
      <!-- FeatureTypeStyles describe how to render different features -->
      <!-- A FeatureTypeStyle for rendering polygons -->
      <FeatureTypeStyle>
        <Rule>
          <Name>rule1</Name>
          <Title>Section &lt; 1:70000</Title>
          <Abstract>Section du PCI vecteur</Abstract>
          <MaxScaleDenominator>70000</MaxScaleDenominator>
          <PolygonSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#993301</CssParameter>
              <CssParameter name="stroke-width">2</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
        <Rule>
          <Name>rule2</Name>
          <Title>Identifiant de section entre 1:5000 et 1:20000</Title>
          <MinScaleDenominator>5000</MinScaleDenominator>
          <MaxScaleDenominator>20000</MaxScaleDenominator>
          <TextSymbolizer>
             <Label>
               <ogc:PropertyName>tex</ogc:PropertyName>
             </Label>
            <Font>
               <CssParameter name="font-family">Arial</CssParameter>
               <CssParameter name="font-size">11</CssParameter>
               <CssParameter name="font-style">normal</CssParameter>
             </Font>           
           </TextSymbolizer>
         </Rule>
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>