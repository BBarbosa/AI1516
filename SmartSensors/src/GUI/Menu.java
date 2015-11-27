package GUI;

import jade.core.AID;
import jade.core.Agent;
import jade.core.AgentContainer;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Label;
import java.awt.Toolkit;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import smartsensors.InterfaceAgent;
import smartsensors.ProfileManager;
import smartsensors.Rule;
import smartsensors.RuleCondition;
import smartsensors.TempSensor;

public class Menu extends javax.swing.JFrame {
    
    private InterfaceAgent agente;
    /* temperature */
    private TimeSeries tempLRoomDataset;
    private TimeSeries tempKitchenDataset;
    private TimeSeries tempGarageDataset;
    private TimeSeries tempBr1Dataset;
    private TimeSeries tempBr2Dataset;
    /* humidity */
    private TimeSeries humLRoomDataset;
    private TimeSeries humKitchenDataset;
    private TimeSeries humGarageDataset;
    private TimeSeries humBr1Dataset;
    private TimeSeries humBr2Dataset;
    /* movement */
    private TimeSeries movLRoomDataset;
    private TimeSeries movKitchenDataset;
    private TimeSeries movGarageDataset;
    private TimeSeries movBr1Dataset;
    private TimeSeries movBr2Dataset;
    /* smoke */
    private TimeSeries smokeLRoomDataset;
    private TimeSeries smokeKitchenDataset;
    private TimeSeries smokeGarageDataset;
    private TimeSeries smokeBr1Dataset;
    private TimeSeries smokeBr2Dataset;
    /* luminosity */
    private TimeSeries lumLRoomDataset;
    private TimeSeries lumKitchenDataset;
    private TimeSeries lumGarageDataset;
    private TimeSeries lumBr1Dataset;
    private TimeSeries lumBr2Dataset;
    
    /**
     * Creates new form Menu
     */
    public Menu() {
        initComponents();
        ImageIcon image = new ImageIcon("images/house_plan_1.jpg"); 
        JLabel label = new JLabel();
        label.setBounds(0, 0, 754, 510);
        label.setIcon(image);
        
        this.jPanel1.add( label );
        /* temperature */
        this.tempLRoomDataset = new TimeSeries("Living Room");
        this.tempKitchenDataset = new TimeSeries("Kitchen");
        this.tempGarageDataset = new TimeSeries("Garage");
        this.tempBr1Dataset = new TimeSeries("Bedroom 1");
        this.tempBr2Dataset = new TimeSeries("Bedroom 2");
        /* humidity */
        this.humLRoomDataset = new TimeSeries("Living Room");
        this.humKitchenDataset = new TimeSeries("Kitchen");
        this.humGarageDataset = new TimeSeries("Garage");
        this.humBr1Dataset = new TimeSeries("Bedroom 1");
        this.humBr2Dataset = new TimeSeries("Bedroom 2");
        /* movement */
        this.movLRoomDataset = new TimeSeries("Living Room");
        this.movKitchenDataset = new TimeSeries("Kitchen");
        this.movGarageDataset = new TimeSeries("Garage");
        this.movBr1Dataset = new TimeSeries("Bedroom 1");
        this.movBr2Dataset = new TimeSeries("Bedroom 2");
        /* smoke */
        this.smokeLRoomDataset = new TimeSeries("Living Room");
        this.smokeKitchenDataset = new TimeSeries("Kitchen");
        this.smokeGarageDataset = new TimeSeries("Garage");
        this.smokeBr1Dataset = new TimeSeries("Bedroom 1");
        this.smokeBr2Dataset = new TimeSeries("Bedroom 2");
        /* luminosity */
        this.lumLRoomDataset = new TimeSeries("Living Room");
        this.lumKitchenDataset = new TimeSeries("Kitchen");
        this.lumGarageDataset = new TimeSeries("Garage");
        this.lumBr1Dataset = new TimeSeries("Bedroom 1");
        this.lumBr2Dataset = new TimeSeries("Bedroom 2");
        
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
                        
                    }
                    if(jTable1.getValueAt(row, col).equals(flag)){
                        
                        System.out.println("VAI FICAR oFFLINE ::: ESTA "+jTable1.getValueAt(row, col));
                        sendMsg(jTable1.getValueAt(row, 0).toString()+".offline");
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
    }
    
    /* UPDATE CHARTS */
    public void updateTempChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(tempLRoomDataset);
        dataset.addSeries(tempKitchenDataset);
        dataset.addSeries(tempGarageDataset);
        dataset.addSeries(tempBr1Dataset);
        dataset.addSeries(tempBr2Dataset);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Temperature", "Time", "Temperature", dataset, true, true, false);
        
        XYPlot catPlot = chart.getXYPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);
        
        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,616,482);
        jPanel4.removeAll();
        jPanel4.add(charPanel,BorderLayout.CENTER);
        jPanel4.validate();
    }
    
    public void updateHumChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(humLRoomDataset);
        dataset.addSeries(humKitchenDataset);
        dataset.addSeries(humGarageDataset);
        dataset.addSeries(humBr1Dataset);
        dataset.addSeries(humBr2Dataset);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Humidity", "Time", "Humidity", dataset, true, true, false);
        
        XYPlot catPlot = chart.getXYPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);
        
        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,616,482);
        jPanel5.removeAll();
        jPanel5.add(charPanel,BorderLayout.CENTER);
        jPanel5.validate();
    }
    
    public void updateMovChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(movLRoomDataset);
        dataset.addSeries(movKitchenDataset);
        dataset.addSeries(movGarageDataset);
        dataset.addSeries(movBr1Dataset);
        dataset.addSeries(movBr2Dataset);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Movement", "Time", "Movement", dataset, true, true, false);
        
        XYPlot catPlot = chart.getXYPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);
        
        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,616,482);
        jPanel6.removeAll();
        jPanel6.add(charPanel,BorderLayout.CENTER);
        jPanel6.validate();
    }
    
    public void updateSmokeChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(smokeLRoomDataset);
        dataset.addSeries(smokeKitchenDataset);
        dataset.addSeries(smokeGarageDataset);
        dataset.addSeries(smokeBr1Dataset);
        dataset.addSeries(smokeBr2Dataset);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Smoke", "Time", "Smoke", dataset, true, true, false);
        
        XYPlot catPlot = chart.getXYPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);
        
        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,616,482);
        jPanel7.removeAll();
        jPanel7.add(charPanel,BorderLayout.CENTER);
        jPanel7.validate();
    }
    
    public void updateLumChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.addSeries(lumLRoomDataset);
        dataset.addSeries(lumKitchenDataset);
        dataset.addSeries(lumGarageDataset);
        dataset.addSeries(lumBr1Dataset);
        dataset.addSeries(lumBr2Dataset);
        JFreeChart chart = ChartFactory.createTimeSeriesChart("Luminosity", "Time", "Luminosity", dataset, true, true, false);
        
        XYPlot catPlot = chart.getXYPlot();
        catPlot.setRangeGridlinePaint(Color.BLACK);
        
        ChartPanel charPanel = new ChartPanel(chart);
        charPanel.setBounds(0,0,616,482);
        jPanel8.removeAll();
        jPanel8.add(charPanel,BorderLayout.CENTER);
        jPanel8.validate();
    }
    
    /* ADD VALUES TO CHARTS */
    public void addTemp(int value, String div) {
        switch(div) {
            case "kitchen" :  this.tempKitchenDataset.addOrUpdate(new Second(), value); break;
            case "lroom" : this.tempLRoomDataset.addOrUpdate(new Second(), value); break;
            case "garage" : this.tempGarageDataset.addOrUpdate(new Second(), value); break;
            case "br1" : this.tempBr1Dataset.addOrUpdate(new Second(), value); break;
            case "br2" : this.tempBr2Dataset.addOrUpdate(new Second(), value); break;
        }
        this.updateTempChart();
    }
    
    public void addHum(int value, String div) {
        switch(div) {
            case "kitchen" :  this.humKitchenDataset.addOrUpdate(new Second(), value); break;
            case "lroom" : this.humLRoomDataset.addOrUpdate(new Second(), value); break;
            case "garage" : this.humGarageDataset.addOrUpdate(new Second(), value); break;
            case "br1" : this.humBr1Dataset.addOrUpdate(new Second(), value); break;
            case "br2" : this.humBr2Dataset.addOrUpdate(new Second(), value); break;
        }
        this.updateHumChart();
    }
    
    public void addMov(int value, String div) {
        switch(div) {
            case "kitchen" :  this.movKitchenDataset.addOrUpdate(new Second(), value); break;
            case "lroom" : this.movLRoomDataset.addOrUpdate(new Second(), value); break;
            case "garage" : this.movGarageDataset.addOrUpdate(new Second(), value); break;
            case "br1" : this.movBr1Dataset.addOrUpdate(new Second(), value); break;
            case "br2" : this.movBr2Dataset.addOrUpdate(new Second(), value); break;
        }
        this.updateMovChart();
    }
    
    public void addSmoke(int value, String div) {
        switch(div) {
            case "kitchen" :  this.smokeKitchenDataset.addOrUpdate(new Second(), value); break;
            case "lroom" : this.smokeLRoomDataset.addOrUpdate(new Second(), value); break;
            case "garage" : this.smokeGarageDataset.addOrUpdate(new Second(), value); break;
            case "br1" : this.smokeBr1Dataset.addOrUpdate(new Second(), value); break;
            case "br2" : this.smokeBr2Dataset.addOrUpdate(new Second(), value); break;
        }
        this.updateSmokeChart();
    }
    
    public void addLum(int value, String div) {
        switch(div) {
            case "kitchen" :  this.lumKitchenDataset.addOrUpdate(new Second(), value); break;
            case "lroom" : this.lumLRoomDataset.addOrUpdate(new Second(), value); break;
            case "garage" : this.lumGarageDataset.addOrUpdate(new Second(), value); break;
            case "br1" : this.lumBr1Dataset.addOrUpdate(new Second(), value); break;
            case "br2" : this.lumBr2Dataset.addOrUpdate(new Second(), value); break;
        }
        this.updateLumChart();
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
    
    
    
    public JTextField getjTextField2() {
        return jTextField2;
    }

    public void setjTextField2(JTextField jTextField2) {
        this.jTextField2 = jTextField2;
    }

    public Label getLabel1() {
        return label1;
    }

    public void setLabel1(Label label1) {
        this.label1 = label1;
    }

    public JLabel getjLabel1() {
        return jLabel1;
    }

    public void setjLabel1(JLabel jLabel1) {
        this.jLabel1 = jLabel1;
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
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel4 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel7 = new javax.swing.JPanel();
        jPanel8 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
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
        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        label1 = new java.awt.Label();
        jButton4 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        jTextField4 = new javax.swing.JTextField();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 621, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("House Overview", jPanel1);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Temperature", jPanel4);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Humidity", jPanel5);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Movement", jPanel6);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Smoke", jPanel7);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 616, Short.MAX_VALUE)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 482, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("Luminosity", jPanel8);

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

        jButton1.setText("Add");
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

        jButton6.setText("Scan");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setText("Register");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setText("Reset");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Remove");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
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
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel5)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(54, 54, 54)
                                        .addComponent(jButton7))))
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 74, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                        .addGap(3, 3, 3)
                                        .addComponent(jButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButton5)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel7)
                            .addComponent(jLabel6))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton8))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jButton2))
                .addContainerGap())
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

        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        jLabel1.setText("label");

        jButton3.setText("Scan");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        label1.setFont(new java.awt.Font("Consolas", 1, 24)); // NOI18N
        label1.setText("Smart Sensors");

        jButton4.setText("Quit");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel2.setText("Console Log");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jLabel8.setText("Agent Overview");

        jButton10.setText("Adicionar");
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 27, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(label1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 228, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel8))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTextField2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton10)
                        .addGap(52, 52, 52)
                        .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 626, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(label1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(40, 40, 40)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(13, 13, 13))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton10)
                                    .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(28, 28, 28))))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        sendMsg(".scan"); 
        int i = 0;
        while(i < this.agente.labels.size()){
             this.agente.labels.get(this.jTable1.getValueAt(i, 0).toString()).setVisible(false);
             i++;
        }
        
        i=0;
        while(i<this.agente.activeSensors.size()){
            if(this.agente.labels.containsKey(this.agente.activeSensors.get(i)) && this.agente.labels.get(this.agente.activeSensors.get(i)).isVisible() == false)    
                this.agente.labels.get(this.agente.activeSensors.get(i)).setVisible(true);
                
            else 
                this.agente.labels.get(this.agente.activeSensors.get(i)).setVisible(false);
            
            i++;
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
       
         JFileChooser fc = new JFileChooser();
        fc.showOpenDialog(this);
        File selFile = fc.getSelectedFile();
        String fileName;
        fileName = selFile.getName();
        this.agente.automationProfile = ProfileManager.loadProfile(fileName);
        //falta log
        System.out.println("Loaded!");
        
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
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
        
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        int i = 0;
        Boolean flag;
        RuleCondition ruleCond;
        Rule rule;
        HashMap <String, RuleCondition> conds = new HashMap<>();
        
        while(i < this.getjTable2().getRowCount() && this.getjTable2().getRowCount() > 0){
            
            if(this.getjTable2().getValueAt(i, 1).toString().equals(">")){ flag = true;}
            else{flag = false;}
            String factorName = (String)this.getjTable2().getValueAt(i, 0);
            ruleCond = new RuleCondition(factorName, flag, (String) this.getjTable2().getValueAt(i, 2));
            i++;
            conds.put(factorName, ruleCond);
        }
        
        rule = new Rule(Boolean.FALSE, conds, this.getjTextField3().getText(), this.getjTextField1().getText());
        this.agente.automationProfile.add(rule);
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
     DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable2().getModel();
     Vector newRow = new Vector();
                    newRow.add("");
                    newRow.add(">");
                    newRow.add("");
                    defaultModel.addRow(newRow); 
                    this.agente.menu.getjTable2().setModel(defaultModel);
                    
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
          DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable2().getModel();
        
          defaultModel.removeRow(defaultModel.getRowCount()-1);
          this.agente.menu.getjTable2().setModel(defaultModel);
        
    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
         DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable2().getModel();
         int i = this.agente.menu.getjTable2().getRowCount();

         while (i > 0  && this.agente.menu.getjTable2().getRowCount() > 0){
            defaultModel.removeRow(i-1);
            i--;
         }
         
          this.agente.menu.getjTable2().setModel(defaultModel);
        
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        
        //falta printar log
        if (ProfileManager.saveProfile(this.agente.automationProfile,"test"))
            System.out.println("Saved!");
        else System.out.println("Could not save file!");
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
        // TODO add your handling code here:
        
    }//GEN-LAST:event_jButton10ActionPerformed

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
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
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
    private javax.swing.JTextField jTextField4;
    private java.awt.Label label1;
    // End of variables declaration//GEN-END:variables
}
