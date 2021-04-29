<?xml version="1.0" encoding="ISO-8859-1"?>
<!--
  
  nom du SLD : horsrm_geo_comm_plg
  
  couche source dans la base :  cadastre.horsrm_edi_comm
  layer cible du style       :  ref_cad:horsrm_edi_comm_plg
  
  objet :
  Style relatif au fond gris des communes en dehors de Rennes Métropole.
  Il reprend la représentation/style de réseau carto.
  
  Historique des versions :
  date        |  auteur              |  description
  19/06/2019  |  Arnaud LECLERE      |  version initiale
  
-->
<StyledLayerDescriptor version="1.0.0"
    xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
    xmlns="http://www.opengis.net/sld"
    xmlns:ogc="http://www.opengis.net/ogc"
    xmlns:xlink="http://www.w3.org/1999/xlink"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
    
   <se:NamedLayer>
    <se:Name>ref_cad:hors_edi_comm</se:Name>
     
    <se:UserStyle>
      <se:Name>horsrm_geo_comm_plg</se:Name>
      
      <se:Description>
        <se:Title>Communes du cadastre hors Rennes Métropole
        </se:Title>
        <se:Abstract>Communes du cadastre hors Rennes Métropole</se:Abstract>
      </se:Description> 
      
      
      <se:FeatureTypeStyle>

        <!-- affichage des noms des communes uniqument sur le cache au 1/136494
             on génère un point au centroïde comme support plutôt que placement libre dans le polygone
             afin de ne pas avoir +ieurs étiquettes dans le cache -->
        <se:Rule>
        
          <se:Name>Communes hors Rennes Métropole : étiquettes</se:Name>
          
          <se:MinScaleDenominator>135000</se:MinScaleDenominator>
          <se:MaxScaleDenominator>140000</se:MaxScaleDenominator>
          
          <se:TextSymbolizer>
          
            <se:Geometry>
              <ogc:Function name="centroid">
                <ogc:PropertyName>shape</ogc:PropertyName>
              </ogc:Function>
            </se:Geometry>
            
            <se:Label>
              <ogc:PropertyName>nomcom</ogc:PropertyName>
            </se:Label>
            
            <se:Font>
              <se:SvgParameter name="font-family">DejaVu Sans</se:SvgParameter>
              <se:SvgParameter name="font-size">9</se:SvgParameter>
              <se:SvgParameter name="font-style">normal</se:SvgParameter>
              <se:SvgParameter name="font-weight">bold</se:SvgParameter>
              <se:SvgParameter name="font-color">#787878</se:SvgParameter>
            </se:Font>
            
            <se:LabelPlacement>
              <se:PointPlacement>
                <se:AnchorPoint>
                  <se:AnchorPointX>0.5</se:AnchorPointX>
                  <se:AnchorPointY>0.5</se:AnchorPointY>
                </se:AnchorPoint>
              </se:PointPlacement>
            </se:LabelPlacement>
            
            <se:Halo>
              <se:Radius>1</se:Radius>
              <se:Fill>
                <se:SvgParameter name="fill">#FFFFFF</se:SvgParameter>
              </se:Fill>
            </se:Halo>
          </se:TextSymbolizer>
        </se:Rule>

        <!-- fond gris -->
        <se:Rule>
          <se:Name>Communes hors Rennes Métropole</se:Name>
          
          <se:MinScaleDenominator>69000</se:MinScaleDenominator>
          
          <se:PolygonSymbolizer>
            <se:Fill>
              <se:SvgParameter name="fill">#f0f0f0</se:SvgParameter>
            </se:Fill>
            <se:Stroke>
              <se:SvgParameter name="stroke">#dcdcdc</se:SvgParameter>
              <se:SvgParameter name="stroke-width">1</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>

      </se:FeatureTypeStyle>
    </se:UserStyle>
  </se:NamedLayer>
</StyledLayerDescriptor>