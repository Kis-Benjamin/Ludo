FROM quay.io/almalinuxorg/9-minimal:latest

# Install Java 17
RUN microdnf upgrade -y && \
    microdnf install -y java-17-openjdk-headless && \
    microdnf clean all
