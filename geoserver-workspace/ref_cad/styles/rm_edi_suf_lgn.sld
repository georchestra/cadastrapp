<?xml version="1.0" encoding="UTF-8"?>
<!--
  
  nom du SLD : rm_edi_suf_lgn
  
  couche source dans la base :  bdu.cad_arcopole:rm_suf_lin
  layer cible du style       :  ref_cad:ref_cad:rm_suf_lgn
  
  objet :
  Style relatif aux limites de subdivisions fiscales des communes de Rennes Métropole.
  Il s'agit de la repréntation/style présent sur réseau carto.
  
  Historique des versions :
  date        |  auteur              |  description
  28/02/2015  |  Arnaud LECLERE      |  version initiale
  09/06/2016  |  Maël REBOUX         |  mise en forme + suppression limite affichage 1/500
  
-->
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <Name>rm_edi_suf_lgn</Name>
    <UserStyle>
      <Title>Limites de subdivisions fiscales des communes de Rennes Métropole</Title>
      <FeatureTypeStyle>
        <Rule>
          <Name>Ech: 500 - 10 000</Name>
          <MaxScaleDenominator>10000</MaxScaleDenominator>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#ff0000</CssParameter>
              <CssParameter name="stroke-width">0.25</CssParameter>
              <CssParameter name="stroke-linejoin">round</CssParameter>
              <CssParameter name="stroke-linecap">square</CssParameter>
              <CssParameter name="stroke-dasharray">3 2</CssParameter>
            </Stroke>
          </LineSymbolizer>
        </Rule>

        <Rule>
          <Name>Ech: 10 000 - 25 000</Name>
          <MinScaleDenominator>10000</MinScaleDenominator>
          <MaxScaleDenominator>25000</MaxScaleDenominator>
          <LineSymbolizer>
            <Stroke>
              <CssParameter name="stroke">#ff0000</CssParameter>
              <CssParameter name="stroke-width">0.25</CssParameter>
              <CssParameter name="stroke-linejoin">round</CssParameter>
              <CssParameter name="stroke-linecap">square</CssParameter>
              <CssParameter name="stroke-dasharray">3 2</CssParameter>
            </Stroke>
          </LineSymbolizer>
        </Rule>

      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>