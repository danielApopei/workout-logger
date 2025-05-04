package org.example.dao;

import org.example.model.WeightLog;
import org.example.util.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeightLogDao {
    public List<WeightLog> findByUser(int userId) throws Exception {
        String sql = "SELECT * FROM weight_log WHERE user_id=? ORDER BY log_date DESC";
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            List<WeightLog> list = new ArrayList<>();
            while (rs.next()) {
                list.add(new WeightLog(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDate("log_date").toLocalDate(),
                        rs.getDouble("weight"),
                        rs.getString("notes")
                ));
            }
            return list;
        }
    }

    public WeightLog insert(WeightLog w) throws Exception {
        String sql =
                "INSERT INTO weight_log(user_id,log_date,weight,notes) VALUES(?,?,?,?) RETURNING id";
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, w.getUserId());
            ps.setDate(2, java.sql.Date.valueOf(w.getLogDate()));
            ps.setDouble(3, w.getWeight());
            ps.setString(4, w.getNotes());
            ResultSet rs = ps.executeQuery();
            rs.next();
            w.setId(rs.getInt(1));
            return w;
        }
    }

    public void update(WeightLog w) throws Exception {
        String sql =
                "UPDATE weight_log SET weight=?, notes=? WHERE id=?";
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDouble(1, w.getWeight());
            ps.setString(2, w.getNotes());
            ps.setInt(3, w.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws Exception {
        try (Connection c = JdbcUtil.getConnection();
             PreparedStatement ps = c.prepareStatement("DELETE FROM weight_log WHERE id=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public WeightLog upsert(WeightLog w) throws Exception {
        if (w.getId() == 0) {
            return insert(w);
        } else {
            update(w);
            return w;
        }
    }
}
