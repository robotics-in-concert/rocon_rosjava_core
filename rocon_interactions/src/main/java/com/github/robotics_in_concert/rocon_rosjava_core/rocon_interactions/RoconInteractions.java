package com.github.robotics_in_concert.rocon_rosjava_core.rocon_interactions;

/*****************************************************************************
** Imports
*****************************************************************************/

import org.apache.commons.logging.Log;
import org.ros.exception.RosRuntimeException;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;

import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.ListenerNode;
import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.RosTopicInfo;

import rocon_interaction_msgs.Roles;

/*****************************************************************************
** RoconInteractions
*****************************************************************************/

public class RoconInteractions extends AbstractNodeMain {

	private GraphName namespace;
	private GraphName rolesTopicName;
	private Log log;
	private Roles roles;
	
    /**
     * Go get the RoconInteractionsrmation.
     *
     * @param connectedNode
     */
    @Override
    public void onStart(final ConnectedNode connectedNode) {
        log = connectedNode.getLog();
        String topicName = "";
        RosTopicInfo topicInformation = new RosTopicInfo(connectedNode);
        try {
        	topicName = topicInformation.findTopic("rocon_interaction_msgs/Roles");
        } catch(RosRuntimeException e) {
        	log.error("Interactions : timed out looking for the rocon interactions roles topic [" + "]");
        	return;
        }
        this.rolesTopicName = GraphName.of(topicName);
        this.namespace = this.rolesTopicName.getParent();
        log.info("Interactions : namespace [" + this.namespace.toString() + "]");
    	ListenerNode<Roles> rolesListener = 
    			new ListenerNode<Roles>(
    					connectedNode,
    					this.rolesTopicName.toString(),
    					Roles._TYPE);
    	rolesListener.waitForResponse();
    	this.roles = rolesListener.getMessage();
    }

    /****************************************
    ** Getters
    ****************************************/

    @Override
    public GraphName getDefaultNodeName() {
		return GraphName.of("rocon_rosjava_interactions");
    }
    
    public rocon_interaction_msgs.Roles getRoles() {
    	return this.roles;
    }
    
    /****************************************
    ** Main
    ****************************************/

   public static void main(String argv[]) throws java.io.IOException {
    	String[] args = { "com.github.rocon_rosjava_core.rocon_interactions.RoconInteractions" };
    	try {
    		org.ros.RosRun.main(args);
    	} catch(RosRuntimeException e) {
    		System.out.println("Interactions: ros runtime error");
		} catch(Exception e) {
			System.out.println("Interactions: unknown error");
    	}
    }
}


