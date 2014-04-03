package com.github.rocon_rosjava_core.master_info;

//import org.ros.master.client;

import org.apache.commons.logging.Log;
import org.ros.message.MessageListener;
import org.ros.namespace.GraphName;
import org.ros.namespace.NameResolver;
import org.ros.node.AbstractNodeMain;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.ros.exception.RosRuntimeException;

public class MasterInfo extends AbstractNodeMain {
    private MessageListener<rocon_std_msgs.MasterInfo> listener;
    private ConnectedNode connectedNode;
    private Subscriber<rocon_std_msgs.MasterInfo> subscriber;
    // data from the master info message
    private String name = "";
    private String description = "";
    private String iconResourceName = "";
    private String iconFormat = "";
    //private Something icon;
    private String errorMessage = "";

    /**
     * Utility function to block until platform info's callback gets processed.
     *
     * @throws org.ros.exception.RosRuntimeException : when it times out waiting for the service.
     */
    public void waitForResponse() throws RosRuntimeException {
        int count = 0;
        while ( this.name == "" ) {
            if ( errorMessage != "" ) {  // errorMessage gets set by an exception in the run method
                throw new RosRuntimeException(errorMessage);
            }
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                throw new RosRuntimeException(e);
            }
            if ( count == 20 ) {  // timeout.
                this.errorMessage = "timed out waiting for a gateway_info publication";
                throw new RosRuntimeException(this.errorMessage);
            }
            count = count + 1;
        }
    }

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
        if (this.connectedNode != null) {
            errorMessage = "gateway info subscribers may only ever be executed once.";
            return;
        }
        this.connectedNode = connectedNode;
        final Log log = connectedNode.getLog();
    	log.warn("onStart");
        NameResolver resolver = this.connectedNode.getResolver().newChild("concert");
        String topicName = resolver.resolve("info").toString();
        subscriber = connectedNode.newSubscriber(topicName, "rocon_std_msgs/MasterInfo");
        this.listener = new MessageListener<rocon_std_msgs.MasterInfo>() {
            @Override
            public void onNewMessage(rocon_std_msgs.MasterInfo message) {
                name = message.getName();
                description = message.getDescription();
                iconResourceName = message.getIcon().getResourceName();
                iconFormat = message.getIcon().getFormat();
                // do something with getIcon().getData() - uint8[]
                log.info("Master Info : retrieved successfully [" + name + "]");
            }
        };
        subscriber.addMessageListener(this.listener);
        log.info("Master Info : latched subscriber created [" + topicName + "]");
    }
    @Override
    public GraphName getDefaultNodeName() {
		return GraphName.of("rocon/master_info");
    }
    
    public String getName() {
    	return this.name;
    }

    public String getDescription() {
    	return this.description;
    }

    public String getIconResourceName() {
    	return this.iconResourceName;
    }

    public String getIconFormat() {
    	return this.iconFormat;
    }

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
