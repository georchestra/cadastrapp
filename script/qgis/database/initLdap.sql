CREATE EXTENSION multicorn;

CREATE SERVER ldap_srv foreign data wrapper multicorn options (
    wrapper 'multicorn.ldapfdw.LdapFdw'
);

CREATE FOREIGN TABLE #schema_cadastrapp.organisations (
    cn character varying,
    description character varying
) server ldap_srv options (
    uri '#ldap_uri',
    path '#ldap_path',
    scope 'sub',
    binddn '#ldap_binddn',
    bindpwd '#ldap_bindpwd',
    objectClass 'groupOfMembers'
);
GRANT SELECT ON TABLE #schema_cadastrapp.organisations TO #user_cadastrapp;