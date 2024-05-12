## Simple Request
* http://localhost:8080/starter.zip?type=maven-project&language=java&bootVersion=3.2.2&groupId=com.example&artifactId=demo&name=demo&dependencies=web
* http://localhost:8080/starter.zip?type=maven-project&language=java&bootVersion=3.2.2&groupId=com.example&artifactId=demo&name=demo

## A simple POST request:
```
curl --location 'http://localhost:8080/generate.zip' \
--header 'Content-Type: application/json' \
--output my-file.zip \
--data '{
    "costCenter": "ABC",
    "type": "maven-project",
    "bootVersion": "3.2.2",
    "name": "myproject",
    "groupId": "com.droveda.app",
    "artifactId": "myproject",
    "dependencies": ["web"]
}'
```