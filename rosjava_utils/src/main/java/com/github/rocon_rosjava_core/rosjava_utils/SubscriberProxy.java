package com.github.rocon_rosjava_core.rosjava_utils;

/*****************************************************************************
** Imports
*****************************************************************************/

import org.apache.commons.logging.Log;
import org.ros.exception.RosRuntimeException;
import org.ros.message.MessageListener;
import org.ros.namespace.NameResolver;
import org.ros.namespace.NodeNameResolver;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

/*****************************************************************************
** Classes
*****************************************************************************/

public class SubscriberProxy<MsgType> {
	private MsgType msg;
	private Subscriber<MsgType> subscriber;
	private MessageListener<MsgType> listener;
	final Log log;
	String errorMessage;
	
	public SubscriberProxy(ConnectedNode connectedNode, String topicName, String topicType) {
		// DJS: Can we extract topic type from the MsgType class?
		this.log = connectedNode.getLog();
		// don't start listening till we actually call
		this.listener = null;
		this.msg = null;
		NameResolver resolver = NodeNameResolver.newRoot();
        // NameResolver resolver = connectedNode.getResolver().newChild("concert");
        String resolvedTopicName = resolver.resolve(topicName).toString();
		this.subscriber = connectedNode.newSubscriber(
				resolvedTopicName,
				topicType
				);
	}
	
	/**
	 * Blocking call style - loops around for a hardcoded length of 4 seconds right now
	 * waiting for a message to come in.
	 * 
	 * @todo : an option for getting the last message caught if available
	 * @todo : a timeout argument
	 * @return
	 * @throws RosRuntimeException
	 */
	public MsgType call() throws RosRuntimeException {
		this.msg = null;
		this.errorMessage = "";
		if (this.listener == null) {
			this.addListener();
		}
        int count = 0;
        while ( this.msg == null ) {
            if ( this.errorMessage != "" ) {  // errorMessage gets set by an exception in the run method
                throw new RosRuntimeException(this.errorMessage);
            }
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                throw new RosRuntimeException(e);
            }
            // timeout.
            if ( count == 20 ) {
                this.errorMessage = "timed out waiting for a " + subscriber.getTopicName() + "publication";
                throw new RosRuntimeException(this.errorMessage);
            }
            count = count + 1;
        }
		return this.msg;
	}
	
	private void addListener() {
		// @todo : check if listener is not null and handle appropriately
        this.listener = new MessageListener<MsgType>() {
            @Override
            public void onNewMessage(MsgType message) {
            	msg = message;
            }
        };
		this.subscriber.addMessageListener(this.listener);
	}

    /****************************************
    ** Getters
    ****************************************/

	public MsgType getMessage() {
    	return this.msg;
    }


}
