# TODO

### Add cache to application
* Create application with cache pattern implementation
* Create product service with contract
  * Product search by number of criterias
  * Produce search result as a collection
* Implement configurable cache feature toggling (for example via environment)     

### Homework requirements
* A remark with description of cache pattern implementation
* Helm install command
  * With namespace if significant  
* Postman tests

### Pre-install
Install and configure [minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)

Install and configure [helm](https://helm.sh/docs/intro/install/)

Add result of ```echo "$(minikube ip) arch.homework"``` to /etc/hosts and restart network

Create directory ```mkdir ./helmchart``` and go to it

Start minikube and set new namespace ```kubectl create namespace product``` followed by ```kubectl config set-context --namespace product --current```

Install external helm charts 
* helm repo add stable https://kubernetes-charts.storage.googleapis.com/
* helm install nginx -f ./[nginx-ingress.yaml](https://github.com/DmitryAEfimov/otusSoftwareArchitect/blob/lesson07_prometheus/deployment/nginx-ingress.yaml) stable/nginx-ingress --atomic
 
### Install & Run
Download [deployment directory](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson15_cache/deployment) to local host

Execute command: ```cd <deployment directory>``` followed by ```helm install app ./app-chart/``` to run application

Execute command: ```helm uninstall app``` to stop application  

#### Usage
Set helm variable ```application.cache.enabled=false|true``` to turn off/on caching and reinstall helm chart

Optionally set helm variable ```application.simulateDBDelay=true``` to simulate DB roundtrip lag. It adds 300ms to latency

#### Supported operations
* Create new product
  * POST /products
* Delete product
  * DELETE /products/{vendorCode}. Force cache evict    
* Partial match & case insensitive searh by name and/or description
  * GET /products/search[?(name={name})|desc={desc})]
* Search by vendorCode. Force caching 
  * GET /products/{vendorCode}

See [postman tests](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson15_cache/src/test/resources/postman) for details