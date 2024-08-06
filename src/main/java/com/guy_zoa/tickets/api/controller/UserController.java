package com.guy_zoa.tickets.api.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.guy_zoa.tickets.api.dtos.UserDto;
import com.guy_zoa.tickets.api.exceptions.UserNotFoundException;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.model.User;
import com.guy_zoa.tickets.api.repository.UserRepository;
import com.guy_zoa.tickets.api.service.UserServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

@Api("API pour les opérations sur les utilisateurs.")
@RestController
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserServiceImpl userService;

	
	
	

	@ApiOperation(value = "Récupère tous les utilisateurs à condition d'être connecté en tant qu'admin")
	@GetMapping("/users")
	@PreAuthorize("hasRole('ADMIN')")
	public Iterable<User> getAllUsers(){
		return userRepository.findAll();
	}
	
	@ApiOperation(value = "Récupère tous les tickets d'un utilisateur à partir de son id à condition qu'il soit en base de données. L'utilisateur envoyant la requête doit être authentifié")
	@GetMapping("/users/{id}/ticket")
	@PreAuthorize("hasAnyRole('ADMIN', 'USER')")
	public List<Ticket> allTicketsOfUser(@PathVariable Long id){
		return userService.allTicketsOf(id);
	}
	
	@ApiOperation(value = "crée un nouvel utilisateur")
	@PostMapping("/users")
	public ResponseEntity<User> registerUser(@RequestBody UserDto userDto){
			
		if(Objects.isNull(userDto)) noContentResponse();
	    
		User userRegistered = userService.registerUser(userDto);
		if(userRegistered!= null) {
			URI location = ServletUriComponentsBuilder

              .fromCurrentRequest()

              .path("/{id}")

              .buildAndExpand(userRegistered.getId())

              .toUri();
		
		    return ResponseEntity.created(location).build();
	}
		else return null;
}
	
	 @ApiOperation(value = "met à jour un utilisateur connaissant son id et ses modifications! L'utilisateur doit exister en base de données. Requête authorisée uniquement pour ADMIN")
	 @PutMapping("/users/{id}")
	 @PreAuthorize("hasRole('ADMIN')")
	 public ResponseEntity<User> updateUser(@RequestBody UserDto userDto, @PathVariable Long id)throws UserNotFoundException{
		
	  	if(Objects.isNull(userDto)) noContentResponse();
		
		//we ckeck if the user with the given id exist
		 User user = userRepository.findById(id).orElseThrow(()-> new UserNotFoundException("l'utilisateur avec id = "+id+" n'existe pas!"));
		 if(user!=null) {
		    user.setUsername(userDto.username());
		    user.setEmail(userDto.email());
		 }
		 return ResponseEntity.ok(userRepository.save(user));
		 
	}
	private ResponseEntity<User> noContentResponse(){
		return ResponseEntity.noContent().build();
}
	
	
	
}
