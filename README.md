# Execution Lock Spring Boot Starter

Execution Lock makes sure that your method are executed at most once at the same time. 
If a task is being executed on one node, it acquires a lock which prevents execution of the same task from another node (or thread). 
Please note, that if one task is already being executed on one node, execution on other nodes does not wait, it is simply skipped.

Execution Lock uses an external store like Mongo, JDBC database, Redis, Hazelcast, ZooKeeper or others for coordination.

Feedback and pull-requests welcome!

## Technology Stack

* Java 1.8
    * Streams
    * Lambdas
* Third Party Libraries
    * Commons-Lang3 (Apache License)
    * Jackson XML Databind (Apache License)
    * Junit (EPL 1.0 License)
* Code-Analyses
    * Sonar
    * Jacoco

## License

MIT (unless noted otherwise)

## Test Coverage

95 %
