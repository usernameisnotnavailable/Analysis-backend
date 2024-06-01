FROM openjdk:17

WORKDIR /app

COPY /src/main/java .
COPY /plaindata .

EXPOSE 8080

COPY target/analysis.jar analysis.jar

CMD ["java", "-jar", "analysis.jar"]