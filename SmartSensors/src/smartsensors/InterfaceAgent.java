package smartsensors;

import GUI.Menu;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;

public class InterfaceAgent extends Agent
{
    public Menu menu;
    
    @Override
    protected void setup()
    {
        super.setup();
        System.out.println(this.getLocalName()+" starting!");
        
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