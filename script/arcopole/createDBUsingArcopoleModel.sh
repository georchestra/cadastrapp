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
# @date   08/11/2017
# Version : 1.7
#
# Change version | Author         |    Date    | Comments
#   1.0          | Pierre JEGO    | 15/06/2015 | Init
#   1.1          | Pierre JEGO    | 08/07/2015 | Add proprieteBatie et ProprieteNonBatie
#   1.2          | Pierre JEGO    | 29/07/2015 | Add parameters
#   1.3          | Pierre JEGO    | 23/12/2015 | Add properties tables
#   1.4          | Maël REBOUX    | 07/06/2016 | Add capability to use non local postgresql database
#   1.5          | Pierre JEGO    | 20/06/2016 | Use script in batch mode
#   1.6          | Pierre JEGO    | 22/06/2016 | Add lot view
#   1.7          | Pierre JEGO    | 08/11/2017 | Add port as done in Qgis Model
#////////////////////////////////////////////////////////////////////


# Set parameters
if [ "$#" -ne 14 ]; then
  echo "No parameters given or not the good number of params" >&2
  echo "Usage could be : $0 Batchmode(0/1) DatabaseHost DatabasePort DatabaseAdminUser DatabaseName DatabaseSchema DatabaseUser DatabasePasswd ArcopoleHost ArcopolePort ArcopoleDataBaseName ArcopoleDataBaseSchema ArcopoleDataBaseUser ArcopoleDataBasePasswd" >&2
  echo "Use constant in script" >&2

  ## TO BE SET MANUALLY IF NOT USING SCRIPT PARAMETERS
  # Script configuration

  # batch mode information
  # 0 not batch mode (password wil be prompted)
  # 1 batch mode using information from .pgpass
  #   make sure to have new user for cadastrapp in .pgpass files before launching this script
  batchmode=0

  # Postgresql information (the database to load)
  dbhost="localhost"
  dbport="5432"
  # Postgres user which have role creation and schema creation rights
  dbadminuser="admindbuser"
  dbname="cadastrapp_arcopole"
  schema="cadastrapp_arcopole"
  username="cadastrapp_user"
  userpwd="cadastrapp_pwd"

  # REMOTE Arcopole Database information (the database to read)
  arcopoleDBHost="xxx"
  arcopolePort="5432"
  arcopoleDBName="xxx"
  arcopoleDBSchema="xxx"
  arcopoleDBUser="xxx"
  arcopoleDBPassword="xxx"
else
  echo "Launch Script using parameters" >&2
  batchmode=$1
  dbhost=$2
  dbport=$3
  dbadminuser=$4
  dbname=$5
  schema=$6
  username=$7
  userpwd=$8

  arcopoleDBHost=$9
  arcopoleDBPort=$10
  arcopoleDBName=$11
  arcopoleDBSchema=$12
  arcopoleDBUser=$13
  arcopoleDBPassword=$14
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
echo "If using batch mode, make sure username and password had been set in pgpass.conf in order to use batch mode"

echo "Arcopole Database host : $arcopoleDBHost"
echo "Arcopole Database port : $arcopoleDBPort"
echo "Arcopole Database name : $arcopoleDBName"
echo "Arcopole Schema : $arcopoleDBSchema"
echo "Arcopole UserName : $arcopoleDBUser"
echo "Arcopole Password : $arcopoleDBPassword"
echo "--------------------------------";

if [ $batchmode = "1" ]; then
  connectionOption=" -w"
fi

# replaceAndLaunch
# Replace fields in sql file and launch sql execution
#
# #user_cadastrapp replace with $rolename
# #schema_cadastrapp replace with $schema
# #pwd_cadastrapp replace with $userpwd
# #DBHost_arcopole replace with $arcopoleDBHost
# #DBPort_arcopole replace wiht $arcopoleDBPort
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
          s/#DBPort_arcopole/$arcopoleDBPort/g
          s/#DBName_arcopole/$arcopoleDBName/g
          s/#DBUser_arcopole/$arcopoleDBUser/g
          s/#DBpasswd_arcopole/$arcopoleDBPassword/g
          s/#DBSchema_arcopole/$arcopoleDBSchema/g }" |\
       psql -h $dbhost -p $dbport -U $username -d $dbname $connectionOption
}

# Init database
echo "--------------------------------";
echo " Init database";
echo "--------------------------------";
cat ./database/init.sql | sed  "{ s/#user_cadastrapp/$username/g
                  s/#pwd_cadastrapp/$userpwd/g
                  s/#dbname_arcopole/$dbname/g
                  s/#schema_cadastrapp/$schema/g }" |\
                  psql -h $dbhost -p $dbport -U $dbadminuser -d postgres $connectionOption

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
replaceAndLaunch ./views/arcopoleLot.sql

# Create users correlation tables
echo "--------------------------------";
echo " Create additional tables";
echo "--------------------------------";
replaceAndLaunch ../commun/user/groupe_autorisation.sql

# -- create table for information request
replaceAndLaunch ../commun/tables/request_information.sql

# -- create index
replaceAndLaunch ../commun/index.sql
