package com.pinguela.SwingMultipleCalendar;

public class EventOverlapException extends Exception{

	public EventOverlapException() {
		super();
	}

	public EventOverlapException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);

	}

	public EventOverlapException(String message, Throwable cause) {
		super(message, cause);

	}

	public EventOverlapException(String message) {
		super(message);

	}

	public EventOverlapException(Throwable cause) {
		super(cause);

	}
	

}
