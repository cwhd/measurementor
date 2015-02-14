# measurementor
measurementor helps you visualize data from your development cycle to help you be a better team.  This code is
used as an example data collection system in the book [Continuous Improvement](http://manning.com/davis2/).

# Getting Started
First note the [vagrant file](https://www.vagrantup.com/).  That will call the [puppet](http://puppetlabs.com/) manifest
that will install everything you need to get going.  In a nutshell it installs:
- [Elasticsearch](http://www.elasticsearch.org/) : used for indexing data for searching
- [Kibaba](http://www.elasticsearch.org/guide/en/kibana/current/) : used for displaying pretty dashboards
- [MongoDB](http://www.mongodb.org/) : used to back the [Grails](https://grails.org/) based application

To get the measurementor grails app running inside the vagrant box:
- Update the shared directory in your Vagrantfile on line 39.  This will share the source code on your local box with the
appropriate directory on the Vagrant box.
- Get your vagrant box up and running and use the following commands to get it up and running
-- vagrant up
-- vagrant ssh
-- cd /measurementor
-- grails run-app -Dgrails.server.port.http=8070
Just as a note by default Grails uses port 8080 which is used by many other things, so I changed it to 8070.

Measurementor has a number of configurable settings including the URLs and credentials of the source systems and system
defaults.  Update the application.properties (measurementor.properties) to set everything up.

# In Progress...
This is still a bit of a work in progress but feel free to let me know if there's anything that could be better.
thanks!

# Other Stuff
The puppet manifest has some known issues.  Sometimes the vagrant box forgets the right JAVA_HOME and you have to
restart elasticsearch and nginx.  See the notes in the Vagrantfile for workarounds.
logs get output to /tmp/log/measurementor.log.


