# TODO

### Create web service
* listening on localhost tcp port 8000
* has http method GET with path /health/
* response as: {"status": "OK"}

### Wrap application with docker-image 
* build image locally
* push it on Docker hub public repository

### Install & Run
docker pull defimov/otus_lesson2:01
<<<<<<< HEAD
docker run --rm -dit --name lesson2_app -p 8000:8000 defimov/otus_lesson2:01
=======

docker run --rm -dit --name lesson2_app -p 8080:8080 defimov/otus_lesson2:01
>>>>>>> lesson02_dockerbase
