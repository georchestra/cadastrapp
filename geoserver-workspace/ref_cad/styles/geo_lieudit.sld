<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : geo_lieudit

  couche source dans la base :  ref_fond:geo_lieudit
  layer cible du style       :  ref_cad:geo_lieudit

  objet : style relatif aux lieux-dits : ensemble de parcelles entières comportant une même dénomination géographique résultant de l'usage.

  Historique des versions :
  date        |  auteur              |  description
  28/05/2019  |  arnaud LECLERE      |  version initiale

-->

<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                       
  <NamedLayer>
    <se:Name>ref_cad:geo_lieudit</se:Name>
    <UserStyle>
      <se:Name>geo_lieudit</se:Name>
      
      <se:Description>
        <se:Title>lieux-dits</se:Title>
        <se:Abstract>Lieux-dits</se:Abstract>
      </se:Description> 
      
      <se:FeatureTypeStyle>
        <se:Rule>
          <se:Name>Lieux-dits</se:Name>
          
          <se:MinScaleDenominator>0</se:MinScaleDenominator>
          <se:MaxScaleDenominator>2800</se:MaxScaleDenominator>
          
          <se:PolygonSymbolizer>
            <se:Stroke>
              <se:SvgParameter name="stroke">#a782a7</se:SvgParameter>
              <se:SvgParameter name="stroke-width">2</se:SvgParameter>
              <se:SvgParameter name="stroke-linejoin">bevel</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
          
        </se:Rule>
      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>