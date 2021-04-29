<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : geo_tronfluv

  couche source dans la base :  cadastre_qgis:geo_tronfluv
  layer cible du style       :  ref_cad:geo_tronfluv

  objet : style relatif aux élément surfaciques (fermé) utilisé pour tous les cours d'eau et les rivages de mers. Un libellé y est associé.

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
    <se:Name>ref_cad:geo_tronfluv</se:Name>
    <UserStyle>
      <se:Name>geo_tronfluv</se:Name>
      <se:Description>
        <se:Title>Hydrographie</se:Title>
        <se:Abstract>Hydrographie</se:Abstract>
      </se:Description>
      <se:FeatureTypeStyle>
      
        <se:Rule>
          <se:Name>Hydrographie</se:Name>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>60000</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <se:Fill>
              <se:SvgParameter name="fill">#99ccff</se:SvgParameter>
            </se:Fill>
          </se:PolygonSymbolizer>
        </se:Rule>

      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>