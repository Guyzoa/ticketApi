package com.guy_zoa.tickets.api.service;

import com.guy_zoa.tickets.api.dtos.TicketDto;
import com.guy_zoa.tickets.api.exceptions.TicketNotFoundException;
import com.guy_zoa.tickets.api.exceptions.UserNotFoundException;
import com.guy_zoa.tickets.api.model.Ticket;

public interface TicketService {
	public Ticket assignTicketToUser(TicketDto ticketDto,Long ticket_id, Long user_id)throws UserNotFoundException,TicketNotFoundException;
}