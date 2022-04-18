FROM adoptopenjdk:11-jre-hotspot
ARG JAR_FILE="build/libs/cards-0.0.1-SNAPSHOT.jar"
COPY ${JAR_FILE} application.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/application.jar"]