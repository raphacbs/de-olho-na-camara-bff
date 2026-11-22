FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
RUN apk add --no-cache tzdata \
    && addgroup -S app && adduser -S app -G app
COPY --from=build /app/target/de-olho-na-camara-bff-*.jar app.jar
RUN chown -R app:app /app
USER app
EXPOSE 8080
ENV TZ=America/Recife
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75.0 -Duser.timezone=America/Recife"
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
