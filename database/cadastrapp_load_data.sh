#! /bin/bash


clear
git_version=$(git rev-parse HEAD)

echo "--------------------------------------------------------------"
echo " Cadastrapp : script de chargement des données"
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

# verifie si le mode silencieux est activé
silentMode=False
while [[ "$#" -gt 0 ]]
do
	case $1 in
		-s|--silent)
		silentMode=True
		;;
		-*|--*)
		echo "Invalid option: $1"
      	;;
	esac
	shift
done

if [ "$uniqueDB" = True ] ; then
  echo "  1 seule base de données sera utilisée : $cadastrappDBName sur la machine $cadastrappDBHost"
  echo "    schéma des données cadastre QGIS : $qgisDBSchema"
  echo "    schéma des données cadastrapp    : $cadastrappDBSchema"
elif [ "$uniqueDB" = False ] ; then
  echo "  2 bases de données seront utilisées"
  echo "    host > db > schema"
  echo "    source QGIS      : $qgisDBHost > $qgisDBName > $qgisDBSchema"
  echo "    cible cadastrapp : $cadastrappDBHost > $cadastrappDBName > $cadastrappDBSchema"
else
  echo "  pb de configuration : stop"
  exit 1
fi

if [ "$silentMode" = False ] ; then
	echo ""
	read -p "  Si ces infos sont exactes : appuyer sur la touche [Entrée] sinon faire ctrl + C pour arrêter."
	echo ""
fi

#
# cette fonction permet de remplacer les infos de connection
# avant exécution
#
replaceAndLaunch (){
    
  if [ -z "$1" ] || [ ! -e $1 ] ; then
    echo "Le fichier SQL $1 n'existe pas."
    exit 1
  else
    echo "    Exécution du fichier : $1"
  fi
  
  cat $1 | sed "{
    s/#schema_cadastrapp/$cadastrappDBSchema/g
    s/#user_cadastrapp/$cadastrappDBUser/g
    s/#DBHost_qgis/$qgisDBHost/g
    s/#DBPort_qgis/$qgisDBPort/g
    s/#DBName_qgis/$qgisDBName/g
    s/#DBSchema_qgis/$qgisDBSchema/g
    s/#DBUser_qgis/$qgisDBUser/g
    s/#DBpasswd_qgis/$qgisDBPassword/g
  }" |\
  PGPASSWORD=$cadastrappDBPassword psql -h $cadastrappDBHost -p $cadastrappDBPort -d $cadastrappDBName -U $cadastrappDBUser 

}


# test de connection aux bases de données
# notamment que cadastrappDBUser puisse lire dans qgisDBSchema
# TODO

# on purge la BD cadastrapp existante
. purge_db.sh
#read -p "  pause pour vérifier que la BD est bien vide"

# on recrée les tables
. create_tables.sh

# on remplit les tables
# les tables sont remplies à l'étape d'avant

# on recrée les vues
. create_views.sh

# on applique les commentaires
. create_comments.sh

# traitement à part pour les tables d'information foncière
. tables_request.sh

# on fait un bilan
# TODO

echo ""
echo "--------------------------------------------------------------"
echo " FIN "
echo "--------------------------------------------------------------"
