package com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils;

/*****************************************************************************
** Imports
*****************************************************************************/

import org.ros.exception.RemoteException;
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
	
	public BlockingServiceClientNode(ConnectedNode connectedNode, String serviceName, String serviceType, RequestType request) throws ServiceNotFoundException {
		NameResolver resolver = NodeNameResolver.newRoot();
        String resolvedServiceName = resolver.resolve(serviceName).toString();
        try {
        	srvClient = connectedNode.newServiceClient(resolvedServiceName, serviceType);
        } catch (ServiceNotFoundException e) {
        	throw e;
        }
		srvClient.call(request, this.setupListener());
	}

	public void waitForResponse() {
		// otherwise wait for the notification
		synchronized(this.srvClient) {
			try {
				if ((this.response == null) && (this.errorMessage != "")) {
					this.srvClient.wait(4000); // milliseconds
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private ServiceResponseListener<ResponseType> setupListener() {
		ServiceResponseListener<ResponseType> listener = new ServiceResponseListener<ResponseType>() {
            @Override
            public void onSuccess(ResponseType r) {
                synchronized(srvClient) {
                    response = r;
	                srvClient.notifyAll();
                }
            }
            @Override
            public void onFailure(RemoteException e) {
                synchronized(srvClient) {
                	errorMessage = e.getMessage();
                	srvClient.notifyAll();
                }
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

	public String getErrorMessage() {
    	return this.errorMessage;
    }


}
