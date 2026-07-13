package pe.edu.unmsm.fisi.ui;

import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class AppTheme {
    public static final Color NAVY = new Color(18, 42, 74);
    public static final Color NAVY_DARK = new Color(12, 29, 51);
    public static final Color BLUE = new Color(31, 111, 235);
    public static final Color BLUE_LIGHT = new Color(226, 237, 255);
    public static final Color BACKGROUND = new Color(244, 247, 251);
    public static final Color SURFACE = Color.WHITE;
    public static final Color TEXT = new Color(31, 41, 55);
    public static final Color MUTED = new Color(100, 116, 139);
    public static final Color BORDER = new Color(218, 226, 236);
    public static final Color SUCCESS = new Color(24, 135, 84);
    public static final Color WARNING = new Color(180, 112, 0);
    public static final Color DANGER = new Color(190, 48, 48);

    private AppTheme() {
    }

    public static void install() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            System.err.println("No se pudo aplicar Nimbus: " + e.getMessage());
        }

        Font base = new Font("SansSerif", Font.PLAIN, 14);
        UIManager.put("defaultFont", base);
        UIManager.put("Label.font", base);
        UIManager.put("Button.font", base.deriveFont(Font.BOLD));
        UIManager.put("TextField.font", base);
        UIManager.put("PasswordField.font", base);
        UIManager.put("ComboBox.font", base);
        UIManager.put("Table.font", base);
        UIManager.put("TableHeader.font", base.deriveFont(Font.BOLD));
        UIManager.put("Table.rowHeight", 30);
        UIManager.put("Table.gridColor", BORDER);
        UIManager.put("Table.selectionBackground", BLUE_LIGHT);
        UIManager.put("Table.selectionForeground", TEXT);
        UIManager.put("ScrollPane.border", BorderFactory.createLineBorder(BORDER));
        UIManager.put("TextField.margin", new java.awt.Insets(7, 9, 7, 9));
        UIManager.put("PasswordField.margin", new java.awt.Insets(7, 9, 7, 9));
    }
}
