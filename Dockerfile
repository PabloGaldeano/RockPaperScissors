FROM openjdk:15
COPY target/RockPaperScissors-1.0.0.jar rockpaperscissors.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar", "--enable-preview", "rockpaperscissors.jar"]