# TODO

### Enrich RESTful CRUD application (see [lesson 5](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson05_minikubeadv)) with metrics
* Add prometheus metrics to application
* Create Grafana application dashboard with metrics
  * 0.5, 0.9, 0.99 percentile latency
  * RPS
  * 5xx errors count
* Configure alerting rules in Grafana   

### Homework requirements
* Use external prometheus-operator and nginx-controller helm charts
* Deploy application with custom helm chart
  * Servicemonitor config file is part of helm chart
  * Dashboard config file is part of helm chart
* Create a standalone job with stress test
  * Should produce rps rate 5-20
  * Should be infinity
  * Should request all application's API
  * Should apply service name via ENV or helm value
  * Should request service via ingress-controller    
* Dashboard snapshots with at least 5-10 minutes stress test activity 
#### Optional
* Configure database prometheus exporter. Add db metrics to dashboard
* Add kubernetes system metrics to dashboard
  * CPU usage
  * Memory usage

### Pre-install
Install and configure [minikube](https://kubernetes.io/docs/tasks/tools/install-minikube/)

Install and configure [helm](https://helm.sh/docs/intro/install/)

Add result of ```echo "$(minikube ip) arch.homework"``` to /etc/hosts and restart network

Create directory ```mkdir ./helmchart``` and go to it

Start minikube and set new namespace ```kubectl create namespace monitoring``` followed by ```kubectl config set-context --namespace monitoring --current```

Install external helm charts 
* helm repo add bitnami https://charts.bitnami.com/bitnami
* helm install prom -f ./[prometheus.yaml](https://github.com/DmitryAEfimov/otusSoftwareArchitect/blob/lesson07_prometheus/deployment/prometheus.yaml) stable/prometheus-operator --atomic

* helm repo add stable https://kubernetes-charts.storage.googleapis.com/
* helm install nginx -f ./[nginx-ingress.yaml](https://github.com/DmitryAEfimov/otusSoftwareArchitect/blob/lesson07_prometheus/deployment/nginx-ingress.yaml) stable/nginx-ingress --atomic
 
### Install & Run
Download [deployment directory](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson07_prometheus/deployment) to local host

Execute command: ```cd <deployment directory>``` followed by ```helm install app ./app-chart/``` to run application

Execute command: ```helm uninstall app``` to stop application  

### Stress test

Download [stresstest directory](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson07_prometheus/stresstest) to local host

Execute command: ```cd <stresstest directory>``` followed by ```helm install stress ./stresstest-chart/``` to start test

Execute command: ```helm uninstall stress``` to stop test

#### Results
See monitoring results in [screenshots](https://github.com/DmitryAEfimov/otusSoftwareArchitect/tree/lesson07_prometheus/stresstest/result)