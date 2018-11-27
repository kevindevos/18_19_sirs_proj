# SIRS Monitoring and Intrusion Recovery of Network Infrastructures

### Prerequesites:

KerbIST is required, run:

```
git clone https://github.com/tecnico-distsys/kerbist kerbist
cd kerbist
mvn clean install -DskipTests
```
It is possible for kerbist to have some compilation issues related to Tests, just remove the folder in kerbist kerby-lib/src/test

```
rm -rf kerby-lib/src/test
```

If kerby-lib is not installed in .m2 automatically then:
```
mkdir -p ~/.m2/kerby-lib/kerby-lib/1.1.0-SNAPSHOT
cp -R kerbist/kerby-lib/target/kerby-lib-1.1.0-SNAPSHOT.jar ~/.m2/kerby-lib/kerby-lib/1.1.0-SNAPSHOT
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
mvn exec:java
```
To run multiple application servers, for each server, add -Dws.i=<number between 0 and 9>

```
mvn exec:java -Dws.i=4
```

Running spring boot web server:

```
cd source/webinterface
mvn spring-boot:run
```
