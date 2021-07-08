#! /bin/bash

# fichier de configuration des infos de connexions
# aux bases de données

# BD cadastre QGIS  (lecture)
qgisDBHost=localhost
qgisDBPort=5432
qgisDBName=bdu
qgisDBSchema=cadastre_qgis
qgisDBUser=cadastre_qgis_user
qgisDBPassword=cadastre_qgis_mdp

# BD cadastrapp (écriture)
cadastrappDBHost=localhost
cadastrappDBPort=5432
cadastrappDBName=georchestra
cadastrappDBSchema=cadastrapp
cadastrappDBUser=cadastrapp_user
cadastrappDBPassword=cadastrapp_mdp

# le schéma cadastrapp et le schéma cadastre_qgis sont dans la même base de données
# True / False
uniqueDB=True

# Récupération des autorisations geographiques configurées pour les organisations dans la console geOrchestra
# True / False
orgsAutorisations=False

# Obligatoires si orgsAutorisations=True
#ldapUri=ldaps://ldap.georchestra.org
#ldapPath=ou=orgs,dc=georchestra,dc=org
#ldapBindDn=uid=cadastrapp,ou=users,dc=lepuyenvelay,dc=fr
#ldapBindPwd=secret
