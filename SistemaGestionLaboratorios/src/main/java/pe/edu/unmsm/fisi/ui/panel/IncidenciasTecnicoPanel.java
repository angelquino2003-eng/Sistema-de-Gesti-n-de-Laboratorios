package pe.edu.unmsm.fisi.ui.panel;

import java.awt.Font;
import java.beans.Beans;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.table.DefaultTableModel;
import pe.edu.unmsm.fisi.model.entity.Incidencia;
import pe.edu.unmsm.fisi.service.IncidenciaService;
import pe.edu.unmsm.fisi.service.IncidenciaServiceImpl;
import pe.edu.unmsm.fisi.ui.AppTheme;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel FIFO del técnico editable desde Design. */
public class IncidenciasTecnicoPanel extends javax.swing.JPanel {

    private final IncidenciaService service = new IncidenciaServiceImpl();
    private DefaultTableModel model;
    private Incidencia actual;

    public IncidenciasTecnicoPanel() {
        initComponents();
        configurarPanel();
        if (!Beans.isDesignTime()) {
            cargarPendientes();
        }
    }

    private void configurarPanel() {
        setOpaque(false);
        UiKit.styleTitle(titleLabel);
        UiKit.styleSubtitle(subtitleLabel);
        UiKit.styleCard(detailPanel);
        UiKit.stylePrimary(nextButton);
        UiKit.styleSecondary(processButton);
        UiKit.stylePrimary(resolveButton);
        descriptionLabel.setForeground(AppTheme.TEXT);
        descriptionLabel.setBorder(BorderFactory.createEmptyBorder(6, 0, 6, 0));

        javax.swing.JLabel[] captions = {idTitleLabel, pcTitleLabel, typeTitleLabel, dateTitleLabel, statusTitleLabel};
        javax.swing.JLabel[] values = {idLabel, pcLabel, typeLabel, dateLabel, statusLabel};
        javax.swing.JPanel[] blocks = {idBlock, pcBlock, typeBlock, dateBlock, statusBlock};
        for (javax.swing.JLabel caption : captions) {
            UiKit.styleSubtitle(caption);
        }
        for (javax.swing.JLabel value : values) {
            value.setForeground(AppTheme.NAVY);
            value.setFont(value.getFont().deriveFont(Font.BOLD, 15f));
        }
        for (javax.swing.JPanel block : blocks) {
            block.setOpaque(false);
        }

        model = UiKit.tableModel("ID", "PC", "Usuario", "Tipo", "Fecha", "Estado", "Descripción");
        table.setModel(model);
        UiKit.configureTable(table);
        processButton.setEnabled(false);
        resolveButton.setEnabled(false);
        nextButton.addActionListener(e -> cargarSiguiente());
        processButton.addActionListener(e -> cambiarEstado("EN_PROCESO"));
        resolveButton.addActionListener(e -> cambiarEstado("RESUELTA"));
    }

    private void cargarSiguiente() {
        UiKit.async(this, nextButton, "Consultando...", service::siguienteFIFO, this::mostrarActual);
    }

    private void mostrarActual(Incidencia incidencia) {
        actual = incidencia;
        boolean existe = incidencia != null;
        processButton.setEnabled(existe);
        resolveButton.setEnabled(existe);
        if (!existe) {
            idLabel.setText("—");
            pcLabel.setText("—");
            typeLabel.setText("—");
            dateLabel.setText("—");
            statusLabel.setText("Sin pendientes");
            descriptionLabel.setText("<html>La cola FIFO no contiene incidencias con estado PENDIENTE.</html>");
            return;
        }
        idLabel.setText(String.valueOf(incidencia.getIdIncidencia()));
        pcLabel.setText(String.valueOf(incidencia.getIdComputadora()));
        typeLabel.setText(incidencia.getTipoIncidencia());
        dateLabel.setText(String.valueOf(incidencia.getFechaReporte()));
        statusLabel.setText(incidencia.getEstado());
        descriptionLabel.setText("<html><b>Descripción:</b> " + escape(incidencia.getDescripcion()) + "</html>");
    }

    private void cambiarEstado(String estado) {
        if (actual == null) {
            UiKit.error(this, "Cargue primero una incidencia pendiente.");
            return;
        }
        JButton trigger = "RESUELTA".equals(estado) ? resolveButton : processButton;
        UiKit.async(this, trigger, "Actualizando...", () -> service.actualizarEstado(actual.getIdIncidencia(), estado), ok -> {
            if (ok) {
                UiKit.info(this, "La incidencia fue actualizada a " + estado + ".");
                if ("EN_PROCESO".equals(estado)) {
                    actual.setEstado(estado);
                    mostrarActual(actual);
                } else {
                    mostrarActual(null);
                }
                cargarPendientes();
                if ("RESUELTA".equals(estado)) {
                    cargarSiguiente();
                }
            }
        });
    }

    private void cargarPendientes() {
        UiKit.async(this, nextButton, "Actualizando cola...", service::listarPendientes, this::llenarTabla);
    }

    private void llenarTabla(List<Incidencia> incidencias) {
        model.setRowCount(0);
        for (Incidencia incidencia : incidencias) {
            model.addRow(new Object[]{incidencia.getIdIncidencia(), incidencia.getIdComputadora(),
                incidencia.getIdUsuarioReporta(), incidencia.getTipoIncidencia(), incidencia.getFechaReporte(),
                incidencia.getEstado(), incidencia.getDescripcion()});
        }
    }

    private String escape(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        subtitleLabel = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        detailPanel = new javax.swing.JPanel();
        fieldsPanel = new javax.swing.JPanel();
        idBlock = new javax.swing.JPanel();
        idTitleLabel = new javax.swing.JLabel();
        idLabel = new javax.swing.JLabel();
        pcBlock = new javax.swing.JPanel();
        pcTitleLabel = new javax.swing.JLabel();
        pcLabel = new javax.swing.JLabel();
        typeBlock = new javax.swing.JPanel();
        typeTitleLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        dateBlock = new javax.swing.JPanel();
        dateTitleLabel = new javax.swing.JLabel();
        dateLabel = new javax.swing.JLabel();
        statusBlock = new javax.swing.JPanel();
        statusTitleLabel = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        actionsPanel = new javax.swing.JPanel();
        nextButton = new javax.swing.JButton();
        processButton = new javax.swing.JButton();
        resolveButton = new javax.swing.JButton();
        tableScroll = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        titleLabel.setText("Cola FIFO de incidencias");
        headerPanel.add(titleLabel);

        subtitleLabel.setText("La incidencia pendiente más antigua se presenta primero para su atención.");
        headerPanel.add(subtitleLabel);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setOpaque(false);
        centerPanel.setLayout(new java.awt.BorderLayout(0, 14));

        detailPanel.setLayout(new java.awt.BorderLayout(0, 16));

        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new java.awt.GridLayout(1, 5, 14, 0));

        idBlock.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        idTitleLabel.setText("Incidencia");
        idBlock.add(idTitleLabel);

        idLabel.setText("—");
        idBlock.add(idLabel);

        fieldsPanel.add(idBlock);

        pcBlock.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        pcTitleLabel.setText("Computadora");
        pcBlock.add(pcTitleLabel);

        pcLabel.setText("—");
        pcBlock.add(pcLabel);

        fieldsPanel.add(pcBlock);

        typeBlock.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        typeTitleLabel.setText("Tipo");
        typeBlock.add(typeTitleLabel);

        typeLabel.setText("—");
        typeBlock.add(typeLabel);

        fieldsPanel.add(typeBlock);

        dateBlock.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        dateTitleLabel.setText("Fecha de reporte");
        dateBlock.add(dateTitleLabel);

        dateLabel.setText("—");
        dateBlock.add(dateLabel);

        fieldsPanel.add(dateBlock);

        statusBlock.setLayout(new java.awt.GridLayout(0, 1, 0, 5));

        statusTitleLabel.setText("Estado");
        statusBlock.add(statusTitleLabel);

        statusLabel.setText("Sin pendientes");
        statusBlock.add(statusLabel);

        fieldsPanel.add(statusBlock);

        detailPanel.add(fieldsPanel, java.awt.BorderLayout.NORTH);

        descriptionLabel.setText("<html>La cola FIFO está vacía.</html>");
        detailPanel.add(descriptionLabel, java.awt.BorderLayout.CENTER);

        actionsPanel.setOpaque(false);
        actionsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 10, 0));

        nextButton.setText("Cargar siguiente incidencia FIFO");
        actionsPanel.add(nextButton);

        processButton.setText("Marcar EN_PROCESO");
        actionsPanel.add(processButton);

        resolveButton.setText("Marcar RESUELTA");
        actionsPanel.add(resolveButton);

        detailPanel.add(actionsPanel, java.awt.BorderLayout.SOUTH);

        centerPanel.add(detailPanel, java.awt.BorderLayout.NORTH);

        tableScroll.setViewportView(table);

        centerPanel.add(tableScroll, java.awt.BorderLayout.CENTER);

        add(centerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionsPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JPanel dateBlock;
    private javax.swing.JLabel dateLabel;
    private javax.swing.JLabel dateTitleLabel;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JPanel detailPanel;
    private javax.swing.JPanel fieldsPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JPanel idBlock;
    private javax.swing.JLabel idLabel;
    private javax.swing.JLabel idTitleLabel;
    private javax.swing.JButton nextButton;
    private javax.swing.JPanel pcBlock;
    private javax.swing.JLabel pcLabel;
    private javax.swing.JLabel pcTitleLabel;
    private javax.swing.JButton processButton;
    private javax.swing.JButton resolveButton;
    private javax.swing.JPanel statusBlock;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JLabel statusTitleLabel;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JTable table;
    private javax.swing.JScrollPane tableScroll;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JPanel typeBlock;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JLabel typeTitleLabel;
    // End of variables declaration//GEN-END:variables
}
