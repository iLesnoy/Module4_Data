FROM maven:3.5.2-jdk-8-alpine AS MAVEN_TOOL_CHAIN

WORKDIR /spring/app

COPY pom.xml pom.xml
COPY repository/pom.xml repository/pom.xml
COPY service/pom.xml service/pom.xml
COPY web/pom.xml web/pom.xml

# Resolve dependencies for `common` module, e.g., shared libraries
# Also build all the required projects needed by the common module.
# In this case, it will also resolve dependencies for the `root` module.
# Copy full sources for `common` module
COPY repository repository
COPY service service
COPY web web

RUN mkdir -p /jar-layers
WORKDIR /jar-layers
# Extract JAR layers

FROM openjdk:17-oracle

RUN mkdir -p /app
WORKDIR /app

# Copy JAR layers, layers that change more often should go at the end
COPY repository/target/repository-1.0.0.jar .
COPY service/target/service-1.0.0.jar .
COPY web/target/web-1.0.0.jar .

EXPOSE 8086

ENTRYPOINT ["java","-jar","web-1.0.0.jar"]
#CMD ["-start"]
