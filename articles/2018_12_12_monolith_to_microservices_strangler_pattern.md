# Monolith to Microservices With the Strangler Pattern

source: https://dzone.com/articles/monolith-to-microservices-with-the-strangler-patte

date: 2018-12-12

tags: monolith, microservice, strangler, pattern, legacy, code

- Maintaining a legacy application is cumbersome
    - Lack of unit tests
    - Violation of the Single Responsibility Principle
    - High complexity leads to more time spent in maintenance activities
    - Inability to scale individual components to meet increased demand
    - Tight coupling between components makes it difficult to deploy regularly
    - Technical debt is accumulated over time and makes future development difficult
- Teams working on a monolith can block each other with bad code
- Code complexity for these applications has grown and components were tightly-coupled with each other
- Rewriting is big effort and good amount of risks
- Very good understanding of the entire legacy system necessary
- New features hard to implement in co-existence
- The Strangler Pattern is a popular design pattern to incrementally transform your monolithic application into microservices by replacing a particular functionality with a new service
- Once the new functionality is ready, the old component is strangled, the new service is put into use, and the old component is decommissioned altogether
- Strangler Pattern process
    - Transform
    - Co-Exist
    - Eliminate
- Start with simple component (good test coverage and less technical dept) or with a component which needs to be deployed frequently
- Journey is challenging and needs to be well architected and planned
- Strangling a monolith is not a quick process and may take some time
