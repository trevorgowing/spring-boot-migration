FROM openjdk:8u171-jdk-alpine3.7

ENV GRADLE_OPTS -Dorg.gradle.daemon=false

RUN mkdir -p /opt/spring-boot-migration

WORKDIR /opt/spring-boot-migration

EXPOSE 8080 5005

COPY gradlew .
COPY gradle gradle
COPY settings.gradle .
COPY build.gradle .

RUN ./gradlew resolveDependencies

COPY src/main src/main

RUN ./gradlew bootJar

CMD ["java", "-Xdebug", "-Xrunjdwp:server=y,transport=dt_socket,address=5005,suspend=n", "-jar", "/opt/spring-boot-migration/build/libs/spring-boot-migration-2.0.x.jar"]
