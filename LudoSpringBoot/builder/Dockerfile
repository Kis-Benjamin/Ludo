FROM ludo_base:latest

WORKDIR /opt/ludo-server

COPY gradlew build.gradle.kts settings.gradle.kts ./
COPY gradle gradle
COPY game-server/build.gradle.kts ./game-server/
COPY game-server/src/main/kotlin/hu/bme/aut/alkfejl/ludospringboot/gameserver/GameServerApplication.kt ./game-server/src/main/kotlin/hu/bme/aut/alkfejl/ludospringboot/gameserver/GameServerApplication.kt

ENV GRADLE_USER_HOME /cache

RUN ./gradlew --no-daemon build
