package org.georchestra.cadastrapp.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.support.LdapContextSource;

@Configuration
@PropertySource(
        value = { 
                "file:${georchestra.datadir}/default.properties", 
                "file:${georchestra.datadir}/cadastrapp/cadastrapp.properties}"
        },
        ignoreResourceNotFound = true
)
public class LdapConfig {
    @Autowired
    private Environment env;
    
    @Bean
    public LdapContextSource contextSource() {
        LdapContextSource contextSource = new LdapContextSource();
        // Base in Orgs because env based annotation not work in OrganismeAutorisation's Entry 
        contextSource.setBase(env.getProperty("ldapOrgsRdn")+","+env.getProperty("ldapBaseDn"));
        contextSource.setUrl(env.getProperty("ldapScheme")+"://"+env.getProperty("ldapHost")+":"+env.getProperty("ldapPort"));
        contextSource.setUserDn(env.getProperty("ldapAdminDn"));
        contextSource.setPassword(env.getProperty("ldapAdminPassword"));

        return contextSource;
    }
}
