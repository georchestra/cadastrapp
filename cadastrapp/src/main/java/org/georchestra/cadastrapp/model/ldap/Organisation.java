package org.georchestra.cadastrapp.model.ldap;

import javax.naming.Name;

import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

@Entry(objectClasses={ "groupOfMembers" })
public class Organisation {
    @Id
    private Name dn;
    
    @Attribute(name="cn")
    String cn;
    
    @Attribute(name="description")
    String description;
    
    public Organisation() {
        super();
    }

    public Organisation(Name dn, String cn, String description) {
        super();
        this.dn = dn;
        this.cn = cn;
        this.description = description;
    }

    public Name getDn() {
        return dn;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

    public String getCn() {
        return cn;
    }

    public void setCn(String cn) {
        this.cn = cn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
