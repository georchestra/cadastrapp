#!/bin/bash

clear
git_version=$(git rev-parse HEAD)

echo "--------------------------------------------------------------"
echo " Cadastrapp : script de création du cron job pour rafraichier la vue des organisations LDAP"
echo "--------------------------------------------------------------"
echo ""
echo "  version : $git_version" 
echo ""
echo "--------------------------------------------------------------"
echo " Lecture du fichier de configuration"
echo "--------------------------------------------------------------"
echo ""

# lecture du fichier de configuration des connexions
. config.sh

replaceAndLaunch (){ 
  if [ -z "$1" ] || [ ! -e $1 ] ; then
    echo "Le fichier SQL $1 n'existe pas."
    exit 1
  else
    echo "    Exécution du fichier : $1"
  fi

  cat $1 | sed "{
    s/#schema_cadastrapp/$cadastrappDBSchema/g
    s/#cadastrapp_db_name/$cadastrappDBName/g
    s/#user_cadastrapp/$cadastrappDBUser/g
  }" |\
  PGPASSWORD=$cadastrappDBPassword psql -h $cadastrappDBHost -p $cadastrappDBPort -d $cadastrappDBName -U $cadastrappDBUser 
}

replaceAndLaunch sql/ldap/cronjob.sql

echo ""
echo "--------------------------------------------------------------"
echo " FIN "
echo "--------------------------------------------------------------"
