<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : geo_parcelle_anno_grdes_parcelles

  couche source dans la base :  cadastre_qgis.geo_parcelle
  layer cible du style       :  ref_cad:geo_parcelle

  objet : Style spécifique pour les numéros de parcelles.
  La couche edi_anpt contient effectivement les numéros des parcelles MAIS on va différencier selon la surface des parcelles : > ou < à 10 0000 m2
  On a effet besoin de voir les numéros sur les grandes parcelles avant les petites parcelles, surtout pour le bordereau parcellaire.
  Ici on va étiqueter les polygones des grandes parcelles tandis que < au 1/3000 on repasse sur la couche anpt_pnt

  Historique des versions :
  date        |  auteur              |  description
  17/06/2019  |  Arnaud LECLERE      |  version initiale
  21/08/2019  |  Maël REBOUX         |  modif diverses avant mise en prod

-->

<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <se:Name>ref_cad:geo_parcelle</se:Name>
    <UserStyle>
      <se:Name>geo_parcelle_anno_grdes_parcelles</se:Name>
      <se:Description>
        <se:Title>Etiquettes des grandes parcelles</se:Title>
        <se:Abstract>Etiquettes des grandes parcelles</se:Abstract>
      </se:Description>
      <se:FeatureTypeStyle>


        <se:Rule>
          <se:Name>Numéros des grandes parcelles</se:Name>
          <ogc:Filter>
            <ogc:PropertyIsGreaterThanOrEqualTo>
              <ogc:PropertyName>supf</ogc:PropertyName>
              <ogc:Literal>7500</ogc:Literal>
            </ogc:PropertyIsGreaterThanOrEqualTo>
          </ogc:Filter>
          <se:MinScaleDenominator>2500</se:MinScaleDenominator>
          <se:MaxScaleDenominator>10000</se:MaxScaleDenominator>
          <se:TextSymbolizer>
            <se:Geometry>
              <ogc:Function name="centroid">
                <ogc:PropertyName>geom</ogc:PropertyName>
              </ogc:Function>
            </se:Geometry>
            <se:Label>
              <ogc:PropertyName>tex</ogc:PropertyName>
            </se:Label>
            <se:Font>
              <se:SvgParameter name="font-family">DejaVu Sans Mono</se:SvgParameter>
              <se:SvgParameter name="font-size">7</se:SvgParameter>
              <se:SvgParameter name="font-style">normal</se:SvgParameter>
              <se:SvgParameter name="font-weight">normal</se:SvgParameter>
            </se:Font>
            <se:LabelPlacement>
              <se:PointPlacement>
                <se:AnchorPoint>
                  <se:AnchorPointX>0.5</se:AnchorPointX>
                  <se:AnchorPointY>0.5</se:AnchorPointY>
                </se:AnchorPoint>
                <se:Displacement>
                  <se:DisplacementX>0</se:DisplacementX>
                  <se:DisplacementY>0</se:DisplacementY>
                </se:Displacement>
              </se:PointPlacement>
            </se:LabelPlacement>
            <se:Fill>
              <se:SvgParameter name="fill">#606060</se:SvgParameter>
            </se:Fill>
            <se:Priority>10</se:Priority>
            <se:VendorOption name="conflictResolution">false</se:VendorOption>
          </se:TextSymbolizer>
        </se:Rule>

      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>