# Matrice des fonctionnalités

<style type="text/css">
.tg  {border-collapse:collapse;border-spacing:0;}
.tg td {border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  overflow:hidden;padding:10px 5px;word-break:normal;}
.tg td.min {width:}
.tg th {border-color:black;border-style:solid;border-width:1px;font-family:Arial, sans-serif;font-size:14px;
  font-weight:normal;overflow:hidden;padding:10px 5px;word-break:normal;}
.tg .tg-0lax {text-align:left;vertical-align:top}
.tg thead th {font-weight:bold;}
</style>




* {string} = texte libre
* {code} = doit correspondre à une valeur en base ou une valeur codée
* {0|1} = liste de valeurs autorisées


### Configuration / préférences


Au chargement de l'addon, il faut aller chercher la configuration car elle dépend des droits accordées à l'utilisateur (niveau CNIL, communes autorisées).

<table class="tg">
	<thead> 
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax">  Récupérer la configuration </td>
			<td class="tg-0lax">              </td>
			<td class="tg-0lax">  Récupérer la configuration     </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  GET /cadastrapp/services/getConfiguration?</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Récupérer le manifest      </td>
			<td class="tg-0lax">              </td>
			<td class="tg-0lax">  Récupérer le manifest          </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  GET /mapfishapp/ws/addons/cadastrapp/manifest.json</td>
		</tr>
	</tbody>
</table>




### Rechercher des parcelles


La recherche de parcelles se fait via un formulaire qui propose 4 onglets qui correspondent à 4 façons de rechercher des parcelles :

* par référence
* par identifiant
* par adresse cadastrale
* par lot

<table class="tg">
	<thead>
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax" rowspan="4">  Recherche par référence </td>
			<td class="tg-0lax" rowspan="4">X</td>
			<td class="tg-0lax">  Sélectionner une commune</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getCommune?libcom={string}
				</br>
				GET /cadastrapp/services/getCommune?cgocommune={string}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Sélectionner une section</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  GET /cadastrapp/services/getSection?cgocommune={code}</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Sélectionner une parcelle</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  GET /cadastrapp/services/getDnuplaList?cgocommune={code}&ccopre={code}&ccosec={code}</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Afficher le résultat</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : cgocommune={code}&dnupla={code}&ccopre={code}&ccosec={code}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="2">  Recherche par identifiant </td>
			<td class="tg-0lax" rowspan="2"> </td>
			<td class="tg-0lax">  Chercher une parcelle </br> sur la carte</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
			GET /geoserver/wfs?request=getfeature&version=1.0.0&service=wfs 
			</br>
			&typename={workspace:layer}&outputFormat=application/json 
			/br>
			&cql_filter=geo_parcelle='{code}'
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Afficher le résultat</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
			POST /cadastrapp/services/getParcelle
			</br>
			FORM_DATA : parcelle={code}
			</td>
		</tr>		
		
		<tr>
			<td class="tg-0lax" rowspan="3">  Recherche par adresse cadastrale </td>
			<td class="tg-0lax" rowspan="3">X</td>
			<td class="tg-0lax">  Sélectionner une commune</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getCommune?libcom={string}
				</br>
				GET /cadastrapp/services/getCommune?cgocommune={string}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Sélectionner une voie ou un lieu-dit</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  GET /cadastrapp/services/getVoie?cgocommune={code}&dvoilib={string}</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Afficher le résultat</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : cgocommune={code}&dvoilib={string}&dnvoiri={number}&dindic={string} 
			</td>
		</tr>
			<td class="tg-0lax" rowspan="3">  Recherche par lot </td>
			<td class="tg-0lax" rowspan="3"> </td>
			<td class="tg-0lax">  Afficher le résultat</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : parcelle={code}%0{code}…
			</td>
		</tr>
	</tbody>
</table>




### Rechercher des propriétaires


La recherche de propriétaires se fait via un formulaire qui propose 3 onglets qui correspondent à 3 façons de rechercher des parcelles via la recherche de propriétaires :

* par nom d'usage ou nom de naissance
* par compte propriétaire (identifiant)
* par lot (liste d'identifiants)

<table class="tg">
	<thead>
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax" rowspan="4">  Recherche par nom d'usage ou nom de naissance </td>
			<td class="tg-0lax" rowspan="4">X</td>
			<td class="tg-0lax">  Sélectionner une commune</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getCommune?libcom={string}
				</br>
				GET /cadastrapp/services/getCommune?cgocommune={string}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Recherche par nom d'usage  </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getProprietaire?cgocommune={code}
				</br>
				&ddenom={string}&birthsearch=false
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Recherche par nom de naissance</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getProprietaire?cgocommune={code}
				</br>
				&ddenom={string}&checkBoxSearchByBirthNames=on&details={integer}&birthsearch=true
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Afficher le résultat</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : comptecommunal={string}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="3">  Recherche par compte propriétaire </td>
			<td class="tg-0lax" rowspan="3"> </td>
			<td class="tg-0lax">  Sélectionner une commune</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getCommune?libcom={string}
				</br>
				GET /cadastrapp/services/getCommune?cgocommune={string}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Rechercher un compte proprio</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax"> 
				GET /cadastrapp/services/getProprietaire?cgocommune={code}
				</br>
				&details={integer}&dnupro={string} 
				</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Afficher le résultat</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : comptecommunal={string} 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  Recherche par lot </td>
			<td class="tg-0lax" > </td>
			<td class="tg-0lax">  Afficher le résultat</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : comptecommunal={code}&comptecommunal={code}&…
			</td>
		</tr>
	</tbody>
</table>






### Sélection graphique des parcelles


La sélection de parcelles sur la carte se fait en choisissant l'un des 3 modes de sélection graphique suivant :

* point
* ligne
* polygone



<table class="tg">
	<thead>
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax" rowspan="2">  Par point  </td>
			<td class="tg-0lax" rowspan="2">X</td>
			<td class="tg-0lax">  Sélectionner une parcelle sur la carte</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				<img src ="../images/selection_parcelle_par_point.PNG">
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Afficher le résultat   </td>
			<td class="tg-0lax">    x  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : parcelle={code}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="2">  Par ligne  </td>
			<td class="tg-0lax" rowspan="2">X</td>
			<td class="tg-0lax">  Sélectionner une parcelle sur la carte</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				<img src ="../images/selection_parcelle_par_ligne.PNG">
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Afficher le résultat   </td>
			<td class="tg-0lax">    x  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : parcelle={code}1parcelle={code}...
			</td>
		</tr>		
		<tr>
			<td class="tg-0lax" rowspan="2">  Par polygone  </td>
			<td class="tg-0lax" rowspan="2">X</td>
			<td class="tg-0lax">  Sélectionner une parcelle sur la carte</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				<img src ="../images/selection_parcelle_par_polygone.PNG">
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Afficher le résultat   </td>
			<td class="tg-0lax">    x  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/getParcelle
				</br>
				FORM_DATA : parcelle={code}1parcelle={code}...
			</td>
		</tr>			
	</tbody>
</table>



### Fenêtre de sélection de parcelles


Toutes les recherches aboutisse à l'ouverture de la fenêtre de "Sélection de parcelle" qui est la fenêtre la plus importante de Cadastrapp.
Elle liste les parcelles qui résultent des différentes méthodes de recherche décrites ci-dessus.

En sur-sélectionnant une ou des parcelles de cette liste, on a accès aux fonctions suivantes :

* zoom sur la parcelle sélectionné / zoom sur toutes les parcelles
* affichage de la fiche d'informations sur une parcelle
* unité foncière de la / des parcelles sélectionnées
* exports :

    * liste de parcelles
    * liste de propriétaires
    * liste de co-propriétaires
    * lots des copropriétés

<table class="tg">
	<thead>
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax" rowspan="2">  Zoom sur ... </td>
			<td class="tg-0lax" rowspan="2">X</td>
			<td class="tg-0lax">  liste des parcelle</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">

			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  parcelles sélectionnées   </td>
			<td class="tg-0lax">    x  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  Fiche d'info parcelle </td>
			<td class="tg-0lax" >X</td>
			<td class="tg-0lax">  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="2">  Fiche unité foncière </td>
			<td class="tg-0lax" rowspan="2"> </td>
			<td class="tg-0lax">  Récupérer la géométrie de l'unité foncière</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				<img src ="../images/fenetre_sel_parcelle_unite_fonciere.PNG">	
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Ouvrir la fiche   </td>
			<td class="tg-0lax">    x  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="5">   Exporter sélection</td>
			<td class="tg-0lax" rowspan="5"> </td>
			<td class="tg-0lax">  Exporter liste de parcelles (CSV)</td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				POST /cadastrapp/services/exportParcellesAsCSV 
				</br>					
				FORM_DATA : parcelles={code1,code2,…}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Exporter liste de propriétaires (CSV)</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				POST /cadastrapp/services/exportProprietaireByParcelles 
				</br>					
				FORM_DATA : parcelles={code1,code2,…}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Exporter liste de co-propriétaires (CSV)</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				POST /cadastrapp/services/exportCoProprietaireByParcelles 
				</br>					
				FORM_DATA : parcelles={code1,code2,…}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Exporter lots de co-propriétés (PDF)</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				POST /cadastrapp/services/exportLotsAsPDF 
				</br>					
				FORM_DATA : parcelles={code1,code2,…}
			</td>
		</tr>		
		<tr>
			<td class="tg-0lax">  Exporter lots de co-propriétés (CSV)</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				POST /cadastrapp/services/exportLotsAsCSV 
				</br>					
				FORM_DATA : parcelle={code}&dnubat=+{code}
			</td>
		</tr>			
	</tbody>
</table>

### Fiche information parcelle


Cette fenêtre affiche beaucoup d'information sur les parcelles et les objets associés : propriétaires, co-propriétaires, détails des locaux, subdivisions fiscales, historique de mutation, etc.

<table class="tg">
	<thead>
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax" rowspan="2">  onglet Parcelle </td>
			<td class="tg-0lax" rowspan="2">X</td>
			<td class="tg-0lax">  Afficher les infos  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getFIC?parcelle={code}&onglet=0
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Bordereau parcellaire   </td>
			<td class="tg-0lax">    x  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/createBordereauParcellaire?parcelle={code}
				</br>
				&personaldata={0|1}&basemapindex={0|n}
				</br>
				&fillcolor=81BEF7&opacity=0.4&strokecolor=111111&strokewidth=3  (3)
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="3">  onglet Propriétaires </td>
			<td class="tg-0lax" rowspan="3">X</td>
			<td class="tg-0lax">  Afficher les infos  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getProprietairesByParcelles?parcelles={code}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Relevé de propriété PDF     </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/createRelevePropriete?
				</br>
				compteCommunal={code}&parcelleId={NULL|code}&exportType=on  (4)
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Relevé de propriété CSV     </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/createReleveProprieteAsCSV?
				</br>
				compteCommunal={code}&parcelleId={NULL|code}&exportType=on  (4)
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="3">  onglet Copropriétaires </td>
			<td class="tg-0lax" rowspan="3">X</td>
			<td class="tg-0lax">  Afficher les infos  </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getCoProprietaire?start=0&limit=25&parcelle={code} (1)
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Relevé de propriété PDF     </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				-> Relevé de propriété de l'onglet Propriétaires 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Relevé de propriété CSV     </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				-> Relevé de propriété de l'onglet Propriétaires 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="5">  onglet Bâtiments </td>
			<td class="tg-0lax" rowspan="5">X</td>
			<td class="tg-0lax">  Afficher les infos  </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getBatiments?dnubat=%20A&parcelle={code} (2)
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Relevé de propriété PDF     </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				-> Relevé de propriété de l'onglet Propriétaires 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Relevé de propriété CSV     </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				-> Relevé de propriété de l'onglet Propriétaires 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Descriptif d'habitation     </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getHabitationDetails?invar={code}&annee={integer}
			</td>
		</tr>
				<tr>
			<td class="tg-0lax">  Lots en PDF      </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/exportLotsAsPDF
				</br>
				FORM_DATA : parcelle={code}&dnubat=+{code}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  onglet Subdivisions fiscales </td>
			<td class="tg-0lax" >X</td>
			<td class="tg-0lax">  Afficher les infos  </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getFIC?parcelle={code}&onglet=3 
			</td>
		</tr>
			<tr>
			<td class="tg-0lax" >  onglet Historique de mutation </td>
			<td class="tg-0lax" >X</td>
			<td class="tg-0lax">  Afficher les infos  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getFIC?parcelle={code}&onglet=4    
			</td>
		</tr>	
	</tbody>
</table>


### Traitement des sélections


Ces fonctionnalités sont accessibles depuis le menu "Avancées" dans la barre d'outils.


<table class="tg">
	<thead>
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax" rowspan="2">  Parcelles </td>
			<td class="tg-0lax" rowspan="2"></td>
			<td class="tg-0lax">  Bordereau parcellaire multipages </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/createBordereauParcellaire?parcelle={code1,code2,…}
				</br>
				&personaldata={0|1}&basemapindex={0|n} 
				</br>
				 &fillcolor=81BEF7&opacity=0.4&strokecolor=111111&strokewidth=3  (3) 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Export liste CSV   </td>
			<td class="tg-0lax">    X  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/exportParcellesAsCSV
				</br>
				FORM_DATA : parcelles={code1,code2,…}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  Propriétaires et copropriétaires</td>
			<td class="tg-0lax" > </td>
			<td class="tg-0lax"> Export liste CSV   </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				 POST /cadastrapp/services/exportProprietaireByParcelles  
				 </br>
				 FORM_DATA : parcelles={code1,code2,…}
			</td>
		</tr>
	</tbody>
</table>


### Unité foncière


Une unité foncière (UF) est le regroupement des parcelles contiguës et appartenant à un même compte propriétaire.

La fiche d'information sur une unité foncière permet de présenter l'ensemble des informations descriptives de cette unité foncière :

* propriétaire(s)
* contenance DGFiP, surfaces calculées, pourcentage de la surface bâtie
* liste des parcelles composant l'unité foncière

<table class="tg">
	<thead>
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax" >  Carte </td>
			<td class="tg-0lax" ></td>
			<td class="tg-0lax">  Afficher les couches de la carte courante </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /geowebcache/service/wms?LAYERS={layers}&…  
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="2">   Informations sur l'UF  </td>
			<td class="tg-0lax" rowspan="2"></td>
			<td class="tg-0lax">  Infos sur l'UF  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				 GET /cadastrapp/services/getInfoUniteFonciere?parcelle={code} 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Calculer le pourcentage de la surface bâtie </td>
			<td class="tg-0lax">    X  </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				POST /cadastrapp/services/exportParcellesAsCSV
				</br>
				FORM_DATA : parcelles={code1,code2,…}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" > Infos sur le(s) propriétaire(s) </td>
			<td class="tg-0lax" > </td>
			<td class="tg-0lax"> Infos sur le(s) propriétaire(s)   </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				  GET /cadastrapp/services/getProprietaire?details=2&comptecommunal={code} 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" > Infos sur les parcelles  </td>
			<td class="tg-0lax" > </td>
			<td class="tg-0lax"> Infos sur les parcelles   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				 GET /cadastrapp/services/getParcelle?unitefonciere={code} 
			</td>
		</tr>
	</tbody>
</table>


### Module de demande d'information foncière


Le module de demande d'information foncière permet de gérer les demandes d'informations tout en respectant la réglementation.

TODO : vérifier les droits niveaux CNIL


<table class="tg">
	<thead>
		<th class="tg-0lax">Fonctionnalité</th>
		<th class="tg-0lax">  Responsive</th>
		<th class="tg-0lax">  Action</th>
		<th class="tg-0lax"> CNIL&nbsp;0 </th>
		<th class="tg-0lax"> CNIL&nbsp;1 </th>
		<th class="tg-0lax"> CNIL&nbsp;2 </th>
		<th class="tg-0lax">  Appel API </th>
	</thead>
	<tbody>
		<tr>
			<td class="tg-0lax" >  Vérifier si le demandeur est en droit de faire une nouvelle demande </td>
			<td class="tg-0lax" ></td>
			<td class="tg-0lax">  Récupérer infos de contrôle </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/checkRequestLimitation?cni={string}&type={A|P1|P2|P3} 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="3">Parcelle par référence </td>
			<td class="tg-0lax" rowspan="3"></td>
			<td class="tg-0lax">Sélectionner une commune</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				 GET /cadastrapp/services/getCommune?libcom={string}
				</br>
				 GET /cadastrapp/services/getCommune?cgocommune={string} 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">  Sélectionner une section </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getSection?cgocommune={code}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">Sélectionner une parcelle </td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getDnuplaList?cgocommune={code}&ccopre={code}&ccosec={code}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  Parcelle par identifiant  </td>
			<td class="tg-0lax" ></td>
			<td class="tg-0lax">  - </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="2">Propriétaire par nom d'usage</td>2
			<td class="tg-0lax" rowspan="2"></td>
			<td class="tg-0lax">Sélectionner une commune</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				 GET /cadastrapp/services/getCommune?libcom={string}
				</br>
				 GET /cadastrapp/services/getCommune?cgocommune={string} 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">Recherche par nom d'usage</td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getProprietaire?cgocommune={code}
				</br>
				&ddenom={string}&birthsearch=false
			</td>  
		</tr>
		<tr>
			<td class="tg-0lax" rowspan="2">Propriétaire par nom de naissance</td>2
			<td class="tg-0lax" rowspan="2"></td>
			<td class="tg-0lax">Sélectionner une commune</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				 GET /cadastrapp/services/getCommune?libcom={string}
				</br>
				 GET /cadastrapp/services/getCommune?cgocommune={string} 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">Recherche par nom d'usage</td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getProprietaire?cgocommune={code}
				</br>
				&ddenom={string}&birthsearch=true  
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  Propriétaire par identifiant  </td>
			<td class="tg-0lax" ></td>
			<td class="tg-0lax">  - </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  Copropriété </td>
			<td class="tg-0lax" ></td>
			<td class="tg-0lax">  - </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">
			</td>
		</tr>
	    <tr>
			<td class="tg-0lax" rowspan="4"> Lot de copropriété </td>2
			<td class="tg-0lax" rowspan="4"></td>
			<td class="tg-0lax">Sélectionner une commune</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">
				 GET /cadastrapp/services/getCommune?libcom={string}
				</br>
				 GET /cadastrapp/services/getCommune?cgocommune={string} 
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">Sélectionner une section</td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getSection?cgocommune={code}     
			</td>
		</tr>
		<tr>
			<td class="tg-0lax">Sélectionner une parcelle</td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getDnuplaList?cgocommune={code}&ccopre={code}&ccosec={code}
			</td>     
		</tr>
		<tr>
			<td class="tg-0lax">Sélectionner un co-propriétaire</td>
			<td class="tg-0lax">      </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">    X   </td>
			<td class="tg-0lax">  
				GET /cadastrapp/services/getProprietairesByInfoParcelles?
				</br>		
				commune={code}&section={code}&numero={code}&ddenom={string}	
			</td>			
		</tr>
		<tr>
			<td class="tg-0lax" >  Sauvegarder les informations sur la demande </td>
			<td class="tg-0lax" ></td>
			<td class="tg-0lax">  Envoyer au serveur </td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">     X  </td>
			<td class="tg-0lax">     X </td>
			<td class="tg-0lax">
				GET /cadastrapp/services/getParcelle?/saveInformationRequest?type={A|P1|P2|P3} 
				</br>
				&cni={string}
				</br>
				&lastname={string}&firstname={string}&adress={string} 
				</br>
				&commune={string}&codepostal={string}
				</br>
				&mail={string}
				</br>
				&parcelleIds={string}
				</br>
				&comptecommunaux={string}
				</br>
				&coproprietes={string}
				</br>
				&parcelles={string}
				</br>
				&proprietaires={string}
				</br>
				&proprietaireLots={string}
				</br>
				&responseby={1|2|3}&askby={1|2|3}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  Imprimer la demande</td>
			<td class="tg-0lax" ></td>
			<td class="tg-0lax">  Récupérer le formulaire PDF</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">     X  </td>
			<td class="tg-0lax">     X </td>
			<td class="tg-0lax">
				 GET /cadastrapp/services/printPDFRequest?requestid={code}
			</td>
		</tr>
		<tr>
			<td class="tg-0lax" >  Récupérer les documents </td>
			<td class="tg-0lax" ></td>
			<td class="tg-0lax">  Récupérer le(s) PDF</td>
			<td class="tg-0lax">       </td>
			<td class="tg-0lax">     X  </td>
			<td class="tg-0lax">     X </td>
			<td class="tg-0lax">
				 GET /cadastrapp/services/createDemandeFromObj?requestid={code}  
			</td>
		</tr>	
	</tbody>
</table>



### Notes


1. pagination

1. par défaut on sélectionne le premier bâtiment, soit le bâtiment A

1. ces informations de style proviennent des préférences

1. parcelleId={NULL} pour un relevé de propriété de toutes les parcelles



Notes de réflexion :

* Rechercher des parcelles > Recherche par identifiant : utilise uniquement les parcelles du plan cadastral. Ce n'est pas logique.

* Rechercher des co-propriétés : accessible uniquement par le menu de recherche avancée : est-ce nécessaire de le maintenir ?


