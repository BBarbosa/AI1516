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
    
    @Override
    public void action()
    {
        ACLMessage msg = agente.receive( or(
                MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
                MessageTemplate.MatchPerformative( ACLMessage.FAILURE )));

        if (msg != null)
        {
            System.out.println("Request "+msg.getConversationId()+" done. Content: "+msg.getContent()+"\n*\n");
            
            if (msg.getPerformative() == ACLMessage.FAILURE )
                printLog("Failed request "+agente.requestMap.get(Integer.parseInt(msg.getConversationId())));
            
            if (msg.getContent() != null)
            {    
                // if scan found something
                int currentLine = 0;
                
                String[] agentTypes = msg.getContent().split("[\n]");
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

            agente.requestMap.remove(Integer.parseInt(msg.getConversationId()));
        }
        
        block();
    }
    
}
