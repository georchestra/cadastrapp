<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : geo_commune_hors_rm_fond

  couche source dans la base :  cadastre_qgis.geo_commune_hors_rm
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
      <se:Name>geo_commune_hors_rm_fond</se:Name>
      <se:Description>
        <se:Title>Communes hors Rennes Métropole</se:Title>
        <se:Abstract>Communes hors Rennes Métropole</se:Abstract>
      </se:Description>
      <se:FeatureTypeStyle>

        <!-- fond gris -->
        <se:Rule>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>5000000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <se:Fill>
              <se:SvgParameter name="fill">#f0f0f0</se:SvgParameter>
            </se:Fill>
          </se:PolygonSymbolizer>
        </se:Rule>

      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>