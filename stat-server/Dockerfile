FROM amazoncorretto:11-alpine-jdk
VOLUME /tmp
COPY target/stat-server-0.0.1-SNAPSHOT.jar stat-server-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/stat-server-0.0.1-SNAPSHOT.jar"]