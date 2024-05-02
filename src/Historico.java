//Para ejecutar primero se deben de agregar las respectivas librerias(jcommon y jfreechart)//
//Para que se vean las imagenes toca cambiar la ruta de acceso dependiendo del PC, por tal motivo lo guardé en la memoria usb de ruta D:\ o E:\//
import java.awt.Desktop;
import java.awt.Image;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;//Librerias
public class Historico extends javax.swing.JFrame {

//Constructor 
    public Historico() {
        initComponents();
    }
    
//Crear tabla de notas de los alumnos
public void tabla1() {
        tabla1.setVisible(true);
        
        // Obtiene la cantidad de filas y columnas desde los campos de texto
        int filas = Integer.parseInt(texto1.getText());
        int columnas = Integer.parseInt(texto2.getText()) + 1;

        // Crea un arreglo de nombres de columnas
        String[] nombresColumnas = new String[columnas];
        nombresColumnas[0] = "nombre";  // La primera columna se llama "nombre"
        for (int i = 1; i < columnas; i++) {
            nombresColumnas[i] = "nota " + i;  // Las siguientes columnas se llaman "nota 1", "nota 2", etc.
        }
        
        // Crea y configura el modelo de tabla
        DefaultTableModel dtm = new DefaultTableModel(filas, columnas);
        dtm.setColumnIdentifiers(nombresColumnas);
        tabla1.setModel(dtm);
    }

//Exportar el archivo csv  
public void exportar() {
    // Nombre fijo para el archivo CSV
    String fileName = "Datos.csv";

    // Manejo de excepciones
    try (FileWriter writer = new FileWriter(fileName)) {
        // Obtener el modelo de la tabla
        DefaultTableModel model = (DefaultTableModel) tabla1.getModel();

        // Escribir los nombres de las columnas en el archivo CSV
        for (int col = 0; col < model.getColumnCount(); col++) {
            writer.write(model.getColumnName(col));
            if (col < model.getColumnCount() - 1) {
                writer.write(";"); // Separador de columna
            }
        }
        writer.write("\n"); // Nueva línea después de las columnas

        // Recorrer filas y escribir datos
        for (int row = 0; row < model.getRowCount(); row++) {
            for (int col = 0; col < model.getColumnCount(); col++) {
                writer.write(model.getValueAt(row, col).toString());
                if (col < model.getColumnCount() - 1) {
                    writer.write(";"); // Separador de columna
                }
            }
            writer.write("\n"); // Nueva línea después de cada fila
        }

        // Mostrar mensaje de confirmación
        JOptionPane.showMessageDialog(null, "Los datos se han exportado exitosamente a " + fileName, "Exportación exitosa", JOptionPane.INFORMATION_MESSAGE);
    } catch (IOException e) {
        // Mostrar mensaje de error
        JOptionPane.showMessageDialog(null, "Ocurrió un error al exportar los datos: " + e.getMessage(), "Error de exportación", JOptionPane.ERROR_MESSAGE);
    }
}

//Abrir el archivo csv
public void abrir() {
    // Nombre fijo para el archivo CSV
    String fileName = "Datos.csv";

    // Crear un objeto File para el archivo CSV
    File archivoCSV = new File(fileName);

    try {
        // Comprobar si Desktop está soportado
        if (Desktop.isDesktopSupported()) {
            Desktop desktop = Desktop.getDesktop();
            // Abrir el archivo CSV
            desktop.open(archivoCSV);
        } else {
            // Si Desktop no es soportado, muestra un mensaje de advertencia
            JOptionPane.showMessageDialog(null, "No se puede abrir el archivo CSV automáticamente.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    } catch (IOException e) {
        // Si ocurre un error al abrir el archivo, muestra un mensaje de error
        JOptionPane.showMessageDialog(null, "Ocurrió un error al intentar abrir el archivo CSV: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

//Crear tabla de resultados
public void tabla2() {
        // Define los nombres de las columnas de la segunda tabla
        String[] titulo = new String[]{"Nombres", "Promedio", "Resultado"};
        DefaultTableModel dtm2 = new DefaultTableModel(0, 3);
        dtm2.setColumnIdentifiers(titulo);
        tabla2.setModel(dtm2);

        // Itera sobre cada fila de la tabla1
        DefaultTableModel modeloTabla1 = (DefaultTableModel) tabla1.getModel();
        int filas = modeloTabla1.getRowCount();

        for (int i = 0; i < filas; i++) {
            // Obtiene el nombre del estudiante
            String nombre = modeloTabla1.getValueAt(i, 0).toString();
            
            // Extrae las notas desde la segunda columna hasta la última de tabla1
            double[] notas = new double[modeloTabla1.getColumnCount() - 1];
            for (int j = 1; j < modeloTabla1.getColumnCount(); j++) {
                Object celda = modeloTabla1.getValueAt(i, j);
                
                // Manejo de valores nulos
                double nota = 0;
                if (celda != null) {
                    try {
                        nota = Double.parseDouble(celda.toString());
                    } catch (NumberFormatException e) {
                        nota = 0;
                    }
                }
                notas[j - 1] = nota;
            }

            // Crea un objeto de la clase Logica
            Logica logica = new Logica(notas);

            // Calcula el promedio y determina el resultado
            double promedio = logica.calcularPromedio();
            String resultado = logica.determinarResultado(promedio);

            // Añade una nueva fila a tabla2
            Object[] nuevaFila = new Object[]{nombre, promedio, resultado};
            dtm2.addRow(nuevaFila);
        }
    }

//Método para la imágen
private void SetImageLabel(JLabel labelName,String root){
        ImageIcon image = new ImageIcon(root);
        Icon icon = new ImageIcon(image.getImage().getScaledInstance(labelName.getWidth(),labelName.getHeight(),Image.SCALE_DEFAULT));
        labelName.setIcon(icon);
        this.repaint();
        }    
    
//Mostrar el histórico de notas de un estudiante en una gráfica XY
public void mostrarHistorico() {
        imagen.setVisible(true);
        
        // Obtiene el nombre del estudiante desde el campo de texto
        String nombreEstudiante = textoEstudiante.getText().trim();
        
        // Verifica si el nombre del estudiante está vacío
        if (nombreEstudiante.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, ingrese el nombre del estudiante.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Verifica si el estudiante existe en tabla1
        DefaultTableModel modeloTabla1 = (DefaultTableModel) tabla1.getModel();
        int filas = modeloTabla1.getRowCount();
        int columnas = modeloTabla1.getColumnCount();
        XYSeries series = new XYSeries("Histórico de Notas de " + nombreEstudiante);
        boolean estudianteEncontrado = false;

        // Buscar el estudiante por nombre en tabla1
        for (int i = 0; i < filas; i++) {
            // Obtiene el nombre del estudiante
            String nombreActual = modeloTabla1.getValueAt(i, 0).toString().trim();

            // Si el nombre coincide, recopila las notas
            if (nombreEstudiante.equals(nombreActual)) {
                estudianteEncontrado = true;

                // Recopila las notas y añade a la serie XY
                for (int j = 1; j < columnas; j++) {
                    Object celda = modeloTabla1.getValueAt(i, j);

                    // Manejo de posibles valores null
                    if (celda == null) {
                        series.add(j, 0);
                        continue;
                    }

                    double nota;
                    try {
                        nota = Double.parseDouble(celda.toString().trim());
                    } catch (NumberFormatException e) {
                        nota = 0;
                    }
                    series.add(j, nota);
                }
                break;
            }
        }

        // Muestra un mensaje de error si el estudiante no se encuentra
        if (!estudianteEncontrado) {
            JOptionPane.showMessageDialog(this, "Estudiante "+textoEstudiante.getText()+" no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crea un conjunto de datos (dataset) para la gráfica
        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(series);
        
        // Crea la gráfica de líneas
        JFreeChart chart = ChartFactory.createXYLineChart(
            "Histórico de Notas de " + nombreEstudiante,
            "Número de Nota",
            "Valor de la Nota",
            dataset,
            PlotOrientation.VERTICAL,
            true, // Leyenda
            true, // Tooltips
            false // URLs
        );
        
        // Guarda la gráfica como imagen en la ubicación especificada
        String rutaArchivo = "D:\\grafica_historico_" + nombreEstudiante + ".jpeg";

        try {
            // Guarda la gráfica como imagen JPEG
            ChartUtilities.saveChartAsJPEG(new File(rutaArchivo), chart, 600, 400);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la gráfica en " + rutaArchivo + ". " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Muestra la imagen en la interfaz
        try {
            SetImageLabel(imagen, rutaArchivo);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen en la interfaz: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
//Mostrar la mortalidad de los estudiantes en una gráfica de pastel
    public void mostrarMortalidad() {
        imagen.setVisible(true);
        
        // Verifica si tabla2 está disponible
        if (tabla2 == null) {
            JOptionPane.showMessageDialog(this, "La tabla de resultados no está disponible", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        DefaultTableModel modeloTabla2 = (DefaultTableModel) tabla2.getModel();
        int totalEstudiantes = modeloTabla2.getRowCount();
        int aprobados = 0;
        int reprobados = 0;

        // Contar el número de aprobados y reprobados
        for (int i = 0; i < totalEstudiantes; i++) {
            String resultado = modeloTabla2.getValueAt(i, 2).toString().trim(); // Columna "Resultado"
            
            if (resultado.equals("Aprobó")) {
                aprobados++;
            } else if (resultado.equals("Reprobó")) {
                reprobados++;
            }
        }

        // Crea un objeto de la clase Logica con los resultados
        Logica logica = new Logica(totalEstudiantes, aprobados, reprobados);

        // Calcula los porcentajes usando Logica
        double porcentajeAprobados = logica.calcularPorcentajeAprobados();
        double porcentajeReprobados = logica.calcularPorcentajeReprobados();

        // Muestra los porcentajes de forma escrita
        String mensaje = String.format("Aprobó: %.2f%%\nReprobó: %.2f%%", porcentajeAprobados, porcentajeReprobados);
        JOptionPane.showMessageDialog(this, mensaje, "Estadísticas de Mortalidad de la Asignatura", JOptionPane.INFORMATION_MESSAGE);

        // Crea un conjunto de datos para la gráfica
        DefaultPieDataset dataset = new DefaultPieDataset();
        dataset.setValue("Aprobó", porcentajeAprobados);
        dataset.setValue("Reprobó", porcentajeReprobados);

        // Crea la gráfica de pastel (torta) usando JFreeChart
        JFreeChart chart = ChartFactory.createPieChart(
            "Estadísticas de Mortalidad de la Asignatura",
            dataset,
            true, // Leyenda
            true, // Tooltips
            false // URLs
        );

        // Guarda la gráfica como imagen en la ubicación especificada
        String rutaArchivo2 = "D:\\mortalidad_estudiantes.jpeg";

        try {
            // Guarda la gráfica como imagen JPEG
            ChartUtilities.saveChartAsJPEG(new File(rutaArchivo2), chart, 600, 400);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error al guardar la gráfica en " + rutaArchivo2 + ". " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Muestra la imagen en la interfaz
        try {
            SetImageLabel(imagen, rutaArchivo2);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar la imagen en la interfaz: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tabla1 = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        texto1 = new javax.swing.JTextField();
        texto2 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        tabla2 = new javax.swing.JTable();
        imagen = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        textoEstudiante = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tabla1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nombre", "Nota", "Nota", "Nota"
            }
        ));
        jScrollPane1.setViewportView(tabla1);

        jLabel1.setText("Cantidad de estudiantes");

        jLabel2.setText("Cantidad de notas por estudiante");

        texto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                texto1ActionPerformed(evt);
            }
        });

        texto2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                texto2ActionPerformed(evt);
            }
        });

        jButton1.setText("Crear Tabla");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Calcular Finales");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        tabla2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Nombre", "Promedio", "Resultado"
            }
        ));
        jScrollPane2.setViewportView(tabla2);

        jButton3.setText("Mostrar Histórico");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        textoEstudiante.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                textoEstudianteActionPerformed(evt);
            }
        });

        jLabel3.setText("Nombre estudiante");

        jButton4.setText("Mostrar Mortalidad");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Eliminar Datos");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton6.setText("Exit");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Exportar datos");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(14, 14, 14)
                                        .addComponent(jLabel1)))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(texto2, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(texto1, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(textoEstudiante, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(30, 30, 30)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(imagen, javax.swing.GroupLayout.PREFERRED_SIZE, 591, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(textoEstudiante, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(9, 9, 9)
                                .addComponent(jLabel3))
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addComponent(imagen, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 256, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(texto1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(9, 9, 9)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(texto2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(31, 31, 31))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(43, 43, 43)))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(20, 20, 20)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void texto2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_texto2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_texto2ActionPerformed

    private void texto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_texto1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_texto1ActionPerformed
//Crear tabla 1
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        tabla1();
        tabla1.setVisible(true);
        texto1.setText("");
        texto2.setText("");
    }//GEN-LAST:event_jButton1ActionPerformed
//Crear tabla 2
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        tabla2();
        tabla2.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed
//Mostrar Histórico
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        mostrarHistorico();
        textoEstudiante.setText("");
    }//GEN-LAST:event_jButton3ActionPerformed
//Mostrar Mortalidad
    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        mostrarMortalidad();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void textoEstudianteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_textoEstudianteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_textoEstudianteActionPerformed
//Eliminar datos
    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        textoEstudiante.setText(" ");
        tabla1.setVisible(false);
        tabla2.setVisible(false);
        imagen.setVisible(false);
    }//GEN-LAST:event_jButton5ActionPerformed
//Salir
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton6ActionPerformed
//Exportar
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        exportar();
        abrir();
    }//GEN-LAST:event_jButton7ActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Historico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Historico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Historico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Historico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Historico().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel imagen;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable tabla1;
    private javax.swing.JTable tabla2;
    private javax.swing.JTextField texto1;
    private javax.swing.JTextField texto2;
    private javax.swing.JTextField textoEstudiante;
    // End of variables declaration//GEN-END:variables
}
