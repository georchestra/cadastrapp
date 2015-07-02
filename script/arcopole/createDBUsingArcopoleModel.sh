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
arcopoleDBSchema=

# replaceAndLaunch
# Replace fields in sql file and launch sql execution
# 
# #role_arcopole replace with $rolename
# #schema_arcopole replace with $schema
# #DBHost_arcopole replace with $arcopoleDBHost
# #DBSchema_arcopole replace with $arcopoleDBSchema
# #DBName_arcopole replace with $arcopoleDBName
# #DBUser_arcopole replace with $arcopoleDBUser
# #DBpasswd_arcopole replace with $arcopoleDBPassword
#
replaceAndLaunch (){
	
	if [ -z ${sql+x} ]; then echo "sql file is unset" exit 1; else echo "Launch file :  '$sql'"; fi
	
	cat $1 | sed "{ s/#role_arcopole/$rolename/g
				 	s/#schema_arcopole/$schema/g
				 	s/#DBHost_arcopole/$arcopoleDBHost/g
				 	s/#DBName_arcopole/$arcopoleDBName/g
				 	s/#DBUser_arcopole/$arcopoleDBUser/g
				 	s/#DBpasswd_arcopole/$arcopoleDBPassword/g }" |\
			 psql -d $dbname
}

# Init database
#cat ./database/init.sql | sed  "{ s/#role_arcopole/$rolename/g
#						 		  s/#dbname_arcopole/$dbname/g
#						 	  	  s/#schemaname_arcopole/$schema/g }" |\
#								  psql

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

# Create users correlation tables
replaceAndLaunch ../user/groupeAutorisation.sql
