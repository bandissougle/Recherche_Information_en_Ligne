/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package container;

import agents.AgentsRechercheur;
import agents.AgentsUtilisateur;
import agents.AgentsBibliotekeur;
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
public class UtilisateurContainer {

    public static void main(String arg[]) {
        try {
            jade.core.Runtime runtime;
            runtime = jade.core.Runtime.instance();

            ProfileImpl profileImpl = new ProfileImpl(false);
            profileImpl.setParameter(profileImpl.MAIN_HOST, "localhost");
            profileImpl.setParameter(profileImpl.CONTAINER_NAME, "Utilisateur");

            AgentContainer agentContainer = (AgentContainer) runtime.createAgentContainer(profileImpl);
            AgentController agentController = agentContainer.createNewAgent("Utilisateur1", AgentsUtilisateur.class.getName(), new Object[]{"SIRI"});

            agentController.start();




        } catch (ControllerException ex) {
        }

    }
}
