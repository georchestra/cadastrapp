Ext.namespace("GEOR.Addons.Cadastre");

/**
 * Affiche la fenêtre de détails d'habitation pour les éléments données
 * 
 * @param batiment
 * @param niveau
 * @param porte
 * @param annee
 * @param invar
 * 
 */
GEOR.Addons.Cadastre.showHabitationDetails = function(batiment, niveau, porte, annee, invar) {
    
    // Creation de la fenetre de details d'habitation
    var habitationDetailsWindows = new Ext.Window({
        title:" Batiment " + batiment + " - niveau "+ niveau+ " - porte " + porte,
        frame : true,
        autoScroll : true,
        layout:'accordion',
        defaults: {
            // applied to each contained panel
            bodyStyle: 'padding:15px'
        },
        layoutConfig: {
            // layout-specific configs go here
            titleCollapse: false,
            animate: true,
            activeOnTop: true
        },
        items: [],
        listeners : {
            close : function(window) {
                habitationDetailsWindows = null;
            }
        },
    });
           
    // récuperation des données article40, article50 et article60
    Ext.Ajax.request({
        url : GEOR.Addons.Cadastre.cadastrappWebappUrl + 'getHabitationDetails?invar='+invar+'&annee='+annee,
        method : 'GET',
        success : function(response) {
            var result = Ext.decode(response.responseText);
            
            // Pour chaque Habitation
            Ext.each(result.article40, function(element, indexNumber){
               if(element){
                   var article40Panel = GEOR.Addons.Cadastre.createArcticle40Panel(element);
                   if(article40Panel!=null){
                       habitationDetailsWindows.add(article40Panel);
                   }
                  
               }
            });
            
            // Pour chaque locaux professionel
            Ext.each(result.article50, function(element, indexNumber){
                if(element){
                    var article50Panel = GEOR.Addons.Cadastre.createArcticle50Panel(element);
                    if(article50Panel!=null){
                        habitationDetailsWindows.add(article50Panel);
                    }
                }
            });
            
         // Pour chaque dépendance
            Ext.each(result.article60, function(element, indexNumber){
                if(element){
                    var article60Panel = GEOR.Addons.Cadastre.createArcticle60Panel(element);
                    if(article60Panel!=null){
                        habitationDetailsWindows.add(article60Panel);
                    }
                }
            });
            
            habitationDetailsWindows.doLayout();
        }
    });

    GEOR.Addons.Cadastre.createArcticle40Panel = function(article40Details) {
        
        if(article40Details && article40Details.detent && article40Details.detent!=null){
           
            var details = '<div class=\'habitationDetailsMenuTitle\'> Caractéristiques générales </div>'; 
            article40Details.detent &&  article40Details.detent!='00' ? details = details + '<div> Etat d\'entretien &nbsp;:&nbsp;&nbsp;' + article40Details.detent + '</div>' : null;
            article40Details.dsupdc &&  article40Details.dsupdc!='00' ? details = details + '<div> Surface habitable &nbsp;:&nbsp;&nbsp;' + article40Details.dsupdc + ' m²</div>' : null;
            article40Details.dnbppr && article40Details.dnbpdc &&  article40Details.dnbpdc!='00' ? details = details + '<div> Nombre de pièces &nbsp;:&nbsp;&nbsp;' + article40Details.dnbpdc.replace(/^0+/,'') + ' dont ' + article40Details.dnbppr.replace(/^0+/,'') + ' principales</div>' : null;
            article40Details.dnbniv &&  article40Details.dnbniv!='00' ? details = details +  '<div> Nombre de niveaux &nbsp;:&nbsp;&nbsp;' + article40Details.dnbniv.replace(/^0+/,'' ) + '</div>' : null;  
                 
            article40Details.dmatgmdesc ? details = details + '<div class=\'habitationDetailsMenuTitle\'> Type de matériaux pour les murs</div><div>' + article40Details.dmatgmdesc + '</div>' : null;
            article40Details.dmattodesc ? details = details + '<div class=\'habitationDetailsMenuTitle\'> Type de matériaux pour la toiture</div><div>' + article40Details.dmattodesc + '</div>' : null;
            
            details = details + '<div class=\'habitationDetailsMenuTitle\'>Répartition des pièces</div>';
            article40Details.dnbsam &&  article40Details.dnbsam!='00' ? details = details +         '<div>' + article40Details.dnbsam.replace(/^0+/,'' ) + ' salle(s) à manger</div>'  : null;  
            article40Details.dnbcha &&  article40Details.dnbcha!='00' ? details = details +  '<div>' + article40Details.dnbcha.replace(/^0+/,'' ) + ' chambre(s)</div>' : null; 
            article40Details.dnbcu8 &&  article40Details.dnbcu8!='00' ? details = details +   '<div>' + article40Details.dnbcu8.replace(/^0+/,'' ) + ' cuisine(s) de moins de 9m²</div>' : null; 
            article40Details.dnbcu9 &&  article40Details.dnbcu9!='00' ? details = details +   '<div>' + article40Details.dnbcu9.replace(/^0+/,'' ) + ' cuisine(s) d\'au moins de 9m²</div>' : null;  
            article40Details.dnbsea &&  article40Details.dnbsea!='00' ? details = details +   '<div>' + article40Details.dnbsea.replace(/^0+/, '') + ' salle(s) de bain</div>' : null; 
            article40Details.dnbann &&  article40Details.dnbann!='00' ? details = details +   '<div>'   + article40Details.dnbann.replace(/^0+/,'' ) + ' annexe(s)</div>' : null; 
            
            details = details + '<div class=\'habitationDetailsMenuTitle\'>Eléments de confort</div>'; 
            article40Details.dnbbai &&  article40Details.dnbbai!='00' ? details = details +   '<div>' + article40Details.dnbbai.replace(/^0+/,'' ) + ' baignoire(s)</div>'  : null; 
            article40Details.dnblav &&  article40Details.dnblav!='00' ? details = details +   '<div>' + article40Details.dnblav.replace(/^0+/,'' ) + ' lavabo(s)</div>'  : null; 
            article40Details.dnbdou &&  article40Details.dnbdou!='00' ? details = details +   '<div>' + article40Details.dnbdou.replace(/^0+/,'' ) + ' douche(s)</div>'  : null; 
            article40Details.dnbwc &&  article40Details.dnbwc!='00' ? details = details +   '<div>' + article40Details.dnbwc.replace(/^0+/,'' ) + ' WC</div>' : null;                 
            article40Details.geaulc &&  article40Details.geaulc=='O' ? details = details + '<div>Eau</div>' : null; 
            article40Details.gelelc &&  article40Details.gelelc=='O'? details = details + '<div>Electricité</div>' : null; 
            article40Details.ggazlc &&  article40Details.ggazlc=='O' ? details = details + '<div>Gaz</div>' : null;        
            article40Details.gchclc &&  article40Details.gchclc=='O' ? details = details + '<div>Chauffage central</div>' : null; 
            article40Details.gteglc &&  article40Details.gteglc=='O' ? details = details + '<div>Tout à l\'égoût</div>' : null; 
            article40Details.gesclc &&  article40Details.gesclc=='O' ? details = details + '<div>Escalier de service</div>' : null;
            article40Details.gaslc &&  article40Details.gaslc=='O' ? details = details + '<div>Ascenseur</div>' : null; 
            article40Details.gvorlc &&  article40Details.gvorlc=='O' ? details = details + '<div>Vide-ordure</div>' : null;

            
            return  new Ext.Panel({
                title: 'Habitation    ' + article40Details.dnudes,
                html:details,
                layout: 'fit'
            });
        }
    }
    
  GEOR.Addons.Cadastre.createArcticle50Panel = function(article50Details) {
        
        if(article50Details){
            return  new Ext.Panel({
                title: 'Partie professionelle   ' + article50Details.dnudes,
                html:'<div>Surface réelle ' + article50Details.vsurzt + ' m²</div>',
                layout: 'fit'
            });
        }
    }
  
  GEOR.Addons.Cadastre.createArcticle60Panel = function(article60Details) {
       
      if(article60Details){
          
          var details = '<div>' + article60Details.cconad_lib + ' ' + article60Details.dsudep + ' m²</div>'; 
          
          article60Details.dmatgmdesc ? details = details + '<div class=\'habitationDetailsMenuTitle\'>Type de matériaux pour les murs</div><div>' + article60Details.dmatgmdesc + '</div>' : null;
          article60Details.dmattodesc ? details = details + '<div class=\'habitationDetailsMenuTitle\'>Type de matériaux pour la toiture</div><div>' + article60Details.dmattodesc + '</div>' : null;
        
          article60Details.dnbbai &&  article60Details.dnbbai!='00' ? details = details + '<div>' + article60Details.dnbbai.replace(/^0+/, '') + ' Baignoire(s)</div>' : null; 
          article60Details.dnbdou &&  article60Details.dnbdou!='00'? details = details + '<div>' + article60Details.dnbdou.replace(/^0+/, '') + ' Douche(s)</div>' : null; 
          article60Details.dnblav &&  article60Details.dnblav!='00' ? details = details + '<div>' + article60Details.dnblav.replace(/^0+/, '') + ' Lavabo(s)</div>' : null; 
          article60Details.dnbwc &&  article60Details.dnbwc!='00' ? details = details +  '<div>' + article60Details.dnbwc.replace(/^0+/,'' ) + ' WC</div>'  : null; 
          article60Details.geaulc &&  article60Details.geaulc=='0' ? details = details + '<div>Eau</div>' : null; 
          article60Details.gelelc &&  article60Details.gelelc=='0'  ? details = details + '<div>Electricité</div>'  : null;  
          article60Details.gchclc &&  article60Details.gchclc=='0'  ? details = details + '<div>Chauffage centrale</div>' : null; ;
          
          
          return  new Ext.Panel({
              title: 'Dépendance(s)  ' + article60Details.dnudes,
              html: details,
              layout: 'fit'
          });
      }
  }

    habitationDetailsWindows.show();
}
    
