
Environment setup:
------------------
+ Install Node.js from https://nodejs.org/ (at least version 0.12.4)

+ Install npm for all users using the following command
    >sudo npm install -g

+ Install bower for all users using the following command
    >sudo bower install -g

+ Install java

+ you will need to update the crypto on your machine.
For Java 8: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
For Java 7: http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html

On a mac:
find where your home directory is:
	/usr/libexec/java_home -v 1.8
	
cd into the directory.
cd into the jre/lib/security/ directory

backup the us_export_policy.jar as well as the local_policy.jar

Copy the us_export_policy.jar as well as the local_policy.jar files from the download into the directory...

+ All done.

To build the application
------------------------

+ Run the following command:
    >gradle clean bootRepackage

+ you can then run the generated WAR file using the following command (name of WAR file may vary, update accordingly):
     >java -jar build/libs/measurementor-0.0.1-SNAPSHOT.war

To run the application within your IDE
--------------------------------------
You have 2 options:
- run the UI layer side-by-side with the Java back-end but in different processes. This is the prefered approach if you are planning
  to work on the UI layer as you can rely on grunt to hot-deploy your changes
- run the UI layer in the same process as the java back-end. This is the prefered approach if you plan to make changes
  on the java side but the UI layer will be fairly stable

If you chose to run the UI layer side-by-side with the Java back-end but in different processes, you need to:
- run the following command to build and hot-deploy the UI layer, from the /ui folder:
    >grunt serve, you need to first build the UI.
- run the back-end java application like a regular spring boot application using either the command line or your favorite IDE.

If you chose to run both UI and back-end on the same process, you need to first build the UI.
To do so, run the following gradle task, to build the UI layer and create a new folder under src/main/webapp.
This new folder will then be automatically picked up by Spring boot as you run the application like a standard
spring boot application from your favorite IDE.
    >gradle copyWebApp
Then run the application from your IDE like a standard spring boot application or using the command line


need to do the dynamic newrelic stuff.
   http://jdpgrailsdev.github.io/blog/2014/04/08/spring_boot_gradle_newrelic.html

SSL Article: http://blog.nerdability.com/2013/01/tech-how-to-fix-sslpeerunverifiedexcept.html
