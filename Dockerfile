FROM gradle:7.6-jdk17 AS BUILD_IMAGE

ENV APP_HOME=/root/dev/chessgamebackend/
RUN mkdir -p $APP_HOME/src/main/java
WORKDIR $APP_HOME

COPY ./build.gradle ./gradlew ./gradlew.bat $APP_HOME
COPY gradle $APP_HOME/gradle/
COPY ./src $APP_HOME/src/

RUN ./gradlew clean bootJar

FROM amazoncorretto:17.0.0
WORKDIR /root/
COPY --from=BUILD_IMAGE /root/dev/chessgamebackend/build/libs/*.jar /app/chess-app-1.0.0.jar
EXPOSE 8080
ENTRYPOINT ["java", "-Dspring.profiles.active=container", "-jar","/app/chess-app-1.0.0.jar"]