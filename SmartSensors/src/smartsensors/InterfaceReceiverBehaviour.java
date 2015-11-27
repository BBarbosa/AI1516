package smartsensors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JTable;
import javax.swing.JTextField;

public class InterfaceReceiverBehaviour extends CyclicBehaviour
{
    private InterfaceAgent agente;
    
    public InterfaceReceiverBehaviour(InterfaceAgent a)
    {
        agente = a;
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
        int padding =20;
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

                JTextField label = new JTextField();
                label.setBounds(0, padding , 100, 20);
                label.setName(agentNames[j]);
                this.agente.menu.getjPanel1().add(label);
                label.setVisible(false);

                this.agente.labels.put((String) agentNames[j], label);
            }
            currentLine += agentNames.length - 1;
            padding=padding*2;
        }
        printLog("Scanned Sensors!");
    }
    
    private void processRules(String sensorName, String content)
    {
        for (Rule r : agente.automationProfile)
        {
            // Process rule
            if (r.getActive())
            {
                // if a rule sensor is offline, skip evaluation
                Boolean carryOn = true;
                for (String ruleSensor : r.getRuleSensors())
                    if (!ruleSensor.equals("time") && !agente.activeSensors.contains(ruleSensor))
                    {
                        carryOn = false;
                        r.setOn(false);
                        System.out.println("RuleSensor: "+ruleSensor);
                    }

                if (carryOn)
                {
                    String evalResult = r.evaluateRule(sensorName, content);
                    if (evalResult != null)
                        printLog(evalResult);   
                }
            }
            else
                r.setOn(false);
        }
    }
    
    public void processSensorValue(String content, Integer id)
    {
        String sensorName = agente.requestMap.get(id).split("[.]")[0];
        
        // refresh value   
        System.out.println("SENSOR:"+sensorName);
        System.out.println("SENSOR VALUE: "+content);
        
        this.agente.labels.get(sensorName).setText("Sensor: "+sensorName+" = "+content);

        Matcher m = Pattern.compile("[0-9]").matcher(content);
        
        if (m.find())
        {
            //sensorName example: sensorType-div
            String type = sensorName.split("-")[0]; 
            String div = sensorName.split("-")[1];
            switch(type) {
                case "temp" : agente.menu.addTemp(Integer.parseInt(content), div); break;
                case "humi" : agente.menu.addHum(Integer.parseInt(content), div); break;
                case "mov" : agente.menu.addMov(Integer.parseInt(content), div); break;
                case "smoke" : agente.menu.addSmoke(Integer.parseInt(content), div); break;
                case "lumi" : agente.menu.addLum(Integer.parseInt(content), div); break; 
            }
        }
        
        processRules(sensorName, content);
    }
    
    public void processStatus(String content)
    {
        String[] tokens = content.split("[.]");
        printLog("Agent "+tokens[0]+" is now "+tokens[1]+"!");
        
        Boolean checkboxValue = true;
        
        if (tokens[1].equals("online")) 
            agente.activeSensors.add(tokens[0]);
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
        ACLMessage msg = agente.receive( or(
            or( MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
                MessageTemplate.MatchPerformative( ACLMessage.NOT_UNDERSTOOD )),
            or( MessageTemplate.MatchPerformative( ACLMessage.CONFIRM ),
                MessageTemplate.MatchPerformative( ACLMessage.FAILURE ))
            ));
        
        if (msg != null)
        {
            String requestContent = agente.getRequestContent(Integer.parseInt(msg.getConversationId()));
            System.out.println("Request "+msg.getConversationId()+" done. Content: "+requestContent+"\n*\n");

            switch (msg.getPerformative())
            {
                case ACLMessage.INFORM:
                    if (requestContent.contains("scan"))
                        processScan(msg.getContent());
                    else
                        processSensorValue(msg.getContent(), Integer.parseInt(msg.getConversationId()));
                    break;
                    
                case ACLMessage.CONFIRM:
                    processStatus(requestContent);
                    break;
                
                case ACLMessage.FAILURE:
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
