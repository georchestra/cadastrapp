<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : geo_ptcanv

  couche source dans la base :  cadastre_qgis:geo_ptcanv
  layer cible du style       :  ref_cad:geo_ptcanv

  objet : style relatif aux objets ponctuels servant d'appui aux opérations de lever des plans...

  Historique des versions :
  date        |  auteur              |  description
  28/05/2019  |  arnaud LECLERE      |  version initiale
  21/08/2019  |  Maël REBOUX         |  modif avant mise en prod

-->

<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <se:Name>ref_cad:geo_ptcanv</se:Name>
    <UserStyle>
      <se:Name>geo_ptcanv</se:Name>

      <se:Description>
        <se:Title>Points de canevas</se:Title>
        <se:Abstract>Objet ponctuel servant d'appui aux opérations de lever des plans...</se:Abstract>
      </se:Description> 
      <se:FeatureTypeStyle>

        <se:Rule>
          <!-- Filtre sur les bornes limites de commune -->
          <se:Name>Bornes limites de commune</se:Name>
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>81</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des bornes limites de commune -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0045'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


        <se:Rule>
          <se:Name>Autre repère de nivellement</se:Name>       
          <!-- Filtre des autres repères de nivellement -->
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>80</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des autres repères de nivellement -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0044'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
              <!-- rotation selon champ angle -->
              <se:Rotation>
                <ogc:PropertyName>90</ogc:PropertyName>
              </se:Rotation>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


        <se:Rule>
          <se:Name>Borne du NGF</se:Name>
          <!-- Filtre sur les bornes du N.G.F. -->
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>78</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des bornes du N.G.F. -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0043'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
              <!-- rotation selon champ angle -->
              <se:Rotation>
                <ogc:PropertyName>90</ogc:PropertyName>
              </se:Rotation>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


        <se:Rule>
          <se:Name>Repère NGF</se:Name>
          <!-- Filtre sur les repères du N.G.F. -->
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>77</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des repères du N.G.F. -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0043'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
              <!-- rotation selon champ angle -->
              <se:Rotation>
                <ogc:PropertyName>90</ogc:PropertyName>
              </se:Rotation>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


        <se:Rule>
          <se:Name>Point borné d'appui de géoréférencement</se:Name>
          <!-- Filtre sur les points bornés d'appui de géoréférencement -->
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>76</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des points bornés d'appui de géoréférencement -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0042'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
              <!-- rotation selon champ angle -->
              <se:Rotation>
                <ogc:PropertyName>90</ogc:PropertyName>
              </se:Rotation>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


        <se:Rule>
          <se:Name>Point bornés pérenne ou cadastral de précision</se:Name>
          <!-- Filtre sur les points bornés pérenne ou cadastral de précision -->
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>74</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des points bornés pérenne ou cadastral de précision -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0042'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
              <!-- rotation selon champ angle -->
              <se:Rotation>
                <ogc:PropertyName>90</ogc:PropertyName>
              </se:Rotation>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


        <se:Rule>
          <se:Name>Point borné de canevas cadastral ordinaire</se:Name>
          <!-- Filtre sur les points bornés cadastral ordinaire -->
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>73</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des points bornés cadastral ordinaire -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0042'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
              <!-- rotation selon champ angle -->
              <se:Rotation>
                <ogc:PropertyName>90</ogc:PropertyName>
              </se:Rotation>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>      


        <se:Rule>
          <se:Name>Point géodésique non borné</se:Name>
          <!-- Filtre sur les points géodésiques non bornés -->
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>72</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des points géodésiques non bornés -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0041'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
              <!-- rotation selon champ angle -->
              <se:Rotation>
                <ogc:PropertyName>90</ogc:PropertyName>
              </se:Rotation>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


        <se:Rule>
          <se:Name>Point géodésique borné</se:Name>
          <!-- Filtre sur les points géodésiques bornés -->
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsEqualTo>
              <ogc:PropertyName>geo_sym</ogc:PropertyName>
              <ogc:Literal>71</ogc:Literal>
            </ogc:PropertyIsEqualTo>
          </ogc:Filter>
          <!-- Echelle d'affichage -->
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>1800</se:MaxScaleDenominator>
          <!-- représentation des points géodésiques bornés -->
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>ttf://cadastre_rm#${'U+0041'}</se:WellKnownName>
                <se:Fill>
                  <se:SvgParameter name="fill">#878787</se:SvgParameter>
                </se:Fill>
                <se:Stroke>     
                  <se:SvgParameter name="stroke-width">0</se:SvgParameter>
                  <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>15</se:Size>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>