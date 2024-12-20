package se.gustav.databasProjekt;

import java.io.*;
import java.sql.*;
import java.util.Properties;

public class JDBCUtil {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = JDBCUtil.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Could not find application.properties");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties file.", e);
        }
    }



    public static Connection getConnection() {
        String url = PROPERTIES.getProperty("db.url");
        String user = PROPERTIES.getProperty("db.user");
        String password = PROPERTIES.getProperty("db.password");

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();  // Detta ger mer detaljerad felinformation
            throw new RuntimeException("Could not establish database connection", e);
        }
    }

    public static String getDatabaseProductName(Connection conn) {
        try {
            if (conn == null) {
                throw new IllegalArgumentException("Connection cannot be null.");
            }
            return conn.getMetaData().getDatabaseProductName();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch database product name.", e);
        }
    }

}