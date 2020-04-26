### Build Java Application image ###
FROM maven:3.6.3-jdk-11 as builder

WORKDIR /tmp/app

COPY src/ ./src/
COPY pom.xml .

RUN mvn clean install

### Run Java Application image ###
FROM adoptopenjdk:11-jre-openj9
MAINTAINER  Dmitry Efimov <d.efimov@argustelecom.ru>

EXPOSE 8000

WORKDIR /opt/app
COPY --from=builder /tmp/app/target/app.jar .

RUN groupadd -g 999 appuser && \
    useradd -r -u 999 -g appuser appuser && \
    chmod 644 app.jar
USER appuser

CMD java -Dspring.application.json=${SPRING_APPLICATION_JSON} -jar app.jar
