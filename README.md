
# Spring Boot - Phone Reservation Application
## Build Rest API for Phone Reservation Application

•	Open the project into IntelliJ 
•	Run the PhoneReservationApplication.java can also run using command 
•	Reach till spring boot application folder (phone-reservation-app)
•	Run this command (mvn spring-boot:run)

API Details :
http://localhost:8090/swagger-ui.html#


## Docker packaging
docker build --tag phone-reservation-app .
docker run -p 8080:8090 -t phone-reservation-app --name -app
Access the application using URL : http://localhost:8080/api/phone/book

## Design document
Refer the PhoneRervationUML.jpg for the UML design.

