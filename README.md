# Analysis-backend

Project goal is to provide an interface for historical stock information, and stock analysis tools.
It is designed to handle daily trading data from bet.hu.

Project:
- Based on Spring
- SQL database integration with JPA, hibernate.

  - Stock input:
    - Txt file reader.
    - CSV file reader.

- Custom Cache service.
- Logging service.

# Starting the application:

Required environment variables declared in the docker-compose.yaml
The project is set up to run the app and the sql server in different docker containers.
Containers communicate through docker container network.


Testing:
In order to run the integration test. An application-test.properties file need to be created and configured based on the txt file.
application-testproperties.txt, contains the necessary scripts to create the appropriate table and entries.





