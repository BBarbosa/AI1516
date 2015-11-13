package smartsensors;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class RelayerBehaviour extends CyclicBehaviour
{
    private Agent agente;
    
    public RelayerBehaviour(Agent a)
    {
        agente = a;
    }

    @Override
    public void action()
    {
        ACLMessage msg = agente.receive(
                MessageTemplate.MatchPerformative( ACLMessage.INFORM ));

        if (msg != null)
        {
            System.out.println("Request "+msg.getConversationId()+" result: "+msg.getContent());
        }
        
        block();
    }
    
}
