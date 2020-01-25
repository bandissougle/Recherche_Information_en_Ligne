/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import gui.Gui;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.lang.acl.ACLMessage;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import stock.Information;

/**
 *
 * @author hfmlcode
 */
public class AgentsAdministrateur extends GuiAgent {

    private Gui gui;

    @Override
    protected void setup() {

        //gui = new AdministrateuGui();
        System.out.println("Agent Administrateur " + this.getAID().getName());
        
        
            addBehaviour(new OneShotBehaviour() {

            @Override
            public void action() {
             
            }
        });
            
        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                
                
                
                ACLMessage msg = receive();
                if(msg != null){
                    //System.err.println(msg.getContent());
                }
            }
        });

    }

    @Override
    public void onGuiEvent(GuiEvent ge) {
        //gui = new Gui();

        switch (ge.getType()) {
            case 1:
                
                
                Map<String, Object> params = (Map<String, Object>) ge.getParameter(0);
                String agent = (String) params.get("AGENT");
                String info_recherche = (String) params.get("INFORMATION");
                String prix = (String) params.get("PRIX");

                //System.err.println(" avant l'envoie du message ");

                ACLMessage newRequest = new ACLMessage(ACLMessage.INFORM);
                newRequest.addReceiver(new AID(agent, AID.ISLOCALNAME));
                newRequest.setContent(info_recherche+" "+prix);
                send(newRequest);
                
                break;
        }

    }

    public void setGui(Gui gui) {
        this.gui = gui;
    }
    
    
   
}
