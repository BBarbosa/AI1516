package smartsensors;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

public class ControllerAgent extends Agent {

    @Override
    protected void setup()
    {
        super.setup();
        System.out.println(this.getLocalName()+" a começar!");
        
        this.addBehaviour(new ControllerBehaviour(this));
    }
    
    @Override
    protected void takeDown()
    {
       super.takeDown();
       
       System.out.println(this.getLocalName() + "a morrer...");  
    }
    
}
