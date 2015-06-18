you will need to update the crypto on your machine. 

For Java 8: http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html
For Java 7: http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html

On a mac:
find where your home directory is:
	/usr/libexec/java_home -v 1.8
	
cd into the directory.
cd into the jre/lib/security/ directory

backup the us_export_policy.jar as well as the local_policy.jar

Copy the us_export_policy.jar as well as the local_policy.jar files from the download into the directory...

All done.

need to do the dynamic newrelic stuff.
   http://jdpgrailsdev.github.io/blog/2014/04/08/spring_boot_gradle_newrelic.html

SSL Article: http://blog.nerdability.com/2013/01/tech-how-to-fix-sslpeerunverifiedexcept.html
