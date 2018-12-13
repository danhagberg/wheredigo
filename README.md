#Wheredigo (Where did it go?)
Simple spend tracker with primary purpose to load data into Elastic search for Kibana dashboards.

## Project Layout
The project is divided into three sub-modules.

#### wheredigo-api 
Defines REST API and service implementations.

#### wheredigo-integration-test 
Runs full API tests by spinning up dependencies in docker containers.
Integration tests will not be run as part of normal verification.  Integrations tests 
are invoked using the following command:

    gradlew integrationTest

#### wheredigo-vaadin
Provides basic UI for CRUD operations on spend transactions.

