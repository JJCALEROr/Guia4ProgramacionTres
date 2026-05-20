/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package guia4_progra3;

import guia4_progra3.dao.AlumnoDAO;
import guia4_progra3.model.Alumno;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

/**
 *
 * @author josjo
 */
public class MainJFrame extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(MainJFrame.class.getName());
    private final AlumnoDAO dao = new AlumnoDAO();
    private int idSeleccionado = -1;

    private static final String[] CARRERAS = {
        "Ingeniería en Sistemas",
        "Administración de Empresas",
        "Contaduría Pública",
        "Ingeniería Civil",
        "Derecho",
        "Psicología"
    };

    /**
     * Creates new form MainJFrame
     */
    public MainJFrame() {
        initComponents();
        configurarTabla();
        cargarComboCarrera();
        cargarTabla();

        // ── Eventos de botones ────────────────────────────────────────────
        btnAgregar.addActionListener(e -> agregar());
        btnModificar.addActionListener(e -> modificar());
        btnEliminar.addActionListener(e -> eliminar());
        btnBuscar.addActionListener(e -> buscar());
        btnLimpiar.addActionListener(e -> limpiarCampos());

        // Clic en tabla → carga campos
        jtbListado.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                cargarDesdeTabla();
            }
        });
    }

    private void cargarComboCarrera() {
        cmbCarrera.removeAllItems();
        cmbCarrera.addItem("Ingeniería en Sistemas");
        cmbCarrera.addItem("Administración de Empresas");
        cmbCarrera.addItem("Contaduría Pública");
        cmbCarrera.addItem("Ingeniería Civil");
        cmbCarrera.addItem("Derecho");
        cmbCarrera.addItem("Psicología");
    }

    private void configurarTabla() {
        DefaultTableModel modelo = new DefaultTableModel(
                new String[]{"ID", "Nombres", "Apellidos", "Carnet", "Carrera", "Fecha"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };
        jtbListado.setModel(modelo);
        jtbListado.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Ocultar columna ID
        jtbListado.getColumnModel().getColumn(0).setMinWidth(0);
        jtbListado.getColumnModel().getColumn(0).setMaxWidth(0);
        jtbListado.getColumnModel().getColumn(0).setWidth(0);
    }

    private DefaultTableModel getModelo() {
        return (DefaultTableModel) jtbListado.getModel();
    }

    private void agregar() {
        if (!validar()) {
            return;
        }
        Alumno a = leerCampos();
        if (dao.carnetExiste(a.getCarnet(), 0)) {
            JOptionPane.showMessageDialog(this, "El carnet ya existe.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dao.agregar(a)) {
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Alumno agregado correctamente.");
        }
    }

    private void modificar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un alumno de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!validar()) {
            return;
        }
        Alumno a = leerCampos();
        a.setIdAlumno(idSeleccionado);
        if (dao.carnetExiste(a.getCarnet(), idSeleccionado)) {
            JOptionPane.showMessageDialog(this, "El carnet ya está en uso.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (dao.actualizar(a)) {
            cargarTabla();
            limpiarCampos();
            JOptionPane.showMessageDialog(this, "Alumno actualizado correctamente.");
        }
    }

    private void eliminar() {
        if (idSeleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Selecciona un alumno de la tabla.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int conf = JOptionPane.showConfirmDialog(this,
                "¿Eliminar este alumno?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (conf == JOptionPane.YES_OPTION && dao.eliminar(idSeleccionado)) {
            cargarTabla();
            limpiarCampos();
        }
    }

    private void buscar() {
        String texto = txtBuscar.getText().trim().toLowerCase();
        List<Alumno> todos = dao.listarTodos();
        getModelo().setRowCount(0);
        for (Alumno a : todos) {
            if (a.getNombres().toLowerCase().contains(texto)
                    || a.getApellidos().toLowerCase().contains(texto)
                    || a.getCarnet().toLowerCase().contains(texto)) {
                getModelo().addRow(new Object[]{
                    a.getIdAlumno(), a.getNombres(), a.getApellidos(),
                    a.getCarnet(), a.getCarrera(), a.getFechaInscripcion()
                });
            }
        }
    }

    // ── Helpers ──────────────────────────────────────────────────────────────
    private void cargarTabla() {
        getModelo().setRowCount(0);
        idSeleccionado = -1;
        for (Alumno a : dao.listarTodos()) {
            getModelo().addRow(new Object[]{
                a.getIdAlumno(), a.getNombres(), a.getApellidos(),
                a.getCarnet(), a.getCarrera(), a.getFechaInscripcion()
            });
        }
    }

    private void cargarDesdeTabla() {
        int fila = jtbListado.getSelectedRow();
        if (fila < 0) {
            return;
        }
        idSeleccionado = (int) getModelo().getValueAt(fila, 0);
        txtNombres.setText((String) getModelo().getValueAt(fila, 1));
        txtApellidos.setText((String) getModelo().getValueAt(fila, 2));
        txtCarnet.setText((String) getModelo().getValueAt(fila, 3));
        cmbCarrera.setSelectedItem(getModelo().getValueAt(fila, 4));
        ftxtFecha.setText((String) getModelo().getValueAt(fila, 5));
    }

    private void limpiarCampos() {
        txtNombres.setText("");
        txtApellidos.setText("");
        txtCarnet.setText("");
        ftxtFecha.setText("");
        txtBuscar.setText("");
        cmbCarrera.setSelectedIndex(0);
        idSeleccionado = -1;
        jtbListado.clearSelection();
    }

    private Alumno leerCampos() {
        return new Alumno(0,
                txtNombres.getText().trim(),
                txtApellidos.getText().trim(),
                txtCarnet.getText().trim(),
                (String) cmbCarrera.getSelectedItem(),
                ftxtFecha.getText().trim()
        );
    }

    private boolean validar() {
        if (txtNombres.getText().trim().isEmpty()
                || txtApellidos.getText().trim().isEmpty()
                || txtCarnet.getText().trim().isEmpty()
                || ftxtFecha.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.",
                    "Validación", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField3 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        txtBuscar = new javax.swing.JTextField();
        btnBuscar = new javax.swing.JButton();
        btnLimpiar = new javax.swing.JButton();
        btnAgregar = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtbListado = new javax.swing.JTable();
        jLabel4 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNombres = new javax.swing.JTextField();
        txtApellidos = new javax.swing.JTextField();
        txtCarnet = new javax.swing.JTextField();
        cmbCarrera = new javax.swing.JComboBox<>();
        ftxtFecha = new javax.swing.JFormattedTextField();
        jLabel10 = new javax.swing.JLabel();

        jLabel1.setText("jLabel1");

        jLabel3.setText("jLabel3");

        jLabel5.setText("jLabel5");

        jTextField3.setText("jTextField3");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("27-2261-2024 CALERO RODRIGUEZ JOSUE JOHNNATAN");

        jLabel2.setText("GESTION DE ALUMNOS");

        txtBuscar.setToolTipText("");

        btnBuscar.setText("Buscar");

        btnLimpiar.setText("Limpiar");

        btnAgregar.setText("Agregar");

        btnModificar.setText("Modificar");

        btnEliminar.setText("Eliminar");

        jtbListado.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jtbListado);

        jLabel4.setText("Nombres");

        jLabel6.setText("Apellidos");

        jLabel7.setText("Carnet");

        jLabel8.setText("Carrera");

        jLabel9.setText("Fecha Inscripcion");

        cmbCarrera.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        try {
            ftxtFecha.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("####/##/##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        ftxtFecha.addActionListener(this::ftxtFechaActionPerformed);

        jLabel10.setText("Buscar:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 749, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(336, 336, 336)
                            .addComponent(jLabel2))
                        .addGroup(layout.createSequentialGroup()
                            .addGap(56, 56, 56)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(btnBuscar)
                                    .addGap(177, 177, 177)
                                    .addComponent(btnModificar)
                                    .addGap(73, 73, 73)
                                    .addComponent(btnEliminar)
                                    .addGap(60, 60, 60)
                                    .addComponent(btnLimpiar))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                            .addComponent(jLabel9)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(btnAgregar)
                                                .addComponent(ftxtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGap(101, 101, 101))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel4)
                                                .addComponent(jLabel7))
                                            .addGap(31, 31, 31)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                .addComponent(txtNombres)
                                                .addComponent(txtCarnet, javax.swing.GroupLayout.DEFAULT_SIZE, 251, Short.MAX_VALUE))
                                            .addGap(36, 36, 36)))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel6)
                                        .addComponent(jLabel8))
                                    .addGap(28, 28, 28)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(txtApellidos)
                                            .addComponent(cmbCarrera, 0, 285, Short.MAX_VALUE))
                                        .addComponent(jLabel10)))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel6)
                    .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtApellidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(106, 106, 106)
                        .addComponent(jLabel10)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnLimpiar)
                            .addComponent(btnBuscar)
                            .addComponent(btnAgregar)
                            .addComponent(btnModificar)
                            .addComponent(btnEliminar)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(36, 36, 36)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(cmbCarrera, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(44, 44, 44)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel7)
                                    .addComponent(txtCarnet, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel9)
                            .addComponent(ftxtFecha, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(58, 58, 58)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void ftxtFechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ftxtFechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ftxtFechaActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new MainJFrame().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAgregar;
    private javax.swing.JButton btnBuscar;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnLimpiar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JComboBox<String> cmbCarrera;
    private javax.swing.JFormattedTextField ftxtFecha;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTable jtbListado;
    private javax.swing.JTextField txtApellidos;
    private javax.swing.JTextField txtBuscar;
    private javax.swing.JTextField txtCarnet;
    private javax.swing.JTextField txtNombres;
    // End of variables declaration//GEN-END:variables
}
