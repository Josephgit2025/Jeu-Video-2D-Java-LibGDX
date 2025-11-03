FROM openjdk:21-jdk-slim

WORKDIR /app

COPY ./*.java .

RUN javac *.java

EXPOSE 8080

CMD ["java", "Main"]