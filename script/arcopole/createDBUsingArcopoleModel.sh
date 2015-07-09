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
# Version : 1.0
# 
# Change version | Author |    Date    | Comments
#	1.0		     | Pje	  | 15/06/2015 | Init
#	1.1			 | Pje    | 08/07/2015 | Add proprieteBatie et ProprieteNonBatie
#
#////////////////////////////////////////////////////////////////////


# Set parameters
dbname="cadastrapp_arcopole"
schema="cadastrapp_arcopole"
username="cadastrapp"
userpwd="cadastrapp"

arcopoleDBHost=
arcopoleDBName=
arcopoleDBSchema=
arcopoleDBUser=
arcopoleDBPassword=

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
cat ./database/init.sql | sed  "{ s/#user_cadastrapp/$username/g
								  s/#pwd_cadastrapp/$userpwd/g
						 		  s/#dbname_arcopole/$dbname/g
						 	  	  s/#schema_cadastrapp/$schema/g }" |\
								  psql

# Create tables
replaceAndLaunch ../commun/tables/prop_ccodem.sql
replaceAndLaunch ../commun/tables/prop_ccodro.sql
replaceAndLaunch ../commun/tables/prop_ccoqua.sql
replaceAndLaunch ../commun/tables/prop_ccogrm.sql
replaceAndLaunch ../commun/tables/prop_dnatpr.sql

# Launch views creation (views will use DBLINK extension, make sure it is enable on your database)
replaceAndLaunch ./views/arcopoleCommune.sql
replaceAndLaunch ./views/arcopoleParcelle.sql
replaceAndLaunch ./views/arcopoleProprietaire.sql
replaceAndLaunch ./views/arcopoleSection.sql
replaceAndLaunch ./views/arcopoleProprieteBatie.sql
replaceAndLaunch ./views/arcopoleProprieteNonBatie.sql

# Create users correlation tables
replaceAndLaunch ../commun/user/groupeAutorisation.sql
