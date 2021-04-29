<?xml version="1.0" encoding="UTF-8"?>
<!--
  
  nom du SLD : app_urbanisme_parcelle
  
  couche source dans la base :  cadastre.edi_parc
  layer cible du style       :  ref_cad:app_urbanisme_parcelle
  
  objet :
  style spécifique pour cette couche applicative pour le module urbanisme
  
  Historique des versions :
  date        |  auteur              |  description
  28/09/2016  |  Maël REBOUX         |  version initiale pour test module urbanisme
  
  
-->
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">

  <NamedLayer>
    <Name>ref_cad:app_urbanisme_parcelle</Name>
    <UserStyle>
      <!-- nom du style -->
      <Name>ref_cad:app_urbanisme_parcelle</Name>
      <!-- ce title apparaît dans la liste des styles dans le visualiseur -->
      <Title>Limites des parcelles</Title>
      <!-- le résumé apparaît en tooltip sur le nom du style -->
      <Abstract>Limites des parcelles en bordure noire + code des parcelles en libellé en-dessous du 1/3 000.</Abstract>
    
    
      <FeatureTypeStyle>
        
        <Rule>
          <Name>Numéro de parcelle</Name>
          <Title>Numéro de parcelle</Title>
          <MaxScaleDenominator>3000</MaxScaleDenominator>
          <!-- Police, taille de l'étiquette -->
          <TextSymbolizer>
            <!-- on place un pseudo point issu du centroïde pour éviter que le support d'étiquettes se déplace dans le visualiseur -->
            <Geometry>
              <ogc:Function name="centroid">
                <ogc:PropertyName>shape</ogc:PropertyName>
              </ogc:Function>
             </Geometry>
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
        
        <Rule>
          <!-- polygone vide avec fine bordure noire -->
          <Name>app_urbanisme_parcelle</Name>
          <Title>app_urbanisme_parcelle</Title>
          <MaxScaleDenominator>35001</MaxScaleDenominator>
          <PolygonSymbolizer>
            <Fill>
              <CssParameter name="fill">#808080</CssParameter>
              <CssParameter name="fill-opacity">0.01</CssParameter>
            </Fill>
            <Stroke>
              <CssParameter name="stroke">#000000</CssParameter>
              <CssParameter name="stroke-opacity">0.8</CssParameter>
              <CssParameter name="stroke-width">0.25</CssParameter>
            </Stroke>
          </PolygonSymbolizer>
        </Rule>
        
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>