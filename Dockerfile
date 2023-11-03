FROM openjdk:17
MAINTAINER Michał Mościcki
COPY ./target/ArtAgencyApp-0.0.1-SNAPSHOT.jar artagency=1.0.0.jar
ENTRYPOINT ["java", "-jar", "artagency=1.0.0.jar"]