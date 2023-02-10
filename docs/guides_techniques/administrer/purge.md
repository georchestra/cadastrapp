# Purge des fichiers temporaires

Cadastrapp génère sur le serveur des fichiers temporaires nécessaires pour créer les fichiers PDF demandés par les utilisateurs. Cela peut constituer des volumes non négligeables selon l'intensité d'utilisation de Cadastrapp.

Les fichiers générés par l'application sont déposés dans le répertoire 'tempFolder' paramétré dans le fichier `cadastrapp.properties <https://github.com/georchestra/cadastrapp/blob/master/cadastrapp/src/main/resources/cadastrapp.properties>`_.

Ces fichiers sont supprimés à chaque redémarrage de la JVM.

Ces fichiers peuvent aussi être purgés à la main, ou par un script que vous pouvez créer.

Depuis la version 1.5, l'ordonnanceur Quartz va supprimer l'ensemble des fichiers du répertoire temporaire, et ce en fonction de la configuration de deux paramètres du fichier `cadastrapp.properties <https://github.com/georchestra/cadastrapp/blob/master/cadastrapp/src/main/resources/cadastrapp.properties>`_.

`purge.hours=24` qui correspond aux nombres d'heures à partir du quel un fichier peut être supprimé et `purge.cronExpression=0 0 * * * ?` Qui correspond à quand le processus de vérification et suppression doit être lancé.

Dans l'exemple ici, toutes les heures, un processus va supprimer tous les fichiers qui ont plus de 24 h dans le répertoire temporaire.

