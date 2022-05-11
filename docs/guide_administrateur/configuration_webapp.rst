
Configuration de la webapp
===============================

TODO à compléter !


L'application permet de filter les profils sur deux types de filtres.
Un filtre sur les droits CNIL avec deux niveaux et un filtre géographique

Niveaux de droits
------------------

* Anonyme : Accès aux infos publiques sur les parcelles
* CNIL1 : Anonyme + Visibilité des noms de propriétaires
* CNIL2 : CNIL1 + Visibilité des données de taxation


Gestion des droits géographiques
------------------------------------

Il est possible, en activant la fonction (cf. https://github.com/georchestra/cadastrapp/tree/master/database#si-on-souhaite-remonter-les-autorisations-geographiques-depuis-les-groupes-georchestra) de configurer le filtrage geographique via la zone de compétence de l'organisation renseignée depuis la console georchestra.

Si vous ne souhaitez pas activer cette fonctionnalité, il faudra réaliser cette configuration manuellement dans la table `groupe_autorisation`.

Par exemple pour autoriser le rôle `EXEMPLE` à accéder aux données de Haute-Loire, on utilisera : 

`INSERT INTO cadastrapp.groupe_autorisation(idgroup,ccodep) VALUES ('ROLE_EXEMPLE','043');`

`ccodep` corespond au code département.

Ou bien pour autoriser l'organisation 'Exemple' ayant le code court 'EXPL' à accéder aux données du Puy-en-Velay, on utilisera : 

`INSERT INTO cadastrapp.groupe_autorisation(idgroup,ccodep) VALUES ('EXPL','430157');`

`cgocommune` correspond au code département, puis au code de direction (généralement 0) puis au nuero INSEE de la commune.
