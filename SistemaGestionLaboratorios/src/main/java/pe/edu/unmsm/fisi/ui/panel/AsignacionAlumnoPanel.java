package pe.edu.unmsm.fisi.ui.panel;

import java.awt.Dimension;
import java.beans.Beans;
import java.time.LocalDate;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import pe.edu.unmsm.fisi.model.entity.Alumno;
import pe.edu.unmsm.fisi.model.entity.Reserva;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.service.ReservaService;
import pe.edu.unmsm.fisi.service.ReservaServiceImpl;
import pe.edu.unmsm.fisi.ui.AppTheme;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel editable con Design para la asignación automática del alumno. */
public class AsignacionAlumnoPanel extends javax.swing.JPanel {

    private static final String[] HORAS = {
        "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
        "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"
    };

    private Usuario usuario;
    private final ReservaService service = new ReservaServiceImpl();

    public AsignacionAlumnoPanel() {
        this(new Alumno(0, "Alumno de diseño", "alumno@unmsm.edu.pe", "", "00000000", 120));
    }

    public AsignacionAlumnoPanel(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        configurarFormulario();
    }

    private void configurarFormulario() {
        setOpaque(false);
        UiKit.styleTitle(titleLabel);
        UiKit.styleSubtitle(subtitleLabel);
        UiKit.styleCard(formPanel);
        UiKit.styleCard(resultPanel);
        UiKit.styleFieldLabel(laboratorioLabel);
        UiKit.styleFieldLabel(cursoLabel);
        UiKit.styleFieldLabel(fechaLabel);
        UiKit.styleFieldLabel(inicioLabel);
        UiKit.styleFieldLabel(finLabel);
        UiKit.styleFieldLabel(resultTitleLabel);
        UiKit.stylePrimary(assignButton);
        resultLabel.setForeground(AppTheme.MUTED);

        laboratorioSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        cursoField.setText("Programación Orientada a Objetos");
        fechaField.setText(LocalDate.now().toString());
        fechaField.setEditable(false);
        inicioCombo.setModel(new DefaultComboBoxModel<>(HORAS));
        finCombo.setModel(new DefaultComboBoxModel<>(HORAS));
        inicioCombo.setSelectedItem("14:00");
        finCombo.setSelectedItem("16:00");
        resultPanel.setPreferredSize(new Dimension(310, 0));
        assignButton.addActionListener(e -> asignar());
    }

    private void asignar() {
        UiKit.async(this, assignButton, "Buscando PC...", () -> {
            int laboratorio = (Integer) laboratorioSpinner.getValue();
            LocalDate fecha = LocalDate.parse(fechaField.getText().trim());
            int inicio = UiKit.parseHour(inicioCombo.getSelectedItem());
            int fin = UiKit.parseHour(finCombo.getSelectedItem());
            return service.solicitarAsignacionAutomaticaAlumno(
                    usuario, laboratorio, cursoField.getText(), fecha, inicio, fin
            );
        }, this::mostrarResultado);
    }

    private void mostrarResultado(Reserva reserva) {
        resultLabel.setForeground(AppTheme.SUCCESS);
        resultLabel.setText("<html><b>Asignación completada</b><br><br>Laboratorio: "
                + reserva.getIdLaboratorio() + "<br>Computadora ID: "
                + reserva.getIdComputadora() + "<br>Fecha: " + reserva.getFecha()
                + "<br>Horario: " + UiKit.formatHour(reserva.getHoraInicio())
                + " - " + UiKit.formatHour(reserva.getHoraFin()) + "</html>");
        UiKit.info(this, "La computadora fue reservada y marcada como OCUPADA.");
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        headerPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        subtitleLabel = new javax.swing.JLabel();
        centerPanel = new javax.swing.JPanel();
        formPanel = new javax.swing.JPanel();
        laboratorioLabel = new javax.swing.JLabel();
        laboratorioSpinner = new javax.swing.JSpinner();
        cursoLabel = new javax.swing.JLabel();
        cursoField = new javax.swing.JTextField();
        fechaLabel = new javax.swing.JLabel();
        fechaField = new javax.swing.JTextField();
        inicioLabel = new javax.swing.JLabel();
        inicioCombo = new javax.swing.JComboBox();
        finLabel = new javax.swing.JLabel();
        finCombo = new javax.swing.JComboBox();
        buttonSpacerLabel = new javax.swing.JLabel();
        assignButton = new javax.swing.JButton();
        resultPanel = new javax.swing.JPanel();
        resultTitleLabel = new javax.swing.JLabel();
        resultLabel = new javax.swing.JLabel();

        setLayout(new java.awt.BorderLayout());

        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));

        titleLabel.setText("Asignación automática de computadora");
        headerPanel.add(titleLabel);

        subtitleLabel.setText("El algoritmo voraz selecciona la primera PC disponible del laboratorio.");
        headerPanel.add(subtitleLabel);

        add(headerPanel, java.awt.BorderLayout.NORTH);

        centerPanel.setOpaque(false);
        centerPanel.setLayout(new java.awt.BorderLayout(18, 0));

        formPanel.setLayout(new java.awt.GridLayout(0, 2, 20, 14));

        laboratorioLabel.setText("Laboratorio");
        formPanel.add(laboratorioLabel);
        formPanel.add(laboratorioSpinner);

        cursoLabel.setText("Curso o actividad");
        formPanel.add(cursoLabel);
        formPanel.add(cursoField);

        fechaLabel.setText("Fecha (AAAA-MM-DD)");
        formPanel.add(fechaLabel);
        formPanel.add(fechaField);

        inicioLabel.setText("Hora de inicio");
        formPanel.add(inicioLabel);
        formPanel.add(inicioCombo);

        finLabel.setText("Hora de fin");
        formPanel.add(finLabel);
        formPanel.add(finCombo);

        buttonSpacerLabel.setText(" ");
        formPanel.add(buttonSpacerLabel);

        assignButton.setText("Asignar computadora automáticamente");
        formPanel.add(assignButton);

        centerPanel.add(formPanel, java.awt.BorderLayout.CENTER);

        resultPanel.setLayout(new java.awt.BorderLayout());

        resultTitleLabel.setText("Resultado de la última operación");
        resultPanel.add(resultTitleLabel, java.awt.BorderLayout.NORTH);

        resultLabel.setText("<html>Aún no se ha realizado una asignación.</html>");
        resultPanel.add(resultLabel, java.awt.BorderLayout.CENTER);

        centerPanel.add(resultPanel, java.awt.BorderLayout.EAST);

        add(centerPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton assignButton;
    private javax.swing.JLabel buttonSpacerLabel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JTextField cursoField;
    private javax.swing.JLabel cursoLabel;
    private javax.swing.JTextField fechaField;
    private javax.swing.JLabel fechaLabel;
    private javax.swing.JComboBox finCombo;
    private javax.swing.JLabel finLabel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JComboBox inicioCombo;
    private javax.swing.JLabel inicioLabel;
    private javax.swing.JLabel laboratorioLabel;
    private javax.swing.JSpinner laboratorioSpinner;
    private javax.swing.JLabel resultLabel;
    private javax.swing.JPanel resultPanel;
    private javax.swing.JLabel resultTitleLabel;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
