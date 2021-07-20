INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
VALUES ('0 * * * *', 'REFRESH MATERIALIZED VIEW #schema_cadastrapp.org_autorisation', 
'#cadastrapp_db_host', #cadastrapp_db_port, '#cadastrapp_db_name', '#user_cadastrapp');
