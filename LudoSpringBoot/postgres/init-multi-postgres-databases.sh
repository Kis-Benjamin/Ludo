#!/bin/bash

#
# Copyright © 2023 Benjamin Kis
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

# Exit immediately if a command exits with a non zero status
set -e
# Treat unset variables as an error when substituting
set -u

function create_databases() {
    database=$1
    username=$2
    password=$3
    echo "Creating database '$database' with user '$username' and password '$password'"
    psql -v ON_ERROR_STOP=1 <<-EOSQL
      CREATE USER $username with encrypted password '$password';
      CREATE DATABASE $database;
      GRANT ALL PRIVILEGES ON DATABASE $database TO $username;
EOSQL
    psql -v ON_ERROR_STOP=1 --username "postgres" --dbname "$database" <<-EOSQL
      GRANT USAGE, CREATE ON SCHEMA public TO $username;
EOSQL
}

if [ -n "$POSTGRES_MULTIPLE_DATABASES" ]; then
  echo "Multiple database creation requested: $POSTGRES_MULTIPLE_DATABASES"
  for db in $(echo "$POSTGRES_MULTIPLE_DATABASES" | tr ',' ' '); do
    name=$(echo "$db" | awk -F":" '{print $1}')
    user=$(echo "$db" | awk -F":" '{print $2}')
    pass=$(echo "$db" | awk -F":" '{print $3}')

    create_databases "$name" "$user" "$pass"
  done
  echo "Multiple databases created!"
fi
