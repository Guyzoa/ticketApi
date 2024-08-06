package com.guy_zoa.tickets.api.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
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
import com.guy_zoa.tickets.api.dtos.UserDto;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.model.User;
import com.guy_zoa.tickets.api.repository.UserRepository;
import com.guy_zoa.tickets.api.service.UserServiceImpl;

@WebMvcTest(UserController.class)
@WithMockUser(username = "admin", roles = {"ADMIN","USER"})
public class UserControllerTest{
	
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;
    
	@MockBean
    private UserServiceImpl userService;	
	
	@Autowired
    private ObjectMapper objectMapper;
	
	
	
	@Test
	public void givenUsers_whenGetUsers_thenReturnJsonArray() throws Exception{
		
		  //ARRANGE
		  User alex = new User(1L,"alex","alex@gmail.com");
		  
          List<User> allUsers = Arrays.asList(alex);
          Mockito.when(userRepository.findAll()).thenReturn(allUsers);
 
          //ACT
          ResultActions result = mockMvc.perform(get("/users"));
          
          //ASSERT
          result.andExpect(status().isOk())
          .andDo(print())
          .andExpect(jsonPath("$.size()",
                  is(allUsers.size())));

		
	}
	
	@Test
	public void givenUserAndTickets_whenGetTickets_thenReturnJsonArray() throws Exception{
		
		
		//ARRANGE
		  User alex = new User(1L,"alex","alex@gmail.com");
		  Ticket ticket = new Ticket(2L, "title1","description");
		  alex.addTicket(ticket);
		  
		  Mockito.when(userService.allTicketsOf(alex.getId())).thenReturn(alex.getTickets());
		
		  //ACT
		  ResultActions response = mockMvc.perform(get("/users/{id}/ticket", alex.getId()));
	                
		  //ASSERT	  
		  response.andExpect(status().isOk())
                 .andDo(print())
                 .andExpect(jsonPath("$.size()",
                  is(1)))
                 .andExpect(jsonPath("$[0].title",
                         is("title1")));	
		  
		  
	}
    
	
	@Test
	public void givenUser_whenPostUser_thenReturnStatus201() throws JsonProcessingException, Exception{
		
		 // ARRANGE
		 User alex = new User(1L,"alex","alex@gmail.com");
		 UserDto userDto = new UserDto("alex", "alex@gmail.com");
         Mockito.when(userService.registerUser(Mockito.any(UserDto.class))).thenReturn(alex);

        // ACT
        ResultActions response = mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        // ASSERT
        response.andExpect(status().isCreated());
                  
	}
	
	@Test
	public void givenUser_whenPutUser_thenReturnUserUpdate() throws JsonProcessingException, Exception{
		
		 //ARRANGE
		  User alex = new User(1L,"alex","alex@gmail.com");
		  UserDto userDto = new UserDto("Max","max@gmail.com");
		  Mockito.when(userRepository.findById(alex.getId())).thenReturn(Optional.of(alex));
		 
		  alex.setUsername(userDto.username());
          alex.setEmail(userDto.email());
		
          Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(alex);

        // ACT
        ResultActions response = mockMvc.perform(put("/users/{id}", alex.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDto)));

        // ASSERT
        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.username", is("Max")))
                .andExpect(jsonPath("$.email", is("max@gmail.com")));
	}
	
}
