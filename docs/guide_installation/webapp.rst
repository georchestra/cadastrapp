
Installation et configuration du service web
=================================================

Le service web peut aussi être dénommé webapp, API, backend.


Installation
-------------------

Voir `la documentation technique sur GithUb <https://github.com/georchestra/cadastrapp/blob/master/cadastrapp/README.md>`_.



Configuration
-------------------

La configuration de la webapp passe principalement par le fichier `cadastrapp.properties <https://github.com/georchestra/cadastrapp/blob/master/cadastrapp/src/main/resources/cadastrapp.properties>`_. Mais également parfois par des fichiers de configuration du serveur J2EE.


Source des données
^^^^^^^^^^^^^^^^^^^^^

La configuration de la connexion à la base de données se fait à 2 endroits.

Tout d'abord dans le fichier `server.xml` de Tomcat ou `jetty-env.xml` de Jetty : informations de connexion à la base de données (`plus d'informations <https://github.com/georchestra/cadastrapp/tree/master/cadastrapp#add-datasource>`_)

Puis dans le fichier `cadastrapp.properties <https://github.com/georchestra/cadastrapp/blob/master/cadastrapp/src/main/resources/cadastrapp.properties>`_ pour préciser le nom du schéma à utiliser : ``schema.name=app_cadastrapp``, par exemple.


Millésime des données
^^^^^^^^^^^^^^^^^^^^^^^^

Les différents documents PDF produits par Cadastrapp comportent un rappel sur la version des données utilisées. On parle souvent de *millésime*. On distingue toujours le millésime du plan et le millésime des données foncières car elles ne sont jamais en phase.

Le paramétrage de ces informations se fait dans le fichier `cadastrapp.properties <https://github.com/georchestra/cadastrapp/blob/master/cadastrapp/src/main/resources/cadastrapp.properties#L26-L28>`_

.. code-block::

  pdf.dateValiditeDonneesMajic=01/01/2022
  pdf.dateValiditeDonneesEDIGEO=01/03/2022





