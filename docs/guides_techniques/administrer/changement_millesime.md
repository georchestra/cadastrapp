# Changer le millésime des données cadastrales

Cette procédure distingue trois décisions : la compatibilité du **plugin Cadastre de QGIS** avec les fichiers reçus, la **reconstruction des données Cadastrapp** et une éventuelle **migration du backend**. Un nouveau millésime de données n'impose pas à lui seul une nouvelle version du backend.

Les noms de bases et de schémas ci-dessous sont des exemples. Tester la procédure sur une copie restaurée des bases avant la production.

## Circuit des données

1. Le plugin [Cadastre de QGIS](https://docs.3liz.org/QgisCadastrePlugin/extension-qgis/import/) importe les fichiers **EDIGEO** (plan) et **MAJIC** (matrice foncière) dans PostgreSQL/PostGIS, souvent dans `cadastre_qgis`.
2. Les scripts de `database/` lisent ce schéma source et alimentent les tables et vues matérialisées du schéma applicatif, souvent `cadastrapp`. Les bases source et cible peuvent être distinctes : `uniqueDB` dans `database/config.sh` décrit ce cas.
3. Le backend lit le schéma applicatif ; GeoServer publie les couches géographiques. Les dates indiquées sur les PDF sont configurées dans `cadastrapp.properties`.

Les millésimes du plan et de la matrice foncière peuvent différer. Les relever et les suivre séparément. Pour une installation dont Cadastrapp et GeoServer pointent déjà sur un schéma QGIS fixe, conserver **le nom de ce schéma actif** simplifie la bascule : archiver d'abord le millésime en service dans un autre schéma, puis importer le nouveau millésime dans le schéma actif vidé. Les connexions et noms de schéma configurés dans Cadastrapp et GeoServer restent ainsi stables.

## Choisir les traitements

| Livraison | Plugin QGIS Cadastre | Données Cadastrapp | Backend |
| --- | --- | --- | --- |
| EDIGEO seul, structure source inchangée | Vérifier l'import et les géométries | `cadastrapp_update_data.sh` peut suffire : rafraîchissement des vues existantes | Aucune migration déduite du seul nouveau plan |
| MAJIC, avec ou sans EDIGEO | Vérifier le support du **format** MAJIC livré et des fichiers associés, notamment TOPO | `cadastrapp_load_data.sh` : recréer les nomenclatures et les vues | Vérifier séparément la compatibilité du SQL et du backend |
| Structure de base QGIS ou Cadastrapp modifiée | Tester le plugin et comparer les colonnes produites | Adapter les scripts : un `REFRESH` ne modifie pas la définition d'une vue | Migrer si la version backend retenue exige un nouveau contrat de base |

La [documentation du plugin](https://docs.3liz.org/QgisCadastrePlugin/extension-qgis/import/) indique les formats MAJIC pris en charge et recommande de **séparer les millésimes** dans des schémas ou bases distincts, car la structure peut évoluer. Archiver le millésime en service dans un autre schéma répond à cet objectif ; réutiliser le schéma actif pour le nouveau millésime exige toutefois de vérifier que le plugin peut l'importer avec la structure en place. Consulter aussi son [journal des versions](https://github.com/3liz/QgisCadastrePlugin/blob/master/CHANGELOG.md). Une mise à jour du plugin est nécessaire si la version installée ne prend pas en charge le format reçu ou si un correctif d'import indispensable lui manque. Le numéro d'année seul ne renseigne pas sur la version effectivement installée.

Une migration du backend est nécessaire si la version **du backend que l'on prévoit de déployer** ajoute ou modifie ses tables, colonnes, contraintes, relations ou paramètres. Comparer les notes de version, les scripts SQL et la configuration entre version déployée et version cible. Une livraison MAJIC n'est pas en elle-même une migration de schéma applicatif.

## Préparer la campagne

### Inventorier et qualifier les fichiers

- Noter les versions du plugin, des scripts (commit ou version), du backend et de PostgreSQL/PostGIS ; noter les millésimes MAJIC et EDIGEO en service et reçus.
- Inventorier les fichiers livrés, territoires, lots, dates de validité, codes département et direction, projections et fichiers TOPO. Vérifier leur présence et leur lisibilité.
- Comparer le format reçu avec la documentation et le journal des versions du plugin. Faire un premier import en recette ; la présence de l'année dans l'interface ne suffit pas à prouver la compatibilité.
- Comparer les colonnes QGIS attendues par `database/sql/tables/`, `database/sql/vues/` et, si `uniqueDB=False`, `database/sql/vues_dblink/` aux colonnes de l'import d'essai.
- Relever les connexions, les droits d'accès, `schema.name`, les droits géographiques et les dates PDF du backend.

### Sauvegarder et préparer le retour arrière

Sauvegarder **la base source QGIS et la base Cadastrapp**, y compris les demandes d'information foncière et les autorisations géographiques. Conserver aussi l'ancienne version du backend, les scripts et les configurations. Tester la restauration sur une base de recette et prévoir le retour coordonné des données, du backend et des couches GeoServer. Prévoir une fenêtre pendant laquelle l'application ne lit pas un jeu de données partiellement rechargé.

Les scripts `database/` lancent plusieurs commandes `psql` successives : ils ne garantissent pas un basculement atomique. Vérifier les journaux et l'état final de chaque étape en recette.

### Conserver le nom du schéma QGIS actif

1. Copier ou sauvegarder **les données et la structure** du schéma QGIS actif dans un schéma d'archive distinct, et vérifier que la copie est exploitable. Garder aussi une sauvegarde restaurable de la base.
2. Sur une copie de recette, vérifier comment la version retenue du plugin purge les anciens lots et si les tables existantes acceptent le nouveau format. Son réimport par lot supprime des **lignes** ; il ne reconstruit pas nécessairement toutes les tables et colonnes déjà présentes.
3. Vider les données du millésime précédent du schéma actif par la procédure d'import validée, puis y importer le nouveau millésime sous le **même nom de schéma**. Ne pas mélanger les deux millésimes ni laisser d'anciens lots.
4. Si le changement de format impose de supprimer ou recréer des **tables**, planifier d'abord la suppression contrôlée des vues Cadastrapp qui en dépendent, puis leur reconstruction avec `cadastrapp_load_data.sh` après l'import. Un `DROP` des tables peut échouer à cause des dépendances ; `CASCADE` peut supprimer des objets applicatifs.

Une simple commande `ALTER SCHEMA ancien RENAME TO archive` suivie de la création d'un nouveau schéma portant l'ancien nom ne suffit pas : les vues PostgreSQL déjà créées restent liées aux anciennes tables, qui ont seulement changé de nom de schéma. Contrôler la définition des vues en recette avant la remise en service. La [documentation PostgreSQL sur les vues matérialisées](https://www.postgresql.org/docs/current/rules-materializedviews.html) explique que leur requête source est conservée lors d'un rafraîchissement.

## Importer les données avec QGIS

Dans QGIS, relever la version du plugin dans **À propos** puis suivre sa [procédure d'import](https://docs.3liz.org/QgisCadastrePlugin/extension-qgis/import/) : connexion PostGIS, **schéma actif conservant son nom après archivage de l'ancien millésime**, répertoires EDIGEO et MAJIC, version du **format**, millésime, département, direction, projections et lot. Conserver la correspondance entre lots et territoires pour éviter un écrasement involontaire.

Pour TOPO, contrôler le nom, la décompression et l'en-tête attendus par la version du plugin. Les règles ont changé au fil des versions : la documentation courante du plugin prime sur les anciennes instructions FANTOIR de certaines pages Cadastrapp. Lire le journal d'import jusqu'au bout. Contrôler en base les effectifs de quelques communes, parcelles, propriétaires et locaux connus, puis les couches dans QGIS et GeoServer.

## Alimenter Cadastrapp

Dans `database/`, vérifier `config.sh` (connexions, schémas, `uniqueDB`). Si le nom du schéma source actif est conservé, il n'y a normalement pas de changement de nom à y faire. Vérifier également que GeoServer lit toujours les bonnes couches et que ses connexions restent valides.

### EDIGEO seul, structure inchangée

Après import et contrôle de la source, lancer en recette :

```sh
cd database
./cadastrapp_update_data.sh
```

Ce script exécute `sql/vues/_update.sql` : il recalcule le contenu des vues matérialisées, sans modifier les requêtes SQL qui les définissent ni les tables de nomenclature. Si les données sont remplacées dans les **mêmes tables** du schéma actif et que la structure reste compatible, le rafraîchissement peut suffire pour une livraison EDIGEO seule. Changer seulement `qgisDBSchema` dans `config.sh` ne modifierait pas la requête des vues déjà créées ; cette difficulté est évitée en conservant le nom et les tables du schéma actif.

### MAJIC ou changement exigeant une reconstruction

Après validation de l'import QGIS et de la compatibilité des scripts SQL, lancer en recette :

```sh
cd database
./cadastrapp_load_data.sh
```

Ce script purge et recrée les tables de nomenclature et les vues matérialisées. Le nom du schéma source peut rester identique : les vues sont tout de même **reconstruites**, mais il n'est pas nécessaire de changer leurs références de schéma dans la configuration ni les connexions GeoServer si elles restent valides. Il préserve en principe les tables de demandes d'information foncière : `tables_request.sh` ne les crée que si elles n'existent pas. Cette conservation **ne remplace pas une sauvegarde** et ne migre pas leur structure. Vérifier les journaux SQL, les vues, les nomenclatures et les effectifs avant la remise en service.

Tester également les autorisations géographiques : la purge actuelle ne supprime pas `groupe_autorisation`, alors que `create_tables.sh` tente de la créer. Une base où elle existe déjà peut donc produire une erreur SQL. Traiter cet écart sur une copie de base, sans effacer les droits existants à l'aveugle.

## Vérifier la nécessité d'une migration backend

Avant de déployer une autre version de la webapp, vérifier :

1. Les vues et scripts SQL de la version visée trouvent-ils toutes les colonnes produites par le plugin retenu ?
2. La version backend ajoute-t-elle ou renomme-t-elle des objets SQL, des contraintes, des relations JPA ou des paramètres ?
3. Une migration est-elle livrée et a-t-elle réussi sur une copie représentative de la base ?
4. Les tables conservées par le rechargement, notamment celles des demandes, restent-elles compatibles ?

Si la réponse à 2 est oui, planifier et tester la migration propre à cette version **avant** son redémarrage. Si aucun changement du contrat backend/base n'est identifié, le seul renouvellement des données ne justifie pas un redéploiement. Valider cette conclusion par les contrôles fonctionnels.

## Contrôler et clôturer

- Tester les recherches de parcelle, propriétaire et commune, la fiche parcellaire, un bordereau et un relevé de propriété selon les droits d'un compte de test.
- Tester les droits géographiques avec deux profils distincts et, si utilisé, l'accès anonyme.
- Lire les journaux QGIS, `psql`, backend et GeoServer ; chercher les erreurs SQL, champs absents et vues non rafraîchies.
- Mettre à jour `pdf.dateValiditeDonneesMajic` et `pdf.dateValiditeDonneesEDIGEO` dans le `cadastrapp.properties` déployé avec les **dates réelles** des jeux de données, puis contrôler les PDF. Ces dates ne sont pas la version de la webapp.
- Consigner les versions, schémas, lots, sauvegardes, commandes, résultats de contrôle et décision de mise en service ou de retour arrière.

Voir aussi [l'alimentation de la base](https://github.com/georchestra/cadastrapp/blob/master/database/README.md), [l'installation des données](/guides_techniques/installer/donnees/) et [les dates des PDF](/guides_techniques/installer/webapp/#millesime-des-donnees).


## Compatibilité constatée par millésime

| Passage | Plugin Cadastre de QGIS | Backend Cadastrapp | Données Cadastrapp |
| --- | --- | --- | --- |
| 2025 → 2026 | Mettre à jour vers **2.3.0 ou une version ultérieure compatible** : la 2.3.0 ajoute l'import MAJIC et TOPO 2026. | **v2.2 compatible** avec les tables produites par l'import 2026 ; aucune mise à jour du backend n'est requise pour ce seul changement de millésime. | Recharger avec `cadastrapp_load_data.sh` si MAJIC change, puis effectuer la recette. |

