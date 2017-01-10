<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns:sld="http://www.opengis.net/sld" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
    <sld:NamedLayer>
        <sld:Name>PARCELLE</sld:Name>
        <sld:UserStyle>
            <sld:FeatureTypeStyle>
                <sld:Rule>
                    <sld:Name>Parcelle &lt; 1:10000</sld:Name>
                    <sld:MinScaleDenominator>1</sld:MinScaleDenominator>
                    <sld:MaxScaleDenominator>10000</sld:MaxScaleDenominator>
                    <sld:PolygonSymbolizer>
                        <sld:Stroke>
                            <sld:CssParameter name="stroke">#A50000</sld:CssParameter>
                            <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
                        </sld:Stroke>
                    </sld:PolygonSymbolizer>
                </sld:Rule>
                <sld:Rule>
                    <sld:Name>Etiquette</sld:Name>
                    <sld:Title>Etiquette</sld:Title>
                    <sld:MinScaleDenominator>1</sld:MinScaleDenominator>
                    <sld:MaxScaleDenominator>1100</sld:MaxScaleDenominator>
                    <sld:TextSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
                        <sld:Geometry>
                            <ogc:Function name="centroid">
                                <ogc:PropertyName>geom</ogc:PropertyName>
                            </ogc:Function>
                        </sld:Geometry>
                        <sld:Label>
                            <ogc:PropertyName xmlns:ogc="http://www.opengis.net/ogc">tex</ogc:PropertyName>
                        </sld:Label>
                        <sld:Font>
                            <sld:CssParameter name="font-family">Times New Roman</sld:CssParameter>
                            <sld:CssParameter name="font-style">italic</sld:CssParameter>
                            <sld:CssParameter name="font-size">0</sld:CssParameter>
                        </sld:Font>
                        <sld:Halo>
                            <sld:Radius>0.1</sld:Radius>
                            <sld:Fill>
                                <sld:CssParameter name="fill">#FFFFFF</sld:CssParameter>
                            </sld:Fill>
                        </sld:Halo>
                        <sld:Fill>
                            <sld:CssParameter name="fill">#A50000</sld:CssParameter>
                        </sld:Fill>
                    </sld:TextSymbolizer>
                </sld:Rule>
            </sld:FeatureTypeStyle>
        </sld:UserStyle>
    </sld:NamedLayer>
</sld:StyledLayerDescriptor>