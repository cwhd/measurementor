#!/bin/bash
echo "Welcome to measurementor setup!"
echo "This script will help you set your system up for the first time.  You can always re-run this script, or update the application.properties file with the latest variables."
echo "copying configuration file..."
#cp measurementor.properties application.properties
while true; do
    read -p "Do you have a JIRA instance to connect to?" yn
    case $yn in
        [Yy]* ) echo "cool"; break;;
        [Nn]* ) echo "whatev"; break;;
        * ) echo "Please answer yes or no.";;
    esac
done

#TODO
#copy measurementor.properties to application.properties
#prompt user for all the values
#for each value update the properties file
#run the grails up when all done