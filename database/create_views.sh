#! /bin/bash

# ce script va créer les vues matérialisées



echo "--------------------------------------------------------------"
echo " Création des vues matérialisées"
echo "--------------------------------------------------------------"
echo ""

replaceAndLaunch sql/vues_dblink/commune.sql
replaceAndLaunch sql/vues_dblink/Parcelle.sql
replaceAndLaunch sql/vues_dblink/Proprietaire.sql
replaceAndLaunch sql/vues_dblink/CoProprieteParcelle.sql
replaceAndLaunch sql/vues_dblink/ProprietaireParcelle.sql
replaceAndLaunch sql/vues_dblink/ProprieteBatie.sql
replaceAndLaunch sql/vues_dblink/ProprieteNonBatie.sql
replaceAndLaunch sql/vues_dblink/ProprieteNonBatieSufExo.sql
replaceAndLaunch sql/vues_dblink/Section.sql
replaceAndLaunch sql/vues_dblink/HabitationDetails.sql
replaceAndLaunch sql/vues_dblink/Lot.sql
replaceAndLaunch sql/vues_dblink/uf_parcelle.sql

echo "    Fait"


echo "--------------------------------------------------------------"
echo " Création des indexes sur les vues matérialisées"
echo "--------------------------------------------------------------"
echo ""

replaceAndLaunch sql/indexes/indexes_vues_materialisees.sql

echo "    Fait"

