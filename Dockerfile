# We are using a multi-stage build to keep our final image clean and minimal
# Start with a builder image for Maven
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app

# Copy the project's pom.xml and source code to the docker image
COPY pom.xml .
COPY src ./src
COPY testData ./testData

# Run Maven to build the application
RUN mvn clean package

# Now, start with a base image for Java 17 for our final image
FROM openjdk:17-jdk

# Create a directory for our application
RUN mkdir /app

# Copy the jar file from the builder image
COPY --from=builder /app/target/dragon-backend-1.0-SNAPSHOT.jar /app/dragonBackendApp.jar

# Set the application's base path as a working directory
WORKDIR /app

# Define an environment variable with a default empty value
ENV APP_ARG=""

# Command to run the application
CMD java -jar dragonBackendApp.jar $APP_ARG