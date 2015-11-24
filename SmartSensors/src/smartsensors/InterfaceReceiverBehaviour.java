package smartsensors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.JTable;

public class InterfaceReceiverBehaviour extends CyclicBehaviour
{
    private InterfaceAgent agente;
    private ArrayList<Rule> automationProfile;
    private Rule r1;
    
    public InterfaceReceiverBehaviour(InterfaceAgent a)
    {
        agente = a;
        automationProfile = new ArrayList<>();
        
        // TEST RULE
        RuleCondition rc1 = new RuleCondition("r1temp", true, 0);
        RuleCondition rc2 = new RuleCondition("r2temp", true, 0);
        HashMap<String, RuleCondition> myConditions = new HashMap<>();
        myConditions.put("r1temp",rc1);
        myConditions.put("r2temp",rc2);
        
        r1 = new Rule(true, myConditions,"Ligou", "Desligou");
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
        for (String at : agentTypes)
        {
            String[] agentNames = at.split("[.]");
            for (int j = 1; j < agentNames.length; j++)
            {
                agente.menu.getjTable1().setValueAt(agentNames[j], currentLine + (j-1), 0);
                agente.menu.getjTable1().setValueAt(agentNames[0], currentLine + (j-1), 1);
            }
            currentLine += agentNames.length - 1;
        }
        printLog("Scanned Sensors!");
    }
    
    public void processSensorValue(String content, Integer id)
    {
        String sensorName = agente.requestMap.get(id).split("[.]")[0];
        
        // refresh value   
        System.out.println("SENSOR:"+sensorName);
        System.out.println("SENSOR VALUE: "+content);
        
        agente.menu.getjTextField2().setText(content);
        agente.menu.getjLabel1().setText(agente.requestMap.get(id));
        
        // TODO: Iterate through rules
        // Process rule
        if (r1.getActive())
        {
            // if a rule sensor is offline, skip evaluation
            Boolean carryOn = true;
            for (String ruleSensor : r1.getRuleSensors())
                if (!agente.activeSensors.contains(ruleSensor))
                {
                    carryOn = false;
                    r1.setOn(false);
                }
            
            if (carryOn)
            {
                String evalResult = r1.evaluateRule(sensorName, content);
                if (evalResult != null)
                    printLog(evalResult);   
            }
        }
        else
            r1.setOn(false);
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
