#THIS IS THE EXAMPLE SHOWN IN REVISION LESSON 
#THIS ONE MANAGED TO MAKE THE IMAGE FOR MULTI STAGE

FROM eclipse-temurin:23-noble AS builder

WORKDIR /src

COPY mvnw .
COPY pom.xml .

COPY .mvn .mvn
COPY src src


RUN chmod a+x ./mvnw && ./mvnw package -Dmaven.test.skip=true


# Get the filename of the jar
# target/vttp5_ssf_day09practice-0.0.1-SNAPSHOT.jar

FROM eclipse-temurin:23-jre-noble

WORKDIR /app

COPY --from=builder /src/target/vttp5_ssf_project-0.0.1-SNAPSHOT.jar app.jar
#the /src at the start is the workdir that was declared in the first stage


#FOR HEALTHCHECK NEED THIS CURL COMMAND
#RUN apt update && apt install -y curl



#!!!
#NOT SURE ABOUT THIS PORT ONE!!!!!!MAYBE go bak to the simple port method in darryl example.
#!!!!!!!!!!!!!! THIS IS CHUK CODE
#ENV PORT=8080
ENV MY_API_KEY=randomkey
#Railway gives a port variable asPORT
#EXPOSE ${PORT}
#SERVER_PORT is the one that links to the spring application. so this takes the railway port given and uses that in our program.
#ENTRYPOINT SERVER_PORT=${PORT} java -jar app.jar

#This is the one that worked with railway previously.
ENV SERVER_PORT 3000
EXPOSE ${SERVER_PORT}
#ALWAYS REMEMBER TO CHANGE THE NAME TO CURRENT PROJECT NAME



#CODE FOR HEALTHCHECK IF NEEDED
#HEALTHCHECK --interval=30s --timeout=10s --start-period=5s --retries=3 CMD curl -f -s http://localhost:${SERVER_PORT}/health || exit 1

ENTRYPOINT SERVER_PORT=${SERVER_PORT} java -jar app.jar






