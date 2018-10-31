# SIRS Monitoring and Intrusion Recovery of Network Infrastructures

### Prerequesites:

KerbIST is required, run:

```
git clone https://github.com/tecnico-distsys/kerbist kerbist
cd kerbist
mvn clean install -DskipTests
```

Install the application:


```
https://github.com/kevindevos/sirs_monitoring_and_intrusion_recovery_distributed.git source
cd source
mvn clean install -DskipTests
```

### Running

Running an application server:

```
cd source/app-ws-cli
mvn exec:java -Dws.i=1
```

Running spring boot web server:

```
cd source/webinterface
mvn spring-boot:run
```
