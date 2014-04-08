package com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils;

/*****************************************************************************
** Imports
*****************************************************************************/

import java.util.concurrent.TimeoutException;
import org.ros.exception.RemoteException;
//import org.ros.exception.RosRuntimeException;
import org.ros.exception.ServiceNotFoundException;
import org.ros.namespace.NameResolver;
import org.ros.namespace.NodeNameResolver;
import org.ros.node.ConnectedNode;
import org.ros.node.service.ServiceClient;
import org.ros.node.service.ServiceResponseListener;

/*****************************************************************************
** Classes
*****************************************************************************/

/**
 * A simple blocking service client with one-shot usage capability.
 *
 * @param <RequestType>
 * @param <ResponseType>
 */
public class BlockingServiceClientNode<RequestType, ResponseType> {
	private ResponseType response;
	private ServiceClient<RequestType, ResponseType> srvClient;
	private String errorMessage;
	
	public BlockingServiceClientNode() {
		this.response = null;
		this.errorMessage = "";
	}

	/**
	 * 
	 * @param connectedNode
	 * @param serviceName
	 * @param serviceType
	 * 
	 * @throws ServiceNotFoundException
	 */
	public void call(ConnectedNode connectedNode, String serviceName, String serviceType) throws ServiceNotFoundException {
		NameResolver resolver = NodeNameResolver.newRoot();
        String resolvedServiceName = resolver.resolve(serviceName).toString();
        try {
        	srvClient = connectedNode.newServiceClient(resolvedServiceName, serviceType);
        } catch (ServiceNotFoundException e) {
        	throw e;
        }
        this.setupListener();
        final RequestType request = srvClient.newMessage();
        srvClient.call(request, this.setupListener());
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
	public void waitForResponse() throws BlockingServiceClientException, TimeoutException {
        int count = 0;
        while ( this.response == null ) {
            if ( this.errorMessage != "" ) {  // errorMessage gets set by an exception in the run method
                throw new BlockingServiceClientException(this.errorMessage);
            }
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                throw new BlockingServiceClientException(e);
            }
            // timeout.
            if ( count == 20 ) {
                this.errorMessage = "timed out waiting for a response";
                throw new TimeoutException(this.errorMessage);
            }
            count = count + 1;
        }
	}
	
	private ServiceResponseListener<ResponseType> setupListener() {
		// @todo : check if listener is not null and handle appropriately
		ServiceResponseListener<ResponseType> listener = new ServiceResponseListener<ResponseType>() {
            @Override
            public void onSuccess(ResponseType r) {
                response = r;
            }

            @Override
            public void onFailure(RemoteException e) {
            	//
            }
        };
        return listener;
	}

    /****************************************
    ** Getters
    ****************************************/

	public ResponseType getResponse() {
    	return this.response;
    }


}
