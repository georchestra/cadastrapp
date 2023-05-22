# Sélection et recherche

## Sélection de parcelles depuis la carte

La sélection d'une parcelle dans la carte peut s'effectuer avec l'un des trois outils suivants :

![image](./images/selection_carte_1.png)


### Sélection par un point
Lorsque l'outil est actif, il apparait sur fond vert. 

![image](./images/selection_carte_2.png)

L'outil sélection d'une parcelle par simple clic permet de sélectionner une parcelle à chaque clic dans la carte. La liste des parcelles sélectionnées apparait dans le panneau à droite de l'écran sous forme de tableau.

![image](./images/selection_carte_3.png)


### Sélection par une polyligne
--------------------------------------
Lorsque l'outil de sélection par une polyligne est actif, il apparait sur fond vert. 


![image](./images/selection_carte_4.png)

Il s'agit de tracer une ligne brisée, toutes les parcelles coupées par cette polyligne seront sélectionnées.
Pour arrêter le tracé de la polyligne il faut faire un double clic.
La sélection peut-être complétée en dessinant une nouvelle polyligne. Les parcelles seront ajoutées à la selection obtenue précédemment. Les parcelles vont ainsi s'ajouter dans le tableau "**Sélection de parcelles**" du panneau droit.


### Sélection par un polygone
Lorsque l'outil de sélection par une polygone est actif, il apparait sur fond vert. 

![image](./images/selection_carte_5.png)

L'outil de sélection d'une parcelle par un polygone permet de dessiner une surface et de sélectionner toutes les parcelles ayant un point commun avec cette forme.
Le dessin d'un autre polygone completera la première sélection de parcelles dans le tableau de "**Sélection de parcelles**".

![image](./images/selectioncarte6.png)


### Résultat d'une sélection
--------------------------------------
Lorsqu'une sélection est effectuée, la liste des parcelles apparaît dans le tableau "**Sélection de parcelles**"


Passer maintenant à la section détaillant la [manipulation des résultats des sélections](#manipulationresultatselection).




## Recherche de parcelles

La recherche de parcelle s'active avec le bouton :

![image](./images/recherche_parc1.png)

Il est possible d'effectuer des recherches selon les critères suivants :

- Référence cadastrale
- Adresse cadastrale
- Identifiant cadastral
- Lot

Choisir l'onglet correspondant aux éléments disponibles pour la recherche.


### Par référence cadastrale

Depuis l'onglet **Référence**, il faut d'abord choisir la commune, puis la section puis la parcelle.

Pour la saisie de la **ville / de la commune**, il est nécéssaire d'écrire à minima les 3 premiers caractères du nom de la commune. Une liste déroulante affiche les noms de communes ainsi que leur code INSEE.

Les majuscules n'ont pas d'importance, par contre les tirets, les espaces et les apostrophes ont une importance car ils sont pris en compte dans la recherche.

Une fois la commune de recherche sélectionnée dans la liste déroulante, choisir la section dans la liste puis le numéro de la parcelle. Enfin, cliquer sur le bouton **Rechercher**.


![image](./images/recherche_parc2.png)

![image](./images/recherche_parc3.gif)


Il est possible d'ajouter d'autres recherches avec le bouton **+**. La corbeille permet d'effacer une des recherches.

![image](./images/recherche_parc4.png)

![image](./images/recherche_parc5.png)

Enfin en cliquant sur **Effacer**, l'onglet de recherche par référence est réinitialisé, les valeurs prédemment saisies sont effacées.

![image](./images/recherche_parc6.png)


### Par adresse cadastrale


/!\ **Attention il s'agit bien ici d'une recherche dans les données cadastrales** fournies par la DGFiP. La plupart du temps la fraîcheur de ces informations est en retard par rapport à d'autres sources de données. Plus particulièrement les référentiels voies-adresses locaux, la Base Adresse Nationale. Votre plate-forme geOrchestra propose peut-être des recherches utilisant ces données plus à jour.


Depuis l'onglet **Adresse cadastrale**, il faut d'abord saisir le nom de la commune ou de la ville. Après avoir saisi les 3 premiers caractères du nom,  une liste déroulante permet de choisir la commune. Le code INSEE de la commune permet d'affiner son choix, si nécessaire.

Ensuite commencer la saisie du nom de la voie ou du lieu-dit, **il s'agit ici de saisir le mot directeur** (ex : « mairie »), et de choisir ensuite dans la liste déroulante. Il ne faut pas commencer par saisir le type de la voie.

Il est possible d'affiner la recherche en indiquant le numéro dans la voie ou une information complémentaire.

Enfin cliquer sur le bouton **Rechercher**.

![image](./images/recherche_parc7.gif)


### Par identifiant cadastral


L'onglet **Identifiant cadastral** permet de sélectionner une parcelle depuis son identifiant cadastral.

L'identifiant cadastral contient 19 caractères structuré comme suit :

* code de département sur 2 caractères. Ex : « 35 »
* code de direction DGFiP sur 1 caractère. Ex : « 0 »
* code de commune sur 3 caractères. Ex : « 238 »
* préfixe de section sur 3 caractères. Ex : « 000 »
* section sur 2 caractères. Ex : « AB »
* parcelle sur 4 caractères. Ex : « 0145 »

Terminer en  cliquant sur le bouton **Rechercher**.

![image](./images/recherche_parc8.gif)


### Par lot


L'onglet **Lot** permet de sélectionner un ensemble de parcelle depuis une liste d'identifiants cadastraux.

Il est possible soit :

- de saisir les identifiants en les séparant par une virgule dans **Liste d'identifiants**
- de déposer ou de pointer vers un fichier au format txt ou csv comprenant une liste d'identifiants cadastraux séparés par des virgules, des espaces, des points-virgules ou des retours à la ligne.

![image](./images/recherche_parc9.gif)



### Résultat d'une sélection


Lorsqu'une sélection est effectuée, la liste des parcelles apparaît dans le tableau "**Sélection de parcelles**".

Passer maintenant à la section détaillant la [manipulation des résultats des sélections](#manipulationresultatselection).




## Recherche de parcelles à partir des propriétaires


La recherche de propriétaires s'obtient avec le bouton :


![image](./images/recherche_proprio1.png)

Plusieurs types de recherche sont possibles en fonction des informations connues : 

- nom du propriétaire : nom d'usage ou nom de naissance
- compte propriétaire
- une liste de comptes propriétaires

Á chacune de ces possibilités correspond un onglet différent.


### Propriétaire -> compte communal


Lire [cette section](../preambule/#prioprietairecomptecommunal) pour comprendre la différence entre un propriétaire et un compte communal.





### Recherche par le nom du propriétaire

#### Cas simple


Il faut d'abord saisir le nom de la commune sur laquelle effectuer la recherche grâce à la liste déroulante.

Saisir ensuite le nom et / ou le prénom du propriétaire recherché. **Une liste de suggestions apparaît. Elle est affinée à chaque caractère supplémentaire saisi.**

Si le nom du propriétaire désiré apparaît : le sélectionner puis valider la recherche en cliquant sur le bouton **Rechercher**.

![image](./images/recherche_parc2.png)

![image](./images/proprietaire_cas_simple.gif)

Vous pouvez ré-initialiser la fenêtre de recherche en cliquant sur le bouton **Effacer**

![image](./images/recherche_parc6.png)


#### Cas des homonymies / Sortie de la recherche assistée


Si vous ne trouvez pas / pas facilement le propriétaire recherché dans la liste déroulante, vous pouvez sortir du mode assisté de la liste en appuyant sur le bouton « Echap » du clavier.

Valider ensuite la recherche en cliquant sur le bouton **Rechercher**.

Une nouvelle fenêtre va s'ouvrir. Elle présente la liste des propriétaires correspondant à la chaîne de caractères recherchée.

Il faut ensuite sélectionner le propriétaire souhaité puis cliquer sur **Voir les parcelles**.

![image](./images/recherche_proprio2.gif)


Vous pouvez ré-initialiser la fenêtre de recherche en cliquant sur le bouton **Effacer**

![image](./images/recherche_parc6.png)



### Recherche par le compte propriétaire


Depuis l'onglet Compte Propriétaire, il faut commencer par saisir le nom de la commune sur laquelle effectuer la recherche,  puis le compte propriétaire.

Le compte propriétaire correspond à 6 caractères avec le premier caractère qui est soit une lettre s'il s'agit d'une personne physique soit un "+" ou une "*" pour les personnes morales. Les 5 autres caractères correspondent à un numéro d'ordre.

Valider la recherche en cliquant sur le bouton **Rechercher**

![image](./images/recherche_parc2.png)


Vous pouvez effectuer la recherche sur plusieurs comptes propriétaires en cliquant sur le bouton + pour ajouter une nouvelle zone de saisie.

![image](./images/recherche_proprio4.gif)

Vous pouvez ré-initialiser la fenêtre de recherche en cliquant sur le bouton **Effacer**

![image](./images/recherche_parc6.png)


### Recherche par lot 


Pour effectuer une recherche par lot, vous devez disposer dans un fichier CSV d'une liste de comptes communaux (code de la commune et compte communal). Le séparateur de champs (séparateur de colonnes) peut être au choix :

- des retours à ligne
- des espaces
- des virgules
- des points-virgules



Il suffit ensuite de faire glisser votre fichier texte dans l'onglet **Par lot** ou de cliquer dans l'espace pour  ouvrir une boite de dialogue et de naviguer jusqu'au fichier texte à pointer.

Valider la recherche en cliquant sur le bouton **Rechercher**

![image](./images/recherche_proprio6.gif)


Vous pouvez ré-initialiser la fenêtre de recherche en cliquant sur le bouton **Effacer**

![image](./images/recherche_parc6.png)




### Résultat d'une sélection

Lorsqu'une sélection est effectuée, la liste des parcelles apparaît dans le tableau "**Sélection de parcelles**"


Passer maintenant à la section détaillant la [manipulation des résultats des sélections](#manipulationresultatselection).




Recherche de parcelles à partir des copropriétaires
===================================================

La recherche de copropriétaires s'obtient avec le bouton :

![image](./images/recherche_coproprio1.png)


### Qu'est-ce qu'une copropriété ?


Lire [cette section](../preambule/#prioprietairecomptecommunal) pour comprendre la définition d'une copropriété.


### Les types de recherche possibles


Plusieurs types de recherche sont possibles en fonction des informations connues. Il faut, a minima, saisir la commune et un des éléments suivant :

- nom et/ou prénom d'un co-propriétaire 
- identifiant de la parcelle
- compte communal


### Recherche par nom et/ou prénom d'un copropriétaire

Après avoir choisi une commune, commencer à saisir le nom du co-propriétaire et le sélectionner dans la liste ou sortir de la liste des propositions  en appuyant sur le bouton « Echap » du clavier.

![image](./images/recherche_coproprio.gif)

Valider la recherche en cliquant sur le bouton **Rechercher**

![image](./images/recherche_parc2.png)


### Recherche par identifiant de la parcelle


Après avoir choisi une commune, saisir (ou copier-coller) l'identifiant de la parcelle.



![image](./images/recherche_coproprio3.gif)


Valider la recherche en cliquant sur le bouton **Rechercher**

![image](./images/recherche_parc2.png)




### Recherche par compte communal


Après avoir choisi une commune, saisir (ou copier-coller) le compte communal.



![image](./images/recherche_coproprio5.gif)

Valider la recherche en cliquant sur le bouton **Rechercher**.

![image](./images/recherche_parc2.png)




### Résultat d'une sélection

Lorsqu'une sélection est effectuée, la liste des parcelles apparaît dans le tableau "**Sélection de parcelles**".


Passer maintenant à la section détaillant la [manipulation des résultats des sélections](#manipulationresultatselection).




##<a id="manipulationresultatselection"></a> Manipulation des résultats des sélections 


Lorsqu'une sélection est effectuée, par interaction avec la carte ou par utilisation des fonctions de recherche, une liste des parcelles apparaît dans le tableau "**Sélection de parcelles**" qui est située dans une zone en-dessous des outils de recherche.

![image](./images/selectioncarte7.png)

Chaque ligne du tableau correspond à une parcelle sélectionnée sur la carte. Les informations affichées concernent la commune, la section cadastrale, l'adresse cadastrale, le numéro de la parcelle ainsi que la contenance DGFiP exprimée en m².

Il est possible de trier les parcelles sur l'une de ces informations en cliquant sur l'entête de la colonne.

![image](./images/selectioncarte8.png)

Depuis cette liste, il est possible de zoomer sur tous les parcelles de la liste avec le bouton "**zoom liste**".

![image](./images/selectioncarte10.png)

En cochant la case en début de ligne, la parcelle correspondante apparaît en sur-sélection sur la carte. Dans l'exemple ci-dessous la liste comprend 5 parcelles dont 1 est sur-sélectionnée.


![image](./images/selectioncarte9.png)

**Une fois une (ou plusieurs) parcelle(s) sur-sélectionnée(s), les outils suivants sont rendus accessibles :**

![image](./images/selectioncarte11.png)

De gauche à droite, ces boutons permettent :

- de recadrer la carte sur l'emprise correspondant à toutes les parcelles de la liste 
- d'afficher les informations sur l'unité foncière d'appartenance de la parcelle sélectionnée
- de supprimer la parcelle sélectionnée de la liste
- d'afficher la fiche d'information parcellaire dans une nouvelle fenêtre
- d'exporter dans un fichier CSV la sélection de :

  - parcelles
  - propriétaires
  - co-propriétaires
  - lots d'une co-propriété


![image](./images/selectioncarte12.png)

La barre d'outils placée juste au-dessus de la liste est composée des 2 outils suivants, permettant de supprimer un onglet de résultat de sélection ou d'en ajouter un.

![image](./images/selectioncarte13.gif)


## Export des résultats des sélections

Lorsque l'on dispose d'une sélection de une ou plusieurs parcelles dans le tableau "**Sélection de parcelles**", toutes les commandes permettant de manipuler cette sélection sont activées. Nous allons nous focaliser sur le dernier bouton qui est un bouton qui propose une sous-liste de fonctionnalités lorsque l'on clique dessus :

![image](./images/export_selection_menu.png)

Cette page détaille les différents exports disponibles.

.. note::
  Les fonctionnalités concernant des données à caractère personnel (comme les noms des propriétaires) ne seront disponibles que si vous disposez d'un [accès à ces données fiscales](../preambule/#controleniveauaccesdonneesfiscales).





### Export de la liste de parcelles

![image](./images/export_selection_parcelles.png)

Cette commande provoque la création d'un fichier CSV qui contient la liste des parcelles de votre sélection.

![image](./images/export_selection_csv.png)

Ce fichier peut ensuite être rechargé ultérieurement afin de refaire une `sélection de parcelles par lot <recherche_parcelle.html#par-lot>`_.



### Export de la liste des propriétaires


![image](./images/export_selection_proprietaires.png)

Cette commande provoque la création d'un fichier CSV qui contient la liste des "**propriétaires des parcelles**" de votre sélection.

Les informations disponibles dans cet export sont :

- dénomination d'usage du/des propriétaires
- dénomination de naissance du/des propriétaires
- adresse déclarée
- lieu de naissance
- date de naissance



### Export de la liste des copropriétaires

![image](./images/export_selection_coproprietaires.png)

Cette commande provoque la création d'un fichier CSV qui contient la liste des "**copropriétaires des parcelles**" de votre sélection.

Les informations disponibles dans cet export sont :

- dénomination d'usage du/des propriétaires
- dénomination de naissance du/des propriétaires
- adresse déclarée
- lieu de naissance
- date de naissance


### Export de la liste des lots de copropriété

![image](./images/export_selection_lots_copro.png)

![image](./images/export_selection_lots_copro_formats.png)


Cette commande provoque la création d'un fichier CSV ou d'un document PDF qui contient la liste des "**lots de copropriété**" de votre sélection.
Ces 2 fichiers sont très différents dans leur forme.

Les informations disponibles dans l'export CSV sont :

- numéro de parcelle
- numéro de local
- identification du bâtiment qui abrite le lot
- part du lot dans le total de la copropriété
- nombre total de parts dans la copropriété
- logement : si le lot est un logement, précise la typologie du logement
- dependance : si le lot est une dépendance, précise la typologie de la dépendance
- local_commercial : si le lot est un local commercial, précise la typologie du local commercial
- type_proprietaire : indique la typologie du droit réel du compte propriétaire du lot
- compte_communal : code du compte communal propriétaire du lot
- dénomination du proprietaire du lot
- adresse déclarée du proprietaire du lot

L'export PDF est une mise en forme beaucoup plus lisible des information brutes du fichier CSV. Il est en outre prêt à être imprimé.



## Impression et annotations

### Impression d'une sélection

La visionneuse permet également d'imprimer une sélection de parcelles.
Pour cela, cliquer sur l'icône d'impression en haut à droite de l'écran.
Dans la fenêtre d'impression qui s'ouvre, il est possible de renseigner/choisir:


- un titre,
- une description,
- une mise en page (taille du papier, choix de la légende, orientation de la page),
- des options de légende s'il y en a une.

Cliquer ensuite sur le bouton Imprimer.


![image](./images/impression.gif)

Il est possible de changer le fond de plan, en cliquant sur  **sélection du fond de plan** en bas à gauche de l'écran.

.. warning::
  Une fois le fond de carte choisi, il faut cliquer sur l'écran de la visionneuse derrière la fenêtre impression pour mettre à jour le fond de plan de la fenêtre d'impression, comme montré dans le gif ci-dessous.

![image](./images/impression_fond.gif)

### Annotations

Il est possible d'enregistrer momentanément votre sélection de parcelles, et de l'afficher sous Mapstore avec d'autres données (occupation du sol, orthophoto...)
Pour cela, il faut créer une **annotation**.

- Faire la sélection des parcelles souhaitées, puis sursélectionnez-les en les cochant,
- Cliquer sur l'icône **Enregistrer en tant qu'annotation**,
- Choisir le titre de l'annotation, ainsi que sa description si besoin,
- Cliquer sur **Enregistrer**.
	
Si la fenêtre de l'outil Cadastrapp est fermée, la sélection est toujours présente sous la forme d'annotation.
Il est possible de la voir en cliquant sur le menu **Couches** en haut à gauche de l'écran Mapstore, comme montré ci-dessous.

![image](./images/annotation2.gif)

Pour ajouter des couches dans Mapstore, en plus de votre sélection de cadastre, cliquer sur l'icône **Ajouter une couche**

![image](./images/ajouter_couche.PNG)

Et faire une recherche pour la couche souhaitée.


