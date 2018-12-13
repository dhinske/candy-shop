# Kubernetes in a nutshell

## Introduction
- Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications
- Written in GO, collection of API-oriented, very fast binaries
- Features: self-healing, horizontal scaling, service discovery, load balancing, automatd rollouts/rollbacks, secret and configuration management, storage orchestration
- Check Clusterstatus: ```kubectl clusterinfo```
- Kubernetes has two typed of nodes
  - Check node status: ```kubectl get nodes```
  - Master nodes
    - Responsible for managing the Kubernetes cluster
    - There can be more than one master node in the cluster.
    - Entry point for all administrative tasks (via CLI (kubectl), GUI (dashboard) or API)
    - Components of a master node:
      - API-Server: Performs administrative tasks; Commands via REST
      - Scheduler: Distributes the work to different worker nodes
      - Controller Manager: Manages different non-terminating control loops, which regulate the state of the Kubernetes cluster
      - etcd: Distributed key-value store which is used to store the cluster state
  - Worker nodes
    - Runs the applications using Pods and is controlled by the master node
    - To access the applications from the external world, we connect to worker nodes and not to the master node/s
    - Components of a worker node:
      - Container runtime: Run and manage a container's lifecycle (Docker is a platform which uses containerd as a container runtime)
      - kubelet: Agent which runs on each worker node and communicates with the master node
      - kube-proxy: Network proxy which runs on each worker node and listens to the API server for each Service endpoint creation/deletion
    - Worker can have labels (key-value pair)
        - ```kubectl label nodes <node-name> <label-key>=<label-value>```
        - Pods can influence with Label Selectors on which nodes they will end up running


## Installation
Provide links...

## Kubernetes Object Model
- Kubernetes has a very rich object model, with which it represents different persistent entities in the Kubernetes cluster.
- Objects are usually described in YAML
- Objects are created via ```kubectl create -f {yaml-file}```
- Pods
  - Smallest and simplest Kubernetes object
  - Represents a single instance of the application
  - Logical collection of one or more containers, which run on the same host, share the same network namespace (and IP) and mount the same external volumes
  ```
      apiVersion: v1    
      kind: Pod             # Type of object
      metadata:
        name: nginx
        labels:             # Optional Label
          env: test
      spec:                 # Container definition: Use image 'nginx'
        containers:
        - name: nginx
          image: nginx
        nodeSelector:       # Optional: Only deploy on node with label disktype=ssd
          disktype: ssd
  ```
- Labels
  - Key-Value pairs that can be attached to any Kubernetes objects
- Label Selectors
  - With Label Selectors, we can select a subset of objects
  - Two types of selectors
   - Equality-Based Selectors: allow filtering of objects based on Label keys and values (=, ==, or !=)
   - Set-Based Selectors: filtering of objects based on a set of values (in, notin, and exist)
- ReplicationController
  - Part of the master node's controller manager
  - Makes sure the specified number of replicas for a Pod is running at any given point in time
  - Always use controllers like ReplicationController to create and manage Pods
- ReplicaSets
  - next-generation ReplicationController with one difference: ReplicaSets support both equality- and set-based selectors, whereas ReplicationControllers only support equality-based Selectors
- Deployments
  - Provide declarative updates to Pods and ReplicaSets
  - DeploymentController is part of the master node's controller manager
  - Deployments have two strategies: Replacement (downtime) and RollingUpdate (no downtime)
- Namespaces
  - If we have numerous users whom we would like to organize into teams/projects, we can partition the Kubernetes cluster into sub-clusters using Namespaces
  - Two default Namespaces: kube-system and default
- DaemonSet
  -  Ensures that all (or some) Nodes run a copy of a Pod.
  ```
    apiVersion: apps/v1
    kind: DaemonSet
    metadata:
      ...
    spec:
      selector:
        matchLabels:
          name: fluentd-elasticsearch
      template:
        metadata:
          labels:
            name: fluentd-elasticsearch
        spec:
          tolerations:                              # Dont run on master nodes
          - key: node-role.kubernetes.io/master
            effect: NoSchedule
          containers:
          - name: fluentd-elasticsearch
            image: k8s.gcr.io/fluentd-elasticsearch:1.20
          terminationGracePeriodSeconds: 30
  ```

## Authentication
- Every access on the API-Server goes through three stages
  - Authentication
    - Two kind of user: normal users (managed outside) and service accounts (usually created automatically)
    - Different modules for Authentication, like Client Certificates, Token, Password File and more
  - Authorization
    - Similar to the Authentication step, Authorization has multiple modules/authorizers, checked in sequence
  - Admission Control
    - Used to specify granular access control policies, which include allowing privileged containers, checking on resource quota, etc
    - To use admission controls, we must start the Kubernetes API server with the admission-control, which takes a comma-delimited, ordered list of controller names, like admission-control=NamespaceLifecycle,ResourceQuota,PodSecurityPolicy,DefaultStorageClass.
    - Kubernetes comes with some built-in admission controllers

## Services
- Service: Higher-level abstraction, which logically groups Pods and a policy to access them
- Grouping is achieved via Labels and Selectors
- Example of a service, which forwards all traffic incoming on port 80 to the Pods that have the Label 'app' set to 'frontend'
```
kind: Service
apiVersion: v1
metadata:
  name: frontend-svc
spec:
  selector:
    app: frontend
  ports:
    - protocol: TCP
      port: 80
      targetPort: 5000
```
- Each Service gets its own IP-Adress
- kube-proxy
  - All of the worker nodes run a daemon called kube-proxy, which watches the API server on the master node for the addition and removal of Services and endpoints.
  - For each new Service, on each node, kube-proxy configures the iptables rules to capture the traffic for its ClusterIP and forwards it to one of the endpoints. When the service is removed, kube-proxy removes the iptables rules on all nodes as well.
- Service discovery
  - Kubernetes has two ways to discover Services
    - Environment variables: As soon as the Pod starts on any worker node, the kubelet daemon running on that node adds a set of environment variables in the Pod for all active Services
    - DNS: Kubernetes has an add-on for DNS, which creates a DNS record for each Service and its format is like my-svc.my-namespace.svc.cluster.local. Services within the same Namespace can reach to other Services with just their name
- ServiceType
  - Defines access scope of a Service
  - ClusterIP: default, Virtual IP address using the ClusterIP
  - NodePort: In addition to creating a ClusterIP, a port from the range 30000-32767 is mapped to the respective Service, from all the worker nodes. The NodePort ServiceType is useful when we want to make our Services accessible from the external world. The end-user connects to the worker nodes on the specified port, which forwards the traffic to the applications running inside the cluster. To access the application from the external world, administrators can configure a reverse proxy outside the Kubernetes cluster and map the specific endpoint to the respective port on the worker nodes.
  - LoadBalancer:
    - NodePort and ClusterIP Services are automatically created, and the external load balancer will route to them
    - The Services are exposed at a static port on each worker node
    - The Service is exposed externally using the underlying cloud provider's load balancer feature.
  - ExternalIP: A Service can be mapped to an ExternalIP address if it can route to one or more of the worker nodes. Traffic that is ingressed into the cluster with the ExternalIP (as destination IP) on the Service port, gets routed to one of the the Service endpoints. ExternalIPs are not managed by Kubernetes. The cluster administrators has configured the routing to map the ExternalIP address to one of the nodes.
  - ExternalName: Has no Selectors and does not define any endpoints. When accessed within the cluster, it returns a CNAME record of an externally configured Service. The primary use case of this ServiceType is to make externally configured Services like my-database.example.com available inside the cluster, using just the name, like my-database, to other Services inside the same Namespace.

## Volumes
- Directory backed by a storage medium. The storage medium and its content are determined by the Volume Type
- A Volume is attached to a Pod and shared among the containers of that Pod
- The Volume has the same life span as the Pod, and it outlives the containers of the Pod - this allows data to be preserved across container restarts
- VolumeTypes
  - emptyDir: An empty Volume is created for the Pod as soon as it is scheduled on the worker node. The Volume's life is tightly coupled with the Pod. If the Pod dies, the content of emptyDir is deleted forever.
  - hostPath: With the hostPath Volume Type, we can share a directory from the host to the Pod. If the Pod dies, the content of the Volume is still available on the host.
  - gcePersistentDisk: Mount a Google Compute Engine (GCE) persistent disk into a Pod
  - awsElasticBlockStore: Mount an AWS EBS Volume into a Pod
  - nfs: Mount an NFS share into a Pod
  - iscsi: Mount iSCSI share into a Pod
  - secret: Pass sensitive information to Pods
  - persistentVolumeClaim:
    - Attach a PersistentVolume to a Pod
    - APIs for users and administrators to manage and consume storage
    - A PersistentVolumeClaim (PVC) is a request for storage by a user. Users request for PersistentVolume resources based on size, access modes, etc. Once a suitable PersistentVolume is found, it is bound to a PersistentVolumeClaim.
    - Once a user finishes its work, the attached PersistentVolumes can be released. The underlying PersistentVolumes can then be reclaimed and recycled for future usage.

## Example: Deploying Applications
