package pe.edu.unmsm.fisi.ui.panel;

import java.awt.Font;
import java.beans.Beans;
import pe.edu.unmsm.fisi.config.ConexionBD;
import pe.edu.unmsm.fisi.model.entity.Administrador;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.model.enums.Rol;
import pe.edu.unmsm.fisi.ui.AppTheme;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel de inicio editable con Design. */
public class InicioPanel extends javax.swing.JPanel {

    private Usuario usuario;

    public InicioPanel() {
        this(new Administrador(0, "Usuario de diseño", "diseno@unmsm.edu.pe", ""));
    }

    public InicioPanel(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        configurarPanel();
    }

    private void configurarPanel() {
        setOpaque(false);
        UiKit.styleTitle(titleLabel);
        UiKit.styleSubtitle(subtitleLabel);
        UiKit.styleCard(userCard);
        UiKit.styleCard(roleCard);
        UiKit.styleCard(connectionCard);
        UiKit.styleCard(helpPanel);

        UiKit.styleSubtitle(userCardTitle);
        UiKit.styleSubtitle(roleCardTitle);
        UiKit.styleSubtitle(connectionCardTitle);
        userNameLabel.setFont(userNameLabel.getFont().deriveFont(Font.BOLD, 20f));
        userNameLabel.setForeground(AppTheme.NAVY);
        roleValueLabel.setFont(roleValueLabel.getFont().deriveFont(Font.BOLD, 20f));
        roleValueLabel.setForeground(AppTheme.NAVY);
        connectionValueLabel.setFont(connectionValueLabel.getFont().deriveFont(Font.BOLD, 20f));
        connectionValueLabel.setForeground(AppTheme.NAVY);
        userEmailLabel.setForeground(AppTheme.MUTED);
        roleDetailLabel.setForeground(AppTheme.MUTED);
        connectionDetailLabel.setForeground(AppTheme.MUTED);
        helpTitleLabel.setFont(helpTitleLabel.getFont().deriveFont(Font.BOLD, 17f));
        helpTitleLabel.setForeground(AppTheme.TEXT);
        helpTextLabel.setForeground(AppTheme.MUTED);

        titleLabel.setText("Panel principal");
        subtitleLabel.setText(descripcionRol(usuario.getRol()));
        userNameLabel.setText(valor(usuario.getNombre()));
        userEmailLabel.setText(valor(usuario.getCorreo()));
        roleValueLabel.setText(usuario.getRol().name());

        if (Beans.isDesignTime()) {
            connectionValueLabel.setText("Vista de diseño");
            connectionDetailLabel.setText("La conexión no se prueba dentro del editor visual.");
        } else {
            boolean conectado = ConexionBD.getInstance().probarConexion();
            connectionValueLabel.setText(conectado ? "Conectada" : "Sin conexión");
            connectionValueLabel.setForeground(conectado ? AppTheme.SUCCESS : AppTheme.DANGER);
            connectionDetailLabel.setText("<html>" + ConexionBD.getInstance().getUrl() + "</html>");
        }
    }

    private String descripcionRol(Rol rol) {
        return switch (rol) {
            case ALUMNO -> "Solicite una computadora, revise sus reservas y reporte fallas técnicas.";
            case PROFESOR -> "Reserve laboratorios sin cruces de horario y consulte sus reservas activas.";
            case ADMINISTRADOR -> "Supervise usuarios, equipos, reservas e incidencias del sistema.";
            case TECNICO -> "Atienda incidencias en orden FIFO y actualice el estado de las computadoras.";
        };
    }

    private String valor(String text) {
        return text == null || text.isBlank() ? "—" : text;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        subtitleLabel = new javax.swing.JLabel();
        cardsPanel = new javax.swing.JPanel();
        userCard = new javax.swing.JPanel();
        userCardTitle = new javax.swing.JLabel();
        userNameLabel = new javax.swing.JLabel();
        userEmailLabel = new javax.swing.JLabel();
        roleCard = new javax.swing.JPanel();
        roleCardTitle = new javax.swing.JLabel();
        roleValueLabel = new javax.swing.JLabel();
        roleDetailLabel = new javax.swing.JLabel();
        connectionCard = new javax.swing.JPanel();
        connectionCardTitle = new javax.swing.JLabel();
        connectionValueLabel = new javax.swing.JLabel();
        connectionDetailLabel = new javax.swing.JLabel();
        helpPanel = new javax.swing.JPanel();
        helpTitleLabel = new javax.swing.JLabel();
        helpTextLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        titleLabel.setText("Panel principal");
        headerPanel.add(titleLabel);

        subtitleLabel.setText("Descripción del rol activo.");
        headerPanel.add(subtitleLabel);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        cardsPanel.setOpaque(false);
        cardsPanel.setLayout(new java.awt.GridLayout(1, 3, 18, 0));

        userCard.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        userCardTitle.setText("Usuario activo");
        userCard.add(userCardTitle);

        userNameLabel.setText("Usuario de diseño");
        userCard.add(userNameLabel);

        userEmailLabel.setText("diseno@unmsm.edu.pe");
        userCard.add(userEmailLabel);

        cardsPanel.add(userCard);

        roleCard.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        roleCardTitle.setText("Rol asignado");
        roleCard.add(roleCardTitle);

        roleValueLabel.setText("ADMINISTRADOR");
        roleCard.add(roleValueLabel);

        roleDetailLabel.setText("Las opciones visibles dependen de este rol.");
        roleCard.add(roleDetailLabel);

        cardsPanel.add(roleCard);

        connectionCard.setLayout(new java.awt.GridLayout(0, 1, 0, 10));

        connectionCardTitle.setText("Conexión MySQL");
        connectionCard.add(connectionCardTitle);

        connectionValueLabel.setText("Configurada");
        connectionCard.add(connectionValueLabel);

        connectionDetailLabel.setText("gestion_laboratorios");
        connectionCard.add(connectionDetailLabel);

        cardsPanel.add(connectionCard);

        add(cardsPanel, java.awt.BorderLayout.CENTER);

        helpPanel.setLayout(new java.awt.BorderLayout());

        helpTitleLabel.setText("Flujo recomendado");
        helpPanel.add(helpTitleLabel, java.awt.BorderLayout.NORTH);

        helpTextLabel.setText("<html>Use el menú lateral para acceder a las operaciones permitidas. Las consultas y registros se ejecutan directamente sobre la base de datos <b>gestion_laboratorios</b>.</html>");
        helpPanel.add(helpTextLabel, java.awt.BorderLayout.CENTER);

        add(helpPanel, java.awt.BorderLayout.SOUTH);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel cardsPanel;
    private javax.swing.JPanel connectionCard;
    private javax.swing.JLabel connectionCardTitle;
    private javax.swing.JLabel connectionDetailLabel;
    private javax.swing.JLabel connectionValueLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel helpPanel;
    private javax.swing.JLabel helpTextLabel;
    private javax.swing.JLabel helpTitleLabel;
    private javax.swing.JPanel roleCard;
    private javax.swing.JLabel roleCardTitle;
    private javax.swing.JLabel roleDetailLabel;
    private javax.swing.JLabel roleValueLabel;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel userCard;
    private javax.swing.JLabel userCardTitle;
    private javax.swing.JLabel userEmailLabel;
    private javax.swing.JLabel userNameLabel;
    // End of variables declaration//GEN-END:variables
}
