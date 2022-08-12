
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

Si vous ne souhaitez pas activer cette fonctionnalité, il faudra réaliser cette configuration manuellement dans la table ``groupe_autorisation``.

Par exemple pour autoriser le rôle ``EXEMPLE`` à accéder aux données de Haute-Loire, on utilisera : 

``INSERT INTO cadastrapp.groupe_autorisation(idgroup,ccodep) VALUES ('ROLE_EXEMPLE','043');``

``ccodep`` corespond au code département.

Ou bien pour autoriser l'organisation 'Exemple' ayant le code court 'EXPL' à accéder aux données du Puy-en-Velay, on utilisera : 

``INSERT INTO cadastrapp.groupe_autorisation(idgroup,ccodep) VALUES ('EXPL','430157');``

``cgocommune`` correspond au code département, puis au code de direction (généralement 0) puis au numero INSEE de la commune (ici 157).

Configuration des logs
-----------------------

Il est possible de configurer les logs de cadastrapp, pour cela il faut créer un fichier ``cadastrapp/log4j/log4j2.properties`` dans le datadir georchestra.

Un exemple de ce fichier peut être trouvé `ici <https://github.com/georchestra/cadastrapp/tree/master/cadastrapp/src/main/resources/log4j2.properties>`_.

Avec la configuration par défaut, seuls les logs de niveau WARNING sont affichés dans le fichier ``/tmp/cadastrapp.log`` (configuration de production).
Si vous souhaitez afficher plus de logs, vous pouvez configurer les differents loggers avec ``level = info`` ou ``level = debug``.

Des logs sur la génération de documents dans cadastrapp sont également produits, la section ci-dessous documente leur utilisation.

Logs sur la génération de documents
------------------------------------

Par défaut, les logs sur la génération de documents sont faits dans le fichier ``/tmp/cadastrapp_audit.log``.

Chaque ligne comporte les mêmes infos que les logs classiques Cadastrapp, à savoir ce layout :

``%d %-5p [%c] %X{uri} - $${ctx:sec-username:-nouser} - $${ctx:sec-roles:-norole} - $${ctx:sec-org:-noorg} - %m%n``

Ci-dessous la composition du message est détaillée (%m) : 

**Bordereau parcellaires :**

Exemple :

``Bordereau Parcellaire - {Methode} - {Demande} - {Parcelle} - {WithProps} - {Copro}``

* Methode => Moyen par lequel le document a été généré
* Demande (ID) => Identifiant de la demande si existe, sinon null
* Parcelle (ID) => Identifiant de la parcelle représentée
* WithProps (0/1) => 1 si avec propriétaires, 0 sinon
* Copro (true/false) => Demande de type Copro

Methodes pour les BP :

* GenerationDirecte
* DemandeParcelleId
* DemandeCCId
* DemandeInfoParcelle
* DemandeInfoProp
* DemandeLot

**Relevés de propriété :**

Exemple :

``Releve de propriete - {Methode} - {Demande} - {CompteCommunal} - {Parcelle} - {Minimal} - {Format}``

* Methode => Moyen par lequel le document a été généré
* Demande (ID) => Identifiant de la demande si existe, sinon null
* CompteCommunal (ID) => Identifiant du compte communal
* Parcelle (ID) => Identifiant de la parcelle exporté si renseignée, sinon null
* Minimal (true/false) => false si export complet, true si minimal
* Format (PDF/CSV) => Format d'export

Methodes pour les RP :

* GenerationDirecte
* DemandeCCId
* DemandeParcelleId
* DemandeCoProCCParcelleId
* DemandeInfoParcelle
* DemandeInfoProp
* DemandeLot

**Demandes**

Exemple :

``Demande - {Demande} - {UserType}``

* Demande (ID) => Identifiant de la demande
* UserType => Type de demandeur

UserType pour les Demandes :

* A => Administration
* P1 => Particulier détendeur des droits
* P2 => Particulier agissant en qualité de mandataire
* P3 => Particuliers tier

**Exports CSV**

Exemple :

``Export CSV - {Type} - {Params}``

* Type =>Type d'export demandé
* Params => Parametres fournis pour l'export

Type d'exports :

* Parcelles
* Propriétaires
* CoPropriétaires
* Lots
* ComptesCommunaux

Logs sur génération de documents dans BDD
------------------------------------------

Cette fonctionnalité n'est pas activée par defaut, mais est commentée dans le `fichier de configuration log4j2 <https://github.com/georchestra/cadastrapp/tree/master/cadastrapp/src/main/resources/log4j2.properties>`_.

Pour l'activer : 

* renseigner les propriétés en haut de fichier (dbUser,dbPassword,dbConnectionString) et les décommenter
* décommenter les configuration des appenders sous `## Configuring JDBC appenders` (dbBP, dbRP, dbDEM, dbEXP)
* décommenter l'ajout de ces mêmes appenders au logger `documents` en fin de fichier

Voici les script pour les tables à créer en BDD, à adapter à votre convenance :

``
CREATE TABLE IF NOT EXISTS public.cadastrapp_bp
(
    username text,
    log_date timestamp with time zone NOT NULL,
    uri text,
    organisation text,
    roles text,
    message text,
    methode text GENERATED ALWAYS AS ( split_part(message,' - ',2) ) STORED,
    demande text GENERATED ALWAYS AS ( split_part(message,' - ',3) ) STORED,
    parcelle text GENERATED ALWAYS AS ( split_part(message,' - ',4) ) STORED,
    proprietaires text GENERATED ALWAYS AS ( split_part(message,' - ',5) ) STORED,
    copro text GENERATED ALWAYS AS ( split_part(message,' - ',6) ) STORED
);
ALTER TABLE public.cadastrapp_bp OWNER TO cadastrapp;

CREATE TABLE IF NOT EXISTS public.cadastrapp_rp
(
    username text,
    log_date timestamp with time zone NOT NULL,
    uri text,
    organisation text,
    roles text,
    message text,
    methode text GENERATED ALWAYS AS ( split_part(message,' - ',2) ) STORED,
    demande text GENERATED ALWAYS AS ( split_part(message,' - ',3) ) STORED,
    ccomunal text GENERATED ALWAYS AS ( split_part(message,' - ',4) ) STORED,
    parcelle text GENERATED ALWAYS AS ( split_part(message,' - ',5) ) STORED,
    minimal text GENERATED ALWAYS AS ( split_part(message,' - ',6) ) STORED,
    format text GENERATED ALWAYS AS ( split_part(message,' - ',7) ) STORED
);
ALTER TABLE public.cadastrapp_rp OWNER TO cadastrapp;

CREATE TABLE IF NOT EXISTS public.cadastrapp_demande
(
    username text,
    log_date timestamp with time zone NOT NULL,
    uri text,
    organisation text,
    roles text,
    message text,
    demande text GENERATED ALWAYS AS ( split_part(message,' - ',2) ) STORED,
    usertype text GENERATED ALWAYS AS ( split_part(message,' - ',3) ) STORED
);
ALTER TABLE public.cadastrapp_demande OWNER TO cadastrapp;

CREATE TABLE IF NOT EXISTS public.cadastrapp_export
(
    username text,
    log_date timestamp with time zone NOT NULL,
    uri text,
    organisation text,
    roles text,
    message text,
    type text GENERATED ALWAYS AS ( split_part(message,' - ',2) ) STORED,
    params text GENERATED ALWAYS AS ( split_part(message,' - ',3) ) STORED
);
ALTER TABLE public.cadastrapp_export OWNER TO cadastrapp;
``

*Note : Si vous souhaitez optimiser la gestion des logs en BDD pour de gros volumes, il est possible d'utiliser l'`extension timesclaedb <https://docs.timescale.com/install/latest/self-hosted/installation-debian/>`_ de PostgeSQL.*