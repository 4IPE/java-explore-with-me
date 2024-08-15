FROM amazoncorretto:21-alpine-jdk
VOLUME /tmp
COPY target/ewm-service-0.0.1-SNAPSHOT.jar ewm-service-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "ewm-service-0.0.1-SNAPSHOT.jar"]