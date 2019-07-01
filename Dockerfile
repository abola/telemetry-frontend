FROM openjdk:8-jdk-alpine
VOLUME /tmp
COPY elastic-apm-agent-1.7.0.jar elastic-apm-agent-1.7.0.jar 
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-javaagent:/elastic-apm-agent-1.7.0.jar", "-Delastic.apm.service_name=telemetry-frontend","-Delastic.apm.server_url=http://apm-server.efk:8200","-Delastic.apm.application_packages=com.systex","-jar","/app.jar"]
