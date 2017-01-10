<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Default Styler</sld:Name>
    <sld:UserStyle>
      <sld:Name>Default Styler</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:Name>zoncommuni</sld:Name>
          <sld:Title>Voies, routes et chemins</sld:Title>
          <sld:MinScaleDenominator>1800.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>3000.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
      <sld:CssParameter name="stroke">#000000</sld:CssParameter>
      <sld:CssParameter name="stroke-opacity">0</sld:CssParameter>
      <sld:CssParameter name="stroke-width">1</sld:CssParameter>
    </sld:Stroke>
          </sld:LineSymbolizer>
          <sld:TextSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Label>
              <ogc:PropertyName>tex</ogc:PropertyName>
            </sld:Label>
            <sld:Font>
              <sld:CssParameter name="font-family">MS Shell Dlg 2</sld:CssParameter>
              <sld:CssParameter name="font-size">5</sld:CssParameter>
              <sld:CssParameter name="font-style">normal</sld:CssParameter>
              <sld:CssParameter name="font-weight">normal</sld:CssParameter>
            </sld:Font>
            <sld:LabelPlacement>
              <sld:PointPlacement>
                <sld:AnchorPoint>
                  <sld:AnchorPointX>0.0</sld:AnchorPointX>
                  <sld:AnchorPointY>0.5</sld:AnchorPointY>
                </sld:AnchorPoint>
              </sld:PointPlacement>
            </sld:LabelPlacement>
            <sld:Fill>
              <sld:CssParameter name="fill">#000000</sld:CssParameter>
            </sld:Fill>
            <sld:VendorOption name="followLine">true</sld:VendorOption>
          </sld:TextSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>