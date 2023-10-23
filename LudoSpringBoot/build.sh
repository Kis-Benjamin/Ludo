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

# Create mkcert certificate
mkcert -install

# Create certificate for localhost
mkcert -cert-file base/certfile.pem -key-file base/keyfile.pem server keycloak postgres localhost 127.0.0.1 ::1

# Copy root certificate to base directory
cp "$(mkcert -CAROOT)/*" base/

# Build the base image
docker compose build base

# Build the builder image
docker compose build builder

# Build the rest of the images
docker compose build