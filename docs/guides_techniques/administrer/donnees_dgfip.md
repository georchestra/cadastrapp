# Les données DGFiP


## À propos des données cadastrales en France


Le cadastre est un document fiscal qui sert à lever divers impôts et taxes liés au foncier. En France,  la [DGFiP](https://www.collectivites-locales.gouv.fr/le-cadastre-partenaire-des-collectivites-locales) met à la disposition des collectivités territoriales les données cadastrales brutes sous la forme de 3 jeux de données séparés :

- des données géographiques : le Plan Cadastral Informatisé (PCI) dans le format EDIGEO
- des données foncières : la matrice foncière dans le format MAJIC III
- un fichier de voies



## Les fichiers du plan cadastral

Chaque commune est subdivisée en sections, elles-mêmes subdivisées en feuilles (ou planches). Une feuille cadastrale comporte des parcelles, qui peuvent supporter des bâtiments, ainsi que de nombreux autres objets d’habillage ou de gestion. Le plan cadastral informatisé (PCI) est l'assemblage d’environ 600 000 feuilles. Il existe sous la forme de deux produits complémentaires : le PCI Vecteur et le PCI Image.

- Le PCI Vecteur regroupe les feuilles qui ont été numérisées et couvre l’essentiel du territoire.

- Le PCI Image regroupe les feuilles qui n’ont été que scannées, et complète la couverture.


Les données géographiques du PCI sont disponibles en *open data* ici : [https://cadastre.data.gouv.fr/datasets/plan-cadastral-informatise](https://cadastre.data.gouv.fr/datasets/plan-cadastral-informatise).


## Les fichiers de la matrice cadastrale


La matrice cadastrale est également parfois appelée matrice foncière.

Ensemble de 6 fichiers textes : BATI, NBAT, LLOC, PDLL, PROP.

- **BATI** : regroupe, par direction, l’ensemble des informations concernant le local et la partie d’évaluation (PEV). Il permet pour un local donné de disposer de son descriptif, de son évaluation et des bases de taxation. L’attribution du local à son propriétaire est assurée par l’intermédiaire du compte communal. Un local correspond à une seule déclaration fiscale (habitation ou commerciale); 

- **NBAT** :  recense l’ensemble des parcelles et des subdivisions fiscales cadastrées en France. Ce fichier permet de connaître, pour une parcelle donnée, les différentes natures de culture, les contenances et les revenus cadastraux des subdivisions fiscales qui la composent ainsi que son attribution à partir du compte communal de propriétaire; 

- **LLOC** : est une table de correspondance entre les identifiants des locaux (numéro invariant) et les indicatifs des lots correspondants;

- **PDLL** :  comporte la description des propriétés divisées en lots;

- **PROP** : regroupe, par direction, des informations concernant le compte communal et la personne. Le compte communal est composé de l’ensemble des personnes exerçant des droits concurrents sur un ou plusieurs biens d’une commune.Le fichier permet de disposer, pour un compte communal donné, des personnes (dans la limite de six) titulaires du compte, avec la désignation complète ou structurée, le droit exercé, l’adresse; 


**Les données de la matrice cadastrale ne sont pas en libre accès. Elles doivent être commandées et achetées auprès de la DGFiP.**


## Le fichiers des voies du cadastre

Le fichier **FANTOIR** répertorie pour chaque commune le nom des lieux-dits et des voies, y compris celles situées dans les lotissements et les copropriétés.

Il est en *open data* et est téléchargeable à cette page : [https://www.data.gouv.fr/fr/datasets/fichier-fantoir-des-voies-et-lieux-dits/](https://www.data.gouv.fr/fr/datasets/fichier-fantoir-des-voies-et-lieux-dits/).

**ATTENTION : ce fichier est censé disparaître !** Suivre l'actualité sur [cette page](https://www.data.gouv.fr/fr/datasets/objet-disparition-des-fichiers-fantoir-fimoca-et-fimoct-en-juillet-2023-mise-en-place-des-fichiers-topo-structures-uamissions-competences-et-acheminement/).