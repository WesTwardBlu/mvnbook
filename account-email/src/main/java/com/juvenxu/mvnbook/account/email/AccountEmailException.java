package com.juvenxu.mvnbook.account.email;

public class AccountEmailException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AccountEmailException() {
	}
	
	public AccountEmailException(String msg,Throwable throwable){
		super(msg, throwable);
	}
}
