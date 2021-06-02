
Publication des couches applicatives
=======================================

Pour fonctionner, Cadastrapp a besoin que l'on monte deux couches applicatives en WMS et WFS et **en projection webmercator (EPSG:3857)** qui sont utilisées :

- par l'addon pour les clics pour interroger les parcelles ou les unités foncières
- par la webapp pour produire les bordereaux parcellaires

**Il vous faut donc publier 2 couches applicatives** :

- 1 pour les parcelles cadastrales
- 1 pour les unités foncières

Et les déclarer dans le fichier ** `cadastrapp.properties <https://github.com/georchestra/cadastrapp/blob/master/cadastrapp/src/main/resources/cadastrapp.properties>`_ ** :

- pour la couche des parcelles : ``cadastre.wms.layer.name``
- pour la couche des unités foncières : ``uf.wms.layer.name``

Ne pas oublier de les déclarer également un peu plus bas en variables ``cadastre.wfs.layer.name`` et ``uf.wfs.layer.name`` si votre plate-forme geOrchestra nécessite une authentification préalable des utilisateurs pour utiliser les services.

.. warning::
  **Il faut absolument que ces 2 couches soit publiées dans les capacités WxS de votre serveur de données géographiques.**

  Sous GeoServer : cocher la case "Activé" (la couche est active et fonctionnelle) ET la case "Publié" (la couche apparaîtra dans les capacités WxS).

  Ne pas oublier de forcer la reprojection vers EPSG:3857 (en fait : le système de projection de votre visualiseur).


Comme ces 2 couches seront chargées dans le visualiseur et appelées par la webapp pour générer les documents PDF, il est recommandé d'appliquer un style invisible sur ces couches. Exemple ci-dessous.


.. code-block:: XML

        <!-- polygone vide avec fine bordure noire. Changer en opacité à 0 pour mise en prod -->
        <se:Rule>
          <se:Name>Polygone</se:Name>
          <se:MinScaleDenominator>1</se:MinScaleDenominator>
          <se:MaxScaleDenominator>35001</se:MaxScaleDenominator>
          <se:PolygonSymbolizer>
            <se:Fill>
              <se:SvgParameter name="fill">#505050</se:SvgParameter>
              <se:SvgParameter name="fill-opacity">0.001</se:SvgParameter>
            </se:Fill>
            <se:Stroke>
              <se:SvgParameter name="stroke">#000000</se:SvgParameter>
              <se:SvgParameter name="stroke-width">0</se:SvgParameter>
              <se:SvgParameter name="stroke-opacity">0</se:SvgParameter>
            </se:Stroke>
          </se:PolygonSymbolizer>
        </se:Rule>
