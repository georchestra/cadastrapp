<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : geo_commune_fond

  couche source dans la base :  cadastre_qgis:geo_commune
  layer cible du style       :  ref_cad:geo_commune

  objet : fond du plan cadastral

  Historique des versions :
  date        |  auteur              |  description
  21/08/2019  |  Maël REBOUX         |  version initiale reprise de la version arcopole

-->
<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <se:Name>ref_cad:geo_commune</se:Name>
    <UserStyle>
      <se:Name>geo_commune_fond</se:Name>
      <se:Description>
        <se:Title>Fond gris du cadastre</se:Title>
        <se:Abstract>Fond gris du cadastre</se:Abstract>
      </se:Description>
      <se:FeatureTypeStyle>

        
        <se:Rule>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>69000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <se:Fill>
              <se:SvgParameter name="fill">#e5e3e3</se:SvgParameter>
            </se:Fill>
          </se:PolygonSymbolizer>
        </se:Rule>


      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>