package GUI;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import smartsensors.InterfaceAgent;
import smartsensors.ProfileManager;
import smartsensors.Rule;
import smartsensors.RuleCondition;

public class Menu extends javax.swing.JFrame {

    private InterfaceAgent agente;

    /* temperature */
    private DefaultCategoryDataset tempDataset;
    private DefaultCategoryDataset humiDataset;
    private DefaultCategoryDataset movDataset;
    private DefaultCategoryDataset smokeDataset;
    private DefaultCategoryDataset lumiDataset;
    private DefaultCategoryDataset arduinoDataset;

    private ScheduledExecutorService exec;

    /**
     * Creates new form Menu
     */
    public Menu() {


     DefaultTableCellRenderer dtcr = new DefaultTableCellRenderer();
     dtcr.setHorizontalAlignment(DefaultTableCellRenderer.CENTER);
        initComponents();



        /* init datasets */
        this.tempDataset = new DefaultCategoryDataset();
        this.humiDataset = new DefaultCategoryDataset();
        this.movDataset = new DefaultCategoryDataset();
        this.smokeDataset = new DefaultCategoryDataset();
        this.lumiDataset = new DefaultCategoryDataset();
        this.arduinoDataset = new DefaultCategoryDataset();

        this.updateAllCharts();

            ImageIcon img;

            img = new ImageIcon("images/clock.png");
            this.jButton8.setSize(24, 24);
            this.jButton8.setIcon(img);


            img = new ImageIcon("images/sensor.png");
            this.jButton1.setSize(24, 24);
            this.jButton1.setIcon(img);



        jTable1.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Boolean flag = true;
                int row = jTable1.rowAtPoint(evt.getPoint());
                int col = jTable1.columnAtPoint(evt.getPoint());


                if (row >= 0 && col == 2) {

                    if(jTable1.getValueAt(row, col).equals(!flag)){

                        System.out.println("VAI FICAR ONLINE ::: ESTA "+jTable1.getValueAt(row, col));
                        sendMsg(jTable1.getValueAt(row, 0).toString()+".online");
                        agente.labels.get(jTable1.getValueAt(row, 0).toString()).setVisible(true);

                    }
                    if(jTable1.getValueAt(row, col).equals(flag)){

                        System.out.println("VAI FICAR oFFLINE ::: ESTA "+jTable1.getValueAt(row, col));
                        sendMsg(jTable1.getValueAt(row, 0).toString()+".offline");
                        agente.labels.get(jTable1.getValueAt(row, 0).toString()).setVisible(false);
                    }

                }
            }
        });

        jTable2.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = jTable2.rowAtPoint(evt.getPoint());
                int col = jTable2.columnAtPoint(evt.getPoint());


                if (row >= 0 && col == 1) {

                    if(jTable2.getValueAt(row, col).equals("<")){
                        jTable2.setValueAt(">", row, col);

                    }else{
                        jTable2.setValueAt("<", row, col);

                    }

                }
            }
        });

        jTable4.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {

                int row = jTable4.rowAtPoint(evt.getPoint());
                int col = jTable4.columnAtPoint(evt.getPoint());
                int i;
                Boolean flag = Boolean.TRUE;
                int fst = 0;

                if (row >= 0 && col == 0) {
                    i=agente.automationProfile.size();
                    if(jTable4.getValueAt(row, 0).equals(!flag)){
                        jTable4.setValueAt(flag, row, 0);
                      while(i>0){
                          if(agente.automationProfile.get(i-1).ruleString().equals(jTable4.getValueAt(row, 1).toString())&& fst ==0)
                          {
                             agente.automationProfile.get(i-1).setActive(flag);
                             fst=1;
                             System.out.println("VAI FICAR ON"+agente.automationProfile.get(i-1).ruleString());
                          }
                          i--;
                      }

                    }else{
                         jTable4.setValueAt(!flag, row, 0);
                      while(i>0){
                          if(agente.automationProfile.get(i-1).ruleString().equals(jTable4.getValueAt(row, 1).toString())&& fst ==0)
                          {
                             agente.automationProfile.get(i-1).setActive(!flag);
                             fst=1;
                             System.out.println("VAI FICAR OFF"+agente.automationProfile.get(i-1).ruleString());
                          }
                        i--;
                    }

                }
            }
           }
        });

        //keybindings


        InputMap im = jTable2.getInputMap(JTable.WHEN_FOCUSED);
        ActionMap am = jTable2.getActionMap();

        Action deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel defaultModel = (DefaultTableModel) agente.menu.getjTable2().getModel();
                int selRow =  jTable2.getSelectedRow();
                if (selRow<0){
                    return;
                }
                defaultModel.removeRow(jTable2.getSelectedRow());
                agente.menu.getjTable2().setModel(defaultModel);
            }

        };

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "Delete");
        am.put("Delete", deleteAction);

        //key table 1

        InputMap im2 = jTable1.getInputMap(JTable.WHEN_FOCUSED);
        ActionMap am2 = jTable1.getActionMap();

        Action deleteAction2 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel defaultModel = (DefaultTableModel) agente.menu.getjTable1().getModel();
                int selRow =  jTable1.getSelectedRow();
                if (selRow<0){
                    return;
                }

                sendMsg(""+jTable1.getValueAt(selRow, 0)+".shutdown");
                defaultModel.removeRow(jTable1.getSelectedRow());
                agente.menu.getjTable1().setModel(defaultModel);
            }

        };

        im2.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
        im2.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "Delete");
        am2.put("Delete", deleteAction2);

        //key table 4

        InputMap im4 = jTable4.getInputMap(JTable.WHEN_FOCUSED);
        ActionMap am4 = jTable4.getActionMap();

        Action deleteAction4 = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DefaultTableModel defaultModel = (DefaultTableModel) agente.menu.getjTable4().getModel();
                int selRow =  jTable4.getSelectedRow();
                if (selRow<0){
                    return;
                }

                //remover regra
                int i = 0;

                while(i < getjTable4().getRowCount()){

                    if(getjTable4().getValueAt(i, 1).toString().equals(agente.automationProfile.get(i).ruleString())){
                       agente.automationProfile.remove(i);
                       break;
                    }
                }

                defaultModel.removeRow(jTable4.getSelectedRow());
                agente.menu.getjTable4().setModel(defaultModel);
            }

        };

        im4.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "Delete");
        im4.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "Delete");
        am4.put("Delete", deleteAction4);

        //formats
        this.jTable4.getColumnModel().getColumn(0).setMaxWidth(75);

        this.jTable1.getColumnModel().getColumn(0).setCellRenderer( dtcr);
        this.jTable1.getColumnModel().getColumn(1).setCellRenderer( dtcr);


        this.jTable2.getColumnModel().getColumn(0).setCellRenderer( dtcr);
        this.jTable2.getColumnModel().getColumn(1).setCellRenderer( dtcr);
        this.jTable2.getColumnModel().getColumn(2).setCellRenderer( dtcr);


        this.jTable4.getColumnModel().getColumn(1).setCellRenderer( dtcr);
        //close event

        this.addWindowListener(new java.awt.event.WindowAdapter() {
    @Override
    public void windowClosing(java.awt.event.WindowEvent windowEvent) {

            agente.sensorsToShutDown = jTable1.getRowCount()-1;

            for(int i=0; i<jTable1.getRowCount();i++){
                sendMsg(jTable1.getValueAt(i, 0).toString()+".shutdown");

            }



    }
});



    }

    /* UPDATE CHARTS */
    public void updateTempChart() {
        JFreeChart chart = ChartFactory.createLineChart("Temperature", "Time", "ºC", tempDataset,PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot catPlot = chart.getCategoryPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);

        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,643,523);
        jPanel4.removeAll();
        jPanel4.add(charPanel,BorderLayout.CENTER);
        jPanel4.validate();
    }

    public void updateHumChart() {
        JFreeChart chart = ChartFactory.createLineChart("Humidity", "Time", "%", humiDataset,PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot catPlot = chart.getCategoryPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);

        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,643,523);
        jPanel5.removeAll();
        jPanel5.add(charPanel,BorderLayout.CENTER);
        jPanel5.validate();
    }

    public void updateMovChart() {
        JFreeChart chart = ChartFactory.createLineChart("Movement", "Time", "Move", movDataset,PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot catPlot = chart.getCategoryPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);

        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,643,523);
        jPanel6.removeAll();
        jPanel6.add(charPanel,BorderLayout.CENTER);
        jPanel6.validate();
    }

    public void updateSmokeChart() {
        JFreeChart chart = ChartFactory.createLineChart("Smoke", "Time", "%", smokeDataset,PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot catPlot = chart.getCategoryPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);

        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,643,523);
        jPanel7.removeAll();
        jPanel7.add(charPanel,BorderLayout.CENTER);
        jPanel7.validate();
    }

    public void updateLumChart() {
        JFreeChart chart = ChartFactory.createLineChart("Luminosity", "Time", "Level", lumiDataset,PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot catPlot = chart.getCategoryPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);

        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,643,523);
        jPanel8.removeAll();
        jPanel8.add(charPanel,BorderLayout.CENTER);
        jPanel8.validate();
    }

    public void updateArduinoChart() {
        JFreeChart chart = ChartFactory.createLineChart("Arduino", "Time", "ºC", arduinoDataset,PlotOrientation.VERTICAL, true, true, false);

        CategoryPlot catPlot = chart.getCategoryPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);

        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,643,523);
        jPanel9.removeAll();
        jPanel9.add(charPanel,BorderLayout.CENTER);
        jPanel9.validate();
    }

    public void updateAllCharts() {
        this.updateTempChart();
        this.updateHumChart();
        this.updateMovChart();
        this.updateSmokeChart();
        this.updateLumChart();
        this.updateArduinoChart();
    }

    /* ADD VALUES TO CHARTS */
    public synchronized void addTemp(Integer value, String div) {
        if(this.tempDataset.getColumnCount() > 5) {
            this.tempDataset.removeColumn(0);
        }
        this.tempDataset.addValue(value,div, ""+new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    public void addHum(Integer value, String div) {
        if(this.humiDataset.getColumnCount() > 5) {
            this.humiDataset.removeColumn(0);
        }
        this.humiDataset.addValue(value,div, ""+new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    public void addMov(Integer value, String div) {
        if(this.movDataset.getColumnCount() > 5) {
            this.movDataset.removeColumn(0);
        }
        this.movDataset.addValue(value,div, ""+new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    public void addSmoke(Integer value, String div) {
        if(this.smokeDataset.getColumnCount() > 5) {
            this.smokeDataset.removeColumn(0);
        }
        this.smokeDataset.addValue(value,div, ""+new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    public void addLum(Integer value, String div) {
        if(this.lumiDataset.getColumnCount() > 5) {
            this.lumiDataset.removeColumn(0);
        }
        this.lumiDataset.addValue(value,div, ""+new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    public void addArduino(Integer value, String div) {
        if(this.arduinoDataset.getColumnCount() > 5) {
            this.arduinoDataset.removeColumn(0);
        }
        this.arduinoDataset.addValue(value,div, ""+new SimpleDateFormat("HH:mm:ss").format(new Date()));
    }

    /* Chart auto update */
    class chartUpdate implements Runnable {
        public Menu m;

        public chartUpdate(Menu m) {
            this.m = m;
        }

        public void run() {
            exec = Executors.newSingleThreadScheduledExecutor();
            exec.scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    try {
                        m.updateAllCharts();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(new Frame(), "Dataset error!", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }, 0, 10, TimeUnit.SECONDS);
        }
    }

    /* OTHERS METHODS */

    public JTextField getjTextField1() {
        return jTextField1;
    }

    public void setjTextField1(JTextField jTextField1) {
        this.jTextField1 = jTextField1;
    }

    public JTextField getjTextField3() {
        return jTextField3;
    }

    public void setjTextField3(JTextField jTextField3) {
        this.jTextField3 = jTextField3;
    }

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label label1) {
        this.label1 = label1;
    }

    public JTable getjTable2() {
        return jTable2;
    }

    public void setjTable2(JTable jTable2) {
        this.jTable2 = jTable2;
    }

    public JTable getjTable3() {
        return jTable3;
    }

    public void setjTable3(JTable jTable3) {
        this.jTable3 = jTable3;
    }

    public JTable getjTable4() {
        return jTable4;
    }

    public void setjTable4(JTable jTable4) {
        this.jTable4 = jTable4;
    }



    public void setAgente(InterfaceAgent a){
        this.agente = a;
    }

    public JPanel getjPanel1() {
        return jPanel1;
    }

    public void setjPanel1(JPanel jPanel1) {
        this.jPanel1 = jPanel1;
    }

    public JPanel getjPanel2() {
        return jPanel2;
    }

    public void setjPanel2(JPanel jPanel2) {
        this.jPanel2 = jPanel2;
    }

    public JPanel getjPanel3() {
        return jPanel3;
    }

    public void setjPanel3(JPanel jPanel3) {
        this.jPanel3 = jPanel3;
    }

    public JScrollPane getjScrollPane1() {
        return jScrollPane1;
    }

    public void setjScrollPane1(JScrollPane jScrollPane1) {
        this.jScrollPane1 = jScrollPane1;
    }

    public JScrollPane getjScrollPane2() {
        return jScrollPane2;
    }

    public void setjScrollPane2(JScrollPane jScrollPane2) {
        this.jScrollPane2 = jScrollPane2;
    }

    public JTabbedPane getjTabbedPane1() {
        return jTabbedPane1;
    }

    public void setjTabbedPane1(JTabbedPane jTabbedPane1) {
        this.jTabbedPane1 = jTabbedPane1;
    }

    public JTable getjTable1() {
        return jTable1;
    }

    public void setjTable1(JTable jTable1) {
        this.jTable1 = jTable1;
    }

    public JTextArea getjTextArea1() {
        return jTextArea1;
    }

    public void setjTextArea1(JTextArea jTextArea1) {
        this.jTextArea1 = jTextArea1;
    }

    private void sendMsg(String msgContent)
    {
       AID receiver = new AID();
        receiver.setLocalName("interface");

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.addReceiver(receiver);

        msg.setContent(msgContent);

        agente.send(msg);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane4 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        ImageIcon image = new ImageIcon("images/house_plan_1.jpg");


 jPanel1 = new javax.swing.JPanel(){
            @Override
            public void paintComponent(Graphics g) {
              g.drawImage(image.getImage(), 0, 40, 616, 510, null);
              super.paintComponent(g);

            }

          };

          jPanel1.setOpaque(false);
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jButton3 = new javax.swing.JButton();
        label1 = new java.awt.Label();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jComboBox1 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        jTable3.setModel(new javax.swing.table.DefaultTableModel(
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
        jScrollPane4.setViewportView(jTable3);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 621, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 552, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("House Overview", jPanel1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 644, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Temperature", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 644, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Humidity", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 644, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Movement", jPanel6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 644, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Smoke", jPanel7);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 644, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Luminosity", jPanel8);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 524, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Arduino", jPanel9);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        jTabbedPane1.addTab("Statistics", jPanel2);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Factor", "Condition", "Value"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                true, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
        }

        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Active", "Rule"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Boolean.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTable4);
        if (jTable4.getColumnModel().getColumnCount() > 0) {
            jTable4.getColumnModel().getColumn(0).setResizable(false);
            jTable4.getColumnModel().getColumn(1).setResizable(false);
        }

        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Load");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton5.setText("Save");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("Register");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel3.setText("Offline Message");

        jLabel4.setText("Online Message");

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel5.setText("Rule Condition Creator");

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel6.setText("Rule Registration");

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel7.setText("Rule's Manager");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jLabel6))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jLabel5))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jScrollPane5)
                        .addContainerGap())
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 135, Short.MAX_VALUE)
                                .addComponent(jButton7)))
                        .addGap(87, 87, 87))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jLabel7))
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 511, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(31, 31, 31)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton1)
                                    .addComponent(jButton8))))
                        .addContainerGap(36, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jButton1)
                        .addGap(18, 18, 18)
                        .addComponent(jButton8))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jLabel6)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel4)
                        .addComponent(jLabel3))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton7))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jButton5))
                .addGap(32, 32, 32))
        );

        jTabbedPane1.addTab("Control Panel", jPanel3);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Agent", "Type", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTable1);
        if (jTable1.getColumnModel().getColumnCount() > 0) {
            jTable1.getColumnModel().getColumn(0).setResizable(false);
            jTable1.getColumnModel().getColumn(1).setResizable(false);
            jTable1.getColumnModel().getColumn(2).setResizable(false);
        }

        jTextArea1.setEditable(false);
        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        jButton3.setText("Scan");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        label1.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        label1.setText("Smart Sensors");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Console Log");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Agent Overview");

        jButton4.setText("Create");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Smoke", "Luminosity", "Humidity", "Movement", "Temperature" }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel1.setText("Create new Agent");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        .addComponent(jLabel8)
                                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(0, 0, Short.MAX_VALUE)
                                            .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jButton4))
                                        .addComponent(jTextField2))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addGap(105, 105, 105)))
                                .addGap(14, 14, 14)))
                        .addGap(18, 18, 18)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 626, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 580, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel8)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(7, 7, 7)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addComponent(jLabel1)
                                .addGap(4, 4, 4)
                                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton4)
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        sendMsg(".scan");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable2().getModel();
        Vector newRow = new Vector();
        newRow.add("time");
        newRow.add(">");
        newRow.add("00:00");
        defaultModel.addRow(newRow);
        this.agente.menu.getjTable2().setModel(defaultModel);
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed

        Boolean flag;
        RuleCondition ruleCond;
        Rule rule;
        HashMap <String, RuleCondition> conds = new HashMap<>();
        int currentTimeFactor = 0;

        if (this.getjTable2().getRowCount() > 0)
            for(int i = 0; i < this.getjTable2().getRowCount(); i++)
            {
                flag = this.getjTable2().getValueAt(i, 1).toString().equals(">");
                String factorName = (String)this.getjTable2().getValueAt(i, 0);
                String value = (String) this.getjTable2().getValueAt(i, 2);

                if (!value.equals("") && !factorName.equals(""))
                {
                    if (factorName.equals("time"))
                    {
                        Pattern r = Pattern.compile("[0-9]{2}\\:[0-9]{2}");
                        Matcher m = r.matcher(value);
                        if (!m.matches()){
                           JFrame parent = new JFrame();
                           JOptionPane.showMessageDialog(parent,
                            "Must provide valid value for the given factorName.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                            return;
                            }
                        if (conds.containsKey("time")) factorName += ++currentTimeFactor;
                    }
                    else
                    {
                        Pattern r = Pattern.compile("[0-9]+");
                        Matcher m = r.matcher(value);
                        if (!m.matches()){
                           JFrame parent = new JFrame();
                           JOptionPane.showMessageDialog(parent,
                            "Must provide valid value for the given factorName.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                            return;
                            }
                    }

                    ruleCond = new RuleCondition(factorName, flag, value);
                    conds.put(factorName, ruleCond);
                }
                else{
                           JFrame parent = new JFrame();
                           JOptionPane.showMessageDialog(parent,
                            "Must provide a value.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                            return;
                        }
            }

        rule = new Rule(Boolean.FALSE, conds, this.getjTextField3().getText(), this.getjTextField1().getText());
        this.agente.automationProfile.add(rule);

        DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable4().getModel();
        Vector newRow = new Vector();
        newRow.add(rule.getActive());
        newRow.add(rule.ruleString());
        defaultModel.addRow(newRow);

    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:

        JFileChooser fc = new JFileChooser();
        fc.showSaveDialog(this);

        File selFile = fc.getSelectedFile();
        String filePath;
        filePath = selFile.getPath();
        //falta printar log
        if (ProfileManager.saveProfile(this.agente.automationProfile,filePath))
        System.out.println("Saved!");
        else System.out.println("Could not save file!");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);

        File selFile = fc.getSelectedFile();

        if (selFile == null)
            return;

        System.out.println(selFile);

        String filePath;
        filePath = selFile.getPath();

        this.agente.automationProfile = ProfileManager.loadProfile(filePath);

        DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable4().getModel();
        int i = this.agente.menu.getjTable4().getRowCount();

        while (i > 0){
            defaultModel.removeRow(i-1);
            i--;
        }

        i = this.agente.automationProfile.size();
        while(i > 0 ){
            Vector newRow = new Vector();
            newRow.add(this.agente.automationProfile.get(i-1).getActive());
            newRow.add(this.agente.automationProfile.get(i-1).ruleString());
            defaultModel.addRow(newRow);

            System.out.println(this.agente.automationProfile.get(i-1).ruleString());
            i--;
        }

        this.agente.menu.getjTable4().setModel(defaultModel);
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable2().getModel();
        Vector newRow = new Vector();
        int selRow = this.jTable1.getSelectedRow();
        if (selRow < 0){
                           JFrame parent = new JFrame();
                           JOptionPane.showMessageDialog(parent,
                            "Must select a sensor so you can link it to a rule.",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                            return;
                        }
        newRow.add(this.jTable1.getValueAt(selRow, 0));
        newRow.add(">");
        newRow.add("");
        defaultModel.addRow(newRow);
        this.agente.menu.getjTable2().setModel(defaultModel);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        String res = null;
        switch(jComboBox1.getSelectedItem().toString()){
                case "Temperature" : res="temp"; break;
                case "Humidity" :  res="humi"; break;
                case "Movement" :  res="move"; break;
                case "Smoke" :  res="smoke"; break;
                case "Luminosity" :  res="lumi"; break;
                }

        sendMsg("create."+jTextField2.getText()+"."+res);

// TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Menu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {

                new Menu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private java.awt.Label label1;
    // End of variables declaration//GEN-END:variables
}
