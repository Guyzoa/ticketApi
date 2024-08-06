package com.guy_zoa.tickets.api.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.guy_zoa.tickets.api.dtos.UserDto;
import com.guy_zoa.tickets.api.exceptions.UserAlreadyRegisteredException;
import com.guy_zoa.tickets.api.exceptions.UserNotFoundException;
import com.guy_zoa.tickets.api.model.Role;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.model.User;
import com.guy_zoa.tickets.api.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	
	public List<Ticket> allTicketsOf(Long user_id)throws UserNotFoundException{
		
		  User user = this.userRepository.findById(user_id).orElseThrow(()-> new UserNotFoundException("l'utilisateur avec id = "+user_id+" n'existe pas!")  );
		    
			  return user.getTickets();
	}
	
	
    public User registerUser(UserDto userDto) throws UserAlreadyRegisteredException {
       
    	User userSavedAlready = userRepository.findByUsername(userDto.username());
    	if(userSavedAlready != null) throw new UserAlreadyRegisteredException("un utilisateur existe déjà avec ce nom avec pour id = "+userSavedAlready.getId());
    	else {
	            
    		User newUser = createUserWithRoles(userDto);

            return userRepository.save(newUser);
    		
       }
}
    public User createUserWithRoles(UserDto userDto) {
    	  
          Set<Role> authorities = new HashSet<Role>();
          authorities.add(new Role());

          User newUser = new User();
		  newUser.setUsername(userDto.username());
          newUser.setEmail(userDto.email());
          newUser.setAuthorities(authorities);
          
    	 return newUser;
    }
}
