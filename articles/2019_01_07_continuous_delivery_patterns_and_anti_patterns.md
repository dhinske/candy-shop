# Continuous Delivery Patterns and Anti-Patterns

source: https://www.mailing.dzone.com/archive/dzone/New-Refcard-Continuous-Delivery-Patterns-and-Anti-Patterns-1815.html

date: 2018-01-07

tags: continuous delivery, patterns, pipeline

- Continuous Delivery =  Deliver new features faster by automation during the entire process: Commit, Build, Test, Deployment and Release
- Implementation of CD through pipelines
    - Visibility
    - (Fast) Feedback
    - Continually Deploy possible
- Therefore
    - Teams get empowered
    - Errors get reduced
    - Deployment Flexibility
    - More safety

## Patterns
For Pattern-names and their antipatterns look at the source.
### Configuration Management
- Evaluate and use third-party software that can be easily configured, deployed, and automated
- Maintain a catalog of all options for each application, how to change these options and storage locations for each application. Automatically create this catalog aspart of the build process
- Minimize merging and keep the number of active code lines manageable by developing on a mainline
- Changes committed to the mainline are applied to each branch on at least a daily basis.
- Store configuration information in secure remotely accessible locations such as a database, directory, or registry
- All source files — executable code, configuration, host environment, and data — are committed to a version control repository
- Branches must be short lived – ideally less than a few days and never more than an iteration.
- Check out the project’s version-control repository and run a single command to build and deploy the application to any accessible environment, including the local development
- Configuration management of the entire system - source, configuration, environment, and data. Any change can be tied back to a single revision in the version-control system

### Continuous Integration
- Fail a build when a project rule is violated – such as architectural breaches, slow tests, and coding standard violations
- Each team member checks in regularly to trunk — at least once a day but preferably after each task to trigger the CI system
- Send automated feedback from CI system to all crossfunctional team members
- Building and testing software with every change committed to a project’s version control repository
- Fix software delivery errors as soon as they occur; stop the line. No one checks in on a broken build, as the fix becomes the highest priority
- Write build scripts that are decoupled from IDEs. These build scripts are executed by a CI system so that software is built at every change
- Provide large visible displays that aggregate information from your delivery system to provide high quality feedback to the Cross-Functional Team in real time

### Testing
- Automate the verification and validation of software to include unit, component, capacity, functional, and deployment tests
- Use transactions for database-dependent tests (e.g. component tests) and roll back the transaction when done. Use a small subset of data to effectively test behavior.
- Run multiple tests in parallel across hardware instances to decrease the time in running tests
- Use stubs to simulate external systems to reduce deployment complexity

### Deployment Pipeline
- A deployment pipeline is an automated implementation of your application’s build, test, deploy, and release process
- Create a map illustrating the process from check in to the version control system to the software release to identify process bottlenecks

### Build and deployment scripting
- Centralize all dependent libraries to reduce bloat, class path problems, and repetition of the same dependent libraries and transitive dependencies from project to project
- As a team, agree upon a common scripting language — such as Perl, Ruby, or Python — so that any team member can apply changes to the Single Delivery System
- Changes between environments are captured as configuration information. All variable values are externalized from the application configuration into build/deployment-time properties
- Fail the build as soon as possible. Design scripts so that processes that usually fail run first. These processes should be run as part of the commit stage.
- The commit build provides feedback on common build problems as quickly as possible — usually in under 10 minutes. - All deployment processes can be written in a script,
checked in to the version-control system, and run as part of the single delivery system.
- The same deployment script is used for each deployment. The protected configuration – per environment — is variable but managed.

### Deploying and releasing applications
- Build your binaries once, while deploying the binaries to multiple target environments, as necessary
- Release software to production for a small subset of users (e.g. 10%) to get feedback prior to a complete rollout
- Deploy software to a non-production environment (call it blue) while production continues to run. Once it’s deployed and “warmed up,” switch production (green) to non-production and blue to green simultaneously
- Launch a new application or features when it affects the least number of users.
- Provide an automated single command rollback of changes after an unsuccessful deployment.
- Any Cross-Functional Team member selects the version and environment to deploy the latest working software
- Automate the process of configuring your environment to include networks, external services, and infrastructure
- Automate tests to verify the behavior of the infrastructure. Continually run these tests to provide near real-time alerting
- Deploy software one instance at a time while conducting behavior-driven monitoring. If an error is detected during the incremental deployment, a rollback release is initiated to revert changes
- Lock down shared environments from unauthorized external and internal usage, including operations staff. All changes are versioned and applied through automation
- Target environments are as similar to production as possible
- Utilizing the Automate Provisioning, Scripted Deployment, and Scripted Database patterns. Any environment should be capable of terminating and launching at will
- Create a lightweight version of your database – using the Isolate Test Data pattern. Each developer uses this lightweight DML to populate his local database sandboxes to expedite test execution
- Ensure your application is backward and forward compatible with your database so you can deploy each independently
- Use scripts to apply incremental changes in each target environment to a database schema and data
- Script all database actions as part of the build process
- Instead of using version-control branches, create an abstraction layer that handles both an old and new implementation. Remove the old implementation
- Deploy new features or services to production but
limit access dynamically for testing purposes.

### Collaboration
- For each iteration, hold a retrospective meeting where everybody on the Cross-Functional Team discusses how to improve the delivery process for the next iteration
- Everybody is responsible for the delivery process. Any person on the Cross-Functional Team can modify any part of the delivery system
- Learn the root cause of a delivery problem by asking “why” of each answer and symptom until discovering the root cause.
