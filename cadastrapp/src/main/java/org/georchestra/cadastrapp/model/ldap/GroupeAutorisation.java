package org.georchestra.cadastrapp.model.ldap;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="groupe_autorisation")
public class GroupeAutorisation {
    @Id
    Integer id;
    
    @Column(name = "idgroup")
    String idGroup;
    
    @Column(name="cgocommune")
    String cgoCommune;
    
    @Column(name="ccodep")
    String ccodep;
    
    public GroupeAutorisation() {
        super();
    }

    public GroupeAutorisation(Integer id, String idGroup, String cgoCommune, String ccodep) {
        super();
        this.id = id;
        this.idGroup = idGroup;
        this.cgoCommune = cgoCommune;
        this.ccodep = ccodep;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getIdGroup() {
        return idGroup;
    }

    public void setIdGroup(String idGroup) {
        this.idGroup = idGroup;
    }

    public String getCgoCommune() {
        return cgoCommune;
    }

    public void setCgoCommune(String cgoCommune) {
        this.cgoCommune = cgoCommune;
    }

    public String getCcodep() {
        return ccodep;
    }

    public void setCcodep(String ccodep) {
        this.ccodep = ccodep;
    }
}
