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
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RequestProcesserBehaviour extends CyclicBehaviour
{
    private Agent agente;
    
    public RequestProcesserBehaviour(ControllerAgent a)
    {
        agente = a;
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
        
        if (msg != null && !ControllerAgent.convoIds.contains(msg.getConversationId()))
        {
            System.out.println("Received message from "+msg.getSender()+". Content: "+msg.getContent());
            ControllerAgent.convoIds.add(msg.getConversationId());
            
            // clears whitespaces
            String line = msg.getContent();
            Pattern r = Pattern.compile("\\s+");
            Matcher m = r.matcher(line);
            line = m.replaceAll("");
            
            // get target agent name        
            String delims = "[.]";
            String[] tokens = msg.getContent().split(delims);
            String agentName = tokens[0];
            
            // check available agents for each type
            String availableAgents = "";
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd  = new ServiceDescription();
            
            sd.setType("temp");
            dfd.addServices(sd);
            
            DFAgentDescription[] result = null;
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }

            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result)
                    availableAgents += "\n"+dfad.getName();
            
            sd.setType("humi");
            dfd.addServices(sd);      
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }

            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result)
                    availableAgents += "\n"+dfad.getName();
            
            sd.setType("mov");
            dfd.addServices(sd);      
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }

            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result)
                    availableAgents += "\n"+dfad.getName();
            
            sd.setType("lumi");
            dfd.addServices(sd);      
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }

            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result)
                    availableAgents += "\n"+dfad.getName();
            
            sd.setType("smoke");
            dfd.addServices(sd);      
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }

            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result)
                    availableAgents += "\n"+dfad.getName();
            
            // if target agent is no longer availabe, inform interface agent
            if (availableAgents.contains(agentName))
            {            
                if (line.equals(".scan"))
                    sendMsg("interface", msg.getConversationId(), availableAgents, ACLMessage.INFORM);   
                else if (line.contains("online"))
                    sendMsg(agentName, msg.getConversationId(), "online", ACLMessage.REQUEST);
                else if (line.contains("offline"))
                    sendMsg(agentName, msg.getConversationId(), "offline", ACLMessage.REQUEST);
                else if (line.contains("value"))
                    sendMsg(agentName, msg.getConversationId(), "value", ACLMessage.REQUEST);
                else
                    sendMsg("interface", msg.getConversationId(), "UNRECOGNIZED REQUEST", ACLMessage.INFORM);
            }
            else
                sendMsg("interface", msg.getConversationId(), availableAgents, ACLMessage.FAILURE);
        }
        
        block();
    }
    
}
