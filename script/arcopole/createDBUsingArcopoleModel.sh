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
rolename="cadastrapp_arcopole"

arcopoleDBHost=
arcopoleDBName=
arcopoleDBSchema=
arcopoleDBUser=
arcopoleDBPassword=

# replaceAndLaunch
# Replace fields in sql file and launch sql execution
# 
# #role_cadastrapp replace with $rolename
# #schema_cadastrapp replace with $schema
# #DBHost_arcopole replace with $arcopoleDBHost
# #DBSchema_arcopole replace with $arcopoleDBSchema
# #DBName_arcopole replace with $arcopoleDBName
# #DBUser_arcopole replace with $arcopoleDBUser
# #DBpasswd_arcopole replace with $arcopoleDBPassword
#
replaceAndLaunch (){
	
	if [ -z ${sql+x} ]; then echo "sql file is unset" exit 1; else echo "Launch file :  '$sql'"; fi
	
	cat $1 | sed "{ s/#role_cadastrapp/$rolename/g
				 	s/#schema_cadastrapp/$schema/g
				 	s/#DBHost_arcopole/$arcopoleDBHost/g
				 	s/#DBName_arcopole/$arcopoleDBName/g
				 	s/#DBUser_arcopole/$arcopoleDBUser/g
				 	s/#DBpasswd_arcopole/$arcopoleDBPassword/g
				 	s/#DBSchema_arcopole/$arcopoleDBSchema/g }" |\
			 psql -d $dbname
}

# Init database
cat ./database/init.sql | sed  "{ s/#role_cadastrapp/$rolename/g
						 		  s/#dbname_arcopole/$dbname/g
						 	  	  s/#schema_cadastrapp/$schema/g }" |\
								  psql

# Create tables
replaceAndLaunch ./tables/prop_ccodem.sql
replaceAndLaunch ./tables/prop_ccodro.sql
replaceAndLaunch ./tables/prop_ccoqua.sql
replaceAndLaunch ./tables/prop_ccogrm.sql
replaceAndLaunch ./tables/prop_dnatpr.sql

# Launch views creation (views will use DBLINK extension, make sure it is enable on your database)
replaceAndLaunch ./views/arcopoleCommune.sql
replaceAndLaunch ./views/arcopoleParcelle.sql
replaceAndLaunch ./views/arcopoleProprietaire.sql
replaceAndLaunch ./views/arcopoleSection.sql
replaceAndLaunch ./views/arcopoleProprieteBatie.sql
replaceAndLaunch ./views/arcopoleProprieteNonBatie.sql

# Create users correlation tables
replaceAndLaunch ../user/groupeAutorisation.sql
