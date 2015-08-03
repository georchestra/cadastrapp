Cadastrapp - Database Script
User geographic limitation
=========================== 

Geographic limitation are store in groupeAutorisation table.

It will make a relation between LDPAP group name and city or "département".


You will find an exemple of insertion in the file :

```
Insert-example-groupe-autorisation.sql
```

##  When and how limitation occurs

Geographics limitation occurs only when CNIL limitation is necessary. 
This is a double check, if CNIL right is enough, we check if you have right on the commune or "département".
To do so, we will look at all LDAP group user and get the list of "département" or communes he can see.
 
 We first check "département" limitation then commune, and will filter information in database request or directly with parameter that were given to webservice. 

There are two limitation possible,

If limitation is set for one (or more) "département", user will be able to see all information from any commune of this "département"
(Depending on CNIL right)

If limitation is set for one (or more) commune, user will only see information of thoses communes

##  How to limit a LDAP group

### Limitation by "département"

```
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, ccodep) VALUES ('EL_DEP', '63');
```
Do not put any value in cgocommune column, it will not be taken in account or not properly.

### Limitation by commune

```
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES', '630001');
```

If you need to put more than one commune, you will need to make a new insertion each time.

For example, I want to limit EL_COMMUNES group to commune 630103 and 630019

Il will have

```
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES', '630103');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES', '630019');
```

For information, a place as Clermont-ferrand contains 21 one communes. If you want to limit to those 21 commues the script would be : 

```
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630013');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630014');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630019');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630032');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630042');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630063');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630069');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630070');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630075');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630099');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630124');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630141');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630164');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630193');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630254');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630263');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630272');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630284');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630307');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630308');
INSERT INTO cadastreapp_qgis.groupe_autorisation (idgroup, cgocommune) VALUES ('EL_COMMUNES_CF', '630345');

```

