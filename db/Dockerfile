### Run migration scripts ###
FROM openjdk:8-jre-alpine

MAINTAINER  Dmitry Efimov <d.efimov@argustelecom.ru>

RUN apk --no-cache add --update bash openssl && \
    adduser -S -h /flyway -D flyway
WORKDIR /flyway

# Change to the flyway user
USER flyway

ENV FLYWAY_VERSION 6.4.0

RUN wget https://repo1.maven.org/maven2/org/flywaydb/flyway-commandline/${FLYWAY_VERSION}/flyway-commandline-${FLYWAY_VERSION}.tar.gz \
  && tar -xzf flyway-commandline-${FLYWAY_VERSION}.tar.gz \
  && mv flyway-${FLYWAY_VERSION}/* . \
  && rm flyway-commandline-${FLYWAY_VERSION}.tar.gz

COPY config/flyway.conf config/flyway.conf
COPY ../helm/app-chart/migration sql/

CMD /flyway/flyway -url=${DB_URL} -user=${DB_USER} -password=${DB_PASSWORD} migrate