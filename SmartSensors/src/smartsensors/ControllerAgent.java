package smartsensors;

import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import java.util.HashMap;

public class ControllerAgent extends Agent
{
    public static HashMap<String,Long> convoIds;
    
    public boolean hasRequestId(String id)
    {
        synchronized(convoIds){return convoIds.containsKey(id);}
    }
    
    public long getTimeStamp(String id)
    {
        synchronized(convoIds){return convoIds.get(id);}
    }
    
    public void saveRequestId(String id)
    {
        synchronized(convoIds){convoIds.put(id,System.currentTimeMillis());}
    }
    
    public void removeRequestId(String id)
    {
        synchronized(convoIds){convoIds.remove(id);}
    }
    
    @Override
    protected void setup()
    {
        super.setup();
        convoIds = new HashMap<>();
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
