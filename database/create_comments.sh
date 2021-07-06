#! /bin/bash

# ce script va créer les commentaires sur les tables quand il en faut


echo "--------------------------------------------------------------"
echo " Création des commentaires sur les tables et les vues"
echo "--------------------------------------------------------------"
echo ""

# Création des commentaires

replaceAndLaunch sql/comments/Commune.sql
replaceAndLaunch sql/comments/Section.sql
replaceAndLaunch sql/comments/Parcelle.sql
replaceAndLaunch sql/comments/Proprietaire.sql
replaceAndLaunch sql/comments/CoProprieteParcelle.sql
replaceAndLaunch sql/comments/ProprietaireParcelle.sql
replaceAndLaunch sql/comments/ProprieteBatie.sql
replaceAndLaunch sql/comments/ProprieteNonBatie.sql
replaceAndLaunch sql/comments/ProprieteNonBatieSufExo.sql
replaceAndLaunch sql/comments/HabitationDetails.sql
replaceAndLaunch sql/comments/Lot.sql
replaceAndLaunch sql/comments/uf_parcelle.sql

echo "    Fait"
