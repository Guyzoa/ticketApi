package com.guy_zoa.tickets.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * Nom de la classe : Ticket
 * représente un ticket
 * un Ticket est caractérisé par un titre, une description et un statut. 
 * un Ticket entretient une relation bidirectionnelle de type n-1 avec un User. 
 * 
 * Date : 03/08/2024
 * @version 1.0
 * 
 * @author Guy Aurélien ZOA
 */

@Entity
@Table(name = "tickets")
@JsonIgnoreProperties(value = {"ticket_id", "theUser"})
public class Ticket{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long ticket_id;
	
	@Column(name="titre")
	private String title;
	
	private String description;

    @Column(name = "statut")
	private Status status = Status.EN_COURS;
	
	@ManyToOne(
	 cascade = {CascadeType.MERGE,
	            CascadeType.PERSIST}
			  )
	@JoinColumn( name = "user_id")
	private User theUser = new User();
	
	public Ticket(String title, String description){
		this.title = title;
		this.description = description;
	}
	
	//for tests
	public Ticket(Long id, String title, String description){
		this.ticket_id = id;
		this.title = title;
		this.description = description;
	}
	
	public Ticket(){
		this.title = "";
		this.description = "";
	}
	
	  	 public String getTitle(){
		 return this.title;
	 }
		 public void setTitle(String title){
		 this.title = title;
	 }
	 
	 	 public String getDescription(){
		 return this.description;
	 }
		 public void setDescription(String description){
		 this.description= description;
	 }
	 	 public Status getStatus(){
		 return this.status;
	 }
		 public void setStatus(Status status){
		 this.status = status;
	 }
	 
	    public User getTheUser(){
		 return this.theUser;
	 }
	    public void setTheUser(User user){
		 this.theUser = user;
	 }
	 public Long getId(){
		 return this.ticket_id;
	 }
}

