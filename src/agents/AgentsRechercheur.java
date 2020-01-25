/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agents;

import container.RechercheurContainer;
 
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
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author hfmlcode
 */
public class AgentsRechercheur extends GuiAgent {

    private Vector<AID> aidVector = new Vector();
    private AID aid;
    private DFAgentDescription dFAgentDescription;
    private ServiceDescription sd;
    private RechercheurContainer agentRechercheur;
    private String nomAgent;
    private Vector bibliotekeurEtInfos = new Vector();
    private int etat = 0;

    public int getEtat() {
        return etat;
    }

    public void setEtat(int etat) {
        this.etat = etat;
    }

    public String getNomAgent() {
        return nomAgent;
    }

    public void setNomAgent(String nomAgent) {
        this.nomAgent = nomAgent;
    }

    @Override
    protected void setup() {

        System.out.println("Agent rechercheur " + this.getAID());

        // Publication pour permettre la récuperation
        dFAgentDescription = new DFAgentDescription();
        dFAgentDescription.setName(getAID());
        //Service
        sd = new ServiceDescription();
        sd.setType("RECHERCHEURS");
        sd.setName("AGENT-RECHERCHEURS");
        dFAgentDescription.addServices(sd);

        //
        try {
            DFService.register(this, dFAgentDescription);
        } catch (FIPAException ex) {
            Logger.getLogger(AgentsRechercheur.class.getName()).log(Level.SEVERE, null, ex);
        }

        //
        Object[] args = getArguments();
     


        addBehaviour(new TickerBehaviour(this, 15000) {

            @Override
            protected void onTick() {
                etat = 0;
                if (bibliotekeurEtInfos.size() != 0) {

                    int minPrix = 0;
                    String sendeur = "vide";
                    String info_recherche = null;

                    //initialistion
                    sendeur = bibliotekeurEtInfos.elementAt(0).toString();
                    info_recherche = bibliotekeurEtInfos.elementAt(1).toString();
                    minPrix = Integer.parseInt(bibliotekeurEtInfos.elementAt(2).toString());

                    int i = 0;
                    while (i < bibliotekeurEtInfos.size()) {

                        if (Integer.parseInt(bibliotekeurEtInfos.elementAt(i + 2).toString()) < minPrix) {
                            sendeur = bibliotekeurEtInfos.elementAt(i).toString();
                            info_recherche = bibliotekeurEtInfos.elementAt(i + 1).toString();
                            minPrix = Integer.parseInt(bibliotekeurEtInfos.elementAt(i + 2).toString());

                        }
                        i += 3;
                    }

                    ACLMessage messageInform = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                    messageInform.addReceiver(new AID(sendeur, AID.ISGUID));

                    messageInform.setContent(info_recherche + " trouvé");
                //            + " à la valeur de " + minPrix + " Dollar...");
                    messageInform.getContent();
                    messageInform.setLanguage("fr");
                    send(messageInform);

                    bibliotekeurEtInfos.clear();

                }
                //fin if
            }
        });


        //Cyclique
        addBehaviour(new CyclicBehaviour() {
            //int i;

            @Override
            public void action() { 
                //Réception des message envoyé

                ACLMessage message = receive();


                //
                DFAgentDescription dfg = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("BIBLIOTEKEUR");
                sd.setName("AGENT-BIBLIOTEKEUR");
                dfg.addServices(sd);
                DFAgentDescription[] bibliotekeurs = null;

                if (message != null) {
                    //System.out.println(" Ecoute RECU " + message.getContent());
                    switch (message.getPerformative()) {
                        //mressage reçu de l'utilisateur
                        case ACLMessage.REQUEST:
                            System.out.println(" Demande de  l'information " + message.getContent());
                            ACLMessage msgReceve = new ACLMessage(ACLMessage.CFP);
                            //message envoyé à tous les bibliotekeurs

                            //
                            try {
                                bibliotekeurs = DFService.search(myAgent, dfg);

                            } catch (FIPAException ex) {
                                Logger.getLogger(AgentsUtilisateur.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            for (int i = 0; i < bibliotekeurs.length; i++) {
                               
                                msgReceve.addReceiver(new AID(bibliotekeurs[i].getName().getLocalName(), AID.ISLOCALNAME));
                            }


                            msgReceve.setContent(message.getContent());
                            msgReceve.getContent();
                            msgReceve.setLanguage("fr");
                            send(msgReceve);
                            break;

                        case ACLMessage.PROPOSE:
                            message.getContent();
                            if (message.getContent().isEmpty()) {
                                // pas de d'information trouvé
                                etat++;
                                ACLMessage messageInform = new ACLMessage(ACLMessage.INFORM);
                                messageInform.addReceiver(new AID("Utilisateur 1", AID.ISLOCALNAME));

                                messageInform.setContent("Aucun agent ne dispose de cette information .");
                                messageInform.getContent();
                                messageInform.setLanguage("fr");
                                if (etat == 1) {
                                    send(messageInform);
                                }

                                break;

                            } else {
                                String[] splitMessage = message.getContent().split(" ");
                                System.out.println("Proposition : Information" + splitMessage[0] + " à une valeur de " + splitMessage[1]);


                                bibliotekeurEtInfos.add(message.getSender().getName());
                                bibliotekeurEtInfos.add(splitMessage[0]);
                                bibliotekeurEtInfos.add(splitMessage[1]);



                                //vendeurEtCesLivre.clear();
                            }



                            break;
                        case ACLMessage.AGREE:
                            ACLMessage messageInform = new ACLMessage(ACLMessage.INFORM);
                            messageInform.addReceiver(new AID("Utilisateur 1", AID.ISLOCALNAME));
                            System.out.println("Information trouvé et remi " + message.getContent());

                            messageInform.setContent("Information remi" + message.getContent() + " par le bibliotekeur (" + message.getSender().getLocalName() + ") est effectué avec succès.");
                            messageInform.getContent();
                            messageInform.setLanguage("fr");
                            send(messageInform);
                            break;

                        case ACLMessage.INFORM:
                            break;
                    }

                } else {
                    block();
                }
            }
        });

    }

    // Méthode à excuter avant la destruction
    @Override
    protected void takeDown() {
        try {
            //System.err.println(" Destruction d l'agent " + this.getAID().getLocalName());
            DFService.deregister(this);
        } catch (FIPAException ex) {
            Logger.getLogger(AgentsRechercheur.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onGuiEvent(GuiEvent gev) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        switch (gev.getType()) {

            //Création
            case 1:

                String nomAgentACree = (String) gev.getParameter(0);
                //agentRechercheur.setAgentContainer((AgentContainer)gev.getParameter(1));

                System.out.print(" argument " + nomAgentACree);
                //System.out.print(" argument "+nomAgentACree);
                agentRechercheur = (RechercheurContainer) gev.getParameter(1);
                agentRechercheur.createAgentRechercheur(nomAgentACree, new Object[]{"SIRI"});
            // déclarer comme un attribut de la classe
            
            //Création
            case 2:
                String nomAgentASupprimer = (String) gev.getParameter(0);

                break;

        }
    }

    public Object[] mesArgment() {
        Object[] args = getArguments();

        return args;
    }
}
