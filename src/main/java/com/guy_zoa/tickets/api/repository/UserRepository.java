package com.guy_zoa.tickets.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.guy_zoa.tickets.api.model.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {
	
      User findByUsername(String username);
}
