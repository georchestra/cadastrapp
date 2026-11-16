#!/bin/sh
set -eu

psql -X -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<'SQL'
CREATE SCHEMA cadastrapp_qgis AUTHORIZATION "www-data";
SQL

for sql_file in /init/request_information.sql /init/groupe_autorisation.sql; do
  test -r "$sql_file"
  sed \
    -e 's/#schema_cadastrapp/cadastrapp_qgis/g' \
    -e 's/#user_cadastrapp/"www-data"/g' \
    "$sql_file" | psql -X -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB"
done
