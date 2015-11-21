package smartsensors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JTextArea;

public class InterfaceReceiverBehaviour extends CyclicBehaviour
{
    private InterfaceAgent agente;
    public int fst;

    public InterfaceReceiverBehaviour(InterfaceAgent a)
    {
        agente = a;
        fst = 0;
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
<<<<<<< HEAD
=======

>>>>>>> origin/master
        String[] agentTypes = content.split("[\n]");
        for (String at : agentTypes)
        {
            String[] agentNames = at.split("[.]");
            for (int j = 1; j < agentNames.length; j++)
            {
                agente.menu.getjTable1().setValueAt(agentNames[j], currentLine + (j-1), 0);
                agente.menu.getjTable1().setValueAt(agentNames[0], currentLine + (j-1), 1); 
<<<<<<< HEAD

                System.out.println(this.agente.activeAgentsBehaviour.toString());
                if(fst==0){
                        this.agente.activeAgentsBehaviour.put(agentNames[j], "0");
                            
                        }
                        
                        if(this.agente.activeAgentsBehaviour.get(agentNames[j]).equals("0")){
                            
                          agente.menu.getjTable1().setValueAt("Offline", currentLine + (j-1), 2);
                        }
                        
                        if(this.agente.activeAgentsBehaviour.get(agentNames[j]).equals("1")){
                          agente.menu.getjTable1().setValueAt("Online", currentLine + (j-1), 2);
                        }


            }
            currentLine += agentNames.length - 1;
            
        }
        fst = 1;
=======
            }
            currentLine += agentNames.length - 1;
        }

>>>>>>> origin/master
        printLog("Scanned Sensors!");
    }
    
    public void refreshSensorValue(String content)
    {
        // TODO
    }
    
    public void processStatus(String content)
    {
        String[] tokens = content.split("[.]");
        printLog("Agent "+tokens[0]+" is now "+tokens[1]+"!");
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
<<<<<<< HEAD


=======
>>>>>>> origin/master

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
                        refreshSensorValue(msg.getContent());
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

            agente.requestMap.remove(Integer.parseInt(msg.getConversationId()));
        }
        
        block();
    }
    
}
