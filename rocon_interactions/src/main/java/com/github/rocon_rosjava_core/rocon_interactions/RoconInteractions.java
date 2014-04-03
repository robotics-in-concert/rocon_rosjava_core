package com.github.rocon_rosjava_core.rocon_interactions;

/*****************************************************************************
** Imports
*****************************************************************************/

import org.apache.commons.logging.Log;
import org.ros.exception.RosRuntimeException;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;

import com.github.rocon_rosjava_core.rosjava_utils.ListenerNode;
import com.github.rocon_rosjava_core.rosjava_utils.RosTopicInfo;

/*****************************************************************************
** RoconInteractions
*****************************************************************************/

public class RoconInteractions extends AbstractNodeMain {

	private GraphName namespace;
	private Log log;
	
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
        	log.error("Interactions : timed out looking for the RoconInteractionsrmation topic [" + "]");
        }
        this.namespace = GraphName.of(topicName).getParent();
        log.info("Interactions : namespace [" + this.namespace.toString() + "]");

    }


    /****************************************
    ** Getters
    ****************************************/

    @Override
    public GraphName getDefaultNodeName() {
		return GraphName.of("rocon_rosjava_interactions");
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


