package com.github.robotics_in_concert.rocon_rosjava_core.master_info;

public class MasterInfoException extends Exception {

	public MasterInfoException(final Throwable throwable) {
	    super(throwable);
	}
	  
	public MasterInfoException(final String message, final Throwable throwable) {
	    super(message, throwable);
	}
	  
	public MasterInfoException(final String message) {
	    super(message);
	}
}
