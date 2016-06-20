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
# @date   20/06/2016
# Version : 1.4
#
# Change version | Author |    Date    | Comments
#   1.0          | Pje    | 07/02/2015 | Init
#   1.1          | Pje    | 08/07/2015 | Change user creation and management
#   1.2          | Pje    | 23/12/2015 | Add properties tables
#   1.3          | Jsa    | 09/06/2016 | Add capability to use non local postgresql database
#   1.4          | Pje    | 20/06/2016 | Use script in batch mode
#////////////////////////////////////////////////////////////////////

# Set parameters
if [ "$#" -ne 12 ]; then
  echo "No parameters given or not the good number of params" >&2
  echo "Usage could be : $0 Batchmode(0/1) DatabaseHost DatabaseAdminUser DatabaseName DatabaseSchema DatabaseUser DatabasePasswd QgisHost QgisDataBaseName QgisDataBaseSchema QgisDataBaseUser QgisDataBasePasswd" >&2
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
    # Postgres user which have role creation and schema creation rights
    dbadminuser="admindbuser"
    dbname="cadastrapp_qgis"
    schema="cadastrapp_qgis"
    username="cadastrapp_user"
    userpwd="cadastrapp_pwd"

    # REMOTE Arcopole Database information (the database to read)
    qgisDBHost=postgis-bdu
    qgisDBName=bdu
    qgisDBSchema=cadastre
    qgisDBUser=xxxxxxxxxxxxxx
    qgisDBPassword=xxxxxxxxxxxxxx
else
  echo "Launch Script using parameters" >&2
  batchmode=$1
  dbhost=$2
  dbadminuser=$3
  dbname=$4
  schema=$5
  username=$6
  userpwd=$7

    qgisDBHost=$8
    qgisDBName=$9
    qgisDBSchema=$10
    qgisDBUser=$11
    qgisDBPassword=$12
fi

echo "--------------------------------";
echo "Batch mode : $batchmode"
echo "Database host : $dbhost"
echo "Database admin user : $dbadminuser"
echo "Database name : $dbname"
echo "Schema name : $schema"
echo "Username : $username"
echo "Password : $userpwd"
echo "If using batch mode, make sure username and password had been set in pgpass.conf in order to use batch mode"

echo "Qgis Database host : $qgisDBHost"
echo "Qgis Database name : $qgisDBName"
echo "Qgis Schema : $qgisDBSchema"
echo "Qgis UserName : $qgisDBUser"
echo "Qgis Password : $qgisDBPassword"
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
                    s/#DBName_qgis/$qgisDBName/g
                    s/#DBSchema_qgis/$qgisDBSchema/g
                    s/#DBUser_qgis/$qgisDBUser/g
                    s/#DBpasswd_qgis/$qgisDBPassword/g }" |\
                    psql -h $dbhost -U $username -d $dbname $connectionOption
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
                                  psql -h $dbhost -U $dbadminuser -d postgres $connectionOption

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

# Create users correlation tables
replaceAndLaunch ../commun/user/groupe_autorisation.sql

# Create table for information request
replaceAndLaunch ../commun/tables/request_information.sql


