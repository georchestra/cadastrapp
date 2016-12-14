<?xml version="1.0" encoding="ISO-8859-1"?>
<StyledLayerDescriptor version="1.0.0" 
 xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
 xmlns="http://www.opengis.net/sld" 
 xmlns:ogc="http://www.opengis.net/ogc" 
 xmlns:xlink="http://www.w3.org/1999/xlink" 
 xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
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
            <Name>Section ME</Name>
            <Title>Section entre 1:4000 et 1:100000</Title>
            <MinScaleDenominator>4000</MinScaleDenominator>
            <MaxScaleDenominator>100000</MaxScaleDenominator>
            <PolygonSymbolizer>
                <Stroke>
                    <CssParameter name="stroke">#993301</CssParameter>
                    <CssParameter name="stroke-width">1</CssParameter>
                </Stroke>
            </PolygonSymbolizer>
        </Rule>
        <Rule>
            <Name>Section PE</Name>
            <Title>Section entre 1:10000 et 1:30000</Title>
            <MinScaleDenominator>100000</MinScaleDenominator>
            <MaxScaleDenominator>300000</MaxScaleDenominator>
            <PolygonSymbolizer>
                <Stroke>
                    <CssParameter name="stroke">#993301</CssParameter>
                    <CssParameter name="stroke-width">0.2</CssParameter>
                </Stroke>
            </PolygonSymbolizer>
        </Rule>
    </FeatureTypeStyle>
    <FeatureTypeStyle>
        <Name>TEXT_LABEL</Name>
        <Rule>
            <MinScaleDenominator>4000</MinScaleDenominator>
            <MaxScaleDenominator>50000</MaxScaleDenominator>
            <TextSymbolizer>
                <Geometry>
                    <ogc:Function name="centroid">
                        <ogc:PropertyName>geom</ogc:PropertyName>
                    </ogc:Function>
                </Geometry>
                <Label>
                    <ogc:PropertyName>tex</ogc:PropertyName>
                </Label>
                <Font>
                    <CssParameter name="font-family">Times New Roman</CssParameter>
                    <CssParameter name="font-size">16</CssParameter>
                    <CssParameter name="font-style">italic</CssParameter>
                    <CssParameter name="font-weight">bold</CssParameter>
                </Font>
                <LabelPlacement>
                    <PointPlacement>
                        <AnchorPoint>
                            <AnchorPointX>
                                <ogc:Literal>0</ogc:Literal>
                            </AnchorPointX>
                            <AnchorPointY>
                                <ogc:Literal>0</ogc:Literal>
                            </AnchorPointY>
                        </AnchorPoint>
                        <Displacement>
                            <DisplacementX>
                                <ogc:Literal>0.0</ogc:Literal>
                            </DisplacementX>
                            <DisplacementY>
                                <ogc:Literal>0.0</ogc:Literal>
                            </DisplacementY>
                        </Displacement>
                        <Rotation>
                            <ogc:Literal>0</ogc:Literal>
                        </Rotation>
                    </PointPlacement>
                </LabelPlacement>
                <Halo>
                    <Radius>
                        <ogc:Literal>2</ogc:Literal>
                    </Radius>
                    <Fill>
                        <CssParameter name="fill">#FFFFFF</CssParameter>
                        <CssParameter name="fill-opacity">0.5</CssParameter>
                    </Fill>
                </Halo>
                <Fill>
                    <CssParameter name="fill">#000000</CssParameter>
                </Fill>
                <VendorOption name="autoWrap">60</VendorOption>
                <VendorOption name="maxDisplacement">150</VendorOption>
            </TextSymbolizer>
        </Rule>
    </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>