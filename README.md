# Cadastrapp 

## À propos de Cadastrapp

Cadastrapp est l’outil de consultation des données cadastrales fournies par la Direction Générale des Finances Publiques (DGFiP) pour geOrchestra. Il est composé d’un module serveur qui est une API d’accès aux données de la matrice foncière et d'un greffon pour le visualiseur de geOrchestra. L’API peut être utilisée par n’importe quelle application client sous réserve de passer par le système d’authentification de [geOrchestra](https://www.georchestra.org).

## Organisation

Cadastrapp est composé des éléments suivants :
- un script de chargement / mise à jour des données cadastrales dans le répertoire `database`. Voir la [documentation](database/README.md).
- une webapp Java qui compose la partie _backend_ / API dans le répertoire `cadastrapp`. Voir la [documentation](cadastrapp/README.md)
- une partie client pour Mapfishapp dans le répertoire `addons`
- une partie client pour MapStore dans un [dépôt dédié](https://github.com/georchestra/mapstore2-cadastrapp/)
- 3 propositions de fond de plan pour GeoServer dans le répertoire `geoserver-workspace`


## Documentation

[https://docs.georchestra.org/cadastrapp/](https://docs.georchestra.org/cadastrapp/) ou [https://docs.georchestra.org/cadastrapp/latest/](https://docs.georchestra.org/cadastrapp/latest/)

Les [notes de versions](https://github.com/georchestra/cadastrapp/releases) pour connaître le détail des évolutions.

## Aide

Posez vos questions à la communauté en écrivant à georchestra@googlegroups.com



----

# Cadastrapp (english)

## About Cadastrapp

Cadastrapp is the tool for consulting cadastral data provided by the Direction Générale des Finances Publiques (DGFiP) for geOrchestra. It is composed of a server module which is an API for accessing data from the land matrix and a plugin to the geOrchestra viewer. The API can be used by any client application as long as it goes through the [geOrchestra](https://www.georchestra.org) authentication system (https://www.georchestra.org).

## Organisation

Cadastrapp is composed by these following elements :
- a data load / data refresh script in the `database` directory. See the [documentation](database/README.md).
- a Java webapp wich compose _backend_ / API part in the `cadastrapp` directory. See the [documentation](cadastrapp/README.md)
- a client part for Mapfishapp in the `addons` directory
- a client part for MapStore in a [dedicated repository](https://github.com/georchestra/mapstore2-cadastrapp/)
- 3 map background propositions for GeoServer in the `geoserver-workspace` directory


## Documentation

[https://docs.georchestra.org/cadastrapp/](https://docs.georchestra.org/cadastrapp/) ou [https://docs.georchestra.org/cadastrapp/latest/](https://docs.georchestra.org/cadastrapp/latest/)

The [releases notes](https://github.com/georchestra/cadastrapp/releases) for knowledge on details of the evolutions.


## Aide

Ask to the community by wrinting to georchestra@googlegroups.com


## Sponsors

This project has been funded by : 


<table>
    <tbody>  <tr>
            <td>Agglomération le puy en velay</td>
            <td align="center"><img src="https://opendata.agglo-lepuyenvelay.fr/images/logos/agglo.png" width="200" alt = "Agglomération le puy en velay"></td>
        </tr>
        <tr>
            <td>Aménagement du territoire et gestion des risques</td>
            <td align="center"><img src="https://cloud.githubusercontent.com/assets/11499415/14116676/41fbce6c-f5e1-11e5-8863-2b1f4cd19034.jpg" width="200" alt = "Aménagement du territoire et gestion des risques"></td>
        </tr>
        <tr>
            <td>Conseil départemental du Bas-Rhin</td>
            <td align="center"><img src="https://cloud.githubusercontent.com/assets/5012040/13945329/ac9a6786-f00c-11e5-8acc-b21705db585b.png" width="200" alt = "Conseil Départemental du Bas-Rhin"></td>
        </tr>
        <tr>
             <td>CRAIG : Centre Régional Auvergne-Rhône-Alpes de l'Information Géographique</td>
            <td align="center"><img src="https://cloud.githubusercontent.com/assets/3421760/14113316/bf38b2e6-f5d2-11e5-87c5-754f776a5962.jpg" width="200" alt = "Centre Réginal Auvergnat de l'Information Géographique"></td>
        </tr>
         <tr>
             <td>Rennes métropole</td>
            <td align="center"><img src="https://cloud.githubusercontent.com/assets/6370443/13951133/407ee162-f02f-11e5-8c70-a7b6cff7ba43.jpg" width="200" alt = "Rennes Métropole"></td>
        </tr>
        <tr>
             <td>Union européenne en Auvergne</td>
            <td align="center"><img src="https://cloud.githubusercontent.com/assets/3421760/14113246/5e8bdf2c-f5d2-11e5-86a1-638b191194d3.png" width="200" alt = "Union Européenne en Auvergne"></td>
        </tr>
    </tbody>
</table>

