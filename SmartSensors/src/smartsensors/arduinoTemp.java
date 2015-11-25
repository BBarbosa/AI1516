/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package smartsensors;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import java.util.Random;

/**
 *
 * @author andregeraldes
 */
public class arduinoTemp extends Agent {
    private boolean sensorState = false;
    private boolean finished = false;
    private int last = new Random().nextInt(60);
    private SerialPortCommunication serial = new SerialPortCommunication();

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
        
        //Init with arduino
        serial.initialize();

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

    public SerialPortCommunication getSerial() {
        return serial;
    }

    public void setSerial(SerialPortCommunication serial) {
        this.serial = serial;
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
                            getSerial().writeData(1);
                            
                            String s = getSerial().getData();
                            String n = "XXXXX";
                            if(s.contains(".")){
                                String[] split = s.split("\\.");
                                n = split[0];
                            }
                            
                            reply.setContent(n + "");
                            reply.setPerformative(ACLMessage.INFORM);
                            myAgent.send(reply);
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
