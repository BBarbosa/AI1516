package smartsensors;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;

public class InterfaceReceiverBehaviour extends CyclicBehaviour
{
    private Agent agente;
    
    public InterfaceReceiverBehaviour(Agent a)
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
            System.out.println("Request "+msg.getConversationId()+" done. "+msg.getContent()+"\n*\n");
        }
        
        block();
    }
    
}
