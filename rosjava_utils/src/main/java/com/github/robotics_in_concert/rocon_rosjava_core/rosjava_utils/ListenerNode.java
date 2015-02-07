package com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils;

/*****************************************************************************
** Imports
*****************************************************************************/

import org.ros.message.MessageListener;
import org.ros.namespace.NameResolver;
import org.ros.namespace.NodeNameResolver;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;

import java.util.concurrent.TimeoutException;

/*****************************************************************************
** Classes
*****************************************************************************/

public class ListenerNode<MsgType> {
	private MsgType msg;
	private Subscriber<MsgType> subscriber;
	private MessageListener<MsgType> listener;
	// final Log log;
	String errorMessage;
	
	public ListenerNode() {
		this.msg = null;
		this.errorMessage = "";
	}

	public void connect(ConnectedNode connectedNode, String topicName, String topicType) {
		this.msg = null;
		this.errorMessage = "";
		NameResolver resolver = NodeNameResolver.newRoot();
        String resolvedTopicName = resolver.resolve(topicName).toString();
		this.subscriber = connectedNode.newSubscriber(
				resolvedTopicName,
				topicType  // DJS: Can we extract topic type from the MsgType class?
				);
		this.setupListener();
	}
	
	/**
	 * Blocking call style - loops around for a hardcoded length of 4 seconds right now
	 * waiting for a message to come in.
	 * 
	 * @todo : an option for getting the last message caught if available
	 * @todo : a timeout argument
	 * 
	 * @return
	 * @throws java.util.concurrent.TimeoutException
	 * @throws com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils.ListenerException
	 */
	public void waitForResponse() throws ListenerException, TimeoutException {
        int count = 0;
        while ( this.msg == null ) {
            if ( this.errorMessage != "" ) {  // errorMessage gets set by an exception in the run method
                throw new ListenerException(this.errorMessage);
            }
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                throw new ListenerException(e);
            }
            // timeout.
            if ( count == 20 ) {
                this.errorMessage = "timed out waiting for a " + subscriber.getTopicName() + "publication";
                throw new TimeoutException(this.errorMessage);
            }
            count = count + 1;
        }
	}
	
	public void waitForNextResponse() throws ListenerException, TimeoutException {
		this.errorMessage = "";
		this.msg = null;
		try {
			this.waitForResponse();
		} catch (ListenerException e) {
			throw e;
		} catch (TimeoutException e) {
			throw e;
		}
	}
	
	private void setupListener() {
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
