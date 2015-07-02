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
#
#////////////////////////////////////////////////////////////////////


# Set parameters
dbname="cadastrapp_qgis"
schema="cadastrapp_qgis"
rolename="cadastrapp_qgis"

DBHost=
DBName=
DBSchema=
DBUser=
DBPassword=

# replaceAndLaunch
# Replace fields in sql file and launch sql execution
# 
# #role_cadastrapp replace with $rolename
# #schema_cadastrapp replace with $schema
# #DBHost_qgis replace with $arcopoleDBHost
# #DBSchema_qgis replace with $arcopoleDBSchema
# #DBName_qgis replace with $arcopoleDBName
# #DBUser_qgis replace with $arcopoleDBUser
# #DBpasswd_qgis replace with $arcopoleDBPassword
#
replaceAndLaunch (){
	
	if [ -z ${sql+x} ]; then echo "sql file is unset" exit 1; else echo "Launch file :  '$sql'"; fi
	
	cat $1 | sed "{ s/#role_cadastrapp/$rolename/g
				 	s/#schema_cadastrapp/$schema/g
				 	s/#DBHost_qgis/$DBHost/g
				 	s/#DBName_qgis/$DBName/g
				 	s/#DBSchema_qgis/$DBSchema/g
				 	s/#DBUser_qgis/$DBUser/g
				 	s/#DBpasswd_qgis/$DBPassword/g }" |\
					psql -d $dbname
}

# Check to user before changing Qgis model
#replaceAndLaunch ../changeOnQGISModel/alterQGISParcelle.sql

# Init database
cat ./database/init.sql | sed  "{ s/#role_cadastrapp/$rolename/g
						 		  s/#dbname_qgis/$dbname/g
						 	  	  s/#schema_cadastrapp/$schema/g }" |\
								  psql

# Create tables
//TODO check necessary tables with cbesche

# Launch views creation (views will use DBLINK extension, make sure it is enable on your database)
replaceAndLaunch ./views/qgisCommune.sql
replaceAndLaunch ./views/qgisParcelle.sql
replaceAndLaunch ./views/qgisProprietaire.sql
replaceAndLaunch ./views/qgisProprieteBati.sql
replaceAndLaunch ./views/qgisProprieteNonBati.sql
replaceAndLaunch ./views/qgisSection.sql

# Create users correlation tables
replaceAndLaunch ../user/groupeAutorisation.sql


