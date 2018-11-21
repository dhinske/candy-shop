# Kubernetes in a nutshell

## Introduction
- Kubernetes is an open-source system for automating deployment, scaling, and management of containerized applications
- Features: self-healing, horizontal scaling, service discovery, load balancing, automatd rollouts/rollbacks, secret and configuration management, storage orchestration
- Kubernetes has two typed of nodes
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


## Installation
Provide links...

## Kubernetes Object Model
- Kubernetes has a very rich object model, with which it represents different persistent entities in the Kubernetes cluster.
- Pods
  - Smallest and simplest Kubernetes object
  - Represents a single instance of the application
  - Logical collection of one or more containers, which run on the same host, share the same network namespace and mount the same external volumes
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
  - Deployments provide features like Deployment recording, with which, if something goes wrong, we can rollback to a previously known state
- Namespaces
  - If we have numerous users whom we would like to organize into teams/projects, we can partition the Kubernetes cluster into sub-clusters using Namespaces
  - Two default Namespaces: kube-system and default

## Authentication
- Every access on the API-Server goes through three stages
  - Authentication
    - Two kind of user: normal users (managed outside) and service accounts (usually created automatically)
    - Different modules for Authentication, like Client Certificates, Token, Password File and more
  - Authorization
    - Similar to the Authentication step, Authorization has multiple modules/authorizers, checked in sequence
  - Admission Control
    - Used to specify granular access control policies, which include allowing privileged containers, checking on resource quota, etc
