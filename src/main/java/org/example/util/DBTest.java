package org.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

public class DBTest {
    public static void main(String[] args) {
        Properties props = new Properties();
        try (InputStream inputStream = DBTest.class.getResourceAsStream("/db.properties")) {
            if (inputStream == null) {
                System.err.println("ERROR: /db.properties missing!");
                return;
            }
            props.load(inputStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }

        String url  = props.getProperty("jdbc.url");
        String user = props.getProperty("jdbc.user");
        String pass = props.getProperty("jdbc.password");

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("PostgreSQL JDBC driver missing!");
            e.printStackTrace();
            return;
        }

        try (
                Connection conn = DriverManager.getConnection(url, user, pass);
                Statement  st   = conn.createStatement();
                ResultSet  rs   = st.executeQuery("SELECT now()")
        ) {
            if (rs.next()) {
                System.out.println("Connected! Server time is: " + rs.getTimestamp(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
