FROM ludo_builder:latest AS builder

WORKDIR /opt/ludo-server

COPY src game-server/src

RUN ./gradlew --no-daemon :game-server:bootJar

RUN rm -f game-server/build/libs/*-plain.jar

FROM ludo_base:latest

WORKDIR /opt/ludo-server

COPY --from=builder /opt/ludo-server/game-server/build/libs/game-server-*.jar game-server.jar

ENTRYPOINT ["java", "-jar", "game-server.jar"]
