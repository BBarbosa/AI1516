package smartsensors;

import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Date;

public class InterfaceTickerBehaviour extends CyclicBehaviour
{
    private InterfaceAgent agente;
    private long refreshRate;
    private long lastRefresh;
    
    public InterfaceTickerBehaviour(InterfaceAgent a)
    {
        agente = a;
        refreshRate = 3000;
        lastRefresh = (new Date()).getTime();
    }
    
    private void sendMsg(String agentName, int performative, String msgContent)
    {
        AID receiver = new AID();
        receiver.setLocalName(agentName);

        ACLMessage msg = new ACLMessage(performative);
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
            // process rules
            for (Rule r : agente.automationProfile)
            {
                String resAction = r.evaluateRule();
                if (resAction != null)
                {
                    sendMsg("interface",ACLMessage.INFORM,"rule."+resAction);
                    if(resAction.equals("ligar led verde")){
                        sendMsg("controller",ACLMessage.REQUEST,"arduino-Temp.turnon");
                    }
                    else if(resAction.equals("ligar led vermelho"))
                    {
                        sendMsg("controller",ACLMessage.REQUEST,"arduino-Temp.turnoff");
                    }
                }
            }
            
            // send value requests for every active sensor, to refresh their values
            for (String as : agente.activeSensors)
                sendMsg("controller",ACLMessage.REQUEST,as+".value");
        
            lastRefresh = currentTime;
            block(refreshRate);
        }
        else
            block(refreshRate - (currentTime - lastRefresh));
    }
}
