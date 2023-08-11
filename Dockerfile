FROM openjdk:latest

WORKDIR /app

COPY target/spring-petclinic-3.1.0-SNAPSHOT.jar app.jar
COPY applicationinsights-agent-3.4.15.jar /app/applicationinsights-agent-3.4.15.jar
COPY WelcomeController\$ClassLoaderLeakExample\$LoadedInChildClassLoader.class /app/WelcomeController\$ClassLoaderLeakExample\$LoadedInChildClassLoader.class
ENV APPLICATIONINSIGHTS_CONNECTION_STRING="InstrumentationKey=165121b0-9783-4196-81df-672a15e344ce;IngestionEndpoint=https://westus-0.in.applicationinsights.azure.com/;LiveEndpoint=https://westus.livediagnostics.monitor.azure.com/"


EXPOSE 8080

ENTRYPOINT ["java", "-javaagent:/app/applicationinsights-agent-3.4.15.jar", "-jar", "app.jar"]