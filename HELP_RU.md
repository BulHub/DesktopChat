# Руководство по скачиванию и запуску приложения

## Скачивание

1. Последняя рабочая версия:
https://github.com/BulHub/DesktopChat/releases/tag/v1.0.0
2. Загрузить zip архив

## Запуск

1. Открыть проект с помощью среда разработки, поддерживающую Java
2. Создать таблицы: DesktopChat/Chat++/src/main/resources/data.sql
3. Заменить данные для подключения к БД: 
DesktopChat/Chat++/src/main/java/ru/bulat/data/DatabaseConnection.java
4. Запустить сервер для чата:
DesktopChat/Chat++/src/main/java/ru/bulat/server/ChatServer.java
5. Запустить окно авторизации:
DesktopChat/Chat++/src/main/java/ru/bulat/frontend/Entrance.java