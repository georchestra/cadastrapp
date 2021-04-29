<?xml version="1.0" encoding="UTF-8"?>
<!--

  nom du SLD : cad_parc_nbat_desc.sld

  couche source dans la base :  cadastre_qgis.rm_parcelle_nbat_desc 
  layer cible du style       :  ref_cad:rm_parcelle_nbat_desc 

  objet : style relatif aux détails typologiques des parcelles non-bâties sur Rennes Métropole

  Historique des versions :
  date        |  auteur              |  description
  18/11/2019  |  Arnaud LECLERE      |  version initiale

-->

<StyledLayerDescriptor version="1.1.0"
                       xsi:schemaLocation="http://www.opengis.net/sld StyledLayerDescriptor.xsd"
                       xmlns="http://www.opengis.net/sld"
                       xmlns:ogc="http://www.opengis.net/ogc"
                       xmlns:se="http://www.opengis.net/se"
                       xmlns:xlink="http://www.w3.org/1999/xlink"
                       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
                       
  <NamedLayer>
    <se:Name>ref_cad.rm_parcelle_nbat_desc </se:Name>
    <UserStyle>
      <se:Name>cad_parc_nbat_desc</se:Name>
      
      <se:Description>
        <se:Title>Cadastre: détail typologique des parcelles non-bâties</se:Title>
        <se:Abstract>Cadastre : détail typologique des parcelles non-bâties sur Rennes Métropole</se:Abstract>
      </se:Description>
   
      <se:FeatureTypeStyle>
        <se:Rule>
          <se:Name>Parcelle à dominante 'artificialisé'</se:Name>
          <se:Description>
            <se:Title>Parcelle à dominante 'artificialisé'</se:Title>
          </se:Description>
          
      <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
           <ogc:PropertyIsGreaterThanOrEqualTo>
              <ogc:PropertyName>surf_artif</ogc:PropertyName>
              <ogc:PropertyName>surf_naf</ogc:PropertyName>
            </ogc:PropertyIsGreaterThanOrEqualTo> 
       </ogc:Filter>
          
          <se:PolygonSymbolizer>
            <se:Fill>
              <se:SvgParameter name="fill">#b7484b</se:SvgParameter>
            </se:Fill>
            <se:Stroke>
              <se:SvgParameter name="stroke">#232323</se:SvgParameter>
              <se:SvgParameter name="stroke-width">1</se:SvgParameter>
              <se:SvgParameter name="stroke-linejoin">bevel</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
        
        <se:Rule>
          <se:Name>Parcelle à dominante 'naturelle, agricole et forestière'</se:Name>
          <se:Description>
            <se:Title>Parcelle à dominante 'naturelle, agricole et forestière'</se:Title>
          </se:Description>
          
          <ogc:Filter xmlns:ogc="http://www.opengis.net/ogc">
            <ogc:PropertyIsLessThan>
              <ogc:PropertyName>surf_artif</ogc:PropertyName>
              <ogc:PropertyName>surf_naf</ogc:PropertyName>
            </ogc:PropertyIsLessThan>
          </ogc:Filter>
          
          <se:PolygonSymbolizer>
            <se:Fill>
              <se:SvgParameter name="fill">#becf50</se:SvgParameter>
            </se:Fill>
            <se:Stroke>
              <se:SvgParameter name="stroke">#232323</se:SvgParameter>
              <se:SvgParameter name="stroke-width">1</se:SvgParameter>
              <se:SvgParameter name="stroke-linejoin">bevel</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
        
      </se:FeatureTypeStyle>
    </UserStyle>
  </NamedLayer>
</StyledLayerDescriptor>