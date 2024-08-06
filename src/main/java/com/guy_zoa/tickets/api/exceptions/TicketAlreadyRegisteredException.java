package com.guy_zoa.tickets.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class TicketAlreadyRegisteredException extends RuntimeException{

	public   TicketAlreadyRegisteredException(String message){
		 super(message);
	}
}
