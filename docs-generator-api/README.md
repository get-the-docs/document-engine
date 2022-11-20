# GetTheDocs generator service

- java 17
- spring 2.6

## Overview

API for listing available templates, document structures and generating impersonated documents with provided data.

### Build

```bash
docker build -t docs-generator-api .
```

Locally:

```bash
mvn clean verify -P coverage 
```

### Run

```bash
docker run \
--network host \
--bind 0.0.0.0 \
-p 8080:8080 \
-e SPRING_PROFILE=local \
-e SERVER_PORT=8080 \
docs-generator-api:latest
```

### Configuration

#### Docker env variables

**Mandatory parameters:**

| Parameter name          | Description                          | Default                            |
|-------------------------|--------------------------------------|------------------------------------|
| SPRING_PROFILE          | local/test/prod                      |                                    |
| SERVER_PORT             | Server port, optional, example: 8080 |                                    |
| OAUTH_RESOURCE_SERVER   | OAuth2 resource server url           | http://localhost/realms/getthedocs |


#### Spring profiles

- local: no auth, swagger
- prod: auth, swagger

#### Sample call

```bash
curl -X GET "http://localhost:8080/api/v1/templates"
```

### Provided endpoints

| Purpose           | Context                    | Authentication     |
|-------------------|----------------------------|--------------------|
| API operations    | /v1/templates/**           | As defined in OAS. |
| API operations    | /v1/documentstructures/**  | As defined in OAS. |
| Swagger UI        | /v1/templates/swagger-ui   |                    |
| Swagger UI config | /v1/templates/v3/api-docs  |                    |
| Health/liveness   | /actuator/health/liveness  | ADMIN role.        |
| Health/readiness  | /actuator/health/readiness | ADMIN role.        |
| Metrics           | /actuator/metrics          |                    |

Publish and load balance only the resource level endpoints like /v1/templates and /v1/documentstructures.
