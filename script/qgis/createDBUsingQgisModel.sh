#!/bin/sh

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
# @date   07/02/2015
# Version : 1.0
# 
# Change version | Author |    Date    | Comments
#	1.0		     | Pje	  | 07/02/2015 | Init
#	1.1			 | Pje    | 08/07/2015 | Change user creation and management
#
#////////////////////////////////////////////////////////////////////

# Set parameters
if [ "$#" -ne 9 ]; then
	echo "No parameters given or not the good number of params" >&2
	echo "Usage could be : $0 DatabaseName DatabaseSchema DatabaseUser DatabasePasswd QgisHost QgisDataBaseName QgisDataBaseSchema QgisDataBaseUser QgisDataBasePasswd" >&2
	echo "Use constant in script" >&2
	
	## TO BE SET MANUALLY IF NOT USING SCRIPT PARAMETERS
	# LOCAL Postgresql information
	dbname="cadastrapp_qgis"
	schema="cadastrapp_qgis"
	username="cadastrapp"
	userpwd="cadastrapp"

	# REMOTE Arcopole Database information
	qgisDBHost=
	qgisDBName=
	qgisDBSchema=
	qgisDBUser=
	qgisDBPassword=
else
	echo "Launch Script using parameters" >&2
	dbname=$1
	schema=$2
	username=$3
	userpwd=$4

	qgisDBHost=$5
	qgisDBName=$6
	qgisDBSchema=$7
	qgisDBUser=$8
	qgisDBPassword=$9
fi

echo "--------------------------------";
echo "Database name : $dbname"
echo "Schema name : $schema"
echo "Username : $username"
echo "Password : $userpwd"

echo "Qgis Database host : $qgisDBHost"
echo "Qgis Database name : $qgisDBName"
echo "Qgis Schema : $qgisDBSchema"
echo "Qgis UserName : $qgisDBUser"
echo "Qgis Password : $qgisDBPassword"
echo "--------------------------------";

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
				 	s/#DBName_qgis/$qgisDBName/g
					s/#DBSchema_qgis/$qgisDBSchema/g
				 	s/#DBUser_qgis/$qgisDBUser/g
				 	s/#DBpasswd_qgis/$qgisDBPassword/g }" |\
					psql -d $dbname
}

# Check to user before changing Qgis model
#replaceAndLaunch ../changeOnQGISModel/alterQGISParcelle.sql

# Init database
echo "--------------------------------";
echo " Init database";
echo "--------------------------------";
cat ./database/init.sql | sed  "{ s/#user_cadastrapp/$username/g
								  s/#pwd_cadastrapp/$userpwd/g
						 		  s/#dbname_qgis/$dbname/g
						 	  	  s/#schema_cadastrapp/$schema/g }" |\
								  psql

echo "--------------------------------";
echo " Drop View and Tables except groupeAutorisation ";
echo "--------------------------------";								  
replaceAndLaunch ../commun/dropTablesAndViews.sql

# Create tables
echo "--------------------------------";
echo " Create tables ";
echo "--------------------------------";

replaceAndLaunch ./tables/uf_parcelle.sql

replaceAndLaunch ../commun/tables/prop_ccodem.sql
replaceAndLaunch ../commun/tables/prop_ccodro.sql
replaceAndLaunch ../commun/tables/prop_ccoqua.sql
replaceAndLaunch ../commun/tables/prop_ccogrm.sql
replaceAndLaunch ../commun/tables/prop_dnatpr.sql

# Launch views creation (views will use DBLINK extension, make sure it is enable on your database)
replaceAndLaunch ./views/qgisCommune.sql
replaceAndLaunch ./views/qgisParcelle.sql
replaceAndLaunch ./views/qgisProprietaire.sql
replaceAndLaunch ./views/qgisProprietaireParcelle.sql
replaceAndLaunch ./views/qgisProprieteBatie.sql
replaceAndLaunch ./views/qgisProprieteNonBatie.sql
replaceAndLaunch ./views/qgisSection.sql
replaceAndLaunch ./views/qgisHabitationDetails.sql

# Create users correlation tables
replaceAndLaunch ../commun/user/groupe_autorisation.sql

# Create table for information request
replaceAndLaunch ../commun/tables/request_information.sql


