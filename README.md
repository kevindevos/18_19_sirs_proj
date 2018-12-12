# SIRS Monitoring and Intrusion Recovery of Network Infrastructures

### 1. Installation:

Install the application:

```
https://github.com/kevindevos/sirs_monitoring_and_intrusion_recovery_distributed.git source
cd source
mvn clean install -DskipTests
```

### 2. Running 

#### First run the kerby web server:

```
cd source/kerby-ws
mvn exec:java
```

####  Run one or more application servers:

```
cd source/app-ws
mvn exec:java
```
To run multiple application servers, for each server, add -Dws.i=<number>

```
mvn exec:java -Dws.i=4
```
In this example, a application server will run on port 8084. Note that for the webinterface to be able to communicate with these servers, their respective ports must be added before installation. This is done by modifying in the AppClientConnectionManager.java class's constructor, the folowing:
```
defaultPorts = new ArrayList<>(Arrays.asList("8081", "8084"));
```
In this case, the webinterface will comunicate with application servers at ports 8081 and 8084, which need to be running.

#### Running spring boot web server:

```
cd source/webinterface
mvn spring-boot:run
```

The web interface should now be available by accessing localhost:8184 providing a login and a register page. After login a dashboard page is displayed allowing the user to add, read and write text notes.

### 3. Monitoring and Recovery

A monitoring and recovery server is used to detect intrusion, provide recovery of compromised and missing data and issue regeneration of communication encryption keys.
To start the monitoring server run:

```
cd source/monitor
mvn exec:java
```
