package com.spring.phone.reservation.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class PhoneAlreadyBookedException extends Exception{
	private static final long serialVersionUID = 1L;
	public PhoneAlreadyBookedException(String message){
    	super(message);
    }
}
