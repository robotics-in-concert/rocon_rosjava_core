package com.github.robotics_in_concert.rocon_rosjava_core.master_info;

/*****************************************************************************
** Imports
*****************************************************************************/

import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.ListenerException;
import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.ListenerNode;
import com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.RosTopicInfo;
import com.google.common.collect.Lists;

import org.ros.internal.loader.CommandLineLoader;
import org.ros.namespace.GraphName;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.DefaultNodeMainExecutor;
import org.ros.node.NodeConfiguration;
import org.ros.node.NodeMainExecutor;

import java.util.concurrent.TimeoutException;

/*****************************************************************************
** MasterInfo
*****************************************************************************/

public class MasterInfo extends AbstractNodeMain {

	private ListenerNode<rocon_std_msgs.MasterInfo> masterInfoListener;

	public MasterInfo() {
		this.masterInfoListener = new ListenerNode<rocon_std_msgs.MasterInfo>();
	}
    @Override
    public void onStart(final ConnectedNode connectedNode) {
        RosTopicInfo topicInformation = new RosTopicInfo(connectedNode);
    	String topicName = topicInformation.findTopic("rocon_std_msgs/MasterInfo");
    	this.masterInfoListener.connect(connectedNode, topicName, rocon_std_msgs.MasterInfo._TYPE);
    }

    /**
     * Wait for data to come in. This uses a default timeout
     * set by ListenerNode.
     * 
     * @see ListenerNode
     * @throws InteractionsException : if listener error, timeout or general runtime problem
     */
    public void waitForResponse() throws MasterInfoException {
    	try {
    		this.masterInfoListener.waitForResponse();
	    } catch(ListenerException e) {
	    	throw new MasterInfoException(e.getMessage());
	    } catch(TimeoutException e) {
	    	throw new MasterInfoException(e.getMessage());
	    }
    }

    /****************************************
    ** Getters
    ****************************************/

    @Override
    public GraphName getDefaultNodeName() {
		return GraphName.of("rocon_rosjava_master_info");
    }
    
    public String getName() throws MasterInfoException {
    	try {
	    	if (this.masterInfoListener.getMessage() == null) {
	    		masterInfoListener.waitForResponse();
	    	}
	    } catch(ListenerException e) {
	    	throw new MasterInfoException(e.getMessage());
	    } catch(TimeoutException e) {
	    	throw new MasterInfoException(e.getMessage());
	    }
    	return this.masterInfoListener.getMessage().getName();
    }

    public String getDescription() throws MasterInfoException {
    	try {
	    	if (this.masterInfoListener.getMessage() == null) {
	    		masterInfoListener.waitForResponse();
	    	}
	    } catch(ListenerException e) {
	    	throw new MasterInfoException(e.getMessage());
	    } catch(TimeoutException e) {
	    	throw new MasterInfoException(e.getMessage());
	    }
    	return this.masterInfoListener.getMessage().getDescription();
    }

    public String getIconResourceName() throws MasterInfoException {
    	try {
	    	if (this.masterInfoListener.getMessage() == null) {
	    		masterInfoListener.waitForResponse();
	    	}
	    } catch(ListenerException e) {
	    	throw new MasterInfoException(e.getMessage());
	    } catch(TimeoutException e) {
	    	throw new MasterInfoException(e.getMessage());
	    }
    	return this.masterInfoListener.getMessage().getIcon().getResourceName();
    }

    public String getIconFormat() throws MasterInfoException {
    	try {
	    	if (this.masterInfoListener.getMessage() == null) {
	    		masterInfoListener.waitForResponse();
	    	}
	    } catch(ListenerException e) {
	    	throw new MasterInfoException(e.getMessage());
	    } catch(TimeoutException e) {
	    	throw new MasterInfoException(e.getMessage());
	    }
    	return this.masterInfoListener.getMessage().getIcon().getFormat();
    }
    
    public rocon_std_msgs.Icon getIcon() throws MasterInfoException {
    	try {
	    	if (this.masterInfoListener.getMessage() == null) {
	    		masterInfoListener.waitForResponse();
	    	}
	    } catch(ListenerException e) {
	    	throw new MasterInfoException(e.getMessage());
	    } catch(TimeoutException e) {
	    	throw new MasterInfoException(e.getMessage());
	    }
    	return this.masterInfoListener.getMessage().getIcon();
    }

    /****************************************
    ** Main
    ****************************************/

    public static void main(String argv[]) throws java.io.IOException {
        // Pulling the internals of rosrun to slightly customise what to run
    	String[] args = { "com.github.robotics_in_concert.rocon_rosjava_core.master_info.MasterInfo" };
    	CommandLineLoader loader = new CommandLineLoader(Lists.newArrayList(args));
    	NodeConfiguration nodeConfiguration = loader.build();
    	MasterInfo masterInfo = new MasterInfo();
    	
        NodeMainExecutor nodeMainExecutor = DefaultNodeMainExecutor.newDefault();
        nodeMainExecutor.execute(masterInfo, nodeConfiguration);
        try {
            masterInfo.waitForResponse();
        	System.out.println("MasterInfo : retrieved information [" + masterInfo.getName() + "]");
	    } catch(MasterInfoException e) {
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


