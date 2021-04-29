<?xml version="1.0" encoding="UTF-8"?>
<!--
  
  nom du SLD : nb_geo_parcelle_contour
  
  couche source dans la base :  cadastre_qgis.geo_parcelle
  layer cible du style       :  ref_cad:geo_parcelle
  
  objet : contour des parcelles : style calé sur geo_parcelle_fond pour venir "écraser" certains détails linéaires
  
  Historique des versions :
  date        |  auteur              |  description
  27/08/2019  |  Maël REBOUX         |  version initiale reprise de la version couleurs
  
-->
<StyledLayerDescriptor version="1.1.0" xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" xmlns="http://www.opengis.net/sld" 
xmlns:ogc="http://www.opengis.net/ogc" xmlns:se="http://www.opengis.net/se" xmlns:xlink="http://www.w3.org/1999/xlink" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <se:Name>ref_cad:geo_parcelle</se:Name>
    <UserStyle>
      <se:Name>nb_geo_parcelle_contour</se:Name>
        <se:Description>
          <se:Title>nb_geo_parcelle_contour</se:Title>
          <!--<se:Abstract>Explications plus détaillées sur le style</se:Abstract>-->
        </se:Description>
      <se:FeatureTypeStyle>
      
        <se:Rule>
          <se:Name>Parcelles cadastrales</se:Name>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>2200</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <!--<se:Fill>
              <se:SvgParameter name="fill">#ffeabe</se:SvgParameter>
              <se:SvgParameter name="fill-opacity">1.0</se:SvgParameter>
            </se:Fill>-->
            <se:Stroke>
              <se:SvgParameter name="stroke">#9b9b9b</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.75</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">1.0</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
      
        <se:Rule>
          <se:Name>Parcelles cadastrales</se:Name>
          <se:MinScaleDenominator>2200</se:MinScaleDenominator>
          <se:MaxScaleDenominator>9000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <!--<se:Fill>
              <se:SvgParameter name="fill">#ffeabe</se:SvgParameter>
              <se:SvgParameter name="fill-opacity">1.0</se:SvgParameter>
            </se:Fill>-->
            <se:Stroke>
              <se:SvgParameter name="stroke">#9b9b9b</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.5</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">0.75</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
        
        <se:Rule>
          <se:Name>Parcelles cadastrales</se:Name>
          <se:MinScaleDenominator>9000</se:MinScaleDenominator>
          <se:MaxScaleDenominator>35000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <!--<se:Fill>
              <se:SvgParameter name="fill">#ffeabe</se:SvgParameter>
              <se:SvgParameter name="fill-opacity">1.0</se:SvgParameter>
            </se:Fill>-->
            <se:Stroke>
              <se:SvgParameter name="stroke">#9b9b9b</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.15</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">0.5</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
        
        <se:Rule>
          <se:Name>Parcelles cadastrales</se:Name>
          <se:MinScaleDenominator>35000</se:MinScaleDenominator>
          <se:MaxScaleDenominator>69000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <!--<se:Fill>
              <se:SvgParameter name="fill">#ffeabe</se:SvgParameter>
              <se:SvgParameter name="fill-opacity">1.0</se:SvgParameter>
            </se:Fill>-->
            <se:Stroke>
              <se:SvgParameter name="stroke">#9b9b9b</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.1</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">0.5</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
      
      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>