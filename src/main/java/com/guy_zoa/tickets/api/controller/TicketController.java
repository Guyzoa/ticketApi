package com.guy_zoa.tickets.api.controller;

import java.net.URI;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.guy_zoa.tickets.api.dtos.TicketDto;
import com.guy_zoa.tickets.api.exceptions.TicketAlreadyRegisteredException;
import com.guy_zoa.tickets.api.exceptions.TicketNotFoundException;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.repository.TicketRepository;
import com.guy_zoa.tickets.api.service.TicketServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Api("API pour les opérations sur les tickets")
@RestController
public class TicketController {

	
	
	@Autowired
	private TicketRepository ticketRepository;
	
	
	
	@Autowired
	private TicketServiceImpl ticketService;
	
	
	
	
	@ApiOperation(value = "récupère tous les tickets en base de données à condition que l'expéditeur de la requête soit ADMIN")
	@GetMapping("/tickets")
	@PreAuthorize("hasRole('ADMIN')")
	public Iterable<Ticket> getAllTickets(){
		return ticketRepository.findAll();
	}
	
	@ApiOperation(value = "récupère un ticket par son id à condition que l'expéditeur de la requête soit authentifié")
	@GetMapping("/tickets/{id}")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public Ticket getTicketWithId(@PathVariable Long id){
		Ticket ticket = checkTicketPresence(id);
		
		return ticket;
	}
	
	@ApiOperation(value = "crée un nouveau ticket en base de données. A condition que l'expéditeur de la requête soit ADMIN")
	@PostMapping("/tickets")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Ticket> createTicket(@RequestBody TicketDto ticketDto){
		
		if(Objects.isNull(ticketDto)) noContentResponse();
		
		Ticket ticketSavedAlready = this.ticketRepository.findByTitle(ticketDto.title());
		if(ticketSavedAlready != null) {
			throw new TicketAlreadyRegisteredException("un ticket avec ce titre est déjà enregistré avec l'id "+ ticketSavedAlready.getId() );
		}
	   else {
		Ticket newTicket = new Ticket(ticketDto.title(), ticketDto.description());
		newTicket = ticketRepository.save(newTicket);
		
		URI location = ServletUriComponentsBuilder

              .fromCurrentRequest()

              .path("/{id}")

              .buildAndExpand(newTicket.getId())

              .toUri();
		
		return ResponseEntity.created(location).build();
	  }
	}
	
	@ApiOperation(value = "met à jour un ticket existant en base de données connaissant son id et les données modifiées.L'expéditeur de la requête doit être ADMIN ")
	@PutMapping("/tickets/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Ticket> updateTicket(@RequestBody TicketDto ticketDto, @PathVariable Long id){
		
		if(Objects.isNull(ticketDto)) noContentResponse();
		
		Ticket ticket = checkTicketPresence(id);
		ticket.setTitle(ticketDto.title());
		ticket.setDescription(ticketDto.description());
		
		return ResponseEntity.ok(ticketRepository.save(ticket));
	}
	
	@ApiOperation(value = "assigne un ticket à un utilisateur connaissant l'id du ticket et celui de l'utilisateur. Le ticket et l'utilisateur doivent exister au préalable")
	@PutMapping("tickets /{id}/assign/{userId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Ticket> assignTicket(@RequestBody TicketDto ticketDto, @PathVariable Long id, @PathVariable Long userId){
		if(Objects.isNull(ticketDto)) noContentResponse();
		
		return ResponseEntity.ok(ticketService.assignTicketToUser(ticketDto,id,userId));
		
	}
	
	@ApiOperation(value = "supprime un ticket connaissant son id. L'expéditeur de la requête doit être ADMIN")
	@DeleteMapping("tickets /{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<Ticket> deleteTicket(@PathVariable Long id){
		
		Ticket ticket = checkTicketPresence(id);
		ticketRepository.deleteById(id);
		
		 return ResponseEntity.ok(ticket);
	}
	
	
	
	
	private Ticket checkTicketPresence(Long ticket_id) throws TicketNotFoundException{
		return ticketRepository.findById(ticket_id).orElseThrow( ()-> new TicketNotFoundException("le ticket d'id = "+ticket_id+" que vous voulez supprimer n'existe pas!"));
	}
	
	private ResponseEntity<Ticket> noContentResponse(){
			return ResponseEntity.noContent().build();
	}
}
