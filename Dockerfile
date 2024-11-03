FROM eclipse-temurin:21

COPY /target/rentalcarpro-0.0.1-SNAPSHOT.jar rentalcar.jar

CMD ["java", "-jar", "rentalcar.jar"]