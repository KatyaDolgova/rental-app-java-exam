package com.example.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBConnection {

    private static final Properties CONFIG = loadConfig();

    private static Properties loadConfig() {
        Properties props = new Properties();
        try (InputStream in = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (in == null) {
                throw new RuntimeException(
                    "db.properties не найден на classpath. Скопируйте src/main/resources/db.properties.example " +
                    "в src/main/resources/db.properties и впишите свои данные подключения.");
            }
            props.load(in);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось загрузить db.properties", e);
        }
        return props;
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
            CONFIG.getProperty("db.url"),
            CONFIG.getProperty("db.user"),
            CONFIG.getProperty("db.password")
        );
    }
}
