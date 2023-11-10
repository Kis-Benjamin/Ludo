FROM quay.io/keycloak/keycloak:latest as builder

# Set working directory
WORKDIR /opt/keycloak

# Set environment variables
ENV \
# Enable health and metrics support
 KC_HEALTH_ENABLED=true \
 KC_METRICS_ENABLED=true \
# Enable docker feature
#  KC_FEATURES=docker \
# Configure the database vendor
 KC_DB=postgres

# Build optimized Keycloak
RUN bin/kc.sh build

FROM ludo_base:latest

# Copy built Keycloak from builder image
COPY --from=builder /opt/keycloak/ /opt/keycloak/

# Set entrypoint
ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]
