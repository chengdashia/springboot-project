FROM java:8
COPY *.jar /app.jar
CMD ["--server.port=8686"]
EXPOSE 8686
ENTRYPOINT ["java","-jar","/app.jar"]