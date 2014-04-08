package com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils;


public class BlockingServiceClientException extends Exception {

	public BlockingServiceClientException(final Throwable throwable) {
	    super(throwable);
	}
	  
	public BlockingServiceClientException(final String message, final Throwable throwable) {
	    super(message, throwable);
	}
	  
	public BlockingServiceClientException(final String message) {
	    super(message);
	}
}
