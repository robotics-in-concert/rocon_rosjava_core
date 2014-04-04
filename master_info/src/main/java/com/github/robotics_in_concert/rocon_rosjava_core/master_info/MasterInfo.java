package com.github.robotics_in_concert.rocon_rosjava_core.master_info;

/*****************************************************************************
** Imports
*****************************************************************************/

import org.apache.commons.logging.Log;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.exception.RosRuntimeException;

import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.ListenerNode;
import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.RosTopicInfo;

/*****************************************************************************
** MasterInfo
*****************************************************************************/

public class MasterInfo extends AbstractNodeMain {

	private rocon_std_msgs.MasterInfo msg;

    @Override
    public void onStart(final ConnectedNode connectedNode) {
        final Log log = connectedNode.getLog();
        RosTopicInfo topicInformation = new RosTopicInfo(connectedNode);
        String topicName = "";
        try {
        	topicName = topicInformation.findTopic("rocon_std_msgs/MasterInfo");
        	//ListenerNode<rocon_std_msgs.MasterInfo> masterInfo = new ListenerNode<rocon_std_msgs.MasterInfo>(connectedNode, topicName, "rocon_std_msgs/MasterInfo");
        	ListenerNode<rocon_std_msgs.MasterInfo> masterInfo = new ListenerNode<rocon_std_msgs.MasterInfo>(connectedNode, topicName, rocon_std_msgs.MasterInfo._TYPE);
    		masterInfo.waitForResponse();
        	this.msg = masterInfo.getMessage();
        	log.info("Master Info : retrieved successfully [" + this.msg.getName() + "]");
        } catch(RosRuntimeException e) {
    		log.error("Master Info : " + e.getMessage());
        }
    }

    /****************************************
    ** Getters
    ****************************************/

    @Override
    public GraphName getDefaultNodeName() {
		return GraphName.of("rocon_rosjava_master_info");
    }
    
    public String getName() {
    	return this.msg.getName();
    }

    public String getDescription() {
    	return this.msg.getDescription();
    }

    public String getIconResourceName() {
    	return this.msg.getIcon().getResourceName();
    }

    public String getIconFormat() {
    	return this.msg.getIcon().getFormat();
    }
    
    public rocon_std_msgs.Icon getIcon() {
    	return this.msg.getIcon();
    }

    /****************************************
    ** Main
    ****************************************/

   public static void main(String argv[]) throws java.io.IOException {
    	String[] args = { "com.github.robotics_in_concert.rocon_rosjava_core.master_info.MasterInfo" };
    	try {
    		org.ros.RosRun.main(args);
    	} catch(RosRuntimeException e) {
    		System.out.println("Master Info: ros runtime error");
		} catch(Exception e) {
			System.out.println("Master Info: unknown error");
    	}
    }    	
}


