# Minimalist Markdown Editor

This is the **simplest** and **slickest** Markdown editor.  
Just write Markdown and see what it looks like as you type. And convert it to HTML in one click.

## Getting started

### How?

Just start typing in the left panel.

### Buttons you might want to use

- **Quick Reference**: that's a reminder of the most basic rules of Markdown
- **HTML | Preview**: *HTML* to see the markup generated from your Markdown text, *Preview* to see how it looks like

### Most useful shortcuts

- `CTRL + O` to open files
- `CTRL + T` to open a new tab
- `CTRL + S` to save the current file or tab

### Privacy

- No data is sent to any server – everything you type stays inside your application
- The editor automatically saves what you write locally for future use.  
  If using a public computer, close all tabs before leaving the editor

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


## Building the application
+ Run the following command:
    >gradle clean bootRepackage

Running the application:
------------------------
+ Standalone server: just copy the war file generated at the previous step in the application server of your choice
  and follow the deployment process detailed in the application server documentation.
+ Embedded server: the war file contains an embedded application server which can be run using the following command:
    >java -jar <path_to_the_war_here>

Thanks!


