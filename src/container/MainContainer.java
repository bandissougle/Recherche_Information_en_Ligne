/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package container;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime ;
import jade.util.ExtendedProperties;
import jade.util.Logger;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.ControllerException;


/**
 *
 * @author jc
 */
public class MainContainer {
    public static void main(String arg[])
    {
        try {
            
        Runtime runtime;
        runtime = Runtime.instance();
        
        Properties properties= new ExtendedProperties();
        properties.setProperty(Profile.GUI, "true");
        properties.setProperty(Profile.CONTAINER_NAME, "TPSAM");
        
        ProfileImpl profileImpl = new ProfileImpl((jade.util.leap.Properties) properties);
        
        AgentContainer agentContainer = (AgentContainer) runtime.createMainContainer(profileImpl);
        
            agentContainer.start();
        } catch (ControllerException ex) {
            //Logger.getLogger(MainContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }
}
