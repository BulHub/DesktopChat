# Application Download and Launch Guide

## Download

1. Latest working version:
https://github.com/BulHub/DesktopChat/releases/tag/v1.0.0
2. Download zip archive

## Running

1. Open a project with a Java-enabled IDE
2. Create tables: DesktopChat/Chat++/src/main/resources/data.sql
3. Replace data for connecting to the database:
DesktopChat/Chat++/src/main/java/ru/bulat/data/DatabaseConnection.java
4. Start chat server:
DesktopChat/Chat++/src/main/java/ru/bulat/server/ChatServer.java
5. Launch login window:
DesktopChat/Chat++/src/main/java/ru/bulat/frontend/Entrance.java
