#! /bin/bash

# ce script va supprimer toutes les tables et les vues / vues matérialisées
# sauf les tables relatives aux demandes d'information foncière


echo "--------------------------------------------------------------"
echo " Purge de la BD $cadastrappDBName"
echo "--------------------------------------------------------------"
echo ""

# Suppression des tables
echo "  Suppression des tables"
replaceAndLaunch sql/tables/_drop.sql
echo "    Fait"

echo ""

if [ "$uniqueDB" = True ] ; then
  # Suppression des vues simples
  echo "  Suppression des vues simples"
  # TODO

elif [ "$uniqueDB" = False ] ; then
  # Suppression des vues matérialisées
  echo "  Suppression des vues matérialisées"
  # TODO

fi
