<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : nb_rm_suf_line
  
  couche source dans la base :  cadastre_qgis.rm_suf_line
  layer cible du style       :  ref_cad:rm_suf_line

  objet : style relatif aux limites de subdivisions fiscales

  Historique des versions :
  date        |  auteur              |  description
  27/08/2019  |  Maël REBOUX         |  version initiale reprise de la version couleurs

-->
<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <se:Name>ref_cad:rm_suf_line</se:Name>
    <UserStyle>
      <se:Name>nb_rm_suf_line</se:Name>
      <se:Description>
        <se:Title>Limites subdivisions fiscales</se:Title>
        <se:Abstract>Limites subdivisions fiscales</se:Abstract>
      </se:Description> 
      <se:FeatureTypeStyle>

        <se:Rule>
          <se:Name>Limites subdivisions fiscales</se:Name>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>10500</se:MaxScaleDenominator>
          <se:LineSymbolizer>
            <se:Stroke>
              <se:SvgParameter name="stroke">#625F5F</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0.2</se:SvgParameter>
              <se:SvgParameter name="stroke-linejoin">bevel</se:SvgParameter>
              <se:SvgParameter name="stroke-linecap">square</se:SvgParameter>
              <se:SvgParameter name="stroke-dasharray">4 2</se:SvgParameter>
            </se:Stroke>
          </se:LineSymbolizer>
        </se:Rule>
        
      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>