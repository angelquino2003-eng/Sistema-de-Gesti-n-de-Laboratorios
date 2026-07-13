package pe.edu.unmsm.fisi.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.beans.Beans;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import pe.edu.unmsm.fisi.model.entity.Administrador;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.model.enums.Rol;
import pe.edu.unmsm.fisi.ui.panel.AsignacionAlumnoPanel;
import pe.edu.unmsm.fisi.ui.panel.EquiposPanel;
import pe.edu.unmsm.fisi.ui.panel.IncidenciasAdminPanel;
import pe.edu.unmsm.fisi.ui.panel.IncidenciasTecnicoPanel;
import pe.edu.unmsm.fisi.ui.panel.InicioPanel;
import pe.edu.unmsm.fisi.ui.panel.MisReservasPanel;
import pe.edu.unmsm.fisi.ui.panel.ReportarIncidenciaPanel;
import pe.edu.unmsm.fisi.ui.panel.ReservaProfesorPanel;
import pe.edu.unmsm.fisi.ui.panel.ReservasAdminPanel;
import pe.edu.unmsm.fisi.ui.panel.UsuariosPanel;

/**
 * Ventana principal editable desde la pestaña Design de NetBeans.
 */
public class DashboardFrame extends javax.swing.JFrame {

    private Usuario usuario;
    private final Map<String, JButton> menuButtons = new LinkedHashMap<>();

    /** Constructor requerido por el GUI Builder. */
    public DashboardFrame() {
        this(new Administrador(0, "Usuario de diseño", "diseno@unmsm.edu.pe", ""));
    }

    public DashboardFrame(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        configurarAspecto();
        if (!Beans.isDesignTime()) {
            registrarVistas();
            mostrarVista("Inicio");
        } else {
            sectionTitle.setText("Vista previa del panel");
        }
    }

    private void configurarAspecto() {
        setTitle("Gestión de Laboratorios · " + usuario.getNombre());
        setMinimumSize(new Dimension(1050, 680));
        setSize(1350, 800);
        setLocationRelativeTo(null);

        rootPanel.setBackground(AppTheme.BACKGROUND);
        sidebarPanel.setBackground(AppTheme.NAVY_DARK);
        sidebarPanel.setPreferredSize(new Dimension(245, 0));
        brandPanel.setOpaque(false);
        brandPanel.setBorder(BorderFactory.createEmptyBorder(28, 22, 24, 22));
        siglaLabel.setForeground(new Color(155, 194, 247));
        siglaLabel.setFont(siglaLabel.getFont().deriveFont(Font.BOLD, 12f));
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setFont(brandLabel.getFont().deriveFont(Font.BOLD, 23f));

        menuPanel.setOpaque(false);
        menuPanel.setBorder(BorderFactory.createEmptyBorder(8, 12, 12, 12));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(16, 20, 24, 20));
        roleLabel.setText(usuario.getRol().name());
        roleLabel.setForeground(new Color(158, 190, 228));
        roleLabel.setFont(roleLabel.getFont().deriveFont(Font.BOLD, 12f));
        userLabel.setText(acortar(usuario.getNombre(), 24));
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(userLabel.getFont().deriveFont(Font.BOLD, 14f));

        mainPanel.setBackground(AppTheme.BACKGROUND);
        headerPanel.setBackground(AppTheme.SURFACE);
        headerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppTheme.BORDER),
                BorderFactory.createEmptyBorder(17, 24, 17, 24)
        ));
        sectionTitle.setFont(sectionTitle.getFont().deriveFont(Font.BOLD, 20f));
        sectionTitle.setForeground(AppTheme.TEXT);
        welcomeLabel.setText("Hola, " + primerNombre(usuario.getNombre()));
        welcomeLabel.setForeground(AppTheme.MUTED);
        UiKit.styleSecondary(logoutButton);
        logoutButton.addActionListener(e -> cerrarSesion());

        contentPanel.setBackground(AppTheme.BACKGROUND);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(22, 24, 24, 24));
    }

    private void registrarVistas() {
        menuPanel.removeAll();
        contentPanel.removeAll();
        menuButtons.clear();

        agregarVista("Inicio", new InicioPanel(usuario));
        if (usuario.getRol() == Rol.ALUMNO) {
            agregarVista("Solicitar computadora", new AsignacionAlumnoPanel(usuario));
            agregarVista("Mis reservas", new MisReservasPanel(usuario));
            agregarVista("Reportar incidencia", new ReportarIncidenciaPanel(usuario));
        } else if (usuario.getRol() == Rol.PROFESOR) {
            agregarVista("Reservar laboratorio", new ReservaProfesorPanel(usuario));
            agregarVista("Mis reservas", new MisReservasPanel(usuario));
            agregarVista("Reportar incidencia", new ReportarIncidenciaPanel(usuario));
        } else if (usuario.getRol() == Rol.ADMINISTRADOR) {
            agregarVista("Estado de equipos", new EquiposPanel());
            agregarVista("Usuarios", new UsuariosPanel());
            agregarVista("Reservas", new ReservasAdminPanel());
            agregarVista("Incidencias", new IncidenciasAdminPanel());
        } else if (usuario.getRol() == Rol.TECNICO) {
            agregarVista("Cola de incidencias", new IncidenciasTecnicoPanel());
            agregarVista("Estado de equipos", new EquiposPanel());
        }
        menuPanel.add(Box.createVerticalGlue());
        menuPanel.revalidate();
        contentPanel.revalidate();
    }

    private void agregarVista(String nombre, JPanel panel) {
        contentPanel.add(panel, nombre);
        JButton button = crearMenuButton(nombre);
        menuButtons.put(nombre, button);
        menuPanel.add(button);
        menuPanel.add(Box.createVerticalStrut(5));
    }

    private JButton crearMenuButton(String text) {
        JButton button = new JButton(text);
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        button.setPreferredSize(new Dimension(215, 44));
        button.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> mostrarVista(text));
        return button;
    }

    private void mostrarVista(String nombre) {
        ((java.awt.CardLayout) contentPanel.getLayout()).show(contentPanel, nombre);
        sectionTitle.setText(nombre);
        menuButtons.forEach((key, button) -> {
            boolean active = key.equals(nombre);
            button.setBackground(active ? AppTheme.BLUE : AppTheme.NAVY_DARK);
            button.setForeground(Color.WHITE);
            button.setFont(button.getFont().deriveFont(active ? Font.BOLD : Font.PLAIN));
        });
    }

    private void cerrarSesion() {
        dispose();
        new LoginFrame().setVisible(true);
    }

    private String primerNombre(String nombre) {
        return nombre == null || nombre.isBlank() ? "Usuario" : nombre.trim().split("\\s+")[0];
    }

    private String acortar(String text, int max) {
        if (text == null) {
            return "Usuario";
        }
        return text.length() <= max ? text : text.substring(0, max - 1) + "…";
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        sidebarPanel = new javax.swing.JPanel();
        brandPanel = new javax.swing.JPanel();
        siglaLabel = new javax.swing.JLabel();
        brandLabel = new javax.swing.JLabel();
        menuPanel = new javax.swing.JPanel();
        footerPanel = new javax.swing.JPanel();
        roleLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        headerPanel = new javax.swing.JPanel();
        sectionTitle = new javax.swing.JLabel();
        headerActionsPanel = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        contentPanel = new javax.swing.JPanel();
        designPlaceholderLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        rootPanel.setLayout(new java.awt.BorderLayout());

        sidebarPanel.setLayout(new java.awt.BorderLayout());

        brandPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        siglaLabel.setText("UNMSM · FISI");
        brandPanel.add(siglaLabel);

        brandLabel.setText("Laboratorios");
        brandPanel.add(brandLabel);

        sidebarPanel.add(brandPanel, java.awt.BorderLayout.NORTH);

        menuPanel.setLayout(new javax.swing.BoxLayout(menuPanel, javax.swing.BoxLayout.Y_AXIS));
        sidebarPanel.add(menuPanel, java.awt.BorderLayout.CENTER);

        footerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 4));

        roleLabel.setText("ADMINISTRADOR");
        footerPanel.add(roleLabel);

        userLabel.setText("Usuario de diseño");
        footerPanel.add(userLabel);

        sidebarPanel.add(footerPanel, java.awt.BorderLayout.SOUTH);

        rootPanel.add(sidebarPanel, java.awt.BorderLayout.WEST);

        mainPanel.setLayout(new java.awt.BorderLayout());

        headerPanel.setLayout(new java.awt.BorderLayout());

        sectionTitle.setText("Inicio");
        headerPanel.add(sectionTitle, java.awt.BorderLayout.WEST);

        headerActionsPanel.setOpaque(false);
        headerActionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 12, 0));

        welcomeLabel.setText("Hola, Usuario");
        headerActionsPanel.add(welcomeLabel);

        logoutButton.setText("Cerrar sesión");
        headerActionsPanel.add(logoutButton);

        headerPanel.add(headerActionsPanel, java.awt.BorderLayout.EAST);

        mainPanel.add(headerPanel, java.awt.BorderLayout.NORTH);

        contentPanel.setLayout(new java.awt.CardLayout());

        designPlaceholderLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        designPlaceholderLabel.setText("Los paneles por rol se cargan dinámicamente aquí");
        contentPanel.add(designPlaceholderLabel, "placeholder");

        mainPanel.add(contentPanel, java.awt.BorderLayout.CENTER);

        rootPanel.add(mainPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(rootPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel brandLabel;
    private javax.swing.JPanel brandPanel;
    private javax.swing.JPanel contentPanel;
    private javax.swing.JLabel designPlaceholderLabel;
    private javax.swing.JPanel footerPanel;
    private javax.swing.JPanel headerActionsPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel menuPanel;
    private javax.swing.JLabel roleLabel;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JLabel sectionTitle;
    private javax.swing.JPanel sidebarPanel;
    private javax.swing.JLabel siglaLabel;
    private javax.swing.JLabel userLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
