# TODO

### Create manifests to deploy application to k8s (minikube)
* create yaml files to provide automatic deployment
* deploy docker image from public repository in Dockerhub
* Base URL is http://arch.homework/otusapp/

### Manifest requirements 
* docker image to deploy defimov/otus_lesson2:v1
* yaml files contains sections for Deployment, Service & Ingress entities 
* yaml files contains sections for Liveness & Readiness probes
* at least 2 replicas should be deployed
* write Ingress rule to forward requests from base URL to service

### Install & Run
Clone or download yaml files to single directory on your local host

execute command: ``` kubectl apply -f ./deployment.yaml -f ./service.yaml -f ./ingress.yaml ```  

check service with GET request ``` curl http://arch.homework/otusapp/health ```