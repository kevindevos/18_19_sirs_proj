# SIRS Monitoring and Intrusion Recovery of Network Infrastructures

### Installation:

Install the application:

```
https://github.com/kevindevos/sirs_monitoring_and_intrusion_recovery_distributed.git source
cd source
mvn clean install -DskipTests
```

### Running 

First run the kerby web server:

```
cd source/kerby-ws
mvn exec:java
```

Run one or more application servers:

```
cd source/app-ws
mvn exec:java
```
To run multiple application servers, for each server, add -Dws.i=<number>

```
mvn exec:java -Dws.i=4
```

Running spring boot web server:

```
cd source/webinterface
mvn spring-boot:run
```

The web interface should now be available by accessing localhost:8184 providing a login and a register page. After login a dashboard page is displayed allowing the user to add, read and write text notes.

### Monitoring and Recovery

A monitoring and recovery server is used to detect intrusion, provide recovery of compromised and missing data and issue regeneration of communication encryption keys.
To start the monitoring server run:

```
cd source/monitor
mvn exec:java
```
