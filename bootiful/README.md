bootiful
--------

Start Postgres (Not needed for tests):
```
docker run --rm -ePOSTGRES_PASSWORD=postgres -p5432:5432 --name my-postgres postgres:13.3
```

Run:
```
./mvnw spring-boot:run
```

Test:
```
./mvnw test
```

Build native exec & run:
```
./mvnw -Pnative native:compile
target/native-kotlin
```

Build native container & start:
```
./mvnw -Pnative spring-boot:build-image
docker run --rm -p 8080:8080 native-kotlin:0.0.1-SNAPSHOT
```
