FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
VOLUME /tmp

COPY target/app.jar .

ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILE:prod}", "-Dconfig=\"./config\"", "-Dloader.path=\"config\"", "-cp","app:app/lib/*:./config","org.springframework.boot.loader.PropertiesLauncher"]
