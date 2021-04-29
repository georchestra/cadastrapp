<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : geo_batiment_hors_rm

  couche source dans la base :  cadastre_qgis.geo_batiment_hors_rm
  layer cible du style       :  ref_cad:geo_batiment_hors_rm

  objet : geo_batiment_hors_rm

  Historique des versions :
  date        |  auteur              |  description
  23/08/2019  |  Maël REBOUX         |  version initiale dérivée de horsrm_edi_bati_plg

-->
<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">                   
  <NamedLayer>
    <se:Name>ref_cad:geo_batiment_hors_rm</se:Name>
    <UserStyle>
      <se:Name>geo_batiment_hors_rm</se:Name>
      <se:Description>
        <se:Title>geo_batiment_hors_rm</se:Title>
        <se:Abstract>geo_batiment_hors_rm</se:Abstract>
      </se:Description>
      <se:FeatureTypeStyle>
        
        
        <se:Rule>
          <se:MaxScaleDenominator>35000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <se:Fill>
              <se:SvgParameter name="fill">#a1a1a1</se:SvgParameter>
              <se:SvgParameter name="fill-opacity">1.0</se:SvgParameter>
            </se:Fill>
            <se:Stroke>
              <se:SvgParameter name="stroke">#7b7b7b</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.3</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">1.0</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
        
        
      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>