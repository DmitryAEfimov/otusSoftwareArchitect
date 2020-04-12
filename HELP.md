# TODO

### Create web service
* listening on localhost tcp port 8080
* has http method GET with path /health/
* response as: {"status": "OK"}

### Wrap application with docker-image 
* build image locally
* push it on Docker hub public repository

### Install & Run
docker pull defimov/otus_lesson2:01

docker run --rm -dit --name lesson2_app -p 8080:8080 defimov/otus_lesson2:01
