package org.georchestra.cadastrapp.cron;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.georchestra.cadastrapp.model.ldap.GroupeAutorisation;
import org.georchestra.cadastrapp.model.ldap.Organisation;
import org.georchestra.cadastrapp.repository.GroupeAutorisationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LDAPGroupsAutorisationsSync {
    private static final Logger LOGGER =  LogManager.getLogger( LDAPGroupsAutorisationsSync.class );
    
    @Autowired
    Environment env;
    
    @Autowired
    LdapTemplate lt;
    
    @Autowired
    GroupeAutorisationRepository gar;
    
    @PostConstruct
    @Scheduled(cron = "${ldapAreas.cronExpression}")
    public void refreshOrganisationsPermissions() {
        if( env.containsProperty("ldapAreas.enable") && env.getProperty("ldapAreas.enable").contentEquals("true") ) {
            List<Organisation> lstOrg = lt.findAll(Organisation.class);
            List<GroupeAutorisation> lstGA = new ArrayList<>();
            for (Organisation o : lstOrg) {
                if( o.getDescription() != null ) {
                    for (String insee : o.getDescription().split(",")) {
                        if( StringUtils.isNumeric(insee) )
                            lstGA.add(new GroupeAutorisation(null, o.getCn(), insee.substring(0, 2)+"0"+insee.substring(2,5), null));
                    }
                }
            }
            
            int index = 1;
            for (GroupeAutorisation ga : lstGA) {
                ga.setId(index++);
            }
            gar.deleteAll();
            gar.saveAllAndFlush(lstGA);
            LOGGER.info("Synced "+lstGA.size()+" group's authorisations from LDAP");
        }
    }
}
