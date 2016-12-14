<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns:sld="http://www.opengis.net/sld" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
    <sld:NamedLayer>
        <sld:Name>test_workspace_sc:CG67_PCI_geo_commune</sld:Name>
        <sld:UserStyle>
            <sld:FeatureTypeStyle>
                <sld:Rule>
                    <sld:Name>geo_commune_GE</sld:Name>
                    <sld:Title>Commune GE</sld:Title>
                    <sld:MinScaleDenominator>1</sld:MinScaleDenominator>
                    <sld:MaxScaleDenominator>10000</sld:MaxScaleDenominator>
                    <sld:PolygonSymbolizer>
                        <sld:Stroke>
                            <sld:CssParameter name="stroke">#ffaa00</sld:CssParameter>
                            <sld:CssParameter name="stroke-width">3</sld:CssParameter>
                        </sld:Stroke>
                    </sld:PolygonSymbolizer>
                </sld:Rule>
                <sld:Rule>
                    <sld:Name>geo_commune_PE</sld:Name>
                    <sld:Title>Commune PE</sld:Title>
                    <sld:MinScaleDenominator>10000</sld:MinScaleDenominator>
                    <sld:MaxScaleDenominator>10000000</sld:MaxScaleDenominator>
                    <sld:PolygonSymbolizer>
                        <sld:Stroke>
                            <sld:CssParameter name="stroke">#ffaa00</sld:CssParameter>
                            <sld:CssParameter name="stroke-width">2.5</sld:CssParameter>
                        </sld:Stroke>
                    </sld:PolygonSymbolizer>
                </sld:Rule>
                <sld:Rule>
                    <sld:MinScaleDenominator>4000</sld:MinScaleDenominator>
                    <sld:MaxScaleDenominator>300000</sld:MaxScaleDenominator>
                    <sld:TextSymbolizer>
                        <sld:Geometry>
                            <ogc:Function name="centroid">
                                <ogc:PropertyName>geom</ogc:PropertyName>
                            </ogc:Function>
                        </sld:Geometry>
                        <sld:Label>
                            <ogc:PropertyName xmlns:ogc="http://www.opengis.net/ogc">tex2</ogc:PropertyName>
                        </sld:Label>
                        <sld:Font>
                            <sld:CssParameter name="font-family">Times New Roman</sld:CssParameter>
                            <sld:CssParameter name="font-size">14</sld:CssParameter>
                            <CssParameter name="font-weight">bold</CssParameter>
                            <sld:CssParameter name="font-style">italic</sld:CssParameter>
                        </sld:Font>
                        <sld:Halo>
                            <sld:Radius>1</sld:Radius>
                            <sld:Fill>
                                <sld:CssParameter name="fill">#FFFFFF</sld:CssParameter>
                            </sld:Fill>
                        </sld:Halo>
                        <sld:Fill>
                            <sld:CssParameter name="fill">#484848</sld:CssParameter>
                        </sld:Fill>
                        <sld:VendorOption name="conflictResolution">true</sld:VendorOption>
                        <sld:VendorOption name="goodnessOfFit">0</sld:VendorOption>
                        <sld:VendorOption name="autoWrap">60</sld:VendorOption>
                    </sld:TextSymbolizer>
                </sld:Rule>
            </sld:FeatureTypeStyle>
        </sld:UserStyle>
    </sld:NamedLayer>
</sld:StyledLayerDescriptor>