package pe.edu.unmsm.fisi.ui.panel;

import java.beans.Beans;
import java.time.LocalDate;
import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;
import pe.edu.unmsm.fisi.model.entity.Profesor;
import pe.edu.unmsm.fisi.model.entity.Usuario;
import pe.edu.unmsm.fisi.service.ReservaService;
import pe.edu.unmsm.fisi.service.ReservaServiceImpl;
import pe.edu.unmsm.fisi.ui.UiKit;

/** Panel de reserva docente editable desde Design. */
public class ReservaProfesorPanel extends javax.swing.JPanel {

    private static final String[] HORAS = {
        "07:00", "08:00", "09:00", "10:00", "11:00", "12:00", "13:00",
        "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00", "21:00"
    };

    private Usuario usuario;
    private final ReservaService service = new ReservaServiceImpl();

    public ReservaProfesorPanel() {
        this(new Profesor(0, "Profesor de diseño", "profesor@unmsm.edu.pe", "", "Ingeniería de Software"));
    }

    public ReservaProfesorPanel(Usuario usuario) {
        this.usuario = usuario;
        initComponents();
        configurarPanel();
    }

    private void configurarPanel() {
        setOpaque(false);
        UiKit.styleTitle(titleLabel);
        UiKit.styleSubtitle(subtitleLabel);
        UiKit.styleCard(formPanel);
        UiKit.styleFieldLabel(laboratorioLabel);
        UiKit.styleFieldLabel(cursoLabel);
        UiKit.styleFieldLabel(fechaLabel);
        UiKit.styleFieldLabel(inicioLabel);
        UiKit.styleFieldLabel(finLabel);
        UiKit.stylePrimary(reserveButton);

        laboratorioSpinner.setModel(new SpinnerNumberModel(1, 1, 999, 1));
        fechaField.setText(LocalDate.now().toString());
        inicioCombo.setModel(new DefaultComboBoxModel<>(HORAS));
        finCombo.setModel(new DefaultComboBoxModel<>(HORAS));
        inicioCombo.setSelectedItem("08:00");
        finCombo.setSelectedItem("10:00");
        reserveButton.addActionListener(e -> reservar());
    }

    private void reservar() {
        UiKit.async(this, reserveButton, "Validando horario...", () -> {
            int laboratorio = (Integer) laboratorioSpinner.getValue();
            LocalDate fecha = LocalDate.parse(fechaField.getText().trim());
            int inicio = UiKit.parseHour(inicioCombo.getSelectedItem());
            int fin = UiKit.parseHour(finCombo.getSelectedItem());
            return service.procesarReservaDocente(
                    usuario, laboratorio, fecha, inicio, fin, cursoField.getText()
            );
        }, ok -> {
            if (ok) {
                UiKit.info(this, "El laboratorio fue reservado correctamente.");
                cursoField.setText("");
            }
        });
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
        cursoLabel = new javax.swing.JLabel();
        cursoField = new javax.swing.JTextField();
        fechaLabel = new javax.swing.JLabel();
        fechaField = new javax.swing.JTextField();
        inicioLabel = new javax.swing.JLabel();
        inicioCombo = new javax.swing.JComboBox<>();
        finLabel = new javax.swing.JLabel();
        finCombo = new javax.swing.JComboBox<>();
        buttonSpacerLabel = new javax.swing.JLabel();
        reserveButton = new javax.swing.JButton();

        setLayout(new java.awt.BorderLayout(0, 20));
        headerPanel.setOpaque(false);
        headerPanel.setLayout(new java.awt.GridLayout(0, 1, 0, 6));
        titleLabel.setText("Reserva docente de laboratorio");
        headerPanel.add(titleLabel);
        subtitleLabel.setText("La validación de intervalos impide registrar horarios que se superponen.");
        headerPanel.add(subtitleLabel);
        add(headerPanel, java.awt.BorderLayout.NORTH);

        formPanel.setLayout(new java.awt.GridLayout(0, 2, 24, 14));
        laboratorioLabel.setText("Laboratorio");
        formPanel.add(laboratorioLabel);
        formPanel.add(laboratorioSpinner);
        cursoLabel.setText("Curso");
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
        reserveButton.setText("Validar y registrar reserva");
        formPanel.add(reserveButton);
        add(formPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel buttonSpacerLabel;
    private javax.swing.JTextField cursoField;
    private javax.swing.JLabel cursoLabel;
    private javax.swing.JTextField fechaField;
    private javax.swing.JLabel fechaLabel;
    private javax.swing.JComboBox<String> finCombo;
    private javax.swing.JLabel finLabel;
    private javax.swing.JPanel formPanel;
    private javax.swing.JPanel headerPanel;
    private javax.swing.JComboBox<String> inicioCombo;
    private javax.swing.JLabel inicioLabel;
    private javax.swing.JLabel laboratorioLabel;
    private javax.swing.JSpinner laboratorioSpinner;
    private javax.swing.JButton reserveButton;
    private javax.swing.JLabel subtitleLabel;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
}
