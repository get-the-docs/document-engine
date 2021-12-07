FROM adoptopenjdk/openjdk16 as build
WORKDIR /workspace/app

COPY mvnw .
COPY .mvn .mvn
COPY . .

RUN ./mvnw clean install -DskipTests -Prelease
RUN mkdir -p /workspace/app/target/dependency
WORKDIR /workspace/app/target/dependency
RUN jar -xf /workspace/app/template-utils-service-api/target/app.jar


FROM adoptopenjdk/openjdk16:alpine-jre

COPY --from=build /workspace/app/template-utils-service-api/target/app.jar /app/app.jar  

VOLUME /app/config
VOLUME /data/repositories/source/templates
VOLUME /data/repositories/source/documentstructures
VOLUME /data/repositories/target/generated-documents

ENTRYPOINT ["java","-Dspring.profiles.active=${SPRING_PROFILE:prod}", "-Dconfig=\"/app/config\"", "-Dloader.path=\"/app/config\"","--add-opens","java.base/java.lang=ALL-UNNAMED","--add-opens", "java.base/java.lang.reflect=ALL-UNNAMED","--add-opens", "java.base/java.io=ALL-UNNAMED","--add-opens", "java.base/java.time=ALL-UNNAMED","--add-exports", "java.base/sun.nio.ch=ALL-UNNAMED","--add-exports", "jdk.unsupported/sun.misc=ALL-UNNAMED", "-cp","/app/app.jar:/app/config","org.springframework.boot.loader.PropertiesLauncher"]