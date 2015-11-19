package smartsensors;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.Random;

public class SmokeSensor extends Agent {
    private boolean sensorState = false;
    private boolean finished = false;
    private int last = new Random().nextInt(100);

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
        sd.setType("smoke");
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

    public int getLast() {
        return last;
    }

    public void setLast(int last) {
        this.last = last;
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
                            int i = getLast() + new Random().nextInt(5) - new Random().nextInt(5);
                            setLast(i);
                            if (i < 0 || i > 100)
                            {
                                setLast(new Random().nextInt(100));
                                reply.setContent("XXXXX");
                                reply.setPerformative(ACLMessage.INFORM);
                                myAgent.send(reply);
                            }
                            else if (i >= 0 && i <= 100)
                            {
                                reply.setContent(i+"");
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
                    myAgent.send(reply);
                }
            }

        if (isFinished())
            myAgent.doDelete();
        
        block();
        }
    }
}
