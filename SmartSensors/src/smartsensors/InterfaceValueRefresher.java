package smartsensors;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Date;

public class InterfaceValueRefresher extends CyclicBehaviour
{
    private InterfaceAgent agente;
    private long refreshRate;
    private long lastRefresh;
    
    public InterfaceValueRefresher(InterfaceAgent a)
    {
        agente = a;
        refreshRate = 3000;
        lastRefresh = (new Date()).getTime();
    }
    
    private void sendMsg(String agentName, String msgContent)
    {
        AID receiver = new AID();
        receiver.setLocalName(agentName);

        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
        int cnvId = agente.getNewConvoId();
        msg.setConversationId(cnvId+"");
        msg.addReceiver(receiver);
        
        msg.setContent(msgContent);

        agente.send(msg);
        
        agente.saveRequest(cnvId,msg.getContent());
    }
    
    @Override
    public void action()
    {
        ACLMessage msg = agente.receive(MessageTemplate.MatchPerformative( ACLMessage.PROPOSE ));
        
        if (msg != null)
            refreshRate = Integer.parseInt(msg.getContent());
        
        long currentTime = (new Date()).getTime();
        
        
        if (currentTime - lastRefresh > refreshRate)
        {
            for (String as : agente.activeSensors)
                sendMsg("controller",as+".value");
        
            lastRefresh = currentTime;
            block(refreshRate);
        }
        else
            block(refreshRate - (currentTime - lastRefresh));
    }
}
