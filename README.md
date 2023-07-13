# aws-image-gallery
Image gallery application on AWS using RDS and S3

## How to
### PostgreSQL
Using Docker:
```
docker compose -f ./src/main/docker/postgresql.yml up
```
PostgreSQL will be available on `localhost:5432`.
### Run
Using Maven Wrapper:
```
mvnw spring-boot:run
```
### Package
```
mvn clean package
```
