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
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestProcesserBehaviour extends CyclicBehaviour
{
    private final Agent agente;
    public enum SensorType
    {
        TEMP {public String toString() {return "temp";}},
        HUMI {public String toString() {return "humi";}},
        MOVE {public String toString() {return "move";}},
        LUX {public String toString() {return "lux";}},
        SMOKE {public String toString() {return "smoke";}},
        ARDUINO {public String toString() {return "arduino";}} 
    }
        
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
            
            //Create new Agent
            boolean created = false;
            if (line.contains("create")){
                String[] split = line.split("\\.");
                String name = split[1];
                String type = split[2];
                
                if(type.equals("temp")){
                    type = "smartsensors.TempSensor";
                }
                else if(type.equals("humi")){
                    type = "smartsensors.HumiditySensor";
                }
                else if(type.equals("move")){
                    type = "smartsensors.MovementSensor";
                }
                else if(type.equals("lux")){
                    type = "smartsensors.LuxSensor";
                }
                else if(type.equals("smoke")){
                    type = "smartsensors.SmokeSensor";
                }
                
                AgentContainer c = agente.getContainerController();
                try {
                    AgentController a = c.createNewAgent( name, type, null );
                    a.start();
                    created = true;
                    System.out.println("Agent "+name+" created!");
                }
                catch (Exception e){ System.out.println(e); }
            }
            
            // get target agent name        
            String delims = "[.]";
            String[] tokens = msg.getContent().split(delims);
            String agentName = tokens[0];
            
            // check available agents for each type
            String availableAgents = "";
            
            for (SensorType st : SensorType.values())
            {
                DFAgentDescription dfd = new DFAgentDescription();
                ServiceDescription sd  = new ServiceDescription();
                sd.setType(st.toString());
                dfd.addServices(sd);
                
                DFAgentDescription[] result = null;
                
                try
                {
                    result = DFService.search(agente, dfd);
                }
                catch (FIPAException ex) { ex.printStackTrace(); }
                
                if (result != null && result.length>0)
                {      
                    availableAgents += "\n"+st.toString();
                    
                    for (DFAgentDescription dfad : result)
                        availableAgents += "."+dfad.getName().getLocalName();
                }
            }
            
            // if target agent is no longer availabe, inform interface agent
            if (availableAgents.contains(agentName) && !created)
            {            
                if (line.equals(".scan"))
                    sendMsg("interface", msg.getConversationId(), availableAgents, ACLMessage.INFORM);   
                else if (line.contains("online"))
                    sendMsg(agentName, msg.getConversationId(), "online", ACLMessage.REQUEST);
                else if (line.contains("offline"))
                    sendMsg(agentName, msg.getConversationId(), "offline", ACLMessage.REQUEST);
                else if (line.contains("value"))
                    sendMsg(agentName, msg.getConversationId(), "value", ACLMessage.REQUEST);
                else if (line.contains("turnon"))
                    sendMsg(agentName, msg.getConversationId(), "turnon", ACLMessage.REQUEST);
                else if (line.contains("turnoff"))
                    sendMsg(agentName, msg.getConversationId(), "turnoff", ACLMessage.REQUEST);
                else
                    sendMsg("interface", msg.getConversationId(),"Unrecognized requested operation!", ACLMessage.NOT_UNDERSTOOD);
            }
            else
                sendMsg("interface", msg.getConversationId(), "Specified agent name ("+agentName+") unavailable!", ACLMessage.FAILURE);
        }
        
        block();
    }
    
}

