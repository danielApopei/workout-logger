package org.example;

import javax.swing.SwingUtilities;
import org.example.model.User;
import org.example.ui.LoginDialog;
import org.example.ui.MainFrame;

/**
 * Entry point: shows login dialog, then main application window for the authenticated user.
 */
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // Show login/register dialog
            LoginDialog login = new LoginDialog(null);
            User user = login.showDialog();
            if (user != null) {
                // Launch main UI
                MainFrame frame = new MainFrame(user);
                frame.setVisible(true);
            } else {
                // Exit if login was cancelled
                System.exit(0);
            }
        });
    }
}
