package smartsensors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;

public class InterfaceReceiverBehaviour extends CyclicBehaviour
{
    private InterfaceAgent agente;
    private int sensorsToUpdate;
    private HashMap<String, Integer> valuesToUpdate;
    public int fst;
    
    public InterfaceReceiverBehaviour(InterfaceAgent a)
    {
        fst = 0;
        agente = a;
        sensorsToUpdate = 0;
        valuesToUpdate = new HashMap<>();
    }

    public void printLog(String txt)
    {
        String t = agente.menu.getjTextArea1().getText();
        String logTime = new SimpleDateFormat("HH:mm:ss").format(new Date());
        
        String output = t + logTime + "   ==>   " + txt + "\n";
        agente.menu.getjTextArea1().setText(output);
    }
    
    public void processScan(String content)
    {
        // if scan found something
        int currentLine = 0;
        DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable1().getModel();
        int padding =0;
        defaultModel.setRowCount(0);
        
        String[] agentTypes = content.split("[\n]");
        
        for (String at : agentTypes)
        {
            String[] agentNames = at.split("[.]");
            for (int j = 1; j < agentNames.length; j++)
            {
                // refresh table
                Vector newRow = new Vector();
                newRow.add(agentNames[j]);
                newRow.add(agentNames[0]);
                newRow.add(agente.activeSensors.contains(agentNames[j]));
                defaultModel.addRow(newRow);  
                this.agente.menu.getjTable1().setModel(defaultModel);
                
                agente.sensorTypes.put(agentNames[j], agentNames[0]);
                
                if (fst == 0){
                    JButton label = new JButton();
                    label.setBounds(padding, 0 , 40, 40);
                    label.setName(agentNames[j]);
                    
                    Path path = Paths.get("images/"+agentNames[0]+".png");

                    if (Files.exists(path)) {
                    // file exist
                        ImageIcon img;
       
                        img = new ImageIcon("images/"+agentNames[0]+".png");

                        label.setIcon(img);
                    }
                    
                    else{
                        ImageIcon img;
       
                        img = new ImageIcon("images/sensor.png");

                        label.setIcon(img);
                   }
                    
            
                    label.addMouseMotionListener(new MouseAdapter(){

                            @Override
                            public void mouseDragged(MouseEvent E)
                            {
                               int X=E.getX()+label.getX();
                               int Y=E.getY()+label.getY();
                               label.setBounds(X,Y,150,60);
                            }
                        });


                    this.agente.menu.getjPanel1().add(label);
                    label.setVisible(false);
                    this.agente.labels.put((String) agentNames[j], label);
                }
               
            }
            currentLine += agentNames.length - 1;
            padding=padding+40;
            
        }
        fst = 1;
        printLog("Scanned Sensors!");
    }
    
    public void updateCharts()
    {
        for (Entry<String,Integer> e: valuesToUpdate.entrySet())
        {
            String type = agente.sensorTypes.get(e.getKey());

            switch(type) {
                case "temp" : agente.menu.addTemp(e.getValue(), e.getKey()); break;
                case "humi" : agente.menu.addHum(e.getValue(), e.getKey()); break;
                case "move" : agente.menu.addMov(e.getValue(), e.getKey()); break;
                case "smoke" : agente.menu.addSmoke(e.getValue(), e.getKey()); break;
                case "lumi" : agente.menu.addLum(e.getValue(), e.getKey()); break;
                case "arduino" : agente.menu.addArduino(e.getValue(), e.getKey()); break;
            }
        }
    }
    
    public void processSensorValue(String content, Integer id)
    {
        String sensorName = agente.requestMap.get(id).split("[.]")[0];
        
        // refresh value   
        System.out.println("SENSOR:"+sensorName);
        System.out.println("SENSOR VALUE: "+content);
        
        this.agente.labels.get(sensorName).setText(sensorName+" = "+content);

        Matcher m = Pattern.compile("[0-9]").matcher(content);
        valuesToUpdate.put(sensorName, (m.find()) ? Integer.parseInt(content) : null);
        
        if (++sensorsToUpdate >= agente.activeSensors.size())
        {
            updateCharts();
            sensorsToUpdate = 0;
        }
        
        // refresh rule conditions with new value
        for (Rule r : agente.automationProfile)
            if (r.getRuleSensors().contains(sensorName))
                r.refreshRuleConditions(sensorName, content);
    }
    
    public void processStatus(String content)
    {
        String[] tokens = content.split("[.]");
        printLog("Agent "+tokens[0]+" is now "+tokens[1]+"!");
        
        Boolean checkboxValue = true;
        
        if (tokens[1].equals("online"))
            agente.activeSensors.add(tokens[0]);
        else if (tokens[1].equals("shutdown")){
           agente.activeSensors.remove(tokens[0]);
           agente.sensorTypes.remove(tokens[0]);
           agente.labels.get(tokens[0]).setVisible(false);
        }
        else
        {
            agente.activeSensors.remove(tokens[0]);
            checkboxValue = false;
        }
        
        JTable table = agente.menu.getjTable1();

        for(int i = 0; i < table.getRowCount(); i++)
            if(table.getValueAt(i, 0).equals(tokens[0]))
                table.setValueAt(checkboxValue, i, 2);
    }
    
    @Override
    public void action()
    {
        ACLMessage msg = agente.receive(
            or( or( MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
                    MessageTemplate.MatchPerformative( ACLMessage.NOT_UNDERSTOOD )),
                or( MessageTemplate.MatchPerformative( ACLMessage.CONFIRM ),
                    MessageTemplate.MatchPerformative( ACLMessage.FAILURE ))
            )
        );
        
        if (msg != null)
        {
            String requestContent = agente.getRequestContent(Integer.parseInt(msg.getConversationId()));
            System.out.println("Request "+msg.getConversationId()+" done. Content: "+requestContent+"\n*\n");

            switch (msg.getPerformative())
            {
                case ACLMessage.INFORM:
                    if (requestContent.contains("scan"))
                        processScan(msg.getContent());
                    else if (requestContent.contains("rule"))
                        printLog(requestContent.split("[.]")[1]);
                    else if (requestContent.contains("create")){
                        String[] aux;
                        aux =  requestContent.split("\\.");
                        
                        JButton label = new JButton();
                        label.setBounds(100, 0 , 40, 40);
                        label.setName(aux[1]);

                        Path path = Paths.get("images/"+aux[2]+".png");

                        if (Files.exists(path)) {
                        // file exist
                            ImageIcon img;

                            img = new ImageIcon("images/"+aux[2]+".png");

                            label.setIcon(img);
                        }

                        else{
                            ImageIcon img;

                            img = new ImageIcon("images/sensor.png");

                            label.setIcon(img);
                        }
                    
            
                        label.addMouseMotionListener(new MouseAdapter(){

                                @Override
                                public void mouseDragged(MouseEvent E)
                                {
                                   int X=E.getX()+label.getX();
                                   int Y=E.getY()+label.getY();
                                   label.setBounds(X,Y,150,60);
                                }
                            });


                        this.agente.menu.getjPanel1().add(label);
                        label.setVisible(false);
                        this.agente.labels.put((String) aux[1], label);
                             DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable1().getModel();
                                Vector newRow = new Vector();
                                newRow.add(aux[1]);
                                newRow.add(aux[2]);
                                newRow.add(false);
                                defaultModel.addRow(newRow);
                                this.agente.menu.getjTable1().setModel(defaultModel);
                            
                        agente.sensorTypes.put(aux[1],aux[2]);
                    }
                    else
                        processSensorValue(msg.getContent(), Integer.parseInt(msg.getConversationId()));
                    break;
                    
                case ACLMessage.CONFIRM:
                    processStatus(requestContent);
                    break;
                    
                case ACLMessage.FAILURE:
                    if (msg.getContent() != null && msg.getContent().contains("timeout"))
                        printLog("Failed request "+msg.getConversationId()+" - "
                            +requestContent+". Reason: Request timeout (10 seconds)");
                    
                    printLog("Failed request "+msg.getConversationId()+" - "
                            +requestContent+". Reason: "+msg.getContent());
                    if (requestContent.contains("value"))
                        agente.activeSensors.remove(requestContent.split("[.]")[0]);
                    break;
                    
                case ACLMessage.NOT_UNDERSTOOD:
                    printLog("Failed request "+msg.getConversationId()+" - "
                            +requestContent+". Reason: "+msg.getContent());
                    break;
            }

            agente.removeRequest(Integer.parseInt(msg.getConversationId()));
        }
        
        block();
    }
    
}
