package smartsensors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.MatchPerformative;

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
        ACLMessage msg = agente.receive(MessageTemplate.MatchPerformative( ACLMessage.INFORM ));

        if (msg != null)
        {
            System.out.println("Request "+msg.getConversationId()+" result: "+msg.getContent());
        }
        
        block();
    }
    
}
