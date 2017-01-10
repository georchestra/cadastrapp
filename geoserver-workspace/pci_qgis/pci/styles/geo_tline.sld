<?xml version="1.0" encoding="UTF-8"?><sld:StyledLayerDescriptor xmlns="http://www.opengis.net/sld" xmlns:sld="http://www.opengis.net/sld" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml" version="1.0.0">
  <sld:NamedLayer>
    <sld:Name>Objets linéaires</sld:Name>
    <sld:UserStyle>
      <sld:Name>Objets linéaires</sld:Name>
      <sld:FeatureTypeStyle>
        <sld:Name>name</sld:Name>
        <sld:Rule>
          <sld:Name>texture transparente</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>13</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#7e0ca4</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>symbole d'église</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>14</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
             <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>symbole de mosquée</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>15</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
             <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>symbole de synagogue</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>16</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
             <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>limite d'Etat</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>17</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#e89d1d</sld:CssParameter>
              <sld:CssParameter name="stroke-width">2</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>limite de département</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>18</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#b86b42</sld:CssParameter>
              <sld:CssParameter name="stroke-width">1.8</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>amorce de limite de commune</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>19</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#ff6501</sld:CssParameter>
              <sld:CssParameter name="stroke-width">1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>chemin</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>21</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#858585</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">round</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">3 2</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>amorce de voie</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>22</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#757575</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>trottoirs et sentiers</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>23</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#6a6a6a</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">3 2</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>gazoduc ou oléoduc</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>24</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
            <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.13</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">3 1 3 1</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>téléphérique</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>26</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.195</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">4 1 4 1 4 1 4 1</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>ligne de transport de force</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>27</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.13</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">3 1</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>rail de chemin de fer</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>29</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.2</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>*flèche de rattachement d'un numéro de
parcelle ou de la référence d'un bâtiment
sur le domaine non cadastré</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>31</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#6a6a6a</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>terrain de sports, petits ruisseaux</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>62</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#000000</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">4 2</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>parking, terrasse, surplomb</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>64</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#7e7e7e</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
              <sld:CssParameter name="stroke-dasharray">4 2</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <sld:Name>*objet linéaire divers</sld:Name>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>98</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#6a6a6a</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.1</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
        <sld:Rule>
          <ogc:Filter>
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal/>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <sld:MinScaleDenominator>1.0</sld:MinScaleDenominator>
          <sld:MaxScaleDenominator>1800.0</sld:MaxScaleDenominator>
          <sld:LineSymbolizer>
             <sld:Stroke>
              <sld:CssParameter name="stroke">#796b91</sld:CssParameter>
              <sld:CssParameter name="stroke-width">0.26</sld:CssParameter>
              <sld:CssParameter name="stroke-linejoin">bevel</sld:CssParameter>
              <sld:CssParameter name="stroke-linecap">square</sld:CssParameter>
            </sld:Stroke>
          </sld:LineSymbolizer>
        </sld:Rule>
      </sld:FeatureTypeStyle>
    </sld:UserStyle>
  </sld:NamedLayer>
</sld:StyledLayerDescriptor>