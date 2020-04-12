### Build Java Application image ###
FROM maven:3.6.3-jdk-11 as builder

WORKDIR /tmp/lesson2_app

COPY src/ ./src/
COPY pom.xml .

RUN mvn clean install

### Run Java Application image ###
FROM adoptopenjdk:11-jre-openj9
MAINTAINER  Dmitry Efimov <d.efimov@argustelecom.ru>

EXPOSE 8000

WORKDIR /opt/app
COPY --from=builder /tmp/lesson2_app/target/lesson2.jar .

RUN groupadd -g 999 appuser && \
    useradd -r -u 999 -g appuser appuser && \
    chmod 644 lesson2.jar
USER appuser

ENTRYPOINT ["java"]
CMD ["-jar", "-Dserver.port=8000", "lesson2.jar"]