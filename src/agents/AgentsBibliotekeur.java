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
 * @author jc
 */
public class AgentsBibliotekeur extends GuiAgent {

    private Gui gui;
    private DFAgentDescription dFAgentDescription;
    private ServiceDescription sd;

    @Override
    protected void setup() {
        System.out.println("Agent Bibliotekeur " + this.getAID().getName());

        //publication pour permettre la récuperation

        // Publication pour permettre la récuperation
        dFAgentDescription = new DFAgentDescription();
        dFAgentDescription.setName(getAID());
        //Service
        sd = new ServiceDescription();
        sd.setType("BIBLIOTEKEUR");
        sd.setName("AGENT-BIBLIOTEKEUR");
        dFAgentDescription.addServices(sd);

        //
        try {
            DFService.register(this, dFAgentDescription);
        } catch (FIPAException ex) {
            Logger.getLogger(AgentsRechercheur.class.getName()).log(Level.SEVERE, null, ex);
        }

        Object[] args = getArguments();
       

        //Cyclique
        addBehaviour(new CyclicBehaviour() {
            //int i;

            @Override
            public void action() {

                StringBuffer messageProposition = new StringBuffer();

                //System.out.println("ici");
                ACLMessage message = receive();

                if (message != null) {

                    switch (message.getPerformative()) {
                        //mressage reçu de l'utilisateur
                        case ACLMessage.CFP:
                            System.out.println("information demandée est : " + message.getContent());
                  
                            Object[] args = getArguments();

                            for (int i = 0; i < args.length; i++) {

                                if (args[i].toString().compareTo(message.getContent()) == 0) {
                                    messageProposition.append(args[i]);
                                    messageProposition.append(" ");
                                    // System.out.println(" " + args[i]);
                                    i++;
                                    messageProposition.append(args[i]);
                                } else {
                                    i++;
                                }

                            }
                            //retour du message contenant les infos à l'agent rechercheur
                            ACLMessage msgReceve = new ACLMessage(ACLMessage.PROPOSE);

                            msgReceve.addReceiver(new AID(message.getSender().getName(), AID.ISGUID));

                            msgReceve.setContent(messageProposition.toString());
                            msgReceve.getContent();
                            msgReceve.setLanguage("fr");
                            send(msgReceve);
                            break;


                        case ACLMessage.ACCEPT_PROPOSAL:
                            ACLMessage msgAccept = new ACLMessage(ACLMessage.AGREE);

                            msgAccept.addReceiver(new AID(message.getSender().getName(), AID.ISGUID));

                            msgAccept.setContent(message.getContent());
                            msgAccept.getContent();
                            System.out.println("  le rechercheur m'envoie ce message " + msgAccept.getContent());
                            msgAccept.setLanguage("fr");
                            send(msgAccept);
                            break;

                        case ACLMessage.INFORM:
                            String[] informationP= message.getContent().split(" ");
                            Vector MesInformations = new Vector();

                            Object[] arbibliotekeur = getArguments();



                            // récupération des anciens arguments
                            for (int i = 0; i < arbibliotekeur.length; i++) {
                                MesInformations.add(arbibliotekeur[i]);
                            }
                            MesInformations.add(informationP[0]);
                           
                            MesInformations.add(informationP[1]);

                            setArguments(MesInformations.toArray());


                            break;
                    }

                } else {
                    block();
                }
            }
        });

    }

    @Override
    public void onGuiEvent(GuiEvent ge) {
    }
}
