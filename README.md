# Sigma-Internship-Pet-Project [![codecov](https://codecov.io/gh/MrZhenShen/Sigma-Internship-Pet-Project/branch/main/graph/badge.svg?token=ZF2D8H6EE1)](https://codecov.io/gh/MrZhenShen/Sigma-Internship-Pet-Project)

## How to set up local environment
1) Install [Docker Desktop](https://www.docker.com/products/docker-desktop/)
2) Run [docker compose](https://docs.docker.com/engine/reference/commandline/compose_up/);
   execute `docker compose up` in `docker/` folder
3) In IntelliJ "Preferences...", under Build, Execution, Deployment > Build Tools > Maven > Runner, select the option "Delegate IDE build/run actions to Maven."
4) Run Application by starting Main class

## Running in a Local Environment

1. Open [Swagger UI](http://localhost:8080/swagger-ui/index.html) in your browser.
2. Sign in using one of the following credentials:
   - **Admin access**: Enter `admin` as the username to authorize with administrator privileges.
   - **User access**: Enter `user` as the username to authorize with general user privileges.

> **Note:** Before running the application locally, ensure the following versions are installed or supported on your machine:
>
> - **Java**: 17
> - **Maven**: Compatible with Maven 3.6+ (required for plugin compatibility)
> - **Spring Boot**: 2.7.5 (defined in the parent POM)
> - **MapStruct**: 1.5.3.Final
> - **PMD**: 3.20.0
> - **OpenAPI (springdoc-openapi)**: 1.6.14
> - **JaCoCo**: 0.8.8
