# Project Overview
 The project consists of a server-side component called "SuperApp" and two client applications known as "MiniApps".
* An Android MiniApp is designed for browsing and exploring points of interest.
* A Vue MiniApp for exploring path angles.
 
Both MiniApps communicate with the SuperApp server to retrieve data.

## Travelers superapp 
### SPRING BOOT & REST API
* The server is a spring boot application that provides a REST API for the MiniApps to consume.
### OpenStreetMap API 
* The server integrates with the OpenStreetMap (OSM) API to retrieve data related to points of interest and other geographical information. For more information about the OSM API, see the [OSM API documentation](https://wiki.openstreetmap.org/wiki/API_v0.6).
* The integration with OSM and the usage in the server are done through `OpenStreetMapService.java` interface while using `OMSObjectConvertor` to convert the data to the superapp objects.
* A predefined user is used to access the OSM API and the objects can be filtered in the DB by a specific **type**. Both The userEmail and the type are stored in the `application.properties` file.
### Initializers
* The server uses `UsersInitializerOSM.java` to initialize the user for OSM logic in the DB.
* The server uses `DataInitializerOSM.java` to initialize the OSM objects data in the DB. The data retrieved is from a predefined search parameters defined in the `application.properties` file and based on a hardcoded list of categories.
* The server uses `MiniappDummyObjectInitializer.java` to initialize the dummy object data in the DB. The object is used by the miniapp app when invoking a command that does not require a specific object, and it should be known to the clients. The object can be asked by a specific type and created by a predefined user. Both can be configured in the `application.properties` file.

## Android MiniApp Features
* The Android MiniApp is a native Android application that allows users to add and explore points of interest.
* Users can upload images of points of interest. The images are stored in a **Firebase** server and url to the image is stored in the superapp DB.
* Users can select location of a point on the map using **Google Maps** integrate within the app.
* Miniapp - Superapp communication is done through REST API using **Retrofit** library.

----------------------------------------------------------------------------------------------------------------------------
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
# TimeToTravel Miniapp - Android Studio Installation and Setup Guide

## Prerequisites

Before starting, ensure that the following prerequisites are met:

- Android Studio is installed on your computer. If not, see instruction below.
- A compatible Android device or emulator is available to run the project.

## Android Studio Installation Instructions

1. Download Android Studio from the official website: [https://developer.android.com/studio](https://developer.android.com/studio).

2. Run the downloaded installer file and follow the on-screen instructions.

3. Choose the installation type:
    - **Standard**: Select this option if you want the recommended setup, including Android SDK and other necessary components.

4. Select the location where you want Android Studio to be installed. The default location is usually fine.

5. If prompted, choose whether to import previous settings from an existing Android Studio installation. If this is your first time installing Android Studio, you can skip this step.

6. Wait for the installation process to complete. This may take several minutes depending on your system's performance.

7. Once the installation is complete, click on the "Next" button.

8. On the "SDK Setup" screen, you can choose whether to install the Android SDK and other required components. It is recommended to leave the default settings selected and click on the "Next" button.

9. On the "Choose Start Menu Folder" screen, you can select the folder where Android Studio shortcuts will be created. Click on the "Install" button to continue.

10. Wait for Android Studio to download and install the selected components. This process may take some time, as it involves downloading large files.

11. Once the installation is complete, click on the "Finish" button to exit the installer.

## Running Android Studio

1. Launch Android Studio by clicking on the desktop shortcut or by searching for "Android Studio" in the Start menu (Windows) or Applications folder (macOS).

2. Android Studio may prompt you to import settings from a previous installation. You can choose to import settings or select the option to start with the default settings.

3. Wait for Android Studio to initialize and set up the necessary files and directories. This may take a few moments.

4. Once Android Studio is fully loaded, you can continue to the Android Project setup.


## Miniapp Project Setup Instructions

1. Extract the contents of the ZIP file to a location of your choice on your computer.

2. Launch Android Studio on your computer.

3. In the Android Studio welcome screen, click on "Open an existing Android Studio project."

4. Browse to the location where you extracted the ZIP file and select the project folder.

5. Click on the "OK" button to open the project.

6. Once the project is loaded, wait for Android Studio to sync and build the project. This process may take a few moments.

* ***Next steps refer to installing the app on a physical device. before doing that make sure your superapp server is running on your computer, and you set up the correct server address at the client (instruction below).***

7. Connect your Android device to your computer using a USB cable or launch an Android emulator.

8. Ensure that USB debugging is enabled on your Android device. You can enable it by following these steps:
    - Go to **Settings** on your Android device.
    - Scroll down and tap on **About phone** or **About device**.
    - Find the **Build number** and tap on it seven times to enable Developer options.
    - Go back to the main **Settings** screen and tap on **Developer options**.
    - Enable **USB debugging**.

9. In Android Studio, click on the "Run" button (green triangle) located in the toolbar or select **Run** from the menu.

10. Choose your connected Android device or emulator from the list of available devices.

11. Click on the "OK" button to run the project on the selected device.

12. Wait for Android Studio to install and launch the app on the device.

13. Once the app is successfully installed and launched, you can interact with the app on your device.

## Setting Up The Server Address

### Finding the IP Address of the Server Computer

To update the server address, you need to determine the IP address of the server computer. Follow these steps to find the IP address:

1. On the server computer, open a command prompt or terminal.

2. Run the following command:
    - On Windows: `ipconfig`
    - On macOS/Linux: `ifconfig`

3. Look for the network interface that is connected to the network. The IP address associated with that interface is the IP address of the server computer. It is typically labeled as "IPv4 Address" or "inet addr".

4. The port is specified in `application.properties` file located in the `src/main/resources` folder of the server project. By default, the port is set to `8081`. You can change the port by editing the `application.properties` file and changing the value of the `server.port` property.

5. Note down the IP address for use in the next section.

### Updating the Server Address in the RetrofitClient Class

To change the server address in the client code, follow these steps:

1. Open the `RetrofitClient.java` file located in the `com.example.miniapppointsofinterest.api` package of your project.

2. Locate the following lines of code:

```java
private static String BASE_URL = "http://192.168.1.200:8081/";
```
3. Update the `BASE_URL` variable with the IP address and port of your server. For example:

```java
private static String BASE_URL = "http://YOUR_SERVER_IP:YOUR_SERVER_PORT/";
```
replace `YOUR_SERVER_IP` with the IP address you obtained in the previous section, and `YOUR_SERVER_PORT` with the port number your server is listening on.

4. Save the changes to the `RetrofitClient.java` file.

----------------------------------------------------------------------------------------------------------------------------

# Path Angles Miniapp - Vue Installation and Setup Guide

first of all, make sure that server is running!

## Installation

### Node.js

1. Download Node.js from the official website: [https://nodejs.org](https://nodejs.org)
2. Follow the installation instructions for your operating system.

### Visual Studio Code

1. Download Visual Studio Code from the official website: [https://code.visualstudio.com](https://code.visualstudio.com)
2. Follow the installation instructions for your operating system.

### Vue.js 3, Vue Router, and Axios

1. Open a terminal or command prompt.
2. Run the following commands to install Vue CLI globally, Vue Router, and Axios:

```shell
npm install -g @vue/cli
npm install vue-router
npm install axios
```

* **Getting Started** - 
Open a terminal or command prompt.
Navigate to your project directory.
Run the following command to install project dependencies:

```shell
npm install
 ```

* **Development** - 
Run the following command to start the development server:

```shell
npm run dev
 ```

* Open your web browser and visit the provided URL to see the project in development mode.
