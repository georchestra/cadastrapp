Ext.namespace("GEOR.Addons.Cadastre");

/**
 * Test if user as "cnil1Role" in his role list
 * 
 * ROLE_ is added to the ldap group by georchestra
 * 
 * @return {Boolean}
 */
GEOR.Addons.Cadastre.isCNIL1 = function() {
    return (GEOR.config.ROLES.indexOf(GEOR.Addons.Cadastre.cnil1RoleName) != -1);
};
  
/**
 * 
 * Test if user as "cnil2Role" in his role list
 * 
 * ROLE_ is added to the ldap group by georchestra
 * 
 * @return {Boolean} 
 */
GEOR.Addons.Cadastre.isCNIL2 = function() {
    return (GEOR.config.ROLES.indexOf(GEOR.Addons.Cadastre.cnil2RoleName) != -1);
};

