FROM openjdk:11
EXPOSE 8091
COPY target/temperature-service-0.0.1.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]