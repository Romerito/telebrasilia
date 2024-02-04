FROM openjdk:17
VOLUME /app                  
EXPOSE 8089                  
ADD target/telebrasilia-0.0.1-SNAPSHOT.jar telebrasilia-0.0.1-SNAPSHOT.jar 
ENTRYPOINT ["java","-jar","/telebrasilia-0.0.1-SNAPSHOT.jar"] 