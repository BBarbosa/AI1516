package smartsensors;

import GUI.Menu;
import jade.core.Agent;
import jade.core.behaviours.ParallelBehaviour;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JButton;

public class InterfaceAgent extends Agent
{
    public Menu menu;
    private Integer currentConvoId;
    public HashMap<Integer, String> requestMap;
    public HashMap<String, String> sensorTypes;
    public ArrayList<String> activeSensors;
    public ArrayList<Rule> automationProfile;
    public HashMap <String, JButton> labels;
    
    public synchronized int getNewConvoId()
    {
        return ++currentConvoId;
    }
    
    public void saveRequest(int id, String content)
    {
        synchronized(requestMap){requestMap.put(id,content);}
    }
    
    public String getRequestContent(int id)
    {
        synchronized(requestMap){return requestMap.get(id);}
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
        sensorTypes = new HashMap<>();
        activeSensors = new ArrayList<>();
        currentConvoId = 0;
        labels =  new HashMap<>();
        automationProfile = new ArrayList<>();
        
        ParallelBehaviour par = new ParallelBehaviour(this, ParallelBehaviour.WHEN_ANY);
        par.addSubBehaviour(new InterfaceRequesterBehaviour(this));
        par.addSubBehaviour(new InterfaceReceiverBehaviour(this));
        par.addSubBehaviour(new InterfaceTickerBehaviour(this));
        
        this.addBehaviour(par);
        
        // opens interface window
        this.menu = new Menu();
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