#!/bin/bash
echo " _  _  ____   __   ____  _  _  ____  ____  _  _  ____  __ _  ____  __  ____ "
echo "( \\/ )(  __) / _\\ / ___)/ )( \\(  _ \\(  __)( \\/ )(  __)(  ( \\(_  _)/  \\(  _ \\"
echo "/ \/ \ ) _) /    \\___ \) \/ ( )   / ) _) / \/ \ ) _) /    /  )( (  O ))   /"
echo "\_)(_/(____)\_/\_/(____/\____/(__\_)(____)\_)(_/(____)\_)__) (__) \__/(__\_)"
echo "Welcome to measurementor setup!"
echo "This script will help you set your system up for the first time.  You can always re-run this script, or update the application.properties file with the latest variables."

CONFIGFILE=application-config.properties
VAGRANTFILE=Vagrantfile

cp measurementor.properties $CONFIGFILE

read -p "Do you have a JIRA system to connect to? (y/n)" hasJira
if [ $hasJira = "y" ]; then
  read -p "Enter JIRA URL: " jiraUrl
  sed -Ei s/jira\.url=/jira\.url=$jiraUrl/g $CONFIGFILE
  read -p "Enter JIRA Credentials: " jiraCreds
  sed -Ei s/jira\.credentials=/jira\.credentials=$jiraCreds/g $CONFIGFILE
fi
read -p "Do you have a Jenkins system to connect to? (y/n)" hasJenkins
if [ $hasJenkins = "y" ]; then
  read -p "Enter Jenkins URL: " jenkinsUrl
  sed -Ei s/jenkins\.url=/jenkins\.url=$jenkinsUrl/g $CONFIGFILE
  read -p "Enter Jenkins Credentials: " jenkinsCreds
  sed -Ei s/jenkins\.credentials=/jenkins\.credentials=$jenkinsCreds/g $CONFIGFILE
fi
read -p "Do you have a Stash system to connect to? (y/n)" hasStash
if [ $hasStash = "y" ]; then
  read -p "Enter Stash URL: " stashUrl
  sed -Ei s/stash\.url=/stash\.url=$stashUrl/g $CONFIGFILE
  read -p "Enter Stash Credentials: " stashCreds
  sed -Ei s/stash\.credentials=/stash\.credentials=$stashCreds/g $CONFIGFILE
fi
read -p "Do you have a Github repo to connect to? (y/n)" hasGithub
if [ $hasGithub = "y" ]; then
  read -p "Enter Github repo URL: " githubUrl
  sed -Ei s/github\.url=/github\.url=$githubUrl/g $CONFIGFILE
  read -p "Enter Github Credentials: " githubCreds
  sed -Ei s/github\.credentials=/github\.credentials=$githubCreds/g $CONFIGFILE
fi
echo "You should be ready to go!  Enjoy!"

#TODO
#do you have an instance of mongoDB to use?
#if so, we need the credentials for the grails config
#do you have an EC you want to use?
#if so, we need to put the info in the grails config
#post the index to EC so you don't have to set it up
#run the grails app when all done