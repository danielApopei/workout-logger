package org.example.dao;

import org.example.model.WorkoutSession;
import org.example.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class WorkoutSessionDao {

    public List<WorkoutSession> findByUser(int userId) throws Exception {
        String sql = "SELECT * FROM workout_session WHERE user_id=? ORDER BY session_date DESC";
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            var rs = ps.executeQuery();
            List<WorkoutSession> list = new ArrayList<>();
            while (rs.next()) {
                WorkoutSession s = new WorkoutSession(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("session_date").toLocalDateTime(),
                        rs.getString("notes")
                );
                list.add(s);
            }
            return list;
        }
    }

    public WorkoutSession insert(WorkoutSession s) throws Exception {
        String sql = """
          INSERT INTO workout_session(user_id,session_date,notes)
          VALUES(?,?,?) RETURNING id
          """;
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, s.getUserId());
            ps.setTimestamp(2, Timestamp.valueOf(s.getSessionDate()));
            ps.setString(3, s.getNotes());
            var rs = ps.executeQuery();
            rs.next();
            s.setId(rs.getInt(1));
            return s;
        }
    }

    public WorkoutSession update(WorkoutSession s) throws Exception {
        String sql = """
          UPDATE workout_session
             SET session_date = ?, notes = ?
           WHERE id = ?
          """;
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setTimestamp(1, Timestamp.valueOf(s.getSessionDate()));
            ps.setString(2, s.getNotes());
            ps.setInt(3, s.getId());
            ps.executeUpdate();
            return s;
        }
    }

    public void delete(int sessionId) throws Exception {
        String sql = "DELETE FROM workout_session WHERE id=?";
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        }
    }
}
