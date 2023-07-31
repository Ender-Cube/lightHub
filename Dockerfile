FROM amazoncorretto:17-alpine-jdk
COPY /build/libs/lightHub-?.?.?.jar app.jar
EXPOSE 25565/tcp
CMD ["java","-jar","/app.jar"]