#!/bin/ksh

#-----------------------------------------------------------------
# Project      : Cadastrapp 
# Description  : Postgresql Schema creation using Arcopole models
#-----------------------------------------------------------------

#////////////////////////////////////////////////////////////////////
#
# Script      :   createViews.sh
#
# Purpose     :   Create table and view neccessary for Cadastrapp addons
# Warnings    :   None<br /><br />
# Description :   n
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

username="cadastrapp"

arcopoleDBHost="MQ-CMS-CRAI-001.fasgfi.fr
arcopoleDBName=
arcopoleDBUser=
arcopoleDBPassword=

# Init database

psql -f ./database/init.sql

# Create tables

psql -d $dbname -e -1 -f ./tables/prop_ccodem.sql
psql -d $dbname -e -1 -f ./tables/prop_ccodro.sql
psql -d $dbname -e -1 -f ./tables/prop_ccoqua.sql
psql -d $dbname -e -1 -f ./tables/prop_ccogrm.sql
psql -d $dbname -e -1 -f ./tables/prop_dnatpr.sql

# Launch views creation (views will use DBLINK extension, make sure it is enable on your database)

psql -d $dbname -e -1 -v p_date=\'20081023\' -f ./views/arcopoleCommune.sql
psql -d $dbname -e -1 -v p_date=\'20081023\' -f ./views/arcopoleParcelle.sql
psql -d $dbname -e -1 -v p_date=\'20081023\' -f ./views/arcopoleProprietaire.sql
psql -d $dbname -e -1 -v p_date=\'20081023\' -f ./views/arcopoleSection.sql

# Create users correlation tables

psql -d $dbname -e -1 -v p_date=\'20081023\' -f ../user/groupeAutorisation.sql
