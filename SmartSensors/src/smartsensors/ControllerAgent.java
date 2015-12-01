package smartsensors;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import java.util.ArrayList;

public class ControllerAgent extends Agent
{
    public static ArrayList<String> convoIds;

    @Override
    protected void setup()
    {
        super.setup();
        convoIds = new ArrayList<>();
        System.out.println(this.getLocalName()+" starting!");
        
        ParallelBehaviour par = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ANY);
        par.addSubBehaviour(new RequestProcesserBehaviour(this));
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
