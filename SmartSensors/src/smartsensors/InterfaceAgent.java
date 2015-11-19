package smartsensors;

import GUI.Menu;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import java.util.HashMap;

public class InterfaceAgent extends Agent
{
    public Menu menu;
    public HashMap<Integer, String> requestMap;
    
    @Override
    protected void setup()
    {
        super.setup();
        System.out.println(this.getLocalName()+" starting!");
        requestMap = new HashMap<>();
        
        ParallelBehaviour par = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ANY);
        par.addSubBehaviour(new InterfaceRequesterBehaviour(this));
        par.addSubBehaviour(new InterfaceReceiverBehaviour(this));
        
        this.addBehaviour(par);
        
        this.menu =  new Menu();
        this.menu.setAgente(this);
        this.menu.setVisible(true);
    }
    
    @Override
    protected void takeDown()
    {
       super.takeDown();
       
       System.out.println(this.getLocalName() + " exiting...");  
    }
}