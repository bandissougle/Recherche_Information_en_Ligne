/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package container;

import agents.AgentsRechercheur;
import agents.AgentsAdministrateur;
import agents.AgentsBibliotekeur;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.util.ExtendedProperties;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.ControllerException;
import stock.Information;

/**
 *
 * @author jc
 */
public class AdministrateurContainer {

    private AgentController agentController;
    private AgentContainer agentContainer;

    public AgentContainer getAgentContainer() {
        return agentContainer;
    }

    public void setAgentContainer(AgentContainer agentContainer) {
        this.agentContainer = agentContainer;
    }

    public AdministrateurContainer() {
        jade.core.Runtime runtime;
        runtime = jade.core.Runtime.instance();

        ProfileImpl profileImpl = new ProfileImpl(false);
        profileImpl.setParameter(profileImpl.MAIN_HOST, "localhost");
        profileImpl.setParameter(profileImpl.CONTAINER_NAME, "Administrateur");
        agentContainer = (AgentContainer) runtime.createAgentContainer(profileImpl);
    }

    public AgentController getAgentController() {
        return agentController;
    }

    public void setAgentController(AgentController agentController) {
        this.agentController = agentController;
    }

    public void CreateAgentAdministrateur(String nom, Object[] object) {
        try {


            AgentController agentController = this.agentContainer.createNewAgent(nom, AgentsAdministrateur.class.getName(), object);
            agentController.start();


        } catch (ControllerException ex) {
        }

    }

}
