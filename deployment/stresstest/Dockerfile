FROM python:3.8.3-alpine3.12
MAINTAINER  Dmitry Efimov <d.efimov@argustelecom.ru>

RUN apk --no-cache add --virtual=.build-dep build-base && \
    apk --no-cache add zeromq-dev libffi-dev && \
    apk update && pip install --no-cache-dir locust && \
    apk del .build-dep

WORKDIR /locust
RUN addgroup locust && adduser --disabled-password locust -G locust
USER locust

CMD locust -f ${LOCUST_FILE:-locustfile.py} --host ${ATTACKED_HOST} --users ${USERS} --hatch-rate ${HATCH_RATE} --run-time ${DURATION} --headless --only-summary