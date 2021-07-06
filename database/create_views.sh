#! /bin/bash

# ce script va créer les vues matérialisées



echo "--------------------------------------------------------------"
echo " Création des vues matérialisées"
echo "--------------------------------------------------------------"
echo ""

if [ "$uniqueDB" = True ] ; then

  replaceAndLaunch sql/vues/Commune.sql
  replaceAndLaunch sql/vues/Section.sql
  replaceAndLaunch sql/vues/Parcelle.sql
  replaceAndLaunch sql/vues/Proprietaire.sql
  replaceAndLaunch sql/vues/CoProprieteParcelle.sql
  replaceAndLaunch sql/vues/ProprietaireParcelle.sql
  replaceAndLaunch sql/vues/ProprieteBatie.sql
  replaceAndLaunch sql/vues/ProprieteNonBatie.sql
  replaceAndLaunch sql/vues/ProprieteNonBatieSufExo.sql
  replaceAndLaunch sql/vues/HabitationDetails.sql
  replaceAndLaunch sql/vues/Lot.sql
  replaceAndLaunch sql/vues/uf_parcelle.sql

elif [ "$uniqueDB" = False ] ; then

  replaceAndLaunch sql/vues_dblink/Commune.sql
  replaceAndLaunch sql/vues_dblink/Section.sql
  replaceAndLaunch sql/vues_dblink/Parcelle.sql
  replaceAndLaunch sql/vues_dblink/Proprietaire.sql
  replaceAndLaunch sql/vues_dblink/CoProprieteParcelle.sql
  replaceAndLaunch sql/vues_dblink/ProprietaireParcelle.sql
  replaceAndLaunch sql/vues_dblink/ProprieteBatie.sql
  replaceAndLaunch sql/vues_dblink/ProprieteNonBatie.sql
  replaceAndLaunch sql/vues_dblink/ProprieteNonBatieSufExo.sql
  replaceAndLaunch sql/vues_dblink/HabitationDetails.sql
  replaceAndLaunch sql/vues_dblink/Lot.sql
  replaceAndLaunch sql/vues_dblink/uf_parcelle.sql

fi

echo "    Fait"


echo "--------------------------------------------------------------"
echo " Création des indexes sur les vues matérialisées"
echo "--------------------------------------------------------------"
echo ""

replaceAndLaunch sql/indexes/indexes_vues_materialisees.sql

echo "    Fait"

