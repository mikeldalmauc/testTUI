
# PILOTES microservice

TUI DX Backend technical Test v2

# Table of Contents
1. [Description](##Description)
2. [Documentation](###Documentation)
3. [Requirements](###Requirements)
4. [Setup](###Setup)
5. [Testing](###Testing)
6. [Configuration](###Configuration)
7. [Road Map](###RoadMap)
8. [Discussion](###Discussion)
9. [Owner](###Owner)


## Description
Create an Api rest that allows us to order the "pilotes" of the great Miquel Montoro. “Pilotes” are a Majorcan recipe that consisting of a meatball stew.

### Documentation

#### API

### Requirements

An outline of what other services, tooling, and libraries needed to make a service operate, providing a complete list of EVERYTHING required to work properly.

#### Lombok
The base project uses lombok, so you have to install it. You can use the following guide https://www.baeldung.com/lombok-ide

### Java Frameworks and Libraries

Initial Data can be inspected though h2 console

### Setup
A step by step outline from start to finish of what is needed to setup and operate a service, providing as much detail as you possibly for any new user to be able to get up and running with a service.

spring.kafka.bootstrap-servers=172.30.128.1:9092

### Testing

Testing - Providing details and instructions for mocking, monitoring, and testing a service, including any services or tools used, as well as links or reports that are part of active testing for a service.

http://localhost:8080/h2-console

https://www.sonarqube.org/features/deployment/


### Configuration
 An outline of all configuration and environmental variables that can be adjusted or customized as part of service operations, including as much detail on default values, or options that would produce different known results for a service.

### Road Map 
An outline broken into three groups, 1) planned additions, 2) current issues, 3) changelog. Providing a simple, descriptive outline of the roadmap for a service with links to any GitHub issues that support what the plan is for a service.

### Discussion
 A list of relevant discussions regarding a service with title, details, and any links to relevant GitHub issues, blog posts, or other updates that tell a story behind the work that has gone into a service.

### Owner
 The name, title, email, phone, or other relevant contact information for the owner, or owners of a service providing anyone with the information they need to reach out to the person who is responsible for a service

### SonarQube

Sonar lint , connect to
    "sonarlint.connectedMode.connections.sonarqube": [
          {
            "serverUrl": "https://localhost:9000",
            "token": ""
          }
    ]
`docker pull sonarqube:9.0.1-developer`

### Docker 

The dockerfile configuration is adapted from the one shown on this article:
https://snyk.io/blog/best-practices-to-build-java-containers-with-docker/

`docker build -t prueba-tui .`
`docker run -p 9000:9000 prueba-tui --server.port=9000`