package com.spring.phone.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneAlreadyAvailableException extends Exception{
	private static final long serialVersionUID = 1L;
	public PhoneAlreadyAvailableException(String message){
    	super(message);
    }
}
