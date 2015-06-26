#!/usr/bin/env bash

sudo cp /usr/lib/jvm/java-8-oracle/jre/lib/security/local_policy.jar /usr/lib/jvm/java-8-oracle/jre/lib/security/local_policy.jar.bk

sudo cp /usr/lib/jvm/java-8-oracle/jre/lib/security/US_export_policy.jar /usr/lib/jvm/java-8-oracle/jre/lib/security/US_export_policy.jar.bk

sudo rm /usr/lib/jvm/java-8-oracle/jre/lib/security/US_export_policy.jar

sudo rm /usr/lib/jvm/java-8-oracle/jre/lib/security/local_policy.jar

sudo cp US_export_policy.jar /usr/lib/jvm/java-8-oracle/jre/lib/security/US_export_policy.jar

sudo cp local_policy.jar /usr/lib/jvm/java-8-oracle/jre/lib/security/local_policy.jar