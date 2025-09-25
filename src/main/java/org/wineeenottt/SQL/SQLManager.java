package org.wineeenottt.SQL;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

public class SQLManager {
    private static final Logger logger = Logger.getLogger(SQLManager.class.getName());
    private Connection connection;

    public SQLManager() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                logger.severe("Файл db.properties не найден в ресурсах");
                return;
            }
            props.load(input);
        } catch (IOException e) {
            logger.severe("Ошибка чтения db.properties: " + e.getMessage());
            return;
        }

        String dbUrl = props.getProperty("db.url");
        String dbUser = props.getProperty("db.user");
        String dbPassword = props.getProperty("db.password");

        try {
            connection = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
            if (connection != null) {
                logger.info("Соединение с базой данных установлено");
            }
        } catch (SQLException e) {
            logger.severe("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }

    public void insertHitResult(BigDecimal x, BigDecimal y, BigDecimal r, boolean hit, double execTime, String curTime) {
        String sql = "INSERT INTO hitResult (x, y, r, hit, exec_time, cur_time) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, x.toString());
            stmt.setString(2, y.toString());
            stmt.setString(3, r.toString());
            stmt.setBoolean(4, hit);
            stmt.setDouble(5, execTime);
            stmt.setString(6, curTime);
            stmt.executeUpdate();
            logger.info("Новая строка успешно вставлена");
        } catch (SQLException e) {
            logger.severe("Ошибка вставки новой строки: " + e.getMessage());
        }
    }

    public void clearHitResult() {
        String sql = "TRUNCATE TABLE hitResult";
        try (Statement stmt = connection.createStatement()) {
            stmt.executeUpdate(sql);
            logger.info("Таблица очищена");
        } catch (SQLException e) {
            logger.severe("Ошибка очистки таблицы: " + e.getMessage());
        }
    }
}
