package smartsensors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;
import java.text.SimpleDateFormat;
import java.util.Date;

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
    
    public void refreshSensorValue(String content, Integer id)
    {
        // TODO
        System.out.println("SENSOR:"+agente.requestMap.get(id));
        System.out.println("SENSOR VALUE: "+content);
        
        agente.menu.getjTextField2().setText(content);
        agente.menu.getjLabel1().setText(agente.requestMap.get(id));
        
    }
    
    public void processStatus(String content)
    {
        String[] tokens = content.split("[.]");
        printLog("Agent "+tokens[0]+" is now "+tokens[1]+"!");
        int i;
        i=0;
        if (tokens[1].equals("online")){
            agente.activeSensors.add(tokens[0]);
           
             
            while(i<agente.menu.getjTable1().getRowCount()&& agente.menu.getjTable1().getValueAt(i, 0)!=null){
                if(agente.menu.getjTable1().getValueAt(i, 0).equals(tokens[0])){
                    System.out.println(tokens[0]+" VAI FICAR ONLINE PROCSTAT");
                    agente.menu.getjTable1().setValueAt(true, i, 2);}
                
                i++;
            }
                    
        }
        else{
            agente.activeSensors.remove(tokens[0]);
            
            System.out.println(tokens[0]);
             
            while(i<agente.menu.getjTable1().getRowCount()&& agente.menu.getjTable1().getValueAt(i, 0)!=null){
                if(agente.menu.getjTable1().getValueAt(i, 0).equals(tokens[0])){
                    
                    System.out.println(tokens[0]+" VAI FICAR OFFLINE PROCSTAT");
                    agente.menu.getjTable1().setValueAt(false, i, 2);
                }
                i++;
            }
        
        }
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
                        refreshSensorValue(msg.getContent(), Integer.parseInt(msg.getConversationId()));
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
