FROM quay.io/almalinuxorg/9-minimal:latest

# Install Java 17 and openssl
RUN microdnf upgrade -y && \
    microdnf install -y java-17-openjdk-headless nss-tools openssl && \
    microdnf clean all

# Set working directory
WORKDIR /opt/

# Install mkcert
RUN curl -JLO "https://dl.filippo.io/mkcert/latest?for=linux/amd64" && \
    chmod +x mkcert-v*-linux-amd64 && \
    cp mkcert-v*-linux-amd64 /usr/local/bin/mkcert

# Copy certificates
COPY rootCA.pem rootCA-key.pem certfile.pem keyfile.pem ./

# Install mkcert root CA
RUN mkdir -p $(mkcert -CAROOT) && \
    cp rootCA.pem rootCA-key.pem $(mkcert -CAROOT) && \
    mkcert -install
