package com.github.robotics_in_concert.rocon_rosjava_core.rocon_interactions;

public class InteractionsException extends Exception {

	public InteractionsException(final Throwable throwable) {
	    super(throwable);
	}
	  
	public InteractionsException(final String message, final Throwable throwable) {
	    super(message, throwable);
	}
	  
	public InteractionsException(final String message) {
	    super(message);
	}
}
