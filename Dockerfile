FROM eclipse-temurin:17-jre
COPY build/libs/helloWorld-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
