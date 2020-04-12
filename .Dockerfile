### Build Java Application image ###
FROM maven:3.6.3-jdk-11 as builder

WORKDIR /tmp/lesson2_app

COPY src/ ./src/
COPY pom.xml .

RUN mvn clean install

### Run Java Application image ###
FROM adoptopenjdk:11-jre-openj9
MAINTAINER  Dmitry Efimov <d.efimov@argustelecom.ru>

WORKDIR /opt/app
COPY --from=builder /tmp/lesson2_app/target/lesson2.jar .
ENTRYPOINT ["java"]
CMD ["-jar", "lesson2.jar"]