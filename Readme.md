# Travelers superapp -Server Side

## Overview:
 The project consists of a server-side component called "SuperApp" and a client application known as "MiniApp".
 
 An Android MiniApp is designed for browsing and exploring points of interest.
 
 The app communicate with the SuperApp server to retrieve data.
 
 The miniapp - [Time To Travel application](https://github.com/noytsafrir/TimeToTravelApp)

### SPRING BOOT & REST API
* The server is a spring boot application that provides a REST API for the MiniApp to consume.
### OpenStreetMap API 
* The server integrates with the OpenStreetMap (OSM) API to retrieve data related to points of interest and other geographical information. For more information about the OSM API, see the [OSM API documentation](https://wiki.openstreetmap.org/wiki/API_v0.6).
* The integration with OSM and the usage in the server are done through `OpenStreetMapService.java` interface while using `OMSObjectConvertor` to convert the data to the superapp objects.
* A predefined user is used to access the OSM API and the objects can be filtered in the DB by a specific **type**. Both The userEmail and the type are stored in the `application.properties` file.

### Initializers
* The server uses `UsersInitializerOSM.java` to initialize the user for OSM logic in the DB.
* The server uses `DataInitializerOSM.java` to initialize the OSM objects data in the DB. The data retrieved is from a predefined search parameters defined in the `application.properties` file and based on a hardcoded list of categories.
* The server uses `MiniappDummyObjectInitializer.java` to initialize the dummy object data in the DB. The object is used by the miniapp app when invoking a command that does not require a specific object, and it should be known to the clients. The object can be asked by a specific type and created by a predefined user. Both can be configured in the `application.properties` file.


# Superapp DB - MongoDB Installation and Setup Guide 

## Prerequisites
Before starting, ensure that the following prerequisites are met:
- You have administrative access to your computer.
- You have a compatible operating system (Windows, macOS, or Linux).

## Installation Instructions

1. Visit the official MongoDB website: [https://www.mongodb.com/](https://www.mongodb.com/).

2. Choose the appropriate MongoDB version for your operating system and click on the download link.

3. Follow the installation instructions specific to your operating system. Below are links to the installation guides for different operating systems:
    - [Windows Installation Guide](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/)
    - [macOS Installation Guide](https://docs.mongodb.com/manual/tutorial/install-mongodb-on-os-x/)
    - [Linux Installation Guide](https://docs.mongodb.com/manual/administration/install-on-linux/)

4. Once the installation is complete, MongoDB should be ready to use.

## Configuration and Setup

1. Open a command prompt or terminal.

2. Start the MongoDB server by running the appropriate command for your operating system:
    - Windows: `mongod`
    - macOS/Linux: `sudo mongod`

3. By default, MongoDB runs on port 27017.

4. MongoDB should now be running and ready to accept connections.

## Testing the Installation

1. Open a new command prompt or terminal window.

2. Run the MongoDB shell by running the `mongo` command.

3. If MongoDB is running correctly, you should see a MongoDB shell prompt.

4. Test the connection by executing a basic command, such as `db.version()` or `db.stats()`. If the command returns the expected output, MongoDB is installed and functioning properly.

----------------------------------------------------------------------------------------------------------------------------
