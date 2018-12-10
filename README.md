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
cd source/app-ws-cli
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
