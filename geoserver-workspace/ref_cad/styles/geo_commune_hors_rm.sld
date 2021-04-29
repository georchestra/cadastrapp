<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : geo_commune_hors_rm

  couche source dans la base :  cadastre_qgis:geo_commune_hors_rm
  layer cible du style       :  ref_cad:geo_commune_hors_rm

  objet : style relatif aux communes hors RM

  Historique des versions :
  date        |  auteur              |  description
  23/08/2019  |  Maël REBOUX         |  version initiale reprise du style arcopole

-->
<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <se:Name>ref_cad:geo_commune_hors_rm</se:Name>
    <UserStyle>
      <se:Name>geo_commune_hors_rm</se:Name>
      <se:Description>
        <se:Title>Communes hors Rennes Métropole</se:Title>
        <se:Abstract>Communes hors Rennes Métropole</se:Abstract>
      </se:Description>
      <se:FeatureTypeStyle>

        <!-- lignes grises -->
        
        <se:Rule>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>69000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <se:Stroke>
              <se:SvgParameter name="stroke">#8e8e8e</se:SvgParameter>
              <se:SvgParameter name="stroke-width">2</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">1.0</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
        
        <se:Rule>
          <se:MinScaleDenominator>69000</se:MinScaleDenominator>
          <se:MaxScaleDenominator>250000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <se:Stroke>
              <se:SvgParameter name="stroke">#8e8e8e</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.5</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">0.25</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
        
        <se:Rule>
          <se:MinScaleDenominator>250000</se:MinScaleDenominator>
          <se:MaxScaleDenominator>5000000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <se:Stroke>
              <se:SvgParameter name="stroke">#8e8e8e</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.1</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">0.25</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>


      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>