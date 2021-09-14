#! /bin/bash

# ce script va créer les tables


echo "--------------------------------------------------------------"
echo " Création des tables"
echo "--------------------------------------------------------------"
echo ""

# Création des tables

replaceAndLaunch sql/tables/prop_ccocac.sql
replaceAndLaunch sql/tables/prop_ccocac_simple.sql
replaceAndLaunch sql/tables/prop_ccodem.sql
replaceAndLaunch sql/tables/prop_ccodro.sql
replaceAndLaunch sql/tables/prop_ccoqua.sql
replaceAndLaunch sql/tables/prop_ccogrm.sql
replaceAndLaunch sql/tables/prop_dnatpr.sql
replaceAndLaunch sql/tables/prop_dmatto.sql
replaceAndLaunch sql/tables/prop_dmatgm.sql
replaceAndLaunch sql/tables/prop_type_filiation.sql
replaceAndLaunch sql/tables/prop_bati_detent.sql

if [ "$orgsAutorisations" = True ] ; then
  replaceAndLaunch sql/ldap/organisations.sql
  replaceAndLaunch sql/ldap/groupe_autorisation.sql
else
  replaceAndLaunch sql/tables/groupe_autorisation.sql
fi

echo "    Fait"
