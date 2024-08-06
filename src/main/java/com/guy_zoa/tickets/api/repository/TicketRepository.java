package com.guy_zoa.tickets.api.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.guy_zoa.tickets.api.model.Ticket;

@Repository
public interface TicketRepository extends CrudRepository<Ticket,Long>{
   Ticket findByTitle(String title);
}
