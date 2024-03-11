FROM eclipse-temurin:17

WORKDIR /app

COPY target/alert-0.0.1-SNAPSHOT.jar /app/alertMNS.jar

ENTRYPOINT ["java", "-jar", "alertMNS.jar"]