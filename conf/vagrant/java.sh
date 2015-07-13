#!/usr/bin/env bash

sudo wget --no-check-certificate https://github.com/aglover/ubuntu-equip/raw/master/equip_java8.sh && bash equip_java8.sh

sudo bash -c 'echo JAVA_HOME="/usr/lib/jvm/java-8-oracle" >> /etc/environment'

sudo add-apt-repository ppa:cwchien/gradle

sudo apt-get update

sudo apt-get -y install gradle

# touch ~/.gradle/gradle.properties && echo "org.gradle.daemon=true" >> ~/.gradle/gradle.properties