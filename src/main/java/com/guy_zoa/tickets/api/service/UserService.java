package com.guy_zoa.tickets.api.service;

import java.util.List;

import com.guy_zoa.tickets.api.dtos.UserDto;
import com.guy_zoa.tickets.api.exceptions.UserAlreadyRegisteredException;
import com.guy_zoa.tickets.api.exceptions.UserNotFoundException;
import com.guy_zoa.tickets.api.model.Ticket;
import com.guy_zoa.tickets.api.model.User;

public interface UserService {
	public List<Ticket> allTicketsOf(Long user_id)throws UserNotFoundException;
	
	public User registerUser(UserDto userDto)throws UserAlreadyRegisteredException;
}
