package smartsensors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class InterfaceRequesterBehaviour extends CyclicBehaviour
{
    private Agent agente;
    
    public InterfaceRequesterBehaviour(Agent a)
    {
        agente = a;
    }
    
    private void sendMsg(String agentName, String convoId, String msgContent)
    {
        AID receiver = new AID();
        receiver.setLocalName(agentName);

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setConversationId(convoId);
        msg.addReceiver(receiver);

        msg.setContent(msgContent);

        myAgent.send(msg);
    }
    
    @Override
    public void action()
    {
        ACLMessage msg = agente.receive(MessageTemplate.MatchPerformative( ACLMessage.REQUEST ));

        if (msg != null)
        {
            System.out.println("Sending message to controller");
            sendMsg("controller", msg.getConversationId(), msg.getContent());
        }
        
        block();
    }
}
