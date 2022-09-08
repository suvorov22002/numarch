FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar
COPY target/bootstrap.properties bootstrap.properties
ADD . /uploads/
ADD . /uploadsT/
ADD . /uploadsR/
ADD . /uploadsE/
ENTRYPOINT ["java","-jar","-Xmx1024m","/app.jar"]