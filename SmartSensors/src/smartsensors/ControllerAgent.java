package smartsensors;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

public class ControllerAgent extends Agent {

    @Override
    protected void setup()
    {
        super.setup();
        System.out.println(this.getLocalName()+" starting!");
        
        ParallelBehaviour par = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ANY);
        par.addSubBehaviour(new ProcessRequestBehaviour(this));
        par.addSubBehaviour(new RelayerBehaviour(this));
        
        this.addBehaviour(par);
    }
    
    @Override
    protected void takeDown()
    {
       super.takeDown();
       
       System.out.println(this.getLocalName() + " exiting...");  
    }
}
