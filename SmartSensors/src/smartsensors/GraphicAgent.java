package smartsensors;

import GUI.Menu;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;


public class GraphicAgent extends Agent {

    protected void setup() 
    {
      
        super.setup(); //To change body of generated methods, choose Tools | Templates.

        this.addBehaviour(new MediumBehaviour(this));
        
    }

    protected void takeDown()
    {
        super.takeDown();

        
    } 
}

class MediumBehaviour extends CyclicBehaviour
{
    public Menu menu;
    Agent agente;
    
  public MediumBehaviour(Agent a)
    {
        agente = a;
    }
    
    @Override
   public void onStart(){
       this.menu =  new Menu();
       this.menu.setAgente(agente);
       
   }
   
   
    @Override
   public void action(){
       this.menu.setVisible(true);
       ACLMessage awnser = agente.receive();
       
       // **desenhar tabela**
       
       // check available agents for each type
            String availableAgents = "";
            DFAgentDescription dfd = new DFAgentDescription();
            ServiceDescription sd  = new ServiceDescription();
            
            //temperatura
            sd.setType("temp");
            dfd.addServices(sd);
            
            DFAgentDescription[] result = null;
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }
            int j=0;
            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result){
                    int i=0;
                       this.menu.getjTable1().setValueAt(dfad.getName().toString(), j, i);  
                       this.menu.getjTable1().setValueAt("Temperatura", j, i+1);
                       this.menu.getjTable1().setValueAt("State", j, i+2);  
               j++;
             }
            
            //humidade
             sd.setType("humi");
            dfd.addServices(sd);      
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }
            
            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result){
                    int i=0;
                       this.menu.getjTable1().setValueAt(dfad.getName().toString(), j, i);  
                       this.menu.getjTable1().setValueAt("Humidade", j, i+1);
                       this.menu.getjTable1().setValueAt("State", j, i+2);  
               j++;
             }
            //movimento
            sd.setType("mov");
            dfd.addServices(sd);      
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }
            
               
            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result){
                    int i=0;
                       this.menu.getjTable1().setValueAt(dfad.getName().toString(), j, i);  
                       this.menu.getjTable1().setValueAt("Movimento", j, i+1);
                       this.menu.getjTable1().setValueAt("State", j, i+2);  
               j++;
             }
            
            //luminosidade
            sd.setType("lumi");
            dfd.addServices(sd);      
            try
            {
                result = DFService.search(agente, dfd);
            } catch (FIPAException ex) { ex.printStackTrace(); }
           
            if (result != null && result.length>0)
                for (DFAgentDescription dfad : result){
                    int i=0;
                       this.menu.getjTable1().setValueAt(dfad.getName().toString(), j, i);  
                       this.menu.getjTable1().setValueAt("Luminosidade", j, i+1);
                       this.menu.getjTable1().setValueAt("State", j, i+2);  
               j++;
             }
  
       if (awnser !=  null){
         this.menu.getjTextArea1().append("Log ==> "+awnser.getContent()+"\n");
           
            }
       
        block();
    
   }   
}
