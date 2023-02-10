# Préambule

Cadastrapp est un outil conçu pour permettre l'exploitation des [données cadastrales](./guide_administrateur/donnees.md)  fournies par la DGFiP aux collectivités territoriales.

Si la consultation du plan cadastral (la carte visible) ne connaît pas de restrictions, l'accès aux informations se rapportant aux bâtiments, aux propriétaires et aux autres informations fiscales nécessite d'être authentifié sur la plateforme geOrchestra.


## Contrôle du niveau d'accès aux données fiscales

Par défaut votre compte utilisateur n'est pas doté des accès permettant l'accès aux données sensibles contenues dans les données cadastrales. La matrice foncière contient en effet des données à caractère personnel. Les administrateurs doivent donc configurer votre compte pour que Cadastrapp vous laisse accéder à ces informations.

Il y a 2 types de limitations d'accès :

- le niveau :

  - niveau 0 : aucun accès aux données fiscales
  - niveau 1 : accès uniquement aux noms d'usage des propriétaires
  - niveau 2 : accès à toutes les données

- l'emprise géographique : vous ne pouvez accéder aux données que pour votre territoire de compétence


Pour tout changement ou demande d’accès aux informations sur les propriétaires, contacter les administrateurs de la plate-forme geOrchestra.



## Remerciements

La réalisation de ce guide a été rendue possible grâce à la contribution des utilisateurs de la cadastrapp.

Nous tenons à les remercier pour leur participation active et leur aide précieuse.


#Vocabulaire / Lexique
---------------------

##Les différentes surfaces des parcelles

**Contenance DGFiP** (ou surface DGFiP) : surface enregistrée dans les fichiers fonciers BATI et NBAT;

**Surface calculée** : superficie calculée par l'application à partir de la forme des parcelles ou des bâtiments issus du plan cadastral;



##Unité foncière

Îlot de propriété d'un seul tenant, composé d'une parcelle ou d'un ensemble de parcelles appartenant à un même propriétaire ou à la même indivision.


## Propriétaire -> compte communal

Dans le cadastre, les entités qui possèdent des droits sur des biens (des parcelles ou des lots de copropriété) sont les comptes communaux.

Un compte communal est composé de l’ensemble des personnes exerçant des droits concurrents sur unou plusieurs biens d’une commune. Un compte communal est composé de 1 à 8 propriétaires. Il peut s'agir de personnes physiques ou de personnes morales.


## Copropriété

Une copropriété est un ou plusieurs lots dans un immeuble appartenant à plusieurs comptes communaux. 

Généralement, dans un immeuble, chaque copropriétaire dispose de parties privatives et d'une quote-part de parties communes appelée "tantièmes de copropriété".


## CSV

Comma-Separated Values, connu sous le sigle CSV, est un format texte ouvert représentant des données tabulaires sous forme de valeurs séparées par un séparateur, le plus souvent une virgule.