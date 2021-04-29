<?xml version="1.0" encoding="UTF-8"?>
<!--
  
  nom du SLD : horsrm_edi_parc_plg
  
  couche source dans la base :  cadastre.edi_parc
  layer cible du style       :  ref_cad:edi_parc_plg
  
  objet :
  Style relatif à la représentation des parcelles cadastrales de Rennes Métropole.
  Il reprend la représentation/style de réseau carto.
  
  Historique des versions :
  date        |  auteur              |  description
  20/08/2015  |  Arnaud LECLERE      |  version initiale
  10/06/2016  |  Maël REBOUX         |  mise en forme + adaptations pour le cache
  
-->
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

    <NamedLayer>
    <Name>edi_parc_plg</Name>
    <UserStyle>
      <Title>Parcelles du cadastre de Rennes Métropole</Title>
      <FeatureTypeStyle>
      
        <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
        <!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
        <!--  étiquettes parcelles -->
        <Rule>
          <Name>Numéro de parcelle</Name>
          <Title>Numéro de parcelle</Title>
          <!-- Filtre sur les sections -->
          <ogc:Filter>
              <ogc:PropertyIsGreaterThanOrEqualTo>
                <ogc:PropertyName>supf</ogc:PropertyName>
                <ogc:Literal>10000</ogc:Literal>
              </ogc:PropertyIsGreaterThanOrEqualTo>
          </ogc:Filter>
          <MinScaleDenominator>3000</MinScaleDenominator>
          <MaxScaleDenominator>9000</MaxScaleDenominator>
          <!-- Police, taille de l'étiquette -->
          <TextSymbolizer>
            <Label>
              <ogc:PropertyName>numero</ogc:PropertyName>
            </Label>
            <Font>
              <CssParameter name="font-family">Courier New</CssParameter>
              <CssParameter name="font-size">10</CssParameter>
              <CssParameter name="font-style">normal</CssParameter>
              <CssParameter name="font-weight">bold</CssParameter>
            </Font>
            <!-- Placement de l'étiquette -->
            <LabelPlacement>
              <PointPlacement>
                <AnchorPoint>
                  <AnchorPointX>0.5</AnchorPointX>
                  <AnchorPointY>0.5</AnchorPointY>
                </AnchorPoint>
                <Displacement>
                  <DisplacementX>0</DisplacementX>
                  <DisplacementY>0</DisplacementY>
                </Displacement>
              </PointPlacement>
            </LabelPlacement>
            <!-- couleur de police du texte -->
            <Fill>
              <CssParameter name="fill">#606060</CssParameter>
            </Fill>
            <Priority>10</Priority>
            <!-- ne pas gérer les conflits de positionnement = superposer -->
            <VendorOption name="conflictResolution">false</VendorOption>
          </TextSymbolizer>
        </Rule>
        
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>