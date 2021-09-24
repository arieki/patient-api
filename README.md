# Instruction

## Prerequisites

To be able to run the application make sure you have java 11 and postgreSQL installed in your machine. 
Here is the default database connection details:

```
  database-name : postgres
  username: postgres
  password: password
```
***Notes*** : Database migration handled automatically by Flyway

## Run the API

Make sure you are inside `parent-api` directory and run the following command:
```
  ./gradlew clean bootRun
```

once the API is running, you may test to invoke each of the enpoint through any convenience HTTP client test tools e.g.: Postman
Here is the API documentation:

```
http://localhost:8080/swagger-ui/index.html?configUrl=/v3/api-docs/swagger-config
```