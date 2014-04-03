package com.github.rocon_rosjava_core.master_info;

/*****************************************************************************
** Imports
*****************************************************************************/

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.namespace.NameResolver;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.ros.exception.RosRuntimeException;

import com.github.rocon_rosjava_core.rosjava_utils.SubscriberProxy;

/*****************************************************************************
** MasterInfo
*****************************************************************************/

public class MasterInfo extends AbstractNodeMain {
    // data from the master info message
    private rocon_std_msgs.MasterInfo msg;
//    private String name = "";
//    private String description = "";
//    private String iconResourceName = "";
//    private String iconFormat = "";
//    //private Something icon;
//    private String errorMessage = "";
//
//    /**
//     * Utility function to block until the callback gets processed.
//     *
//     * @throws org.ros.exception.RosRuntimeException : when it times out waiting for the service.
//     */
//    public void waitForResponse() throws RosRuntimeException {
//        int count = 0;
//        while ( this.name == "" ) {
//            if ( errorMessage != "" ) {  // errorMessage gets set by an exception in the run method
//                throw new RosRuntimeException(errorMessage);
//            }
//            try {
//                Thread.sleep(200);
//            } catch (Exception e) {
//                throw new RosRuntimeException(e);
//            }
//            if ( count == 20 ) {  // timeout.
//                this.errorMessage = "timed out waiting for a gateway_info publication";
//                throw new RosRuntimeException(this.errorMessage);
//            }
//            count = count + 1;
//        }
//    }
//
    /**
     * One off call to the master info topic.
     *
     * Note - you should only ever call (via NodeMainExecutor.execute() this once! It will fail
     * due to this instance being non-unique in the set of rosjava nodemains for this activity.
     *
     * @param connectedNode
     */
    @Override
    public void onStart(final ConnectedNode connectedNode) {
//        if (this.connectedNode != null) {
//            errorMessage = "gateway info subscribers may only ever be executed once.";
//            return;
//        }
        final Log log = connectedNode.getLog();
        NameResolver resolver = connectedNode.getResolver().newChild("concert");
        String topicName = resolver.resolve("info").toString();
    	SubscriberProxy<rocon_std_msgs.MasterInfo> masterInfo = new SubscriberProxy<rocon_std_msgs.MasterInfo>(connectedNode, topicName, "rocon_std_msgs/MasterInfo");
    	try {
    		this.msg = masterInfo.call();
    	} catch (RosRuntimeException e) {
    		log.error("Master Info : " + e.getMessage());
    	}
    	log.info("Master Info : retrieved successfully [" + this.msg.getName() + "]");
//        this.connectedNode = connectedNode;
//        rocon_std_msgs.MasterInfo masterInfo;
//        subscriber = connectedNode.newSubscriber(topicName, "rocon_std_msgs/MasterInfo");
//        this.listener = new MessageListener<rocon_std_msgs.MasterInfo>() {
//            @Override
//            public void onNewMessage(rocon_std_msgs.MasterInfo message) {
//                name = message.getName();
//                description = message.getDescription();
//                iconResourceName = message.getIcon().getResourceName();
//                iconFormat = message.getIcon().getFormat();
//                // do something with getIcon().getData() - uint8[]
//                log.info("Master Info : retrieved successfully [" + name + "]");
//            }
//        };
//        subscriber.addMessageListener(this.listener);
//        log.info("Master Info : latched subscriber created [" + topicName + "]");
    }

    /****************************************
    ** Getters
    ****************************************/

    @Override
    public GraphName getDefaultNodeName() {
		return GraphName.of("rocon/master_info");
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
    
//    public Icon getIcon() {
//    	// dunno yet
//    }

    /****************************************
    ** Main
    ****************************************/

   public static void main(String argv[]) throws java.io.IOException {
    	String[] args = { "com.github.rocon_rosjava_core.master_info.MasterInfo" };
    	try {
    		org.ros.RosRun.main(args);
    	} catch(RosRuntimeException e) {
    		System.out.println("Master Info: ros runtime error");
		} catch(Exception e) {
			System.out.println("Master Info: unknown error");
    	}
    }    	
}


