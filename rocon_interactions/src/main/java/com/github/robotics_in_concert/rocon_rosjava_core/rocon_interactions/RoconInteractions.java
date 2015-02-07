package com.github.robotics_in_concert.rocon_rosjava_core.rocon_interactions;

/*****************************************************************************
** Imports
*****************************************************************************/

import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.BlockingServiceClientNode;
import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.ListenerNode;
import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.RosTopicInfo;
import com.google.common.collect.Lists;

import org.ros.exception.RosRuntimeException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.internal.loader.CommandLineLoader;
import org.ros.message.MessageFactory;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.List;

import rocon_interaction_msgs.GetRoles;
import rocon_interaction_msgs.GetRolesRequest;
import rocon_interaction_msgs.GetRolesResponse;
import rocon_std_msgs.Strings;

// import org.apache.commons.logging.Log;
// final log = connectedNode.getLog();
// log.error("Dude does this work on android?")

/*****************************************************************************
** RoconInteractions
*****************************************************************************/

public class RoconInteractions extends AbstractNodeMain {

	private GraphName namespace;
	private BlockingServiceClientNode<GetRolesRequest, GetRolesResponse> getRolesClient;
	private String roconURI;
	
	public RoconInteractions(String roconURI) {
		this.getRolesClient = null;
		this.roconURI = roconURI;
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
        	topicName = topicInformation.findTopic("rocon_interaction_msgs/InteractiveClients");
        } catch(RosRuntimeException e) {
    		// should be having some sort of error flag that can be picked up in waitForResponse
        	return;
        }
        this.namespace = GraphName.of(topicName).getParent();
        //System.out.println("Interactions : namespace [" + this.namespace.toString() + "]");
        MessageFactory messageFactory = connectedNode.getServiceRequestMessageFactory();
		try {
    		GetRolesRequest request = messageFactory.newFromType(GetRoles._TYPE);
    		request.setUri(this.roconURI);
    		synchronized(this) {
	    		this.getRolesClient = new BlockingServiceClientNode<GetRolesRequest, GetRolesResponse>(
						connectedNode,
						this.namespace.toString() + "/get_roles",
						GetRoles._TYPE,
						request
				);
	    		this.getRolesClient.waitForResponse();
    			notifyAll();
    		}
    	} catch (ServiceNotFoundException e) {
    		// should be having some sort of error flag that can be picked up in waitForResponse
        	return;
    	}
    }

    /**
     * Wait for data to come in. This uses a default timeout
     * set by ListenerNode.
     * 
     * @see ListenerNode
     * @throws InteractionsException : if listener error, timeout or general runtime problem
     */
    public void waitForResponse() throws InteractionsException {
    	synchronized(this) {
    		try {
    			if (getRolesClient == null) {
    				this.wait(4000);
    			}
	    	} catch(InterruptedException e) {
	    		e.printStackTrace();
	    	}
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
    public List<String> getRoles() throws InteractionsException {
    	// could use better logic in here
    	// e.g. if onStart hasn't been called yet, throw some type of exception
    	this.waitForResponse();
    	return getRolesClient.getResponse().getRoles();
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
    	// This is an example rocon uri used by a remocon.
    	String roconURI = "rocon:/"
                + Strings.URI_WILDCARD + "/" + Strings.URI_WILDCARD + "/"
                + Strings.APPLICATION_FRAMEWORK_HYDRO + "|" + Strings.APPLICATION_FRAMEWORK_INDIGO + "/"
                + Strings.OS_ICE_CREAM_SANDWICH + "|" + Strings.OS_JELLYBEAN + "|" + Strings.OS_CHROME;
    	RoconInteractions interactions = new RoconInteractions(roconURI);
    	
        NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
        nodeMainExecutor.execute(interactions, nodeConfiguration);
        try {
        	List<String> roles = interactions.getRoles();
	        for (String role : roles) {
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


