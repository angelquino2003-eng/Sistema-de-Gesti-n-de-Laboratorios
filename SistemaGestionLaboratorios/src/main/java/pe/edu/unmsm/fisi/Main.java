package pe.edu.unmsm.fisi;

import javax.swing.SwingUtilities;
import pe.edu.unmsm.fisi.ui.AppTheme;
import pe.edu.unmsm.fisi.ui.LoginFrame;

public class Main {
    public static void main(String[] args) {
        AppTheme.install();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
