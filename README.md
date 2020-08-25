# TODO

### Add client authentification to application
* Add identification and authorization services to application using apigateway pattern
* APIGateway should implement scenario
  * Register user#1
  * User#1 login
  * Get user#1 profile
  * Nobody (authentificated or not) has read/write access to user#1's profile

### Homework requirements
* A remark with description of service interaction
* Helm install command
  * With namespace if significant  
* Postman tests

### Pre-install
Install and configure [minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)

Install and configure [helm](https://helm.sh/docs/intro/install/)

Add result of ```echo "$(minikube ip) arch.homework"``` to /etc/hosts and restart network

Create directory ```mkdir ./helmchart``` and go to it

Start minikube and set new namespace ```kubectl create namespace auth``` followed by ```kubectl config set-context --namespace auth --current```

Install external helm charts 
* helm repo add stable https://kubernetes-charts.storage.googleapis.com/
* helm install nginx -f ./[nginx-ingress.yaml](https://github.com/DmitryAEfimov/otusSoftwareArchitect/blob/lesson09_auth/helm/nginx-ingress.yaml) stable/nginx-ingress --atomic
 
### Install & Run
Download [deployment directory](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson09_auth/helm) to local host

Execute command: ```cd <deployment directory>``` followed by ```helm install app ./app-chart/``` to run application

Execute command: ```helm uninstall app``` to stop application

### [API Gateway schema](https://github.com/DmitryAEfimov/otusSoftwareArchitect/blob/lesson09_auth/src/main/resources/data/api%20gateway%20interaction%20schema.pdf)
    
#### Run scenario
* Register users
  * Admin user: POST /signup. Expect response code 200
  * SImple user: POST /signup. Expect response code 200
* Test simple user
  * Get user profile with no auth: GET /profiles/{{userId}}.
   
        Expect response code 401
        
        Expect response has a header named "Referer" with value /profiles/{{userId}}
  * Login simple user via login form: POST /signin.
        
        Expect response code 200.
        
        Expect redirecting on user profile form according to header "Referer" (i.e. non empty user profile data in response body).
        
        Expect cookie "SESSION"
  * Update user profile: PUT /profiles/{{userId}}.
  
        Expect response code 200
  * Request admin profile: GET /profile/{{adminId}}.
        
        Expect response code 403
  * Request all users. GET /profile/admin/users.
        
        Expect response code 403
  * Logout simple user. POST /signout.
        
        Expect response code 200
        
        Expect reset cookie "SESSION" value
  * Get user profile after signout. GET /profiles/{{userId}}.
  
        Expect response code 401
* Test admin user
  * Login admin user via login form: POST /signin.
        
        Expect response code 302.
        
        Expect cookie "SESSION" with new value
  * Request all users: GET /profile/admin/users.
        
        Expect response code 200
  * Delete admin user: DELETE /profiles/{{adminId}}.
        
        Expect response code 401.
        
        Expect reset cookie "SESSION" value
  
See [postman tests](https://github.com/DmitryAEfimov/otusSoftwareArchitect/blob/lesson09_auth/src/test/resources/postman) for details
