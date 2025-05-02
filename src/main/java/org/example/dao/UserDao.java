package org.example.dao;

import org.example.model.User;
import org.example.util.JdbcUtil;

import java.sql.*;

public class UserDao {

    public User register(String username, String passwordHash) throws Exception {
        String sql = """
            INSERT INTO app_user(username,password_hash)
            VALUES (?,?)
            RETURNING id, created_at
        """;
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return new User(
                    rs.getInt("id"),
                    username,
                    passwordHash,
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }

    public User findByUsername(String username) throws Exception {
        String sql = "SELECT * FROM app_user WHERE username = ?";
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return null;
            return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("password_hash"),
                    rs.getTimestamp("created_at").toLocalDateTime()
            );
        }
    }

    public boolean authenticate(String username, String passwordHash) throws Exception {
        User u = findByUsername(username);
        return u != null && u.getPasswordHash().equals(passwordHash);
    }
}
