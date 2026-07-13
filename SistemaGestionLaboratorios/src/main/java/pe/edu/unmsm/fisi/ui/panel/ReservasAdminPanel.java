package pe.edu.unmsm.fisi.ui.panel;

import java.beans.Beans;
import java.time.LocalDate;
import java.util.List;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.ReservaComputadora;
import pe.edu.unmsm.fisi.model.entity.ReservaLaboratorio;
import pe.edu.unmsm.fisi.model.enums.EstadoEquipo;
import pe.edu.unmsm.fisi.model.enums.EstadoReserva;
import pe.edu.unmsm.fisi.repository.EquipoRepository;
import pe.edu.unmsm.fisi.repository.EquipoRepositoryImpl;
import pe.edu.unmsm.fisi.repository.ReservaRepository;
import pe.edu.unmsm.fisi.repository.ReservaRepositoryImpl;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel administrativo de reservas editable desde Design. */
public class ReservasAdminPanel extends javax.swing.JPanel {

    private final ReservaRepository repository = new ReservaRepositoryImpl();
    private final EquipoRepository equipoRepository = new EquipoRepositoryImpl();
    private DefaultTableModel model;

    public ReservasAdminPanel() {
        initComponents();
        configurarPanel();
        if (!Beans.isDesignTime()) {
            cargarTodas();
        }
    }

    private void configurarPanel() {
        setOpaque(false);
        UiKit.styleTitle(titleLabel);
        UiKit.styleSubtitle(subtitleLabel);
        UiKit.styleCard(toolbarPanel);
        UiKit.styleFieldLabel(laboratorioLabel);
        UiKit.styleFieldLabel(fechaLabel);
        UiKit.stylePrimary(filterButton);
        UiKit.styleSecondary(allButton);
        UiKit.styleDanger(cancelButton);
        laboratorioSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        fechaField.setText(LocalDate.now().toString());
        fechaField.setColumns(10);
        model = UiKit.tableModel("ID", "Usuario", "Tipo", "Fecha", "Inicio", "Fin", "Estado", "Laboratorio", "PC", "Curso");
        table.setModel(model);
        UiKit.configureTable(table);
        filterButton.addActionListener(e -> filtrar());
        allButton.addActionListener(e -> cargarTodas());
        cancelButton.addActionListener(e -> cancelar());
    }

    private void filtrar() {
        UiKit.async(this, filterButton, "Consultando...", () -> repository.findReservasPorLaboratorio(
                (Integer) laboratorioSpinner.getValue(), LocalDate.parse(fechaField.getText().trim())
        ), this::llenar);
    }

    private void cargarTodas() {
        UiKit.async(this, allButton, "Consultando...", repository::findTodas, this::llenar);
    }

    private void cancelar() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            UiKit.error(this, "Seleccione una reserva de la tabla.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(selected);
        int id = (Integer) model.getValueAt(modelRow, 0);
        Object pcValue = model.getValueAt(modelRow, 8);
        UiKit.async(this, cancelButton, "Cancelando...", () -> {
        boolean ok = repository.actualizarEstado(id, EstadoReserva.CANCELADA);
        if (ok && pcValue instanceof Integer idComputadora) {
        equipoRepository.cambiarEstado(idComputadora, EstadoEquipo.DISPONIBLE.name());
        }
        return ok;
        }, ok -> {
            if (ok) {
                UiKit.info(this, "La reserva fue marcada como CANCELADA y la computadora asociada fue liberada.");
                cargarTodas();
            }
        });
    }

    private void llenar(List<Reserva> reservas) {
        model.setRowCount(0);
        for (Reserva reserva : reservas) {
            
            String tipoReserva = "DESCONOCIDO";
            String pcAsignada = "-";
            String curso = "-";
            
            // Downcasting seguro
            switch (reserva) {
                case ReservaComputadora rc -> {
                    tipoReserva = "COMPUTADORA";
                    pcAsignada = String.valueOf(rc.getIdComputadora());
                    curso = rc.getRequerimientoSoftware();
                }
                case ReservaLaboratorio rl -> {
                    tipoReserva = "LABORATORIO";
                    pcAsignada = "-";
                    curso = rl.getCursoAcademico();
                }
                default -> {
                }
            }

            // Llenado respetando el orden exacto de las columnas de Rodrigo
            model.addRow(new Object[]{
                reserva.getIdReserva(), 
                reserva.getIdUsuario(), 
                tipoReserva,
                reserva.getFecha(), 
                UiKit.formatHour(reserva.getHoraInicio()), 
                UiKit.formatHour(reserva.getHoraFin()),
                reserva.getEstado().name(), // Convertimos el Enum a String para la tabla
                reserva.getIdLaboratorio(),
                pcAsignada, 
                curso
            });
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        subtitleLabel = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        toolbarPanel = new javax.swing.JPanel();
        laboratorioLabel = new javax.swing.JLabel();
        laboratorioSpinner = new javax.swing.JSpinner();
        fechaLabel = new javax.swing.JLabel();
        fechaField = new javax.swing.JTextField();
        filterButton = new javax.swing.JButton();
        allButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        titleLabel.setText("Supervisión de reservas");
        headerPanel.add(titleLabel);

        subtitleLabel.setText("Consulte reservas por fecha o revise el historial completo del sistema.");
        headerPanel.add(subtitleLabel);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setOpaque(false);
        centerPanel.setLayout(new java.awt.BorderLayout(0, 14));

        toolbarPanel.setMinimumSize(new java.awt.Dimension(779, 50));
        toolbarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        laboratorioLabel.setText("Laboratorio:");
        toolbarPanel.add(laboratorioLabel);
        toolbarPanel.add(laboratorioSpinner);

        fechaLabel.setText("Fecha:");
        toolbarPanel.add(fechaLabel);
        toolbarPanel.add(fechaField);

        filterButton.setText("Filtrar por laboratorio y fecha");
        toolbarPanel.add(filterButton);

        allButton.setText("Mostrar todas");
        toolbarPanel.add(allButton);

        cancelButton.setText("Cancelar reserva seleccionada");
        toolbarPanel.add(cancelButton);

        centerPanel.add(toolbarPanel, java.awt.BorderLayout.PAGE_START);

        tableScroll.setViewportView(table);

        centerPanel.add(tableScroll, java.awt.BorderLayout.CENTER);

        add(centerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton allButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JTextField fechaField;
    private javax.swing.JLabel fechaLabel;
    private javax.swing.JButton filterButton;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel laboratorioLabel;
    private javax.swing.JSpinner laboratorioSpinner;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel toolbarPanel;
    // End of variables declaration//GEN-END:variables
}
