package pe.edu.unmsm.fisi.ui.panel;

import java.awt.Component;
import java.beans.Beans;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;
import javax.swing.SpinnerNumberModel;
import pe.edu.unmsm.fisi.model.entity.Alumno;
import pe.edu.unmsm.fisi.model.entity.Computadora;
import pe.edu.unmsm.fisi.model.entity.Incidencia;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.repository.EquipoRepository;
import pe.edu.unmsm.fisi.repository.EquipoRepositoryImpl;
import pe.edu.unmsm.fisi.service.IncidenciaService;
import pe.edu.unmsm.fisi.service.IncidenciaServiceImpl;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel para registrar incidencias, editable desde Design. */
public class ReportarIncidenciaPanel extends javax.swing.JPanel {

    private Usuario usuario;
    private final EquipoRepository equipoRepository = new EquipoRepositoryImpl();
    private final IncidenciaService incidenciaService = new IncidenciaServiceImpl();

    public ReportarIncidenciaPanel() {
        this(new Alumno(0, "Alumno de diseño", "alumno@unmsm.edu.pe", "", "00000000", 120));
    }

    public ReportarIncidenciaPanel(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        configurarPanel();
        if (!Beans.isDesignTime()) {
            cargarComputadoras();
        }
    }

    private void configurarPanel() {
        setOpaque(false);
        UiKit.styleTitle(titleLabel);
        UiKit.styleSubtitle(subtitleLabel);
        UiKit.styleCard(formPanel);
        UiKit.styleFieldLabel(laboratorioLabel);
        UiKit.styleFieldLabel(computadoraLabel);
        UiKit.styleFieldLabel(tipoLabel);
        UiKit.styleFieldLabel(descripcionLabel);
        UiKit.styleSecondary(loadButton);
        UiKit.stylePrimary(saveButton);
        pcRowPanel.setOpaque(false);

        laboratorioSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        tipoCombo.setModel(new DefaultComboBoxModel<>(new String[]{"HARDWARE", "SOFTWARE", "RED", "PERIFERICO", "OTRO"}));
        descripcionArea.setRows(7);
        descripcionArea.setColumns(30);
        descripcionArea.setLineWrap(true);
        descripcionArea.setWrapStyleWord(true);
        computadoraCombo.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Computadora pc) {
                    setText(pc.getCodigoPc() + " · " + pc.getEstado() + " · ID " + pc.getIdComputadora());
                } else {
                    setText("Seleccione una computadora");
                }
                return this;
            }
        });
        loadButton.addActionListener(e -> cargarComputadoras());
        saveButton.addActionListener(e -> guardar());
    }

    private void cargarComputadoras() {
        int laboratorio = (Integer) laboratorioSpinner.getValue();
        UiKit.async(this, loadButton, "Cargando...",
                () -> equipoRepository.listarPorLaboratorio(laboratorio), this::actualizarCombo);
    }

    private void actualizarCombo(List<Computadora> computadoras) {
        computadoraCombo.setModel(new DefaultComboBoxModel<>(computadoras.toArray(Computadora[]::new)));
    }

    private void guardar() {
        Computadora pc = (Computadora) computadoraCombo.getSelectedItem();
        UiKit.async(this, saveButton, "Registrando...", () -> incidenciaService.registrar(
                usuario,
                pc == null ? 0 : pc.getIdComputadora(),
                String.valueOf(tipoCombo.getSelectedItem()),
                descripcionArea.getText()
        ), this::completado);
    }

    private void completado(Incidencia incidencia) {
        descripcionArea.setText("");
        UiKit.info(this, "Incidencia registrada en la cola FIFO con estado PENDIENTE.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        subtitleLabel = new javax.swing.JLabel();
        formPanel = new javax.swing.JPanel();
        laboratorioLabel = new javax.swing.JLabel();
        laboratorioSpinner = new javax.swing.JSpinner();
        computadoraLabel = new javax.swing.JLabel();
        pcRowPanel = new javax.swing.JPanel();
        computadoraCombo = new javax.swing.JComboBox<>();
        loadButton = new javax.swing.JButton();
        tipoLabel = new javax.swing.JLabel();
        tipoCombo = new javax.swing.JComboBox<>();
        descripcionLabel = new javax.swing.JLabel();
        descripcionScroll = new javax.swing.JScrollPane();
        descripcionArea = new javax.swing.JTextArea();
        buttonSpacerLabel = new javax.swing.JLabel();
        saveButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 20));
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));
        titleLabel.setText("Reportar incidencia técnica");
        headerPanel.add(titleLabel);
        subtitleLabel.setText("El reporte se almacenará con estado PENDIENTE para su atención por un técnico.");
        headerPanel.add(subtitleLabel);
        add(headerPanel, java.awt.BorderLayout.NORTH);

        formPanel.setLayout(new java.awt.GridLayout(0, 2, 20, 14));
        laboratorioLabel.setText("Laboratorio");
        formPanel.add(laboratorioLabel);
        formPanel.add(laboratorioSpinner);
        computadoraLabel.setText("Computadora");
        formPanel.add(computadoraLabel);
        pcRowPanel.setLayout(new java.awt.BorderLayout(10, 0));
        pcRowPanel.add(computadoraCombo, java.awt.BorderLayout.CENTER);
        loadButton.setText("Cargar computadoras");
        pcRowPanel.add(loadButton, java.awt.BorderLayout.EAST);
        formPanel.add(pcRowPanel);
        tipoLabel.setText("Tipo de incidencia");
        formPanel.add(tipoLabel);
        formPanel.add(tipoCombo);
        descripcionLabel.setText("Descripción");
        formPanel.add(descripcionLabel);
        descripcionScroll.setViewportView(descripcionArea);
        formPanel.add(descripcionScroll);
        buttonSpacerLabel.setText(" ");
        formPanel.add(buttonSpacerLabel);
        saveButton.setText("Registrar incidencia");
        formPanel.add(saveButton);
        add(formPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel buttonSpacerLabel;
    private javax.swing.JComboBox<Computadora> computadoraCombo;
    private javax.swing.JLabel computadoraLabel;
    private javax.swing.JTextArea descripcionArea;
    private javax.swing.JLabel descripcionLabel;
    private javax.swing.JScrollPane descripcionScroll;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JLabel laboratorioLabel;
    private javax.swing.JSpinner laboratorioSpinner;
    private javax.swing.JButton loadButton;
    private javax.swing.JPanel pcRowPanel;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JComboBox<String> tipoCombo;
    private javax.swing.JLabel tipoLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
