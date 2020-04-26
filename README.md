# TODO

### Create RESTful CRUD application
* Model entity: User
* Persistence layer: database

### Deployment requirements
* Use StatefulSet in kubernetes manifest
* Use official database docker image
* Single DB replica
* App configuration is stored in ConfigMap
* DB password is stored in Secrets
* DB initialization via separate Pod or Job 
* Ingress rule to forward requests from base URL (http://arch.homework/otusapp/) to service
#### Optional
* Describe deployment with helm chart 
* Use official DB helm chart as dependency


#### Pre-install
Install, configure & run [minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)

Install and configure [helm](https://helm.sh/docs/intro/install/)

### Install & Run
Download [app-chart](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson05_minikubeadv/deployment) to local host

Execute command: ``` cd <workdir> ``` followed by ``` helm install hw-lesson5 ./app-chart/ ``` to run application

Execute command: ``` helm uninstall hw-lesson5 ``` to stop application  

### Usage
Before your start you have to resolve host arch.homework. It can be done with
* add result of ``` echo "$(minikube ip) arch.homework" ``` to /etc/hosts and restart network
* change postman collection. Replace ``` arch.homework ``` with result of ``` $(minikube ip) ``` in all url's. Also you have to add header ``` Host: arch.homework ```

Application consume and produce only JSON format

Example value ```{"id": "2122", "userName": "johndoe589", "firstName": "John", "lastName": "Doe", "email": "bestjohn@doe.com", "phoneNumber": "+71002003040"}```

* Create new user
  * POST /users
* Update existing user
  * PUT /users?id=<id>
* Delete existing user
  * DELETE /users?id=<id>    
* Get user info
  * GET /users?id=<id>
* Get all users info
  * GET /users
  
#### Examples
See [postman test](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson05_minikubeadv/src/test/resources/postman)