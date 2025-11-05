FROM openjdk:21-jdk-slim

WORKDIR /app

COPY . .

RUN apt-get update && apt-get install -y \
    maven \
    libx11-6 \
    libxext6 \
    libxrender1 \
    libxtst6 \
    libxi6 \
    libgl1-mesa-glx \
    libgtk-3-0 \
    libglib2.0-0 \
    libfontconfig1 \
    && apt-get clean
RUN cd game && mvn clean package && mvn clean compile

EXPOSE 8080

CMD ["sh", "-c", "cd game && mvn javafx:run"]