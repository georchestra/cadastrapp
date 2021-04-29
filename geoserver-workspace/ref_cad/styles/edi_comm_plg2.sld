<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  
  nom du SLD : edi_comm_plg
  
  auteur :   Arnaud LECLERE
  date :     19/08/2014
  
  couche principale cible du style :  bdu.cad_arcopole:edi_comm
  
  objet :  Style relatif au fond gris des communes du cadastre de Rennes Métropole.
  Il reprend la représentation/style de réseau carto.

-->
<StyledLayerDescriptor version="1.0.0" 
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd" 
    xmlns="http://www.opengis.net/sld" 
    xmlns:ogc="http://www.opengis.net/ogc" 
    xmlns:xlink="http://www.w3.org/1999/xlink" 
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <NamedLayer>
    <Name>edi_comm_plg</Name>
    <UserStyle>
      <Title>Fond communal du cadastre de Rennes Métropole</Title>
      <FeatureTypeStyle>

<!-- début affichage fond RM gris -->
<Rule>
<Name>Fond gris RM</Name>

          <MinScaleDenominator>500</MinScaleDenominator>
          <MaxScaleDenominator>40000</MaxScaleDenominator>
         
          <PolygonSymbolizer>
            <Fill>
              <CssParameter name="fill">#dcdcdc</CssParameter>
            </Fill>                 
          </PolygonSymbolizer>
</Rule>
<!-- fin affichage fond RM gris -->
        
      </FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>