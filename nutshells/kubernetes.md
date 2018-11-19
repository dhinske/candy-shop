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
