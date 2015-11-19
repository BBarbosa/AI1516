package smartsensors;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;

public class InterfaceReceiverBehaviour extends CyclicBehaviour
{
    private InterfaceAgent agente;
    
    public InterfaceReceiverBehaviour(InterfaceAgent a)
    {
        agente = a;
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
            
            // if scan found something
            if (!msg.getContent().equals(""))
            {    
                int currentLine = 0;
                
                String[] agentTypes = msg.getContent().split("[\n]");
                for (int i = 0; i < agentTypes.length; i++)
                {
                    String[] agentNames = agentTypes[i].split("[.]");
                    for (int j = 1; j < agentNames.length; j++)
                    {
                        agente.menu.getjTable1().setValueAt(agentNames[j], currentLine + (j-1), 0);
                        agente.menu.getjTable1().setValueAt(agentNames[0], currentLine + (j-1), 1); 
                    }
                    currentLine += agentNames.length - 1;
                }
            }
        }
        
        block();
    }
    
}
