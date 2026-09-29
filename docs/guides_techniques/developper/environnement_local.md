# Environnement local du backend

Ce démarrage local sert à travailler sur la webapp. Le fichier `compose.dev.yml` démarre uniquement PostgreSQL ; le backend tourne sur la machine avec Maven et Jetty.

## Démarrer la base

Depuis la racine du dépôt, avec Docker Compose disponible et le port `5432` libre :

```sh
docker compose -f compose.dev.yml up -d --wait
```

La base `cadastrapp` est accessible sur `127.0.0.1:5432`. Les identifiants de développement correspondent à ceux de `cadastrapp/src/main/webapp/WEB-INF/jetty-env.xml` : utilisateur et mot de passe `www-data`. Le port est lié à l'interface locale de la machine.

Au premier démarrage, le conteneur crée le schéma `cadastrapp_qgis` et les tables nécessaires à la validation JPA, à partir de `database/sql/tables/request_information.sql` et `database/sql/tables/groupe_autorisation.sql`. Le volume Docker conserve la base entre les redémarrages. Les scripts d'initialisation ne sont rejoués que lorsque ce volume est vide.

Cette base ne contient **aucune donnée cadastrale QGIS**. Elle permet de démarrer la webapp et d'inspecter Swagger, mais les appels API qui lisent les vues cadastrales nécessitent une base alimentée selon la [procédure d'installation](../installer/donnees.md).

## Démarrer la webapp

Installer Java 17 et Maven. Vérifier que `mvn -version` indique Java 17 (sur Debian ou Ubuntu, définir au besoin `JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64`). Puis lancer depuis la racine du dépôt :

```sh
mvn -pl cadastrapp -am -DskipTests package
mvn -pl cadastrapp -Dgeorchestra.datadir="$PWD/dev/datadir" jetty:run
```

Le fichier `dev/datadir/default.properties` donne les paramètres requis pour initialiser le client LDAP, sans lancer de serveur LDAP ; les fonctions qui interrogent cet annuaire ne sont pas testables ici. Le plugin Jetty utilise le port `8287` et le contexte `/cadastrapp`. Ouvrir `http://localhost:8287/cadastrapp/swagger-ui.html` et contrôler que `http://localhost:8287/cadastrapp/services/v2/api-docs` renvoie du JSON. Pour l'issue #633, vérifier dans l'onglet Réseau du navigateur les requêtes Swagger au chargement de la page, puis confirmer que l'interface trouve l'API sans saisir `/cadastrapp/services` à la main.

## Arrêter ou réinitialiser

Arrêter Jetty avec `Ctrl+C`, puis, depuis la racine du dépôt :

```sh
docker compose -f compose.dev.yml down
```

Pour supprimer **uniquement la base de développement créée par ce Compose** et rejouer l'initialisation au prochain démarrage :

```sh
docker compose -f compose.dev.yml down -v
```

