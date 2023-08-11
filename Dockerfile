FROM openjdk:latest

WORKDIR /app

COPY target/spring-petclinic-3.1.0-SNAPSHOT.jar app.jar
COPY applicationinsights-agent-3.4.15.jar /app/applicationinsights-agent-3.4.15.jar
COPY WelcomeController\$ClassLoaderLeakExample\$LoadedInChildClassLoader.class /app/WelcomeController\$ClassLoaderLeakExample\$LoadedInChildClassLoader.class

EXPOSE 8080

ENTRYPOINT ["java", "-javaagent:/app/applicationinsights-agent-3.4.15.jar", "-jar", "app.jar"]