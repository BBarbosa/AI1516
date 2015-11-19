package smartsensors;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class InterfaceRequesterBehaviour extends CyclicBehaviour
{
    private Integer currentConvoId;
    private InterfaceAgent agente;
    
    public InterfaceRequesterBehaviour(InterfaceAgent a)
    {
        currentConvoId = 0;
        agente = a;
    }
    
    private void sendMsg(String agentName, String msgContent)
    {
        AID receiver = new AID();
        receiver.setLocalName(agentName);

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        msg.setConversationId(++currentConvoId+"");
        msg.addReceiver(receiver);

        msg.setContent(msgContent);

        myAgent.send(msg);
        
        // registers request on requestmap
        agente.requestMap.put(currentConvoId,msg.getContent());
    }
    
    @Override
    public void onStart()
    {
        sendMsg("controller", ".scan");
    }
    
    @Override
    public void action()
    {
        ACLMessage msg = agente.receive(MessageTemplate.MatchPerformative( ACLMessage.REQUEST ));
        
        if (msg != null)
        {
            sendMsg("controller", msg.getContent());
            System.out.println("Sending message with id "+currentConvoId+" to controller");
        }
        
        block();
    }
}
