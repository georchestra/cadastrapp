INSERT INTO cron.job (schedule, command, nodename, nodeport, database, username)
VALUES ('0 * * * *', 'REFRESH MATERIALIZED VIEW #schema_cadastrapp.org_autorisation', 
'127.0.0.1', 5432, '#cadastrapp_db_name', '#user_cadastrapp');
