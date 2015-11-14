package smartsensors;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ProcessRequestBehaviour extends CyclicBehaviour
{
    private Agent agente;
    
    public ProcessRequestBehaviour(Agent a)
    {
        agente = a;
    }
    
    private String getAgentName(String s, String prefix)
    {
        return s.replaceFirst(prefix,"");
    }
    
    private void sendMsg(String agentName, String convoId, String line, int performative )
    {
        AID receiver = new AID();
        receiver.setLocalName(agentName);

        ACLMessage msg = new ACLMessage(performative);
        msg.setConversationId(convoId);
        msg.addReceiver(receiver);

        msg.setContent(line);

        myAgent.send(msg);
    }
    
    @Override
    public void action()
    {
        ACLMessage msg = agente.receive(MessageTemplate.MatchPerformative( ACLMessage.REQUEST ));
        
        if (msg != null)
        {
            System.out.println("Received message from "+msg.getSender()+". Content: "+msg.getContent());

            // clears whitespaces
            String line = msg.getContent();
            Pattern r = Pattern.compile("\\s+");
            Matcher m = r.matcher(line);
            line = m.replaceAll("");
            
            if (line.contains("scan"))
            {                
                DFAgentDescription dfd = new DFAgentDescription();
                ServiceDescription sd  = new ServiceDescription();
                sd.setType("temp_sensor");
                dfd.addServices(sd);

                DFAgentDescription[] result;
                try
                {
                    result = DFService.search(agente, dfd);
                    if (result.length>0)
                        for (DFAgentDescription dfad : result)
                            System.out.println("\n" + dfad.getName() );
                } catch (FIPAException ex) { ex.printStackTrace(); }


                sendMsg("interface", msg.getConversationId(), msg.getContent(), ACLMessage.INFORM);
            }
            else if (line.contains("online"))
                sendMsg(getAgentName(line,"online"), msg.getConversationId(), "online", ACLMessage.REQUEST);
            else if (line.contains("offline"))
                sendMsg(getAgentName(line,"offline"), msg.getConversationId(), "offline", ACLMessage.REQUEST);
        }
        
        block();
    }
    
}

