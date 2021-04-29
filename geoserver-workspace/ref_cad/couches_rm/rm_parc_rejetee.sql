
-- 35 min

INSERT INTO cadastre_qgis.rm_parc_rejetee (
   SELECT gp.geo_parcelle,
    SUBSTRING(gp.geo_parcelle, 0, 7) AS geo_commune,
    gp.geo_section,
    gp.supf, ST_PointOnSurface(gp.geom) AS shape
FROM cadastre_qgis.geo_parcelle gp
LEFT JOIN cadastre_qgis.parcelle p ON gp.geo_parcelle = p.parcelle
WHERE p.parcelle IS NULL);

  