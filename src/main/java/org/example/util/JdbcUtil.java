package org.example.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public class JdbcUtil {
    private static final String URL;
    private static final String USER;
    private static final String PASS;

    static {
        try {
            Properties p = new Properties();
            try (InputStream in = JdbcUtil.class.getResourceAsStream("/db.properties")) {
                if (in == null) throw new RuntimeException("db.properties not found");
                p.load(in);
            }
            URL  = p.getProperty("jdbc.url");
            USER = p.getProperty("jdbc.user");
            PASS = p.getProperty("jdbc.password");
            Class.forName("org.postgresql.Driver");
        } catch (Exception e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Connection getConnection() throws Exception {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
