package pe.edu.unmsm.fisi.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.SwingConstants;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.service.AutenticacionService;
import pe.edu.unmsm.fisi.service.AutenticacionServiceImpl;

/**
 * Formulario de acceso editable desde la pestaña Design de NetBeans.
 */
public class LoginFrame extends javax.swing.JFrame {

    private final AutenticacionService autenticacionService = new AutenticacionServiceImpl();

    public LoginFrame() {
        initComponents();
        configurarAspecto();
        getRootPane().setDefaultButton(ingresarButton);
        ingresarButton.addActionListener(this::iniciarSesion);
    }

    private void configurarAspecto() {
        setMinimumSize(new Dimension(900, 600));
        setSize(1000, 700);
        setLocationRelativeTo(null);

        rootPanel.setBackground(AppTheme.BACKGROUND);
        brandPanel.setBackground(AppTheme.NAVY);
        brandPanel.setPreferredSize(new Dimension(380, 0));
        brandPanel.setBorder(BorderFactory.createEmptyBorder(58, 45, 58, 45));
        brandContentPanel.setOpaque(false);

        siglaLabel.setForeground(new Color(188, 215, 255));
        siglaLabel.setFont(siglaLabel.getFont().deriveFont(Font.BOLD, 14f));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 31f));
        descriptionLabel.setForeground(new Color(213, 225, 241));
        descriptionLabel.setFont(descriptionLabel.getFont().deriveFont(15f));
        footerLabel.setForeground(new Color(164, 189, 222));

        rightPanel.setBackground(AppTheme.BACKGROUND);
        UiKit.styleCard(formCard);
        formCard.setPreferredSize(new Dimension(420, 440));
        formCard.setMaximumSize(new Dimension(470, 490));
        UiKit.styleTitle(welcomeLabel);
        UiKit.styleSubtitle(instructionsLabel);
        UiKit.styleFieldLabel(emailLabel);
        UiKit.styleFieldLabel(passwordLabel);
        UiKit.stylePrimary(ingresarButton);
        estadoLabel.setForeground(AppTheme.MUTED);
        estadoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        helpLabel.setForeground(AppTheme.MUTED);
        helpLabel.setFont(helpLabel.getFont().deriveFont(12f));
        helpLabel.setHorizontalAlignment(SwingConstants.CENTER);
        correoField.setPreferredSize(new Dimension(360, 40));
        passwordField.setPreferredSize(new Dimension(360, 40));
    }

    private void iniciarSesion(ActionEvent event) {
        String correo = correoField.getText();
        String password = new String(passwordField.getPassword());
        estadoLabel.setForeground(AppTheme.MUTED);
        estadoLabel.setText("Validando credenciales...");

        UiKit.async(
                this,
                ingresarButton,
                "Ingresando...",
                () -> autenticacionService.login(correo, password),
                this::procesarUsuario
        );
    }

    private void procesarUsuario(Usuario usuario) {
        if (usuario == null) {
            estadoLabel.setText("Credenciales incorrectas.");
            estadoLabel.setForeground(AppTheme.DANGER);
            passwordField.selectAll();
            passwordField.requestFocusInWindow();
            return;
        }
        estadoLabel.setText("Acceso concedido.");
        dispose();
        new DashboardFrame(usuario).setVisible(true);
    }

    /**
     * Código generado por NetBeans GUI Builder. Edite la distribución desde
     * la pestaña Design y coloque la lógica fuera de este método.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        rootPanel = new javax.swing.JPanel();
        brandPanel = new javax.swing.JPanel();
        brandContentPanel = new javax.swing.JPanel();
        siglaLabel = new javax.swing.JLabel();
        titleLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        footerLabel = new javax.swing.JLabel();
        rightPanel = new javax.swing.JPanel();
        formCard = new javax.swing.JPanel();
        welcomeLabel = new javax.swing.JLabel();
        instructionsLabel = new javax.swing.JLabel();
        emailLabel = new javax.swing.JLabel();
        correoField = new javax.swing.JTextField();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        ingresarButton = new javax.swing.JButton();
        estadoLabel = new javax.swing.JLabel();
        helpLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema de Gestión de Laboratorios");

        rootPanel.setLayout(new java.awt.BorderLayout());

        brandPanel.setLayout(new java.awt.BorderLayout());

        brandContentPanel.setOpaque(false);
        brandContentPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 18));

        siglaLabel.setText("UNMSM · FISI");
        brandContentPanel.add(siglaLabel);

        titleLabel.setText("<html>Sistema de Gestión<br>de Laboratorios</html>");
        brandContentPanel.add(titleLabel);

        descriptionLabel.setText("<html>Administra reservas, computadoras e incidencias desde una interfaz organizada por roles.</html>");
        brandContentPanel.add(descriptionLabel);

        footerLabel.setText("Arquitectura Java · Swing · MySQL");
        brandContentPanel.add(footerLabel);

        brandPanel.add(brandContentPanel, java.awt.BorderLayout.CENTER);

        rootPanel.add(brandPanel, java.awt.BorderLayout.WEST);

        rightPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(65, 70, 65, 70));
        rightPanel.setLayout(new java.awt.BorderLayout());

        formCard.setLayout(new java.awt.GridLayout(0, 1, 0, 12));

        welcomeLabel.setText("Bienvenido");
        formCard.add(welcomeLabel);

        instructionsLabel.setText("Ingrese sus credenciales registradas en MySQL.");
        formCard.add(instructionsLabel);

        emailLabel.setText("Correo institucional");
        formCard.add(emailLabel);
        formCard.add(correoField);

        passwordLabel.setText("Contraseña");
        formCard.add(passwordLabel);
        formCard.add(passwordField);

        ingresarButton.setText("Ingresar al sistema");
        formCard.add(ingresarButton);

        estadoLabel.setText(" ");
        formCard.add(estadoLabel);

        helpLabel.setText("<html><center>Configure <b>src/main/resources/db.properties</b><br>con el usuario y contraseña de MySQL.</center></html>");
        formCard.add(helpLabel);

        rightPanel.add(formCard, java.awt.BorderLayout.CENTER);

        rootPanel.add(rightPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(rootPanel, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel brandContentPanel;
    private javax.swing.JPanel brandPanel;
    private javax.swing.JTextField correoField;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JLabel emailLabel;
    private javax.swing.JLabel estadoLabel;
    private javax.swing.JLabel footerLabel;
    private javax.swing.JPanel formCard;
    private javax.swing.JLabel helpLabel;
    private javax.swing.JButton ingresarButton;
    private javax.swing.JLabel instructionsLabel;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel rootPanel;
    private javax.swing.JLabel siglaLabel;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel welcomeLabel;
    // End of variables declaration//GEN-END:variables
}
