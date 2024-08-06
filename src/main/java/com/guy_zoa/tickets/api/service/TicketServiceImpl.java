package com.guy_zoa.tickets.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guy_zoa.tickets.api.dtos.TicketDto;
import com.guy_zoa.tickets.api.exceptions.TicketNotFoundException;
import com.guy_zoa.tickets.api.exceptions.UserNotFoundException;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.model.User;
import com.guy_zoa.tickets.api.repository.TicketRepository;
import com.guy_zoa.tickets.api.repository.UserRepository;

@Service
public class TicketServiceImpl implements TicketService{


	@Autowired
	private TicketRepository ticketRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	
	@Override
	public Ticket assignTicketToUser(TicketDto ticketDto, Long ticket_id, Long user_id)
			throws UserNotFoundException, TicketNotFoundException {
		
		Ticket ticket = this.ticketRepository.findById(ticket_id).orElseThrow( ()-> new TicketNotFoundException("le ticket d'id = "+ticket_id+" n'existe pas!") );
		
		User user = this.userRepository.findById(user_id).orElseThrow( ()-> new TicketNotFoundException("l'utilisateur avec id = "+user_id+" n'existe pas!") );

		ticket.setTitle(ticketDto.title());
		ticket.setDescription(ticketDto.description());
		
		//	assignment of the ticket to the given user 
		user.addTicket(ticket);
		this.userRepository.save(user);
		//we send back the ticket assigned 
		
		return ticket;
	}

}