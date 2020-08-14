FROM python:3.8.5-alpine

ENV VIRTUAL_ENV "/venv"
RUN python -m venv $VIRTUAL_ENV
ENV PATH "$VIRTUAL_ENV/bin:$PATH"

RUN apk add build-base

WORKDIR /opt/data-generator
COPY ./requirements.txt .
RUN python -m pip install --upgrade pip -r requirements.txt

COPY ./gen-app.py .
COPY ./generator.py .
EXPOSE 8000

ENTRYPOINT ["python", "/opt/data-generator/gen-app.py"]