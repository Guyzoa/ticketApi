package com.guy_zoa.tickets.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.guy_zoa.tickets.api.dtos.UserDto;
import com.guy_zoa.tickets.api.model.Role;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.model.User;
import com.guy_zoa.tickets.api.repository.UserRepository;

@SpringBootTest
public class UserServiceImplTest {
	   @Autowired
	   private UserServiceImpl userService;
	   
	   @MockBean
	   private UserRepository userRepository;
	   
	  @Test
	   public void givenUserId_whenAllTicketsOf_ThenReturnTicketsList(){
		   
		   //ARRANGE
		     User userTest = new User(2L,"alex","alex@gmail.com");
		     Ticket ticket1 = new Ticket(1L,"titre1","description1");
			 Ticket ticket2 = new Ticket(2L,"titre2","description2");
			 
			 userTest.addTicket(ticket1);
			 userTest.addTicket(ticket2);
		     Optional<User> user = Optional.of(userTest);
			 
		     Mockito.when(userRepository.findById(userTest.getId())).thenReturn(user);
		     String alex = "alex";
			 
			 //ACT 
		     Optional<User> theUser = userRepository.findById(userTest.getId());
			 
			 //ASSERT
		     assertEquals(alex,theUser.get().getUsername());
		     assertEquals(2, theUser.get().getTickets().size());
		   
	   }
		
		@Test
		public void  givenUser_whenRegisterUser_ThenReturnUserSavedWithId(){
			
			//ARRANGE
			 User userTest = new User(2L,"alex","alex@gmail.com");
			 
	         Set<Role> authorities = new HashSet<Role>();
	         authorities.add(new Role());
		     userTest.setAuthorities(authorities);
		     
			 UserDto userDto = new UserDto("alex","alex@gmail.com");
			 User userBuild = new User(userDto.username(), userDto.email());
			 userBuild.setAuthorities(authorities);
			
			Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(userTest);
			Mockito.when(userRepository.findByUsername(userDto.username())).thenReturn(null);
			
			
			//ACT
			User userSaved = userService.registerUser(userDto);
			
			//ASSERT
			
			assertEquals(2L, userSaved.getId());
		}
}
