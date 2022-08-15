FROM maven:3.5-alpine

WORKDIR /spring/app

COPY repository/pom.xml repository/pom.xml
COPY service/pom.xml service/pom.xml
COPY web/pom.xml web/pom.xml


COPY pom.xml .
RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline

# if  modules  depends each other, can use -DexcludeArtifactIds as follows
# RUN mvn -B -e -C org.apache.maven.plugins:maven-dependency-plugin:3.1.2:go-offline -DexcludeArtifactIds=module1

# Copy the dependencies from the DEPS stage with the advantage
# of using docker layer caches. If something goes wrong from this
# line on, all dependencies from DEPS were already downloaded and
# stored in docker's layers.
FROM maven:3.6-alpine as BUILDER
WORKDIR /spring/app
COPY --from=deps /root/.m2 /root/.m2
COPY --from=deps /spring/app/ /spring/app
COPY repository/src /spring/app/repository/src
COPY service/src /spring/app/service/src
COPY web/src /spring/app/web/src


RUN mvn -B -e -o clean install -DskipTests=true

FROM openjdk:8-alpine
WORKDIR /spring/app
COPY --from=builder /spring/app/epam/esm/my-1.0.0.jar .
EXPOSE 8080
CMD [ "java", "-jar", "/spring/app/my-1.0.0.jar" ]