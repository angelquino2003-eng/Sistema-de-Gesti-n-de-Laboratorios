package pe.edu.unmsm.fisi.ui;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public final class UiKit {
    private UiKit() {
    }


    public static void styleTitle(JLabel label) {
        label.setFont(label.getFont().deriveFont(Font.BOLD, 25f));
        label.setForeground(AppTheme.TEXT);
    }

    public static void styleSubtitle(JLabel label) {
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 14f));
        label.setForeground(AppTheme.MUTED);
    }

    public static void styleFieldLabel(JLabel label) {
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setForeground(AppTheme.TEXT);
    }

    public static void stylePrimary(JButton button) {
        button.setForeground(java.awt.Color.WHITE);
        button.setBackground(AppTheme.BLUE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 18, 10, 18));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 40));
    }

    public static void styleSecondary(JButton button) {
        button.setForeground(AppTheme.NAVY);
        button.setBackground(AppTheme.SURFACE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(9, 16, 9, 16)
        ));
    }

    public static void styleDanger(JButton button) {
        stylePrimary(button);
        button.setBackground(AppTheme.DANGER);
    }

    public static void styleCard(JPanel panel) {
        panel.setBackground(AppTheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));
    }

    public static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 25f));
        label.setForeground(AppTheme.TEXT);
        return label;
    }

    public static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.PLAIN, 14f));
        label.setForeground(AppTheme.MUTED);
        return label;
    }

    public static JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 13f));
        label.setForeground(AppTheme.TEXT);
        return label;
    }

    public static JButton primaryButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(java.awt.Color.WHITE);
        button.setBackground(AppTheme.BLUE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setMargin(new Insets(10, 18, 10, 18));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width, 40));
        return button;
    }

    public static JButton secondaryButton(String text) {
        JButton button = new JButton(text);
        button.setForeground(AppTheme.NAVY);
        button.setBackground(AppTheme.SURFACE);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(9, 16, 9, 16)
        ));
        return button;
    }

    public static JButton dangerButton(String text) {
        JButton button = primaryButton(text);
        button.setBackground(AppTheme.DANGER);
        return button;
    }

    public static JPanel card() {
        JPanel panel = new JPanel();
        panel.setBackground(AppTheme.SURFACE);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                new EmptyBorder(20, 20, 20, 20)
        ));
        return panel;
    }

    public static DefaultTableModel tableModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

    public static void configureTable(JTable table) {
        table.setFillsViewportHeight(true);
        table.setAutoCreateRowSorter(true);
        table.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setBackground(AppTheme.NAVY);
        table.getTableHeader().setForeground(java.awt.Color.WHITE);
        table.setRowHeight(31);
        table.setShowVerticalLines(false);
    }

    public static void info(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, message, "Operación completada", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void error(Component parent, String message) {
        JOptionPane.showMessageDialog(parent, mensajeLimpio(message), "No se pudo completar", JOptionPane.ERROR_MESSAGE);
    }

    public static String formatHour(int value) {
        return String.format("%02d:%02d", value / 100, value % 100);
    }

    public static int parseHour(Object value) {
        String text = String.valueOf(value).replace(":", "").trim();
        return Integer.parseInt(text);
    }

    public static <T> void async(
            Component parent,
            AbstractButton trigger,
            String loadingText,
            CheckedSupplier<T> task,
            Consumer<T> onSuccess
    ) {
        String originalText = trigger.getText();
        trigger.setEnabled(false);
        trigger.setText(loadingText);

        new SwingWorker<T, Void>() {
            @Override
            protected T doInBackground() throws Exception {
                return task.get();
            }

            @Override
            protected void done() {
                trigger.setEnabled(true);
                trigger.setText(originalText);
                try {
                    onSuccess.accept(get());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    error(parent, "La operación fue interrumpida.");
                } catch (ExecutionException e) {
                    Throwable cause = e.getCause() == null ? e : e.getCause();
                    error(parent, cause.getMessage());
                }
            }
        }.execute();
    }

    public static JLabel badge(String text, java.awt.Color background, java.awt.Color foreground) {
        JLabel label = new JLabel("  " + text + "  ", SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(background);
        label.setForeground(foreground);
        label.setFont(label.getFont().deriveFont(Font.BOLD, 12f));
        label.setBorder(new EmptyBorder(5, 7, 5, 7));
        return label;
    }

    private static String mensajeLimpio(String message) {
        if (message == null || message.isBlank()) {
            return "Ocurrió un error inesperado.";
        }
        return message;
    }

    @FunctionalInterface
    public interface CheckedSupplier<T> {
        T get() throws Exception;
    }
}
