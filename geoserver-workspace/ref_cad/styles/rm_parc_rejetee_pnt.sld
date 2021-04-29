<?xml version="1.0" encoding="UTF-8"?>
<!--
  
  nom du SLD : rm_parc_rejetee_pnt
  
  couche source dans la base :  cadastre_qgis.rm_parc_rejetee
  layer cible du style       :  ref_cad:rm_parc_rejetee
  
  objet :    Style pour les parcelles rejetées du cadastre : croix rouge
  
  Historique des versions :
  date        |  auteur              |  description
  13/06/2016  |  Maël REBOUX         |  version initiale
  21/08/2019  |  Maël REBOUX         |  passage en style encoding + cadastre_qgis
  
-->
<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <se:Name>ref_cad:rm_parc_rejetee</se:Name>
    <UserStyle>
      <se:Name>rm_parc_rejetee_pnt</se:Name>
      <se:Description>
        <se:Title>Parcelle rejetée</se:Title>
        <se:Abstract>Parcelle qui apparaît au plan mais sans correspondance dans la matrice foncière.</se:Abstract>
      </se:Description>
      <se:FeatureTypeStyle>


        <se:Rule>
          <se:Name>Parcelle rejetée</se:Name>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>4500</se:MaxScaleDenominator>
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>shape://times</se:WellKnownName>
                <se:Stroke>
                  <se:SvgParameter name="stroke">#ff0000</se:SvgParameter>
                  <se:SvgParameter name="stroke-width">3</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>10</se:Size>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>
        
        <se:Rule>
          <se:Name>Parcelle rejetée</se:Name>
          <se:MinScaleDenominator>4500</se:MinScaleDenominator>
          <se:MaxScaleDenominator>9000</se:MaxScaleDenominator>
          <se:PointSymbolizer>
            <se:Graphic>
              <se:Mark>
                <se:WellKnownName>shape://times</se:WellKnownName>
                <se:Stroke>
                  <se:SvgParameter name="stroke">#ff0000</se:SvgParameter>
                  <se:SvgParameter name="stroke-width">1.5</se:SvgParameter>
                </se:Stroke>
              </se:Mark>
              <se:Size>6</se:Size>
            </se:Graphic>
          </se:PointSymbolizer>
        </se:Rule>


      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>