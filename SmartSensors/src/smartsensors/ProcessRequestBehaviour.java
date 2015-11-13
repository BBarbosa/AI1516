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


public class ProcessRequestBehaviour extends CyclicBehaviour
{
    private Agent agente;
    
    public ProcessRequestBehaviour(Agent a)
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
            System.out.println("Received message from "+msg.getSender()+". Content: "+msg.getContent());

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
            
            
            sendMsg("interface", msg.getConversationId(), msg.getContent());
        }
        
        block();
    }
    
}

