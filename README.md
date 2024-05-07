
# Spring Boot - Phone Reservation Application

## Overview

This Spring Boot application is a REST API that provides endpoints for booking/returning the phones.

### Installation

1. Clone the repository:
    ```bash
    git clone https://github.com/kradhakr/phone-reservation-app.git
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

## API Documentation
http://localhost:8090/swagger-ui.html#

### Base URL
The base URL for the API is:

`http://localhost:8090/api/phone/`


## Docker packaging
 ```bash
docker build --tag phone-reservation-app .
docker run -p 8080:8090 -t phone-reservation-app --name -app
   ```
Access the application using URL : http://localhost:8080/api/phone/book

## Design document
Refer the PhoneReservationUML.jpg for the UML design.

