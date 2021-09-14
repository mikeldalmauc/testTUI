<!--
*** Thanks for checking out the Best-README-Template. If you have a suggestion
*** that would make this better, please fork the repo and create a pull request
*** or simply open an issue with the tag "enhancement".
*** Thanks again! Now go create something AMAZING! :D
***
***
***
*** To avoid retyping too much info. Do a search and replace for the following:
*** github_username, repo_name, twitter_handle, email, project_title, project_description
-->



<!-- PROJECT SHIELDS -->
<!--
*** I'm using markdown "reference style" links for readability.
*** Reference links are enclosed in brackets [ ] instead of parentheses ( ).
*** See the bottom of this document for the declaration of the reference variables
*** for contributors-url, forks-url, etc. This is an optional, concise syntax you may use.
*** https://www.markdownguide.org/basic-syntax/#reference-style-links
-->
[![Contributors][contributors-shield]][contributors-url]
[![Issues][issues-shield]][issues-url]
[![MIT License][license-shield]][license-url]
[![LinkedIn][linkedin-shield]][linkedin-url]



<!-- PROJECT LOGO -->
<br />
<p align="center">
  <h3 align="center">PILOTES microservice</h3>

  <p align="center">
    TUI DX Backend technical Test v2
    <br />
    <a href="https://gitlab.com/mikeldalmauc/testTUI"><strong>Explore the docs »</strong></a>
    <br />
    <br />
    <a href="https://gitlab.com/mikeldalmauc/testTUI">View Demo</a>
    ·
    <a href="https://gitlab.com/mikeldalmauc/testTUI">Report Bug</a>
    ·
    <a href="https://gitlab.com/mikeldalmauc/testTUI">Request Feature</a>
  </p>
</p>



<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary><h2 style="display: inline-block">Table of Contents</h2></summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
        <ul>
        <li><a href="#Docker">Docker</a></li>
        <li><a href="#Other Configuration">Other Configuration</a></li>
        </ul>
      </ul>
    </li>
    <li><a href="#API">API</a></li>
    <li><a href="#roadmap">Roadmap</a></li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#discussion">Discussion</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
    <li><a href="#acknowledgements">Acknowledgements</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

Create an Api rest that allows us to order the "pilotes" of the great Miquel Montoro. “Pilotes” are a Majorcan recipe that consisting of a meatball stew.


### Built With

* [Java]()
* [Maven]()
* [Spring]()



<!-- GETTING STARTED -->
## Getting Started

To get a local copy up and running follow these simple steps.

### Prerequisites

The project requires java 11 and maven.

* maven
* jdk-11.0.12

Alternatively
* docker

### Installation


1. Clone the repo
   ```sh
   git clone https://gitlab.com/mikeldalmauc/testTUI.git
   ```
2. Run maven
   ```sh
   mvn clean install
   ```
3. Execute the aplication
   ```sh
   java -jar target\backend-technical-test-2.0.0-SNAPSHOT-spring-boot.jar
   ```

#### Docker
Running the application with docker

1. Build the container
    ```sh
    docker build -t prueba-tui .
    ```
2. Run the container
    ```sh
    docker run -p 8080:9000 prueba-tui --server.port=9000
    ```

The dockerfile configuration is adapted from the one shown on this article:
https://snyk.io/blog/best-practices-to-build-java-containers-with-docker/

#### Other Configuration

#####H2
The application is running an embedded database, update datasource as required at application.propertie file.
```sh
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=password

spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
```

##### Hibernate
The application is running an embedded artemis indexing for search capabilities, update datasource as required at application.properties.

```sh
spring.artemis.mode=EMBEDDED
spring.artemis.host=localhost
spring.artemis.port=61616
spring.artemis.embedded.enabled=true
```
More configuration at hibernate.properties ...

```sh
hibernate.search.backend.directory.root = /var/lucene/indexes
hibernate.search.backend.type=lucene
```

<b>Note</b>: <i>actually the application will create a folder at the root of your filesystem. /var </i>


##### ActiveMQ
The service uses the ActiveMQ broker in embedded mode to send and receive messages from queues. Integration with Udate datasource as required at application.properties.. If an standalone or remote broker is used this must support scheduling for the application to work properly.

```sh
spring.activemq.in-memory=true
spring.activemq.pooled=false
spring.activemq.schedulerSupport=true
```

##### Sonar Qube

To run SonarQube analisis on a server, this can be done locally with docker or remotely. Access the latest analisis at  http://sonarpruebatui.ddns.net:9000 with auth admin/ADMIN1.

```sh
docker pull sonarqube:9.0.1-developer
```
```sh
docker run sonarqube
```
Run a clean verify maven build pointing to sonar server.
```sh
mvn clean verify sonar:sonar -Dsonar.projectKey=projectName -Dsonar.host.url=http://publicIP:9000 -Dsonar.login=SECURITY_TOKEN
```

Ideally, SonarQube server should return back useful information to the IDE of your choice via sonarLint, on VS Code:
```sh
"sonarlint.connectedMode.connections.sonarqube": [
          {
            "serverUrl": "",
            "token": ""
          }
    ]
```
##### Application exclusive configuration

The application uses three variables, two for queue names, embedded broker will generate the queues on the fly but ideally this should point to correspondent broker queues. Order wait time represents in seconds the period an order can be updated before being saved.

```sh
tui.activemq.orders.queue=orders
tui.activemq.kitchen.queue=kitchen

tui.orders.waittime=300
```

<!-- USAGE EXAMPLES -->
## API

The service exposes the following endpoints:

- <b>/orders</b>
  - <b>/</b> &nbsp; &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; : Greets the user for using our service.
  - <b>/create</b>&nbsp;&nbsp;: Create an order.
  - <b>/update</b>&nbsp;: Updates an order (within 5 minutes after creation)
  - <b>/search</b>&nbsp;: Searchs orders given customer data. Requires authentication and admin level authorization.

Swagger V2 documentation can be accessed in Json format at

<b>/v2/api-docs</b>

or inserting the previous address into the swagger ui searchbox accesible at

<b>/swagger-ui</b>


### Example calls:

#### create

Next call will attempt to create an order. Id and number must be empty to create orders. Address and Client Ids can be informed. 

The service will return the same json with the order number informed, which is required for an update call.

POST <a>/orders/create</a>

<pre>
<code>
{
        "id": "",
        "number": "",
        "pilotes": 5,
        "pricePerPilote": 1.06,
        "orderTotal": 5.300000000000001,
        "client": {
            "id": "",
            "firstName": "Lloyd",
            "lastName": "Bartol",
            "telephone": "700-016-3255",
            "email": "maple.hyatt@gmail.com"
        },
        "deliveryAddress": {
            "id": "",
            "street": "62428 Champlin Springs",
            "postCode": "24130",
            "city": "Trompshire",
            "country": "Thailand"
        }
    }
</code>
</pre>

#### update

Next call will attempt to update order with nubmer 2323... On update id of order must be empty and number informed but client id can be informed. 

Service will return same json but with changed order number. If the later is not changed, then update call couldn't be perfored either due to timeout or unexisting number.

POST <a>/orders/update</a>

<pre>
<code>
{
        "id": "",
        "number": "2323232323",
        "pilotes": 5,
        "pricePerPilote": 1.06,
        "orderTotal": 5.300000000000001,
        "client": {
            "id": "",
            "firstName": "Lloyd",
            "lastName": "Bartol",
            "telephone": "700-016-3255",
            "email": "maple.hyatt@gmail.com"
        },
        "deliveryAddress": {
            "id": "",
            "street": "62428 Champlin Springs",
            "postCode": "24130",
            "city": "Trompshire",
            "country": "Thailand"
        }
    }
</code>
</pre>

#### search
Next call will retrieve orders of customers matching with any character before and after with \*on\* in the first name. If informed, Id field will require exact match.

GET <a>/orders/search</a>

Basic auth: {user: admin, password: admin}
<pre>
<code>{
    "id": "",
    "firstName": "on",
    "lastName": "",
    "telephone": "",
    "email": ""
}</code>
</pre>

##### Aditionally exposed services

The service exposes aditionally the embedded database console (auth: sa/password):

<a>/h2-console</a>

The documentation of the API and swagger consoles:

<a>/v2/api-docs</a>
<a>/swagger-ui</a>

Administration console of the embedded ActiveMQ queue broker:

<a>host:8161/admin</a>

<!-- ROADMAP -->
## Roadmap

See the [open issues](https://gitlab.com/mikeldalmauc/testTUI/-/issues) for a list of proposed features (and known issues).

<!-- CONTRIBUTING -->
## Contributing

Contributions are what make the open source community such an amazing place to learn, inspire, and create. Any contributions you make are **greatly appreciated**.

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## Discussion

As most sowtare projects alive, this should be expected to grow. One of the main carateristic it should have is evolvabilty (1) and this has motivated many of the decisions made in the project. For example, investing more time on the setup of a CI pipeline and a good testing to provide good data usable on the fitness functions that should guide evolutionary change.

Another example, the project is organized by technical layers, this means domain is spread along the layers making domain changes expensive. If the domain is intended to change a lot,  this technical layers should dissapear in order to maintain a good evolvavility (1). But for a starter point, and withouth a clear technical path for the project, having technical concerse separate by layers should help.

<!-- LICENSE -->
## License

Distributed under the Apache License 2.0, January 2004. See `LICENSE` for more information.
                       

<!-- CONTACT -->
## Contact

Mikel Dalmau - mikeldalmauc@gmail.com

Project Link: [https://gitlab.com/mikeldalmauc/testTUI.git](https://gitlab.com/mikeldalmauc/testTUI.git)


## References

1. FORD, Neal; PARSONS, Rebecca; KUA, Patrick. Building evolutionary architectures: support constant change. " O'Reilly Media, Inc.", 2017.

<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/github_username/repo.svg?style=for-the-badge
[contributors-url]: https://gitlab.com/mikeldalmauc/testTUI/-/graphs/master
[issues-shield]: https://img.shields.io/github/issues/github_username/repo.svg?style=for-the-badge
[issues-url]: https://gitlab.com/mikeldalmauc/testTUI/-/issues
[license-shield]: https://img.shields.io/github/license/github_username/repo.svg?style=for-the-badge
[license-url]: https://gitlab.com/mikeldalmauc/testTUI/-/blob/master/README.md
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: www.linkedin.com/in/mkldalmau

