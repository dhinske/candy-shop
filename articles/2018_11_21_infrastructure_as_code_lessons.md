# 5 Lessons Learned From Writing Over 300,000 Lines of Infrastructure Code

source: https://blog.gruntwork.io/5-lessons-learned-from-writing-over-300-000-lines-of-infrastructure-code-36ba7fadeac1

date: 2018-11-21

tags: infrastructure as code

## Lesson 1: The Production-Grade Infrastructure Checklist
- Building production-grade infrastructure is hard. And stressful. And time consuming. Very time consuming.
- Building infrastructure involves a thousand little details. The vast majority of developers don’t know what those details are, so when you’re estimating a project, you usually forget about number of critical—and time consuming—details:
  - Install (software binaries and dependencies)
    - Bash, Chef, Ansible, Salt
  - Configure (the software at runtime)
    - Bash, Chef, Ansible, Salt
  - Provision (the infrastructure)
    - Terraform, Cloudformation
  - Deploy (the service on top of the infrastructure)
    - Scripts, Orchestration tools
  - Security (Encryption in transit (TLS), authentication, authorization, secret management)
    - EBS Volumes, Cognito, Vault, CiS
  - Monitoring ([Availability-business-app-server-event]-metrics, tracing, alert)
    - Cloudwatch, DataDog, New Relic, Honeycomb
  - Logs (Rotation, Aggregate)
    - Cloudwatch Logs, ELK, Sumo Logic, Papertrail
  - Backup and restore (on a regular basis)
    - RDS, ElastiCache, ec2-snapper, Lambda
  - Networking (VPC's, subnets, static/dynamic IP's, DNS, SSH/VPN access)
    - EIP's, ENI's, VPC, NACL, Route 53, OpenVPN
  - High Availability
    - Multi AZ, multi-region, replication, ELB's, ASG's
  - Scalability (horizontally (more servers) or vertically (bigger servers))
    - ASG's, replication, sharding, caching, divide and conquer
  - Performance (Optimization of CPU, memory, disk, network; benchmark, load testing, profiling)
    - Dynatrace, VisualVM, Jmeter, ab
  - Cost Optimization (Pick proper instance types)
  - Documentation
    - README, wiki, slack
  - Tests
    - Terratest
  - Maintenance (Update software/tools, bigfixes, new features)

## Lesson 2: the toolset
In particular, the best tools in the world will not help your team one bit if your team isn’t bought into using those tools or if your team doesn’t have enough time to learn use those tools. Therefore, the key takeaway is that using infrastructure as code is an investment: there’s an up-front cost to get going, but if you invest wisely, you’ll earn big dividends over the long-term.

## Lesson 3: large modules considered harmful
Infrastructure as code newbies often define all of their infrastructure for all of their environments (dev, stage, prod, etc) in a single file or single set of files that are deployed as a unit.
This is a bad idea because:
- Slow
- Insecure
- Risky
- Hard to understand
- Hard to read
- Hard to review

## Lesson 4: infrastructure code without automated tests is broken
To properly test your infrastructure code, you typically have to deploy it to a real environment, run real infrastructure, validate that it does what it should.
- Unit-Tests: deploy and test one or a small number of closely related modules from one type of infrastructure (e.g., test the modules for a single database)
- Integration tests: Deploy and test multiple modules from different types of infrastructure to validate they work together correctly (e.g., test the modules of a database with the modules from a web service)
- End-to-end tests: Deploy and test your entire architecture

Cycle time with infrastructure tests is slow, especially as you go up the pyramid:
1. Build small, simple, standalone modules (remember Lesson 3?) and write lots of unit tests for them to build your confidence that they work properly.
2. Combine these small, simple, battle-tested building blocks to create more complicated infrastructure that you test with a smaller number of integration and e2e tests tests.

## Lesson 5: the release process
- Do lesson 1-4 and make code reviews
