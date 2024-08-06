package com.guy_zoa.tickets.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guy_zoa.tickets.api.dtos.TicketDto;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.repository.TicketRepository;
import com.guy_zoa.tickets.api.service.TicketServiceImpl;

@WebMvcTest(TicketController.class)
@WithMockUser(username = "admin", roles = {"ADMIN","USER"})
public class TicketControllerTest {

	
	   @Autowired
	    private MockMvc mockMvc;
		
		@MockBean
		private TicketRepository ticketRepository;
		
		@MockBean
		private TicketServiceImpl ticketService;
		
		@Autowired
	    private ObjectMapper objectMapper;
		
		
		
		@Test
		public void givenTickets_whenGetTickets_thenReturn_JsonArray() throws Exception{
			
			//ARRANGE
			Ticket ticket = new Ticket(1L,"title1","description1");
			Iterable<Ticket> allTickets = Arrays.asList(ticket);
			
			
	        Mockito.when(ticketRepository.findAll()).thenReturn(allTickets);
			
	         //ACT
	          ResultActions result = mockMvc.perform(get("/tickets"));
	          
	          //ASSERT
	          result.andExpect(status().isOk())
	          .andDo(print())
	          .andExpect(jsonPath("$[0].title",
	                  is("title1")));

		}
		
		@Test
		public void givenTicketId_whenGetTicket_thenReturn_JsonTicket() throws Exception{
			
			// ARRANGE
			 Ticket ticket = new Ticket(1L,"title1","description1");
	         Mockito.when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));

	        // ACT
	        ResultActions response = mockMvc.perform(get("/tickets/{id}", ticket.getId()));

	        // ASSERT
	        response.andExpect(status().isOk())
	                .andDo(print())
	                .andExpect(jsonPath("$.title", is("title1")))
	                .andExpect(jsonPath("$.description", is("description1")));
			
		}
	
		 @Test
		 public void givenTicket_whenPostTicket_ThenReturnStatus201() throws JsonProcessingException, Exception{
			 
	     // ARRANGE
			 Ticket ticket = new Ticket(1L,"title1","description1");
			 TicketDto ticketDto = new TicketDto("title1","description1");
			 Mockito.when(ticketRepository.findByTitle(ticketDto.title())).thenReturn(null);
	         Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(ticket);

	        // ACT
	        ResultActions response = mockMvc.perform(post("/tickets")
	                .contentType(MediaType.APPLICATION_JSON)
	                .content(objectMapper.writeValueAsString(ticket)));

	        // ASSERT
	        response.andExpect(status().isCreated());
	    }
		 
		 @Test
			public void givenUser_whenPutTicket_thenReturnTicketUpdate() throws JsonProcessingException, Exception{
				
				 //ARRANGE
			     Ticket ticket = new Ticket(1L,"title1","description1");
				 TicketDto ticketDtoNew = new TicketDto("title2","description2");
				 Ticket ticket2 = new Ticket(1L,ticketDtoNew.title(),ticketDtoNew.description());
				 
				 Mockito.when(ticketRepository.findById(ticket.getId())).thenReturn(Optional.of(ticket));
				
		         Mockito.when(ticketRepository.save(Mockito.any(Ticket.class))).thenReturn(ticket2);

		        // ACT
		        ResultActions response = mockMvc.perform(put("/tickets/{id}", ticket.getId())
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(objectMapper.writeValueAsString( ticketDtoNew)));

		        // ASSERT
		        response.andExpect(status().isOk())
		                .andDo(print())
		                .andExpect(jsonPath("$.title", is("title2")))
		                .andExpect(jsonPath("$.description", is("description2")));
			}
			
		
		 @Test
			public void givenTicketIdAndUserId_whenPutAssign_thenReturnJsonTicket() throws JsonProcessingException, Exception{
				
				 //ARRANGE
			     Ticket ticket = new Ticket(1L,"title1","description1");
				 TicketDto ticketDtoNew = new TicketDto("title2","description2");
				 Ticket ticket2 = new Ticket(ticketDtoNew.title(),ticketDtoNew.description());
				 Mockito.when(ticketService.assignTicketToUser(ticketDtoNew, 1L, 1L)).thenReturn(ticket2);
			

		        // ACT
		        ResultActions response = mockMvc.perform(put("/tickets/{id}", ticket.getId())
		                .contentType(MediaType.APPLICATION_JSON)
		                .content(objectMapper.writeValueAsString( ticketDtoNew)));

		        // ASSERT
		        response.andExpect(status().isOk())
		                .andDo(print())
		                .andExpect(jsonPath("$.title", is("title2")))
		                .andExpect(jsonPath("$.description", is("description2")));
			}
		 
		 
			
}