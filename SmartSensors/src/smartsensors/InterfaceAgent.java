package smartsensors;

import GUI.Menu;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import java.util.ArrayList;
import java.util.HashMap;

public class InterfaceAgent extends Agent
{
    public Menu menu;
    public HashMap<Integer, String> requestMap;
    public ArrayList<String> activeSensors;
    private Integer currentConvoId;
    
    public synchronized int getNewConvoId()
    {
        return ++currentConvoId;
    }
    
    public synchronized void saveRequest(int id, String content)
    {
        requestMap.put(id,content);
    }
    
    @Override
    protected void setup()
    {
        super.setup();
        System.out.println(this.getLocalName()+" starting!");
        requestMap = new HashMap<>();
        activeSensors = new ArrayList<>();
        currentConvoId = 0;
        
        ParallelBehaviour par = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ANY);
        par.addSubBehaviour(new InterfaceRequesterBehaviour(this));
        par.addSubBehaviour(new InterfaceReceiverBehaviour(this));
        par.addSubBehaviour(new InterfaceValueRefresher(this));
        
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