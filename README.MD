# Linker RESTful service: URL Shortener

## Description

Linker is a minimalistic Spring Boot/JOOQ application for URL shortening.
It uses PostgreSQL 9.X as persistence and Spring Boot's REST stack.

## Web methods
1. GET /{id} - fetch full URL from a short one
2. POST / - add new URL. If URL already exists, the existing one returns

These methods covered by JUnit tests com.seredkin.linker.LinkerWebTests. Use a REST client for manual testing.

## Build and run

1. Specify a connection parameters for the running database in the file config.properties:

```properties

#Database Configuration for JOOQ Codegen
db.driver=org.postgresql.Driver
db.url=jdbc:postgresql:linker
db.username=linker
db.password=linker

```

2. Specify Live(CI) database connection parameters in the file application.yml.
For development and testing purposes it can be the same as in config.properties:

```yaml

linker.prefix: http://dd.xyz
spring:
  datasource:
    driver-class-name: org.postgresql.Driver
    url: jdbc:postgresql:linker
    username: linker
    password: linker

```

linker.prefix parameter specifies the prefix of the shortened link to return.

Run mvn clean install and check out test results:
```
[INFO] Results:
[INFO]
[INFO] Tests run: 4, Failures: 0, Errors: 0, Skipped: 0

.........

[INFO] BUILD SUCCESS
```

To run the application:

java -jar ./target/linker-0.0.1-SNAPSHOT.jar

or

mvn spring-boot:run

Default port number is 8080