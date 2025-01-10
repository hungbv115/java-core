FROM openjdk:11.0.1
RUN adduser  --disabled-password --home /home/java hungbv
USER hungbv
WORKDIR /home/java
ADD target/java-core-1.0-SNAPSHOT.jar /home/java/application.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom"]