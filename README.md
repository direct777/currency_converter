# To build docker image 

## 1.	Build the Spring Boot Application
```
    mvn clean package
```

## 2.	Build the Docker Image
```
    docker build -t currencyconverter-app .
```

## 3.	Run the Docker Container
```
    docker run -p 8080:8080 currencyconverter-app
```

### To run it for development
```
mvn clean install
mvn spring-boot:run
```

### To test
```
mvn test
```

### To see api documentation
```
http://localhost:8080/swagger-ui/#/currency-controller
```

### To get exchange rates according to locale
### Request URL
```
http://localhost:8080/api/currency/convert?locale=en-US&source=eur&target=usd&value=1
```

### To get all available exchange rates
### Request URL
```
http://localhost:8080/api/currency/rates
```

### To clear cache 
### Request URL
```
http://localhost:8080/api/currency/clear-cache
```
