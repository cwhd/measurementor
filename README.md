# measurementor
measurementor helps you visualize data from your development cycle to help you be a better team.

# Getting Started
First note the [vagrant file](https://www.vagrantup.com/).  That will call the [puppet](http://puppetlabs.com/) manifest
that will install everything you need to get going.  In a nutshell it installs:
- [Elasticsearch](http://www.elasticsearch.org/) : used for indexing data for searching
- [Kibaba](http://www.elasticsearch.org/guide/en/kibana/current/) : used for displaying pretty dashboards
- [MongoDB](http://www.mongodb.org/) : used to back the [Grails](https://grails.org/) based application

Run the measurementor grails app inside the vagrant box by updating the shared

# In Progress...
This is still a bit of a work in progress but feel free to let me know if there's anything that could be better.
thanks!

# Other Stuff

logs get output to /tmp/log/measurementor.log.

