<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Surfaces</sld:Name>
    <sld:UserStyle>
      <sld:Name>Surfaces</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:Name>limite surfacique ne formant pas parcelle</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>32</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>250000.0</sld:MaxScaleDenominator>
          <sld:PolygonSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
          <sld:Stroke>
              <sld:CssParameter name="stroke">#838383</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.26</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">4 2</sld:CssParameter>
            </sld:Stroke>
          </sld:PolygonSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>parapet de pont</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>33</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>250000.0</sld:MaxScaleDenominator>
          <sld:PolygonSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:Mark>
                     <sld:Fill>
                      <sld:CssParameter name="fill">#c1c1c1</sld:CssParameter>
                    </sld:Fill>
                  </sld:Mark>
                </sld:Graphic>
              </sld:GraphicFill>
            </sld:Fill>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.26</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">1 2</sld:CssParameter>
            </sld:Stroke>
          </sld:PolygonSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>étang, lac, mare</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>34</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>250000.0</sld:MaxScaleDenominator>
          <sld:PolygonSymbolizer>
           <sld:Fill>
              <sld:CssParameter name="fill">#1a7aac</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>tunnel</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>37</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>250000.0</sld:MaxScaleDenominator>
          <sld:PolygonSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Fill>
              <sld:CssParameter name="fill">#ffffff</sld:CssParameter>
            </sld:Fill>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.26</sld:CssParameter>
            </sld:Stroke>
          </sld:PolygonSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>cimetière chrétien</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>51</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>250000.0</sld:MaxScaleDenominator>
          <sld:PolygonSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:ExternalGraphic>
                    <sld:OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="file:/tsurf_51.svg"/>
                    <sld:Format>image/svg+xml</sld:Format>
                  </sld:ExternalGraphic>
                  <sld:Size>10</sld:Size>
                  <sld:CssParameter name="stroke">#000000</sld:CssParameter>
                  <sld:CssParameter name="stroke-width">1</sld:CssParameter>
                </sld:Graphic>
              </sld:GraphicFill>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.26</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>cimetière israélite</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>52</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>250000.0</sld:MaxScaleDenominator>
          <sld:PolygonSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:ExternalGraphic>
                    <sld:OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="file:/tsurf_52.svg"/>
                    <sld:Format>image/svg+xml</sld:Format>
                  </sld:ExternalGraphic>
                  <sld:Size>10</sld:Size>
                  <sld:CssParameter name="stroke">#000000</sld:CssParameter>
                  <sld:CssParameter name="stroke-width">1</sld:CssParameter>
                </sld:Graphic>
              </sld:GraphicFill>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
               <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.26</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>cimetière musulman</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>53</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>250000.0</sld:MaxScaleDenominator>
          <sld:PolygonSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <sld:Fill>
              <sld:GraphicFill>
                <sld:Graphic>
                  <sld:ExternalGraphic>
                    <sld:OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="file:/tsurf_53.svg"/>
                    <sld:Format>image/svg+xml</sld:Format>
                  </sld:ExternalGraphic>
                  <sld:Size>10</sld:Size>
                   <sld:CssParameter name="stroke">#000000</sld:CssParameter>
                  <sld:CssParameter name="stroke-width">1</sld:CssParameter>
                </sld:Graphic>
              </sld:GraphicFill>
            </sld:Fill>
          </sld:PolygonSymbolizer>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.26</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>piscine</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>65</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>250000.0</sld:MaxScaleDenominator>
          <sld:PolygonSymbolizer>
            <sld:Fill>
              <sld:CssParameter name="fill">#9ac2db</sld:CssParameter>
            </sld:Fill>
          </sld:PolygonSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>
