package com.ss.schedulesys.service.errors;

public class ScheduleSysException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6877986721740677938L;

	private final String message;
	private final String description;

	public ScheduleSysException(String message, String description) {
		super(message);
		this.message = message;
		this.description = description;
	}
	
	public ScheduleSysException(String message) {
		this(message, null);
	}

	public ErrorVM getErrorVM() {
		return new ErrorVM(message, description);
	}

}
