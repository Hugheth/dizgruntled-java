package com.hugheth.dizgruntled;

public class GruntException extends Exception {

	private static final long serialVersionUID = -6363417401540494565L;

	public GruntException(String string, Throwable cause) {
		super(string, cause);
		System.out.println(string);
		if (cause != null)
			System.out.println(cause.getMessage());
	}
	
	@Override
	public String getMessage() {
		if (getCause() != null)
			return getCause().getMessage();
		else
			return super.getMessage();
	}
}
