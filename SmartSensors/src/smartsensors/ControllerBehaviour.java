package smartsensors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class ControllerBehaviour extends CyclicBehaviour
{
    private Agent agente;
    
    public ControllerBehaviour(Agent a)
    {
        agente = a;
    }
    
    private void sendMsg(String agentName, String convoId, String line)
    {
        AID receiver = new AID();
        receiver.setLocalName(agentName);

        ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
        msg.setConversationId(convoId);
        msg.addReceiver(receiver);

        msg.setContent("receiver");

        myAgent.send(msg);
    }
    
    @Override
    public void action()
    {
        ACLMessage msg = agente.receive(MessageTemplate.MatchPerformative( ACLMessage.REQUEST ));
        
        if (msg != null)
        {
            System.out.println("Recebi uma mensagem de "+msg.getSender()+". Conteudo: "+msg.getContent());

            sendMsg("interface", msg.getConversationId(), msg.getContent());
        }
        
        block();
    }
    
}

