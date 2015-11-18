package smartsensors;

import jade.core.AID;
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
            /*
            AID receiver = new AID();
            receiver.setLocalName("graphic");

            ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
            msg2.setConversationId(msg.getConversationId());
            msg2.addReceiver(receiver);
            
            msg2.setContent("Request "+msg.getConversationId()+" done. "+msg.getContent()+"\n*\n");
           
            agente.send(msg2);
            */
            
            System.out.println("Request "+msg.getConversationId()+" done. \n"+msg.getContent()+"\n*\n");
        }
        
        block();
    }
    
}
