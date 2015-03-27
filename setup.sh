#!/bin/bash
echo " _  _  ____   __   ____  _  _  ____  ____  _  _  ____  __ _  ____  __  ____ "
echo "( \\/ )(  __) / _\\ / ___)/ )( \\(  _ \\(  __)( \\/ )(  __)(  ( \\(_  _)/  \\(  _ \\"
echo "/ \/ \ ) _) /    \\___ \) \/ ( )   / ) _) / \/ \ ) _) /    /  )( (  O ))   /"
echo "\_)(_/(____)\_/\_/(____/\____/(__\_)(____)\_)(_/(____)\_)__) (__) \__/(__\_)"
echo "Welcome to measurementor setup!"
echo "This script will help you set your system up for the first time.  You can always re-run this script, or update the application.properties file with the latest variables."
echo "copying configuration file..."
#cp measurementor.properties application.properties
sed -Ei 's/jenkins/whatev/g' application.properties
#while true; do
#    read -p "Do you have a JIRA instance to connect to?" yn
#    case $yn in
#        [Yy]* ) echo "cool"; break;;
#        [Nn]* ) echo "whatev"; break;;
#        * ) echo "Please answer yes or no.";;
#    esac
#done
read -p "Enter JIRA URL: " jiraUrl
read -p "Enter JIRA Credentials: " jiraCreds



#TODO
#copy measurementor.properties to application.properties
#prompt user for all the values
#for each value update the properties file
#do you have an instance of mongoDB to use?
#if so, we need the credentials for the grails config
#do you have an EC you want to use?
#if so, we need to put the info in the grails config
#post the index to EC so you don't have to set it up
#run the grails app when all done