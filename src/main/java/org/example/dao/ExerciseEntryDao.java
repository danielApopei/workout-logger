package org.example.dao;

import org.example.model.ExerciseEntry;
import org.example.model.ExerciseType;
import org.example.util.JdbcUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExerciseEntryDao {

    public List<ExerciseEntry> findBySession(int sessionId) throws Exception {
        String sql = "SELECT * FROM exercise_entry WHERE session_id=?";
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, sessionId);
            ResultSet rs = ps.executeQuery();
            List<ExerciseEntry> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new ExerciseEntry(
                        rs.getInt("id"),
                        sessionId,
                        ExerciseType.valueOf(rs.getString("exercise_type")),
                        rs.getDouble("weight"),
                        rs.getInt("reps"),
                        rs.getInt("sets"),
                        (Integer)rs.getObject("duration_seconds")
                ));
            }
            return list;
        }
    }

    public void insert(ExerciseEntry e) throws Exception {
        String sql = """
      INSERT INTO exercise_entry
        (session_id,exercise_type,weight,reps,sets,duration_seconds)
      VALUES(?,?,?,?,?,?)
      """;
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, e.getSessionId());
            ps.setString(2, e.getType().name());
            ps.setDouble(3, e.getWeight());
            ps.setInt(4, e.getReps());
            ps.setInt(5, e.getSets());
            if (e.getDurationSeconds()==null) ps.setNull(6, Types.INTEGER);
            else ps.setInt(6, e.getDurationSeconds());
            ps.executeUpdate();
        }
    }

    public void deleteBySession(int sessionId) throws Exception {
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "DELETE FROM exercise_entry WHERE session_id=?")) {
            ps.setInt(1, sessionId);
            ps.executeUpdate();
        }
    }
}
