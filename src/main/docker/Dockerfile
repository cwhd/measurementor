FROM java:8
VOLUME /tmp
ADD measurementor-0.0.1-SNAPSHOT.war app.war
RUN bash -c 'touch /app.jar'
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=docker","-jar","/app.war"]