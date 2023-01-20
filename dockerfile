FROM eclipse-temurin:17

COPY build/libs/lightHub-1.0.0-all.jar lightHub.jar

COPY build/libs/worlds .

EXPOSE 25565

ENTRYPOINT [ "java", "-jar", "/lightHub.jar" ]