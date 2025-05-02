package org.example.util;

import org.example.dao.UserDao;
import org.example.model.User;

public class UserDaoTest {
    public static void main(String[] args) throws Exception {
        UserDao dao = new UserDao();
        User u = dao.register("dani", "hellow");
        System.out.println("Registered: " + u.getId() + " at " + u.getCreatedAt());
        System.out.println("Auth ok? " + dao.authenticate("bob","secretHash"));
    }
}
