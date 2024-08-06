package com.guy_zoa.tickets.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.guy_zoa.tickets.api.dtos.TicketDto;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.model.User;
import com.guy_zoa.tickets.api.repository.TicketRepository;
import com.guy_zoa.tickets.api.repository.UserRepository;

@SpringBootTest
public class TicketServiceImplTest {
	
	   @Autowired
	   private TicketServiceImpl ticketService;
	   
	   @MockBean
	   private UserRepository userRepository;
	   
	   @MockBean
	   private TicketRepository ticketRepository;
	   
	   
	   
	   
	   @Test
	   public void givenTicketHisIdAndUserId_whenAssignTicketToUser_thenReturnTicket(){
		
		
		  //ARRANGE
		  Ticket ticket = new Ticket(1L,"title1","description1");
		  Optional<Ticket> optTicket = Optional.of(ticket);
		  
		  User user =  new User(2L,"alex","alex@gmail.com");
		  Optional<User> optUser = Optional.of(user);
		   
		  TicketDto ticketDto = new TicketDto("alex","alex@gmail.com");
		  
		  Mockito.when(ticketRepository.findById(1L)).thenReturn(optTicket);
		  Mockito.when(userRepository.findById(2L)).thenReturn(optUser);
		  
		  //ACT
		  Ticket updateTicket = ticketService.assignTicketToUser(ticketDto, 1L, 2L);
		  
		  
		  //ASSERT
		  assertEquals("alex", updateTicket.getTheUser().getUsername());
		  assertEquals(ticket.getTitle(), updateTicket.getTitle());
	}
}
