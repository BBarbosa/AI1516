package smartsensors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import java.util.HashMap;
import javax.swing.JTable;
import javax.swing.JTextField;
import smartsensors.ProfileManager;

public class InterfaceReceiverBehaviour extends CyclicBehaviour
{
    private InterfaceAgent agente;
    private int fst;
    private Rule r1;
    private Rule r2;
    
    public InterfaceReceiverBehaviour(InterfaceAgent a)
    {
        fst = 0;
        agente = a;
        
        // TEST RULE
        RuleCondition rc1 = new RuleCondition("r1temp", true, 20);
        RuleCondition rc2 = new RuleCondition("r2temp", true, 20);
        HashMap<String, RuleCondition> myConditions = new HashMap<>();
        myConditions.put("r1temp",rc1);
        myConditions.put("r2temp",rc2);
        
        r1 = new Rule(true, myConditions,"Ligou", "Desligou");
        
        if (ProfileManager.saveProfile(r1,"myProfile"))
            r2 = ProfileManager.loadProfile("myProfile");
        else System.out.println("Could not load file!");
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
        String[] agentTypes = content.split("[\n]");
        DefaultTableModel defaultModel = (DefaultTableModel) this.agente.menu.getjTable1().getModel();
        int padding =20;            
        for (String at : agentTypes)
        {
            String[] agentNames = at.split("[.]");
            for (int j = 1; j < agentNames.length; j++)
            {
                if(fst == 0){
                
                    Vector newRow = new Vector();
                    newRow.add(agentNames[j]);
                    newRow.add(agentNames[0]);
                    newRow.add(false);
                    defaultModel.addRow(newRow);  
                    this.agente.menu.getjTable1().setModel(defaultModel);
                    
                     
            JTextField label = new JTextField();
            label.setBounds(0, padding , 100, 20);
            label.setName(agentNames[j]);
            this.agente.menu.getjPanel1().add(label);
            label.setVisible(false);
            
            this.agente.labels.put((String) agentNames[j], label);
                    
                    
                    
                }else{
                    agente.menu.getjTable1().setValueAt(agentNames[j], currentLine + (j-1), 0);
                    agente.menu.getjTable1().setValueAt(agentNames[0], currentLine + (j-1), 1);
                }
            }
            currentLine += agentNames.length - 1;
            padding=padding*2;
        }
        fst = 1;
        printLog("Scanned Sensors!");
    }
    
    public void processSensorValue(String content, Integer id)
    {
        String sensorName = agente.requestMap.get(id).split("[.]")[0];
        
        // refresh value   
        System.out.println("SENSOR:"+sensorName);
        System.out.println("SENSOR VALUE: "+content);
        
        
        
        this.agente.labels.get(sensorName).setText("Sensor: "+sensorName+" = "+content);
        
        //estoura se receber XXXXX
        agente.menu.addTemp(Integer.parseInt(content), "Garagem");
        
        // TODO: Iterate through rules
        // Process rule
        if (r2.getActive())
        {
            // if a rule sensor is offline, skip evaluation
            Boolean carryOn = true;
            for (String ruleSensor : r2.getRuleSensors())
                if (!agente.activeSensors.contains(ruleSensor))
                {
                    carryOn = false;
                    r2.setOn(false);
                }
            
            if (carryOn)
            {
                String evalResult = r2.evaluateRule(sensorName, content);
                if (evalResult != null)
                    printLog(evalResult);   
            }
        }
        else
            r2.setOn(false);
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
        // com o nº de linhas ajustado ao nº de sensores pode-se remover esta segunda pate
        for(int i = 0; i < table.getRowCount() && table.getValueAt(i, 0)!=null; i++)
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
            String requestContent = agente.requestMap.get(Integer.parseInt(msg.getConversationId()));
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
