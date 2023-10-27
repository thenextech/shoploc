FROM openjdk:17-alpine
ENV PORT 8080
EXPOSE 8080
COPY /home/runner/work/shoploc-back-end/shoploc-back-end/target/*.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]
