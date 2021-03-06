# BookStore App
### System requirements

- Java (8 or 11)
- Maven
- MongoDB
- Docker

------------


### How to build and run
Run mongoDB docker container

    docker run -d -p 27017:27017 --name <mongo_container_name> mongo:latest

Build jar and create docker image

    mvn clean install
    docker image build -t <name_of_the__app_image> .

Run Docker application container

    docker run -d -p 7979:7979 --name <name_of_app_container> --link <mongo_container_name>:mongo <name_of_the__app_image>


------------
### Swagger
[[1]Swagger UI][swagger]



[swagger]: http://localhost:7979/swagger-ui.html "Swagger UI"


------------

### Design considerations
1. Apart from registration end points, rest all end points are secured and need an **Authorization header** in the request.
2. There are 2 actors in the app the **Customer** and the **Admin**. The **Customer token** is returned in the response when he/she **registers**, similarly the **Admin token** is generated on **admin registration**. These tokens need to be sent in the Authorization header while accessing the endpoints.
3. **Admin token** is needed to access **add books, update book inventory , search-order-by-id and search-order-by-date** endpoints.
4. **Customer token** is needed to access **place new order , view customer orders and view customer monthly statistics** endpoints.
5. Standard validations on requests are in place.
6. All endpoints are **Restful**.
7. **Tech stack** used :  Java , spring boot, mongoDB.
8. An overall **code coverage** of **>85%** is achieved.
9. **Caffene JVM caching** is implemented on **monthly stats** for faster access with a **TTL of 24 HRS**, which is cleaned by a scheduled process.

------------

### Postman Collection
[[2]PostMan Collection][postman]


[postman]: https://www.getpostman.com/collections/254c09a0a3970e6269f8 "postman collection"
