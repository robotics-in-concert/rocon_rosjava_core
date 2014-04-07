package com.github.robotics_in_concert.rocon_rosjava_core.rosjava_utils;


public class ListenerException extends Exception {

	public ListenerException(final Throwable throwable) {
	    super(throwable);
	}
	  
	public ListenerException(final String message, final Throwable throwable) {
	    super(message, throwable);
	}
	  
	public ListenerException(final String message) {
	    super(message);
	}
}
