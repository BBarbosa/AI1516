package smartsensors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import static jade.lang.acl.MessageTemplate.or;

public class RelayerBehaviour extends CyclicBehaviour
{
    private Agent agente;
    
    public RelayerBehaviour(Agent a)
    {
        agente = a;
    }

    private void sendMsg(String agentName, String convoId, String msgContent, int performative)
    {
        AID receiver = new AID();
        receiver.setLocalName(agentName);

        ACLMessage msg = new ACLMessage(performative);
        msg.setConversationId(convoId);
        msg.addReceiver(receiver);
        
        msg.setContent(msgContent);

        myAgent.send(msg);
    }
    
    @Override
    public void action()
    {
        ACLMessage msg = agente.receive(
            or(
                or( MessageTemplate.MatchPerformative( ACLMessage.INFORM ),
                    MessageTemplate.MatchPerformative( ACLMessage.NOT_UNDERSTOOD )),
                or( MessageTemplate.MatchPerformative( ACLMessage.CONFIRM ),
                    MessageTemplate.MatchPerformative( ACLMessage.FAILURE ))
                ));

        if (msg != null && ControllerAgent.convoIds.contains(msg.getConversationId()))
        {
            System.out.println("Relaying request "+msg.getConversationId()+". Status: "+msg.getPerformative());
            ControllerAgent.convoIds.remove(msg.getConversationId());
            sendMsg("interface", msg.getConversationId(), msg.getContent(), msg.getPerformative());
        }
        
        block();
    }
    
}
