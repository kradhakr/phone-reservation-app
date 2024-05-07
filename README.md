
# Spring Boot - Phone Reservation Application

## Overview

This Spring Boot application is a REST API that provides endpoints for booking/returning the phones.

### Installation

1. Clone the repository:
    ```bash
    git clone git@github.com:kradhakr/phone-reservation-app.git
    cd phone-reservation-app
    ```

2. Build the application:
    ```bash
    mvn clean install
    ```

3. Run the application:
    ```bash
    mvn spring-boot:run
    ```

•	Open the project into IntelliJ 
•	Run the PhoneReservationApplication.java can also run using command 
•	Reach till spring boot application folder (phone-reservation-app)
•	Run this command (mvn spring-boot:run)

## API Documentation
http://localhost:8090/swagger-ui.html#

### Base URL
The base URL for the API is:

`http://localhost:8090//api/phone`


## Docker packaging
docker build --tag phone-reservation-app .
docker run -p 8080:8090 -t phone-reservation-app --name -app
Access the application using URL : http://localhost:8080/api/phone/book

## Design document
Refer the PhoneReservationUML.jpg for the UML design.

