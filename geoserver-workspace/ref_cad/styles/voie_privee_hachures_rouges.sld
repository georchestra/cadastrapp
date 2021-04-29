<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : voie_privee_hachures_rouges

  couche source dans la base :  cadastre_qgis:geo_tronroute
  layer cible du style       :  ref_cad:voie_privee

  objet : style pour utilisation de la couche en mode solo (pas dans le plan cadastral)

  Historique des versions :
  date        |  auteur              |  description
  20/02/2020  |  arnaud LECLERE      |  version initiale

-->
<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <se:Name>ref_cad:voie_privee</se:Name>
    <UserStyle>
      <se:Name>voie_privee_hachures_rouges</se:Name>
      <se:Description>
        <se:Title>hachures rouges</se:Title>
        <se:Abstract>polygone avec des hachures rouges</se:Abstract>
      </se:Description>
      <se:FeatureTypeStyle>

        <se:Rule>
          <se:Name>Voies privées du cadastre</se:Name>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>25000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer uom="http://www.opengeospatial.org/se/units/metre">
            <se:Fill>
              <se:GraphicFill>
                <se:Graphic>
                  <se:Mark>
                    <se:WellKnownName>shape://slash</se:WellKnownName>
                    <se:Stroke>
                      <se:SvgParameter name="stroke">#ff0000</se:SvgParameter>
                      <se:SvgParameter name="stroke-width">1.0</se:SvgParameter>
                    </se:Stroke>
                  </se:Mark>
                  <se:Size>
                    <ogc:Literal>10.0</ogc:Literal>
                  </se:Size>
                </se:Graphic>
              </se:GraphicFill>
            </se:Fill>
            <se:Stroke>
              <se:SvgParameter name="stroke">#ff0000</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.5</se:SvgParameter>
              <se:SvgParameter name="stroke-linejoin">bevel</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>

      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>