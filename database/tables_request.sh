#! /bin/bash

# ici on traite à part les tables liées aux demandes d'information foncière
# si elles n'existent pas --> on les crée
# si elles existent --> on ne touche à rien et on signale

echo "--------------------------------------------------------------"
echo " Tables demandes info foncière"
echo "--------------------------------------------------------------"
echo ""

# on fait une requête à la base

test=`PGPASSWORD=$cadastrappDBPassword psql -h $cadastrappDBHost -p $cadastrappDBPort -d $cadastrappDBName -U $cadastrappDBUser \
    -c "SELECT count(*) FROM $cadastrappDBSchema.request_information ;" \
    --single-transaction \
    --set AUTOCOMMIT=off \
    --set ON_ERROR_STOP=on \
    --no-align \
    -t \
    --field-separator ' ' &>/dev/null`

#echo "test = $test"

if [ -z "$test" ] ; then
  # vide = pas de table
  echo "Les tables n'existent pas, le script va donc les créer."

  replaceAndLaunch sql/tables/request_information.sql

else
  # non vide = les tables existent
  echo "    Les tables existent déjà dans le schéma $cadastrappDBSchema !"
  echo "    Elles restent inchangées avec leur contenu."
  echo ""

fi



echo "    Fait"
