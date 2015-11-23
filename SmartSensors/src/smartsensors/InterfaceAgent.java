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
    
    public void saveRequest(int id, String content)
    {
        synchronized(requestMap){requestMap.put(id,content);}
    }
    
    public void removeRequest(int id)
    {
        synchronized(requestMap){requestMap.remove(id);}
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
        int i;
            i = 0;
            while (i<this.menu.getjTable1().getRowCount()){
                this.menu.getjTable1().setValueAt(false, i, 2);
                i++;
            }
            
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