package smartsensors;

import java.util.Random;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class TempSensor extends Agent 
{
    private boolean sensorState = false;
    private boolean finished = false;

    @Override
    protected void takeDown()
    {
        super.takeDown();

        try { DFService.deregister(this); }
        catch (Exception e) {e.printStackTrace();}

        System.out.println("deregistering "+this.getLocalName()+" from service list...");
    }

    @Override
    protected void setup()
    {
        super.setup();

        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        ServiceDescription sd = new ServiceDescription();
        sd.setName(getLocalName());
        sd.setType("temp");
        dfd.addServices(sd);

        try{ DFService.register(this, dfd );}
        catch (FIPAException fe) { fe.printStackTrace(); }

        System.out.println(this.getLocalName()+" starting!");

        this.addBehaviour(new ReceiveBehaviour());
    }

    public boolean isSensorState() {
            return sensorState;
    }

    public void setSensorState(boolean sensorState) {
            this.sensorState = sensorState;
    }

    public boolean isFinished() {
            return finished;
    }

    public void setFinished(boolean finished) {
            this.finished = finished;
    }

    private class ReceiveBehaviour extends CyclicBehaviour
    {
        @Override
        public void action() 
        {
            ACLMessage msg = receive();
            if (msg != null) 
            {            	
                ACLMessage reply = msg.createReply();
                reply.setConversationId(msg.getConversationId());
                
                if (msg.getPerformative() == ACLMessage.REQUEST)
                {
                    if (msg.getContent().equals("shutdown"))
                    {
                        System.out.println("sensor "+myAgent.getLocalName()+" exiting...");
                        setFinished(true);
                    }
                   
                    if (msg.getContent().equals("online"))
                    {
                        if (isSensorState())
                        {
                            reply.setPerformative(ACLMessage.FAILURE);
                            myAgent.send(reply);
                        }
                        else
                        {
                            System.out.println("Sensor "+myAgent.getLocalName()+" is now online!");
                            reply.setPerformative(ACLMessage.CONFIRM);
                            myAgent.send(reply);
                            setSensorState(true);
                        }
                    }

                    if (msg.getContent().equals("offline"))
                    {
                        if (isSensorState())
                        {
                            System.out.println("Sensor "+myAgent.getLocalName()+" is now offline!");
                            reply.setPerformative(ACLMessage.CONFIRM);
                            myAgent.send(reply);
                            setSensorState(false);
                        }
                        else
                        {
                            reply.setPerformative(ACLMessage.FAILURE);
                            myAgent.send(reply);
                        }
                    }

                    if (msg.getContent().equals("value"))
                    {
                        System.out.println(isSensorState());
                        if (isSensorState())
                        {
                            //int randomNum = Math.abs(new Random().nextInt() % 100);
                            int randomNum = new Random().nextInt(130);
                            if (randomNum > 60 && randomNum < 100)
                            {
                                reply.setContent("XXXXX");
                                reply.setPerformative(ACLMessage.INFORM);
                                myAgent.send(reply);
                            }
                            else if (randomNum >= 100 && randomNum <= 130)
                            {
                                reply.setContent(-randomNum+100+"");
                                reply.setPerformative(ACLMessage.INFORM);
                                myAgent.send(reply);
                            }
                            else if (randomNum >= 0 && randomNum <= 60)
                            {
                                reply.setContent(randomNum+"");
                                reply.setPerformative(ACLMessage.INFORM);
                                myAgent.send(reply);
                            }
                        }
                        else
                        {
                            reply.setPerformative(ACLMessage.FAILURE);
                            myAgent.send(reply);
                        }
                    }
                }
                else
                {
                    reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
                    reply.setContent("Unrecognized request performative. Must be ACLMessage.REQUEST!");
                    myAgent.send(reply);
                }
            }

        if (isFinished())
            myAgent.doDelete();
        
        block();
        }
    }
}