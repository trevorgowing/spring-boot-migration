[![Build Status](https://travis-ci.com/trevorgowing/spring-boot-migration.svg?branch=master)](https://travis-ci.com/trevorgowing/spring-boot-migration)

# Migrating from Spring Boot 1.5 to 2.0
Accompanying demonstration project for [Migrating to Spring Boot 2 and Webflux](https://docs.google.com/presentation/d/1k3G6tUdR_qLqg8VixDts7HG5P0BAGBl-mkVpQYYHvu0/edit?usp=sharing) [meetup](https://www.meetup.com/Cape-Town-Java-Meetup/events/251671231/) talk.

## Technology

### Production Code

* Language: [Java](http://www.oracle.com/technetwork/java/javase/overview/index.html)
* Application Server (Servlet Container): [Apache Tomcat](http://tomcat.apache.org/)
* Application and Web Framework: [Spring Boot](https://projects.spring.io/spring-boot/)
* Code Generation: [Project Lombok](https://projectlombok.org/)

### Test Code

* [Spring Boot Test](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-testing.html)
* [Rest Assured](http://rest-assured.io/)

### Tooling

* Build: [Gradle](https://gradle.org/)
* Code Formatting: [Spotless](https://github.com/diffplug/spotless) with [Google Style Guide](https://google.github.io/styleguide/javaguide.html)
* Containerization: [Docker](https://www.docker.com/)

## Build, Run and Test

The recommended [gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html) is used to build the application and is included in the source code.

### Build

* With Gradle: `./gradlew build`
* With Docker: `sudo docker build -t trevorgowing/spring-boot-demo:X.X.X .`
* With Docker Compose: `sudo docker-compose build spring-boot-demo`

### Run

* With Gradle: `./gradlew bootrun` (will build if not already built)
* With Docker: `sudo docker run trevorgowing/spring-boot-demo:X.X.X` (needs to be built explicitly)
* With Docker Compose: `sudo docker-compose --build up`
* With Bash Script: `./start.sh` (runs docker-compose internally)

### Test

* With Gradle: `./gradlew test`

## License

[MIT License](LICENSE)
