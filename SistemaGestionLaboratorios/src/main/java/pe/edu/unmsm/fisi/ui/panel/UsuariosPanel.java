package pe.edu.unmsm.fisi.ui.panel;

import java.beans.Beans;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import pe.edu.unmsm.fisi.model.entity.Alumno;
import pe.edu.unmsm.fisi.model.entity.Profesor;
import pe.edu.unmsm.fisi.model.entity.Tecnico;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.repository.UsuarioRepository;
import pe.edu.unmsm.fisi.repository.UsuarioRepositoryImpl;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel de usuarios editable con Design. */
public class UsuariosPanel extends javax.swing.JPanel {

    private final UsuarioRepository repository = new UsuarioRepositoryImpl();
    private DefaultTableModel model;

    public UsuariosPanel() {
        initComponents();
        configurarPanel();
        if (!Beans.isDesignTime()) {
            cargar();
        }
    }

    private void configurarPanel() {
        setOpaque(false);
        titlesPanel.setOpaque(false);
        actionsPanel.setOpaque(false);
        UiKit.styleTitle(titleLabel);
        UiKit.styleSubtitle(subtitleLabel);
        UiKit.stylePrimary(refreshButton);
        model = UiKit.tableModel("ID", "Nombre", "Correo", "Rol", "Información específica");
        table.setModel(model);
        UiKit.configureTable(table);
        refreshButton.addActionListener(e -> cargar());
    }

    private void cargar() {
        UiKit.async(this, refreshButton, "Consultando...", repository::listarTodos, this::llenar);
    }

    private void llenar(List<Usuario> usuarios) {
        model.setRowCount(0);
        for (Usuario usuario : usuarios) {
            model.addRow(new Object[]{usuario.getIdUsuario(), usuario.getNombre(), usuario.getCorreo(),
                usuario.getRol(), detalle(usuario)});
        }
    }

    private String detalle(Usuario usuario) {
        if (usuario instanceof Alumno alumno) {
            return "Código: " + valor(alumno.getCodigoAlumno()) + " · Límite: " + alumno.getTiempoLimiteMinutos() + " min";
        }
        if (usuario instanceof Profesor profesor) {
            return "Departamento: " + valor(profesor.getDepartamentoAcademico());
        }
        if (usuario instanceof Tecnico tecnico) {
            return "Especialidad: " + valor(tecnico.getEspecialidad());
        }
        return "Administración del sistema";
    }

    private String valor(String text) {
        return text == null || text.isBlank() ? "No registrado" : text;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        titlesPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        subtitleLabel = new javax.swing.JLabel();
        actionsPanel = new javax.swing.JPanel();
        refreshButton = new javax.swing.JButton();
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.BorderLayout());

        titlesPanel.setOpaque(false);
        titlesPanel.setLayout(new java.awt.BorderLayout());

        titleLabel.setText("Usuarios registrados");
        titlesPanel.add(titleLabel, java.awt.BorderLayout.NORTH);

        subtitleLabel.setText("Listado general de alumnos, profesores, administradores y técnicos.");
        titlesPanel.add(subtitleLabel, java.awt.BorderLayout.SOUTH);

        headerPanel.add(titlesPanel, java.awt.BorderLayout.WEST);

        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        refreshButton.setText("Actualizar usuarios");
        actionsPanel.add(refreshButton);

        headerPanel.add(actionsPanel, java.awt.BorderLayout.EAST);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        tableScroll.setViewportView(table);

        add(tableScroll, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel titlesPanel;
    // End of variables declaration//GEN-END:variables
}
