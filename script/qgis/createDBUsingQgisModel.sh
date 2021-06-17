#!/bin/bash

#-----------------------------------------------------------------
# Project      : Cadastrapp 
# Description  : Postgresql Schema creation using qgis models
#-----------------------------------------------------------------

#////////////////////////////////////////////////////////////////////
#
# Script      :   createDBUsingQgisModel.sh
#
# Purpose     :   Create table and view neccessary for Cadastrapp addons for QGIS model
# Warnings    :   This script should be launch with user postgres on the database server
# Description :
#
# @author Jégo Pierre
# @since
# @brief
# @date   20/06/2016
# Version : 1.4
#
# Change version | Author         |    Date    | Comments
#   1.0          | Pierre JEGO    | 07/02/2015 | Init
#   1.1          | Pierre JEGO    | 08/07/2015 | Change user creation and management
#   1.2          | Pierre JEGO    | 23/12/2015 | Add properties tables
#   1.3          | Julien Sabatier| 09/06/2016 | Add capability to use non local postgresql database
#   1.4          | Pierre JEGO    | 20/06/2016 | Use script in batch mode
#   1.5          | Pierre JEGO    | 27/11/2018 | Add correlation tables
#   1.6          | Julien Sabatier| 17:06/2021 | Handle named parameters and ldap fdw configuration for orgs autorisations
#////////////////////////////////////////////////////////////////////


## A REMPLIR MANUELLEMENT SI VOUS NE SOUHAITEZ PAS UTILISER LES PARAMETRES DE LA LIGNE DE COMMANDE
# Script configuration
#
batchmode=0
#dbhost="localhost"
#dbport="5432"
#dbadminuser="admindbuser"
#dbname="cadastrapp_qgis"
#schema="cadastrapp_qgis"
#username="cadastrapp_user"
#userpwd="cadastrapp_pwd"
#qgisDBHost=postgis-bdu
#qgisDBPort=5432
#qgisDBName=bdu
#qgisDBSchema=cadastre
#qgisDBUser=xxxxxxxxxxxxxx
#qgisDBPassword=xxxxxxxxxxxxxx
ldap=0
#ldapUri=ldaps://ldap.georchestra.org
#ldapPath=ou=orgs,dc=georchestra,dc=org
#ldapBindDn=uid=cadastrapp,ou=users,dc=georchestra,dc=org
#ldapBindPwd=secret

## ----------------------------------------- NE PAS MODIFIER EN DESSOUS ------------------------------------------- ##

invalidparams=0

show_description() {
	echo "Usage : "
	echo "createDBUsingQgisModel.sh [-b] --dbhost <dbhost> --dbport <dbport> --dbadminuser <dbadminuser> --dbname <dbname> --schema <schema> --username <username> --userpwd <userpwd> --qgisDBHost <qgisDBHost> --qgisDBPort <qgisDBPort> --qgisDBName <qgisDBName> --qgisDBSchema <qgisDBSchema> --qgisDBUser <qgisDBUser> --qgisDBPassword <qgisDBPassword> [--ldapOrgs --ldapUri <ldapUri> --ldapPath <ldapPath> --ldapBindDn <bindDn> --ldapBindPwd <bindPwd>]"
	echo "Parametres : "
	echo "-b : (Optionnel) Activation du mode 'batch' qui utilise les identifiants du fichier .pgpass pour la connexion à PostgreSQL"
	echo "--dbhost : Hôte de la BDD de cadastrapp"
	echo "--dbport : Port de la BDD de cadastrapp"
	echo "--dbadminuser : Login de l'utilisateur ayant les droits pour créer des rôles et des schemas dans la BDD de cadastrapp"
	echo "--dbname : Nom de la BDD de cadastrapp"
	echo "--schema : Schema à configurer pour cadastrapp"
	echo "--username : Login de l'utilisateur à configuer pour cadastrapp"
	echo "--userpwd : Mot de passe de l'utilisateur à configuer pour cadastrapp"
	echo "--qgisDBHost : Hôte de la BDD contenant le modèle QGis"
	echo "--qgisDBPort : Port de la BDD contenant le modèle QGis"
	echo "--qgisDBName : Nom de la BDD contenant le modèle QGis"
	echo "--qgisDBSchema : Schema de la BDD contenant le modèle QGis"
	echo "--qgisDBUser : Login de l'utilisateur pour la BDD contenant le modèle QGis"
	echo "--qgisDBPassword : Mot de passe de l'utilisateur pour la BDD contenant le modèle QGis"
	echo "--ldapOrgs : (Optionnel) Si activé, les restrictions geographiques sont remontées directement des organisations geOrchestra"
	echo "Si ldapOrgs est activé, les options suivantes sont obligatoires : "
	echo "--ldapUri : L'URI du serveur LDAP de geOrchestra"
	echo "--ldapPath : Le chemin dans l'annuaire contenant les organisations"
	echo "--ldapBindDn : L'utilisateur à utiliser pour se connecter au LDAP"
	echo "--ldapBindPwd : Le mot de passe de l'utilisateur à utiliser pour se connecter au LDAP"
}

check_params() {
	if [ -z "$dbhost" ] ; then
		echo "Paramètre dbhost manquant !"
		invalidparams=1
	fi
	
	if [ -z "$dbport" ] ; then
		echo "Paramètre dbport manquant !"
		invalidparams=1
	fi
	
	if [ -z "$dbadminuser" ] ; then
		echo "Paramètre dbadminuser manquant !"
		invalidparams=1
	fi
	
	if [ -z "$dbname" ] ; then
		echo "Paramètre dbname manquant !"
		invalidparams=1
	fi
	
	if [ -z "$schema" ] ; then
		echo "Paramètre schema manquant !"
		invalidparams=1
	fi
	
	if [ -z "$username" ] ; then
		echo "Paramètre username manquant !"
		invalidparams=1
	fi
	
	if [ -z "$userpwd" ] ; then
		echo "Paramètre userpwd manquant !"
		invalidparams=1
	fi
	
	if [ -z "$qgisDBHost" ] ; then
		echo "Paramètre qgisDBHost manquant !"
		invalidparams=1
	fi
	
	if [ -z "$qgisDBPort" ] ; then
		echo "Paramètre qgisDBPort manquant !"
		invalidparams=1
	fi
	
	if [ -z "$qgisDBName" ] ; then
		echo "Paramètre qgisDBName manquant !"
		invalidparams=1
	fi
	
	if [ -z "$qgisDBSchema" ] ; then
		echo "Paramètre qgisDBSchema manquant !"
		invalidparams=1
	fi
	
	if [ -z "$qgisDBUser" ] ; then
		echo "Paramètre qgisDBUser manquant !"
		invalidparams=1
	fi
	
	if [ -z "$qgisDBPassword" ] ; then
		echo "Paramètre qgisDBPassword manquant !"
		invalidparams=1
	fi
	
	if [ $ldap -eq 1 ] ; then
		if [ -z "$ldapUri" ] ; then
			echo "Paramètre ldapUri manquant !"
			invalidparams=1
		fi
		
		if [ -z "$ldapPath" ] ; then
			echo "Paramètre ldapUri manquant !"
			invalidparams=1
		fi
		
		if [ -z "$ldapBindDn" ] ; then
			echo "Paramètre ldapUri manquant !"
			invalidparams=1
		fi
		
		if [ -z "$ldapBindPwd" ] ; then
			echo "Paramètre ldapUri manquant !"
			invalidparams=1
		fi
	fi
}

while [[ "$#" -gt 0 ]]
do
	case $1 in
		-b|--batch)
		batchmode=1
		;;
		--dbhost)
		dbhost=$2
		;;
		--dbport)
		dbport=$2
		;;
		--dbadminuser)
		dbadminuser=$2
		;;
		--dbname)
		dbname=$2
		;;
		--schema)
		schema=$2
		;;
		--username)
		username=$2
		;;
		--userpwd)
		userpwd=$2
		;;
		--qgisDBHost)
		qgisDBHost=$2
		;;
		--qgisDBPort)
		qgisDBPort=$2
		;;
		--qgisDBName)
		qgisDBName=$2
		;;
		--qgisDBSchema)
		qgisDBSchema=$2
		;;
		--qgisDBUser)
		qgisDBUser=$2
		;;
		--qgisDBPassword)
		qgisDBPassword=$2
		;;
		--ldapOrgs)
		ldap=1
		;;
		--ldapUri)
		ldapUri=$2
		;;
		--ldapPath)
		ldapPath=$2
		;;
		--ldapBindDn)
		ldapBindDn=$2
		;;
		--ldapBindPwd)
		ldapBindPwd=$2
		;;
		-*|--*)
		echo "Invalid option: $1"
		invalidparams=1
      	;;
	esac
	shift
done

check_params

if [ $invalidparams -eq 1 ] ; then 
	show_description
	exit 1
fi

echo "--------------------------------";
echo "Batch mode : $batchmode"
echo "Database host : $dbhost"
echo "Database port : $dbport"
echo "Database admin user : $dbadminuser"
echo "Database name : $dbname"
echo "Schema name : $schema"
echo "Username : $username"
echo "Password : $userpwd"
echo "Si vous utilisez le mode batch, assurez vous que les identifiants soient bien renseignés dans le fichier pgpass.conf"
echo "Qgis Database host : $qgisDBHost"
echo "Qgis Database port : $qgisDBPort"
echo "Qgis Database name : $qgisDBName"
echo "Qgis Schema : $qgisDBSchema"
echo "Qgis UserName : $qgisDBUser"
echo "Qgis Password : $qgisDBPassword"
echo "LDAP : $ldap"
if [ $ldap -eq 1 ] ; then
    echo "LDAP URI : $ldapUri"
    echo "LDAP Path : $ldapPath"
    echo "LDAP Bind DN : $ldapBindDn"
    echo "LDAP Bind Password : $ldapBindPwd"
fi
echo "--------------------------------";

if [ $batchmode = "1" ]; then
	connectionOption=" -w"
fi

# replaceAndLaunch
# Replace fields in sql file and launch sql execution
# 
# #user_cadastrapp replace with $username
# #schema_cadastrapp replace with $schema
# #DBHost_qgis replace with $qgisDBHost
# #DBName_qgis replace with $qgisDBName
# #DBSchema_qgis replace with $qgisDBSchema
# #DBUser_qgis replace with $qgisDBUser
# #DBpasswd_qgis replace with $qgisDBPassword
#
replaceAndLaunch (){
    if [ -z "$1" ] || [ ! -e $1 ] ; then
        echo "Sql file is unset or file does not exists"
        exit 1
    else
        echo "Launch file :  $1"
    fi
    
    cat $1 | sed "{ s/#user_cadastrapp/$username/g
                    s/#schema_cadastrapp/$schema/g
                    s/#DBHost_qgis/$qgisDBHost/g
                    s/#DBPort_qgis/$qgisDBPort/g
                    s/#DBName_qgis/$qgisDBName/g
                    s/#DBSchema_qgis/$qgisDBSchema/g
                    s/#DBUser_qgis/$qgisDBUser/g
                    s/#DBpasswd_qgis/$qgisDBPassword/g }" |\
                    psql -h $dbhost -p $dbport -U $username -d $dbname $connectionOption
}

# Init database
echo "--------------------------------";
echo " Initialisation de la BDD";
echo "--------------------------------";
cat ./database/init.sql | sed  "{ s/#user_cadastrapp/$username/g
                                  s/#pwd_cadastrapp/$userpwd/g
                                  s/#dbname_qgis/$dbname/g
                                  s/#schema_cadastrapp/$schema/g }" |\
                                  psql -h $dbhost -p $dbport -U $dbadminuser -d postgres $connectionOption

# If ldap activated, configure connection
if [ $ldap -eq 1 ] ; then
    echo "--------------------------------";
    echo " Initialisation de la connexion au LDAP";
    echo "--------------------------------";
	cat ./database/initLdap.sql | sed  "{ s/#user_cadastrapp/$username/g
                                  	  s/#schema_cadastrapp/$schema/g
                                  	  s/#ldap_uri/$ldapUri/g
                                  	  s/#ldap_path/$ldapPath/g
                                  	  s/#ldap_binddn/$ldapBindDn/g
                                  	  s/#ldap_bindpwd/$ldapBindPwd/g }" |\
                                  	  psql -h $dbhost -p $dbport -U $dbadminuser -d postgres $connectionOption
fi

echo "--------------------------------";
echo " Suppression des vues et tables excepté groupeAutorisation ";
echo "--------------------------------";
replaceAndLaunch ../commun/dropTablesAndViews.sql

# Create tables
echo "--------------------------------";
echo " Creation des tables ";
echo "--------------------------------";

replaceAndLaunch ./tables/uf_parcelle.sql

# Add properties correlation tables
replaceAndLaunch ../commun/tables/prop_ccocac.sql
replaceAndLaunch ../commun/tables/prop_ccocac_simple.sql
replaceAndLaunch ../commun/tables/prop_ccodem.sql
replaceAndLaunch ../commun/tables/prop_ccodro.sql
replaceAndLaunch ../commun/tables/prop_ccoqua.sql
replaceAndLaunch ../commun/tables/prop_ccogrm.sql
replaceAndLaunch ../commun/tables/prop_dnatpr.sql
replaceAndLaunch ../commun/tables/prop_dmatto.sql
replaceAndLaunch ../commun/tables/prop_dmatgm.sql
replaceAndLaunch ../commun/tables/prop_type_filiation.sql
replaceAndLaunch ../commun/tables/prop_bati_detent.sql

# Launch views creation (views will use DBLINK extension, make sure it is enable on your database)
echo "--------------------------------";
echo " Create views ";
echo "--------------------------------";
replaceAndLaunch ./views/qgisCommune.sql
replaceAndLaunch ./views/qgisParcelle.sql
replaceAndLaunch ./views/qgisProprietaire.sql
replaceAndLaunch ./views/qgisCoProprieteParcelle.sql
replaceAndLaunch ./views/qgisProprietaireParcelle.sql
replaceAndLaunch ./views/qgisProprieteBatie.sql
replaceAndLaunch ./views/qgisProprieteNonBatie.sql
replaceAndLaunch ./views/qgisProprieteNonBatieSufExo.sql
replaceAndLaunch ./views/qgisSection.sql
replaceAndLaunch ./views/qgisHabitationDetails.sql
replaceAndLaunch ./views/qgisLot.sql

# Create users correlation tables
if [ $ldap -eq 0 ] ; then 
    replaceAndLaunch ../commun/user/groupe_autorisation.sql
else
    replaceAndLaunch ../commun/user/ldap_groupe_autorisation.sql
fi

# Create table for information request
replaceAndLaunch ../commun/tables/request_information.sql

# -- create index
replaceAndLaunch ../commun/index.sql
