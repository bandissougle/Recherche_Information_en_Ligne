/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package container;

import agents.AgentsRechercheur;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;

/**
 *
 * @author hfmlcode
 */
public class RechercheurContainer {

    private AgentController agentController;
    private AgentContainer agentContainer;

    public AgentContainer getAgentContainer() {
        return agentContainer;
    }

    public void setAgentContainer(AgentContainer agentContainer) {
        this.agentContainer = agentContainer;
    }

    public RechercheurContainer() {
        jade.core.Runtime runtime;
        runtime = jade.core.Runtime.instance();

        ProfileImpl profileImpl = new ProfileImpl(false);
        profileImpl.setParameter(profileImpl.MAIN_HOST, "localhost");
        profileImpl.setParameter(profileImpl.CONTAINER_NAME, "Rechercheur");
        agentContainer = (AgentContainer) runtime.createAgentContainer(profileImpl);
    }

    public AgentController getAgentController() {
        return agentController;
    }

    public void setAgentController(AgentController agentController) {
        this.agentController = agentController;
    }

    public void createAgentRechercheur(String nom, Object[] object) {
        try {

            agentController = this.agentContainer.createNewAgent(nom, AgentsRechercheur.class.getName(), object);
            agentController.start();

        } catch (ControllerException ex) {
            //Logger.getLogger(MainContainer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void supprimerAgent(String nomAgent) {
    }

    public static void main(String arg[]) {
    }
}
