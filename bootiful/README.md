bootiful
--------

```
docker run --rm -ePOSTGRES_PASSWORD=postgres -p5432:5432 --name my-postgres postgres:13.3
```

```
./gradlew bootRun
```

With Testcontainers (shutdown docker postgres first)
```
./gradlew test
```


