# Measurementor
measurementor helps you visualize data from your development cycle to help you be a better team.  This code is
used as an example data collection system in the book [Agile Metrics In Action](http://manning.com/davis2/).

## Development Branch
This is a new development branch for measurementor.  There are a bunch of changes in this branch:
- We're using [Spring Boot](http://projects.spring.io/spring-boot/) and [Node.js](https://nodejs.org/) instead of Grails
- A shiny interface that makes it easier to create and manage jobs
- Use Gradle to manage the build process
- Use Docker to manage containers for easier local development and deployments **(work in progress)**

## Getting Started

### Environment setup
+ Install [Java](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html): Note that Java 7 also works for this project.
+ Install [NodeJS](https://nodejs.org/) (at least version 0.12.4).
+ Install **npm** for all users using the following command:
    >sudo npm install -g
+ Install **grunt** using the following command:
    >npm install -g grunt-cli

### Additional Stuff
+ Install [Elastic](https://www.elastic.co/) : used for indexing data for searching - Optional. 

  You will have 2 options for elasticsearch environments:
 	- the default spring profile is using an embedded instance (elastic search files will be stored under /data)
 	- you can create other profiles and configure external instances.
+ Other tools worth looking into
	- [Kibana](http://www.elasticsearch.org/guide/en/kibana/current/) : used for displaying pretty dashboards.
	- [Groovy](http://groovy.codehaus.org/) : because Groovy is fun!


## Building

### Vagrant
The simplest way to get the program started and poke around is to leverage vagrant. You will need the following installed on your machine:

- [Vagrant](https://www.vagrantup.com/downloads.html)
- [VirtualBox](https://www.virtualbox.org/wiki/Downloads)

1. From the root folder, type the following on the command line.

```
vagrant up
```

2. SSH into the VM:

```
vagrant ssh
```

3. Setup a gradle Daemon (Optional):

```
touch ~/.gradle/gradle.properties && echo "org.gradle.daemon=true" >> ~/.gradle/gradle.properties
```

4. Build the application. Note that the -Dspring.profile.active=vagrant is required or your build will break due to some issues with VM's writting to the host machine.

```
gradle -Dspring.profiles.active=vagrant build
```

5. Run the application:

```
gradle -Dspring.profiles.active=vagrant bootRun
```

6. Look at the app. Note you will need to use 127.0.0.1 for now rather than localhost.... we will fix this later.

- Open the browser
- 127.0.0.1:8080

7. 



+ Run the following command:
    >gradle clean bootRepackage

Running the application:
------------------------
+ Standalone server: just copy the war file generated at the previous step in the application server of your choice
  and follow the deployment process detailed in the application server documentation.
+ Embedded server: the war file contains an embedded application server which can be run using the following command:
    >java -jar <path_to_the_war_here>

Thanks!


