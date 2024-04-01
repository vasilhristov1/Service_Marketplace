# Services Marketplace

## Overview
Services Marketplace is a full-stack application designed to connect service providers with customers. This README outlines the setup and management of the application environment using Docker Compose.

## Prerequisites
Before starting, ensure you have installed:
- Docker
- Docker Compose

## Getting Started

### Starting the Application
To launch the Services Marketplace application, follow these steps:
1. Open a terminal.
2. Navigate to the project's root directory.
3. Run the command:
    - For start: `docker-compose up`
    - For stop: `docker-compose down --rmi local`


  
Configuring Remote Debugging in IntelliJ IDEA
If you are using IntelliJ IDEA, follow these steps to configure remote debugging:

Edit Configurations:

Open IntelliJ IDEA.
Click on "Run/Debug Configurations" in the top menu.
Select "Edit Configurations."
Add New Configuration:

Click the "+" icon to add a new configuration.
Choose "Remote" from the list.
Remote JVM Debug:

Set a name for the configuration (e.g., "Remote Debug").
Specify the port (default is 5005).
Click "OK" to save the configuration.
