FROM maven:3.8.2-jdk-11-slim@sha256:b9fbdd9a73bf7fd3eda246de0fd8aa3167751cac101117385739651163400aee AS build
RUN mkdir /project
COPY . /project
WORKDIR /project
RUN mvn clean package -DskipTests

FROM openjdk:18-ea-11-jdk-alpine3.13@sha256:d2b58158c7c5b57707465ff02adc14de2023cd8e142a27a0a9b3031e88a57d25
RUN apk add dumb-init
RUN mkdir /app
RUN addgroup --system javauser && adduser -S -s /bin/false -G javauser javauser
COPY --from=build /project/target/backend-technical-test-2.0.0-SNAPSHOT-spring-boot.jar /app/java-application.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
# CMD "java" "-jar" "java-application.jar"
ENTRYPOINT ["sh", "-c", "java ${JAVA_OPTS} -jar java-application.jar ${0} ${@}"]