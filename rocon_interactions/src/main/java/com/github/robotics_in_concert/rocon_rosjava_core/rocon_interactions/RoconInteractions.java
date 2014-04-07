package com.github.robotics_in_concert.rocon_rosjava_core.rocon_interactions;

/*****************************************************************************
** Imports
*****************************************************************************/

import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.ListenerException;
import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.ListenerNode;
import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.RosTopicInfo;
import com.google.common.collect.Lists;

import java.util.concurrent.TimeoutException;

// import org.apache.commons.logging.Log;
// final log = connectedNode.getLog();
// log.error("Dude does this work on android?")

import org.ros.exception.RosRuntimeException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import rocon_interaction_msgs.Roles;

/*****************************************************************************
** RoconInteractions
*****************************************************************************/

public class RoconInteractions extends AbstractNodeMain {

	private GraphName namespace;
	private GraphName rolesTopicName;
	private ListenerNode<Roles> rolesListener;
	
	public RoconInteractions() {
		this.rolesListener = new ListenerNode<Roles>();
	}
    /**
     * Go get the RoconInteractionsrmation.
     *
     * @param connectedNode
     */
    @Override
    public void onStart(final ConnectedNode connectedNode) {
        String topicName = "";
        RosTopicInfo topicInformation = new RosTopicInfo(connectedNode);
        try {
        	topicName = topicInformation.findTopic("rocon_interaction_msgs/Roles");
        } catch(RosRuntimeException e) {
        	// should be having some sort of error flag here that can be picked up in
        	// the waitForResponse loops.
        	return;
        }
        this.rolesTopicName = GraphName.of(topicName);
        this.namespace = this.rolesTopicName.getParent();
        //System.out.println("Interactions : namespace [" + this.namespace.toString() + "]");
    	this.rolesListener.connect(
    					connectedNode,
    					this.rolesTopicName.toString(),
    					Roles._TYPE);
    }

    /**
     * Wait for data to come in. This uses a default timeout
     * set by ListenerNode.
     * 
     * @see ListenerNode
     * @throws InteractionsException : if listener error, timeout or general runtime problem
     */
    public void waitForResponse() throws InteractionsException {
    	try {
    		rolesListener.waitForResponse();
	    } catch(ListenerException e) {
	    	throw new InteractionsException(e.getMessage());
	    } catch(TimeoutException e) {
	    	throw new InteractionsException(e.getMessage());
	    }
    }

    /****************************************
    ** Getters
    ****************************************/

    @Override
    public GraphName getDefaultNodeName() {
		return GraphName.of("rocon_rosjava_interactions");
    }
    /**
     * Get the list of roles provided by this interactions manager. This
     * will block for the default timeout if it hasn't got a list of
     * roles yet.
     * 
     * @return null or rocon_interaction_msgs.Roles
     * @throws InteractionsException : if listener error, timeout or general runtime problem
     * @see ListenerNode
     */
    public rocon_interaction_msgs.Roles getRoles() throws InteractionsException {
    	// could use better logic in here
    	// e.g. if onStart hasn't been called yet, throw some type of exception
    	try {
	    	if (rolesListener.getMessage() == null) {
	    		rolesListener.waitForResponse();
	    	}
	    } catch(ListenerException e) {
	    	throw new InteractionsException(e.getMessage());
	    } catch(TimeoutException e) {
	    	throw new InteractionsException(e.getMessage());
	    }
    	return rolesListener.getMessage();
    }
    
    public String getNamespace() {
    	return namespace.toString();
    }

    /****************************************
    ** Main
    ****************************************/

    public static void main(String argv[]) throws java.io.IOException {

    	// Pulling the internals of rosrun to slightly customise what to run
    	String[] args = { "com.github.rocon_rosjava_core.rocon_interactions.RoconInteractions" };
    	CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(args));
    	NodeConfiguration nodeConfiguration = loader.build();
    	RoconInteractions interactions = new RoconInteractions();
    	
        NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
        nodeMainExecutor.execute(interactions, nodeConfiguration);
        try {
        	Roles roles = interactions.getRoles();
	        for (String role : roles.getList()) {
	        	System.out.println("Interactions : found role '" + role + "'");
	        }
	    } catch(InteractionsException e) {
	    	System.out.println("Interactions : error getting roles [" + e.getMessage() + "]");
	    }
	    

        // The RosRun way
		//    	try {
		//		org.ros.RosRun.main(args);
		//	} catch(RosRuntimeException e) {
		//		System.out.println("Interactions: ros runtime error");
		//	} catch(Exception e) {
		//		System.out.println("Interactions: unknown error");
		//	}
    }
}


