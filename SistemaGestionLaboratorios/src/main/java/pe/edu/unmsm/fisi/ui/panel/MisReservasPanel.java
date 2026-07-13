package pe.edu.unmsm.fisi.ui.panel;

import java.beans.Beans;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import pe.edu.unmsm.fisi.model.entity.Alumno;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.repository.ReservaRepository;
import pe.edu.unmsm.fisi.repository.ReservaRepositoryImpl;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel de reservas del usuario editable con Design. */
public class MisReservasPanel extends javax.swing.JPanel {

    private Usuario usuario;
    private final ReservaRepository repository = new ReservaRepositoryImpl();
    private DefaultTableModel model;

    public MisReservasPanel() {
        this(new Alumno(0, "Alumno de diseño", "alumno@unmsm.edu.pe", "", "00000000", 120));
    }

    public MisReservasPanel(Usuario usuario) {
        this.usuario = usuario;
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
        model = UiKit.tableModel("ID", "Fecha", "Inicio", "Fin", "Tipo", "Laboratorio", "PC", "Curso", "Estado");
        table.setModel(model);
        UiKit.configureTable(table);
        refreshButton.addActionListener(e -> cargar());
    }

    private void cargar() {
        UiKit.async(this, refreshButton, "Consultando...",
                () -> repository.findReservasActivasPorUsuario(usuario.getIdUsuario()),
                this::llenarTabla);
    }

    private void llenarTabla(List<Reserva> reservas) {
        model.setRowCount(0);
        for (Reserva reserva : reservas) {
            
            // Variables por defecto para armar la fila
            String tipoReserva = "DESCONOCIDO";
            String pcAsignada = "Laboratorio completo";
            String curso = "-";
            
            // Evaluamos polimórficamente qué tipo de reserva es
            switch (reserva) {
                case pe.edu.unmsm.fisi.model.entity.ReservaComputadora rc -> {
                    tipoReserva = "COMPUTADORA";
                    pcAsignada = String.valueOf(rc.getIdComputadora());
                    curso = rc.getRequerimientoSoftware(); // Mostramos el software que pidió el alumno
                }
                case pe.edu.unmsm.fisi.model.entity.ReservaLaboratorio rl -> {
                    tipoReserva = "LABORATORIO";
                    pcAsignada = "Laboratorio completo";
                    curso = rl.getCursoAcademico();
                }
                default -> {
                }
            }

            // Agregamos la fila a la tabla (JTable) con los datos limpios
            model.addRow(new Object[]{
                reserva.getIdReserva(), 
                reserva.getFecha(), 
                UiKit.formatHour(reserva.getHoraInicio()),
                UiKit.formatHour(reserva.getHoraFin()), 
                tipoReserva, 
                reserva.getIdLaboratorio(),
                pcAsignada,
                curso, 
                reserva.getEstado().name() // Extraemos el texto del Enum
            });
        }
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

        titleLabel.setText("Mis reservas activas");
        titlesPanel.add(titleLabel, java.awt.BorderLayout.NORTH);

        subtitleLabel.setText("Reservas con estado ACTIVA o APROBADA asociadas a su cuenta.");
        titlesPanel.add(subtitleLabel, java.awt.BorderLayout.SOUTH);

        headerPanel.add(titlesPanel, java.awt.BorderLayout.WEST);

        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 0, 0));

        refreshButton.setText("Actualizar");
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
