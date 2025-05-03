package org.example.ui;

import org.example.dao.UserDao;
import org.example.model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginDialog extends JDialog {
    private JTextField userField = new JTextField(15);
    private JPasswordField passField = new JPasswordField(15);
    private JButton loginBtn = new JButton("Login");
    private JButton registerBtn = new JButton("Register");
    private User authenticatedUser;

    public LoginDialog(Frame owner) {
        super(owner, "Login", true);
        setLayout(new GridBagLayout());
        GridBagConstraints gc = new GridBagConstraints();
        gc.insets = new Insets(5,5,5,5);
        gc.gridx = 0; gc.gridy = 0; add(new JLabel("Username:"), gc);
        gc.gridx = 1; add(userField, gc);
        gc.gridx = 0; gc.gridy = 1; add(new JLabel("Password:"), gc);
        gc.gridx = 1; add(passField, gc);

        JPanel btns = new JPanel();
        btns.add(loginBtn);
        btns.add(registerBtn);
        gc.gridx = 0; gc.gridy = 2; gc.gridwidth = 2;
        add(btns, gc);

        loginBtn.addActionListener(this::onLogin);
        registerBtn.addActionListener(this::onRegister);

        pack();
        setLocationRelativeTo(owner);
    }

    private void onLogin(ActionEvent e) {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        try {
            UserDao dao = new UserDao();
            if (dao.authenticate(u, p)) {
                authenticatedUser = dao.findByUsername(u);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void onRegister(ActionEvent e) {
        String u = userField.getText().trim();
        String p = new String(passField.getPassword());
        try {
            UserDao dao = new UserDao();
            authenticatedUser = dao.register(u, p);
            dispose();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public User showDialog() {
        setVisible(true);
        return authenticatedUser;
    }
}
