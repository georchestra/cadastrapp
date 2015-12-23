#!/bin/sh

#-----------------------------------------------------------------
# Project      : Cadastrapp 
# Description  : Postgresql Schema creation using Arcopole models
#-----------------------------------------------------------------

#////////////////////////////////////////////////////////////////////
#
# Script      :   createDBUsingArcopoleModel.sh
#
# Purpose     :   Create table and view neccessary for Cadastrapp addons for Arcopole model
# Warnings    :   This script should be launch with user postgres on the database server
# Description :   
#
# @author Jégo Pierre
# @since  
# @brief  
# @date   15/06/2015
# Version : 1.3
# 
# Change version | Author |    Date    | Comments
#	1.0		     | Pje	  | 15/06/2015 | Init
#	1.1			 | Pje    | 08/07/2015 | Add proprieteBatie et ProprieteNonBatie
#   1.2			 | Pje	  | 29/07/2015 | Add parameters
#   1.3			 | Pje	  | 23/12/2015 | Add properties tables
#////////////////////////////////////////////////////////////////////


# Set parameters
if [ "$#" -ne 9 ]; then
	echo "No parameters given or not the good number of params" >&2
	echo "Usage could be : $0 DatabaseName DatabaseSchema DatabaseUser DatabasePasswd ArcopoleHost ArcopoleDataBaseName ArcopoleDataBaseSchema ArcopoleDataBaseUser ArcopoleDataBasePasswd" >&2
	echo "Use constant in script" >&2
	
	## TO BE SET MANUALLY IF NOT USING SCRIPT PARAMETERS
	# LOCAL Postgresql information
	dbname="cadastrapp_arcopole"
	schema="cadastrapp_arcopole"
	username="cadastrapp"
	userpwd="cadastrapp"

	# REMOTE Arcopole Database information
	arcopoleDBHost=
	arcopoleDBName=
	arcopoleDBSchema=
	arcopoleDBUser=
	arcopoleDBPassword=
else
	echo "Launch Script using parameters" >&2
	dbname=$1
	schema=$2
	username=$3
	userpwd=$4

	arcopoleDBHost=$5
	arcopoleDBName=$6
	arcopoleDBSchema=$7
	arcopoleDBUser=$8
	arcopoleDBPassword=$9
fi

echo "--------------------------------";
echo "Database name : $dbname"
echo "Schema name : $schema"
echo "Username : $username"
echo "Password : $userpwd"

echo "Arcopole Database host : $arcopoleDBHost"
echo "Arcopole Database name : $arcopoleDBName"
echo "Arcopole Schema : $arcopoleDBSchema"
echo "Arcopole UserName : $arcopoleDBUser"
echo "Arcopole Password : $arcopoleDBPassword"
echo "--------------------------------";


# replaceAndLaunch
# Replace fields in sql file and launch sql execution
# 
# #user_cadastrapp replace with $rolename
# #schema_cadastrapp replace with $schema
# #pwd_cadastrapp replace with $userpwd
# #DBHost_arcopole replace with $arcopoleDBHost
# #DBSchema_arcopole replace with $arcopoleDBSchema
# #DBName_arcopole replace with $arcopoleDBName
# #DBUser_arcopole replace with $arcopoleDBUser
# #DBpasswd_arcopole replace with $arcopoleDBPassword
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
				 	s/#DBHost_arcopole/$arcopoleDBHost/g
				 	s/#DBName_arcopole/$arcopoleDBName/g
				 	s/#DBUser_arcopole/$arcopoleDBUser/g
				 	s/#DBpasswd_arcopole/$arcopoleDBPassword/g
				 	s/#DBSchema_arcopole/$arcopoleDBSchema/g }" |\
			 psql -d $dbname
}

# Init database
echo "--------------------------------";
echo " Init database";
echo "--------------------------------";
cat ./database/init.sql | sed  "{ s/#user_cadastrapp/$username/g
								  s/#pwd_cadastrapp/$userpwd/g
						 		  s/#dbname_arcopole/$dbname/g
						 	  	  s/#schema_cadastrapp/$schema/g }" |\
								  psql
	
echo "--------------------------------";
echo " Drop View and Tables except groupeAutorisation ";
echo "--------------------------------";								  
replaceAndLaunch ./database/dropAll.sql

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
replaceAndLaunch ../commun/tables/prop_dmatto.sql
replaceAndLaunch ../commun/tables/prop_dmatgm.sql

# Launch views creation (views will use DBLINK extension, make sure it is enable on your database)
echo "--------------------------------";
echo " Create views ";
echo "--------------------------------";
replaceAndLaunch ./views/arcopoleCommune.sql
replaceAndLaunch ./views/arcopoleParcelle.sql
replaceAndLaunch ./views/arcopoleProprietaire.sql
replaceAndLaunch ./views/arcopoleProprietaireParcelle.sql
replaceAndLaunch ./views/arcopoleCoProprieteParcelle.sql
replaceAndLaunch ./views/arcopoleSection.sql
replaceAndLaunch ./views/arcopoleProprieteBatie.sql
replaceAndLaunch ./views/arcopoleProprieteNonBatie.sql
replaceAndLaunch ./views/arcopoleProprieteNonBatieSufExo.sql
replaceAndLaunch ./views/arcopoleHabitationDetails.sql

# Create users correlation tables
echo "--------------------------------";
echo " Create additional tables";
echo "--------------------------------";
replaceAndLaunch ../commun/user/groupe_autorisation.sql

# -- create table for information request
replaceAndLaunch ../commun/tables/request_information.sql

# -- create index
replaceAndLaunch ../commun/index.sql
