package pe.edu.unmsm.fisi.ui.panel;

import java.beans.Beans;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.table.DefaultTableModel;
import pe.edu.unmsm.fisi.model.entity.Computadora;
import pe.edu.unmsm.fisi.repository.EquipoRepository;
import pe.edu.unmsm.fisi.repository.EquipoRepositoryImpl;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel editable con Design para consultar y actualizar computadoras. */
public class EquiposPanel extends javax.swing.JPanel {

    private final EquipoRepository repository = new EquipoRepositoryImpl();
    private DefaultTableModel model;

    public EquiposPanel() {
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
        UiKit.styleFieldLabel(laboratorioLabel);
        UiKit.styleFieldLabel(estadoLabel);
        UiKit.stylePrimary(refreshButton);
        UiKit.styleSecondary(updateButton);
        laboratorioSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        estadoCombo.setModel(new DefaultComboBoxModel<>(new String[]{"DISPONIBLE", "OCUPADA", "MANTENIMIENTO"}));
        model = UiKit.tableModel("ID", "Código", "Estado", "Laboratorio");
        table.setModel(model);
        UiKit.configureTable(table);
        refreshButton.addActionListener(e -> cargar());
        updateButton.addActionListener(e -> actualizarEstado());
    }

    private void cargar() {
        int laboratorio = (Integer) laboratorioSpinner.getValue();
        UiKit.async(this, refreshButton, "Consultando...",
                () -> repository.listarPorLaboratorio(laboratorio), this::llenarTabla);
    }

    private void llenarTabla(List<Computadora> lista) {
        model.setRowCount(0);
        for (Computadora pc : lista) {
            model.addRow(new Object[]{pc.getIdComputadora(), pc.getCodigoPc(), pc.getEstado(), pc.getIdLaboratorio()});
        }
    }

    private void actualizarEstado() {
        int selected = table.getSelectedRow();
        if (selected < 0) {
            UiKit.error(this, "Seleccione una computadora de la tabla.");
            return;
        }
        int modelRow = table.convertRowIndexToModel(selected);
        int id = (Integer) model.getValueAt(modelRow, 0);
        String estado = String.valueOf(estadoCombo.getSelectedItem());
        UiKit.async(this, updateButton, "Actualizando...",
                () -> repository.cambiarEstado(id, estado), ok -> {
                    if (ok) {
                        UiKit.info(this, "El estado de la computadora fue actualizado.");
                        cargar();
                    }
                });
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
        refreshButton = new javax.swing.JButton();
        estadoLabel = new javax.swing.JLabel();
        estadoCombo = new javax.swing.JComboBox();
        updateButton = new javax.swing.JButton();
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        titleLabel.setText("Estado de computadoras");
        headerPanel.add(titleLabel);

        subtitleLabel.setText("Consulte el laboratorio y actualice el estado operativo de un equipo.");
        headerPanel.add(subtitleLabel);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setOpaque(false);
        centerPanel.setLayout(new java.awt.BorderLayout(0, 14));

        toolbarPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 12, 0));

        laboratorioLabel.setText("Laboratorio:");
        toolbarPanel.add(laboratorioLabel);
        toolbarPanel.add(laboratorioSpinner);

        refreshButton.setText("Consultar");
        toolbarPanel.add(refreshButton);

        estadoLabel.setText("Nuevo estado:");
        toolbarPanel.add(estadoLabel);
        toolbarPanel.add(estadoCombo);

        updateButton.setText("Cambiar estado seleccionado");
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
    private javax.swing.JLabel laboratorioLabel;
    private javax.swing.JSpinner laboratorioSpinner;
    private javax.swing.JButton refreshButton;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel toolbarPanel;
    private javax.swing.JButton updateButton;
    // End of variables declaration//GEN-END:variables
}
