<?xml version="1.0" encoding="UTF-8"?>
<sld:StyledLayerDescriptor xmlns:sld="http://www.opengis.net/sld" version="1.0.0" xsi:schemaLocation="http://www.opengis.net/sld http://schemas.opengis.net/sld/1.0.0/StyledLayerDescriptor.xsd" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
    <sld:NamedLayer>
        <sld:Name>pci:geo_label_3</sld:Name>
        <sld:UserStyle>
            <sld:FeatureTypeStyle>
                <sld:Rule>
                    <sld:Name>ParcellesEtiq</sld:Name>
                    <sld:Title>Parcelles étiquettes</sld:Title>
                    <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
                        <ogc:PropertyIsEqualTo>
                            <ogc:PropertyName>ogr_obj_lnk_layer</ogc:PropertyName>
                            <ogc:Literal>PARCELLE_id</ogc:Literal>
                        </ogc:PropertyIsEqualTo>
                    </ogc:Filter>
                    <sld:MinScaleDenominator>1</sld:MinScaleDenominator>
                    <sld:MaxScaleDenominator>1100</sld:MaxScaleDenominator>
                    <sld:TextSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
                        <sld:Label>
                            <ogc:PropertyName xmlns:ogc="http://www.opengis.net/ogc">ogr_atr_val</ogc:PropertyName>
                        </sld:Label>
                        <sld:Font>
                            <sld:CssParameter name="font-family">Times New Roman</sld:CssParameter>
                            <sld:CssParameter name="font-style">italic</sld:CssParameter>
                            <sld:CssParameter name="font-size">3</sld:CssParameter>
                        </sld:Font>
                        <sld:Halo>
                            <sld:Radius>0.2</sld:Radius>
                            <sld:Fill>
                                <sld:CssParameter name="fill">#FFFFFF</sld:CssParameter>
                            </sld:Fill>
                        </sld:Halo>
                        <sld:LabelPlacement>
                            <sld:PointPlacement>
                                <sld:Rotation>
                                    <ogc:Mul>
                                        <ogc:Literal>-1</ogc:Literal>
                                        <ogc:PropertyName>ogr_angle</ogc:PropertyName>
                                    </ogc:Mul>
                                </sld:Rotation>
                            </sld:PointPlacement>
                        </sld:LabelPlacement>
                        <sld:Fill>
                            <sld:CssParameter name="fill">#A50000</sld:CssParameter>
                            <sld:CssParameter name="fill-opacity">1</sld:CssParameter>
                        </sld:Fill>
                    </sld:TextSymbolizer>
                </sld:Rule>
            </sld:FeatureTypeStyle>
        </sld:UserStyle>
    </sld:NamedLayer>
</sld:StyledLayerDescriptor>