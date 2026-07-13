package pe.edu.unmsm.fisi.ui.panel;

import java.beans.Beans;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.table.DefaultTableModel;
import pe.edu.unmsm.fisi.model.entity.Incidencia;
import pe.edu.unmsm.fisi.service.IncidenciaService;
import pe.edu.unmsm.fisi.service.IncidenciaServiceImpl;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel de administración de incidencias editable con Design. */
public class IncidenciasAdminPanel extends javax.swing.JPanel {

    private final IncidenciaService service = new IncidenciaServiceImpl();
    private DefaultTableModel model;

    public IncidenciasAdminPanel() {
        initComponents();
        configurarPanel();
        if (!Beans.isDesignTime()) {
            cargar();
        }
    }

    private void configurarPanel() {
        setOpaque(false);
        UiKit.styleTitle(titleLabel);
        UiKit.styleSubtitle(subtitleLabel);
        UiKit.styleCard(toolbarPanel);
        UiKit.styleFieldLabel(estadoLabel);
        UiKit.stylePrimary(refreshButton);
        UiKit.styleSecondary(updateButton);
        estadoCombo.setModel(new DefaultComboBoxModel<>(new String[]{"PENDIENTE", "EN_PROCESO", "RESUELTA", "CANCELADA"}));
        model = UiKit.tableModel("ID", "Computadora", "Reportado por", "Tipo", "Fecha", "Estado", "Descripción");
        table.setModel(model);
        UiKit.configureTable(table);
        refreshButton.addActionListener(e -> cargar());
        updateButton.addActionListener(e -> actualizar());
    }

    private void cargar() {
        UiKit.async(this, refreshButton, "Consultando...", service::listarTodas, this::llenar);
    }

    private void actualizar() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            UiKit.error(this, "Seleccione una incidencia de la tabla.");
            return;
        }
        int row = table.convertRowIndexToModel(selected);
        int id = (Integer) model.getValueAt(row, 0);
        String estado = String.valueOf(estadoCombo.getSelectedItem());
        UiKit.async(this, updateButton, "Actualizando...", () -> service.actualizarEstado(id, estado), ok -> {
            if (ok) {
                UiKit.info(this, "El estado de la incidencia fue actualizado.");
                cargar();
            }
        });
    }

    private void llenar(List<Incidencia> incidencias) {
        model.setRowCount(0);
        for (Incidencia incidencia : incidencias) {
            model.addRow(new Object[]{incidencia.getIdIncidencia(), incidencia.getIdComputadora(),
                incidencia.getIdUsuarioReporta(), incidencia.getTipoIncidencia(), incidencia.getFechaReporte(),
                incidencia.getEstado(), incidencia.getDescripcion()});
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
        refreshButton = new javax.swing.JButton();
        estadoLabel = new javax.swing.JLabel();
        estadoCombo = new javax.swing.JComboBox();
        updateButton = new javax.swing.JButton();
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        titleLabel.setText("Supervisión de incidencias");
        headerPanel.add(titleLabel);

        subtitleLabel.setText("Historial general de reportes técnicos y su estado actual.");
        headerPanel.add(subtitleLabel);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setOpaque(false);
        centerPanel.setLayout(new java.awt.BorderLayout(0, 14));

        toolbarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        refreshButton.setText("Actualizar listado");
        toolbarPanel.add(refreshButton);

        estadoLabel.setText("Nuevo estado:");
        toolbarPanel.add(estadoLabel);
        toolbarPanel.add(estadoCombo);

        updateButton.setText("Cambiar estado");
        toolbarPanel.add(updateButton);

        centerPanel.add(toolbarPanel, java.awt.BorderLayout.NORTH);

        tableScroll.setViewportView(table);

        centerPanel.add(tableScroll, java.awt.BorderLayout.CENTER);

        add(centerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel centerPanel;
    private javax.swing.JComboBox estadoCombo;
    private javax.swing.JLabel estadoLabel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JButton refreshButton;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel toolbarPanel;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
