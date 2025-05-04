package org.example;

import javax.swing.SwingUtilities;
import org.example.model.User;
import org.example.ui.LoginDialog;
import org.example.ui.MainFrame;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginDialog login = new LoginDialog(null);
            User user = login.showDialog();
            if (user != null) {
                MainFrame frame = new MainFrame(user);
                frame.setVisible(true);
            } else {
                System.exit(0);
            }
        });
    }
}
