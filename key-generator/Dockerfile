FROM openjdk:16-alpine
WORKDIR /home/app
COPY build/libs/*-all.jar /home/app/application.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "/home/app/application.jar"]
