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
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import static sun.net.www.http.HttpClient.New;

/**
 *
 * @author jc
 */
public class AgentsUtilisateur extends GuiAgent {

    //private Frame gui;
    private Gui gui;

    @Override
    protected void setup() {
        gui = new Gui();
        gui.setVisible(true);
        //admin = new AdministrateuGui();
        //admin.setVisible(true);
        gui.displayMessage(" Initialisation des l'agent utilisateur .");
        gui.setAgentsUtilisateur(this);

        System.out.println("Agent Utilisateurs " + this.getAID().getName());

        addBehaviour(new CyclicBehaviour() {

            @Override
            public void action() {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                // pour récuperer un type de message spécifique
                MessageTemplate messageTemplate = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
                ACLMessage message = receive(messageTemplate);
                if (message != null) {
                    //System.out.println(" Message INFORM de l'agent : " + message.getContent());
                    gui.displayMessage(message.getContent());
                } else {
                    block();
                }
            }
        });

        // Périodique
        addBehaviour(new TickerBehaviour(this, 10000) {

            @Override
            protected void onTick() {

                //agent utiisateur 
                DFAgentDescription dfg = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("RECHERCHEURS");
                sd.setName("AGENT-RECHERCHEURS");
                dfg.addServices(sd);
                DFAgentDescription[] rechercheurs = null;

                try {
                    rechercheurs = DFService.search(myAgent, dfg);

                } catch (FIPAException ex) {
                    Logger.getLogger(AgentsUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Liste object agent rechercheur
                Vector<String> comboAgents = new Vector();
                comboAgents.clear();
                //affichage
                for (int i = 0; i < rechercheurs.length; i++) {
                    comboAgents.add(rechercheurs[i].getName().getLocalName());
                }
                //ajout
                gui.setComboAgents(comboAgents);

                //System.err.println(" "+comboAgents.size());

                //agent bibliotekeur
                DFAgentDescription dfgv = new DFAgentDescription();
                ServiceDescription sdv = new ServiceDescription();
                sdv.setType("BIBLIOTEKEUR");
                sdv.setName("AGENT-BIBLIOTEKEUR");
                dfgv.addServices(sdv);
                DFAgentDescription[] bibliotekeurs= null;

                try {
                    bibliotekeurs = DFService.search(myAgent, dfgv);

                } catch (FIPAException ex) {
                    System.out.println(" Erreur d'ajout ");
                    //Logger.getLogger(AgentsUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
                }
                // Liste object agent rechercheur
                Vector<String> comboAgentsbibliotekeur = new Vector();
                comboAgentsbibliotekeur.clear();
                //affichage
                for (int i = 0; i < bibliotekeurs.length; i++) {
                    comboAgentsbibliotekeur.add(bibliotekeurs[i].getName().getLocalName());
                }
                //ajout
                gui.setComboAgentsBibliotekeur(comboAgentsbibliotekeur);
            }
        });

    }

    @Override
    public void onGuiEvent(GuiEvent gev) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        switch (gev.getType()) {
            case 1:
                Map<String, Object> params = (Map<String, Object>) gev.getParameter(0);
                String agent = (String) params.get("AGENT");
                String info_recherche = (String) params.get("INFORMATION");

                //envoie de message à l'agent acheteur.
                ACLMessage newRequest = new ACLMessage(ACLMessage.REQUEST);
                newRequest.addReceiver(new AID(agent, AID.ISLOCALNAME));
                newRequest.setContent(info_recherche);
                send(newRequest);

                break;
                
                case 2:
                
                
                Map<String, Object> param = (Map<String, Object>) gev.getParameter(0);
                String agentv = (String) param.get("AGENT");
                String informationa = (String) param.get("INFORMATION");
                String prixa = (String) param.get("PRIX");

                //System.err.println(" message "+prixa);

                ACLMessage ajoutInformation = new ACLMessage(ACLMessage.INFORM);
                ajoutInformation.addReceiver(new AID(agentv, AID.ISLOCALNAME));
                ajoutInformation.setContent(informationa+" "+prixa);
                send(ajoutInformation);
                
                //System.err.println(" apres l'envoie du message ");

                break;
        }

    }
}
