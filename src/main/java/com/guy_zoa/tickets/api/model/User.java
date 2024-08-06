package com.guy_zoa.tickets.api.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * Nom de la classe : User
 * représente un utilisateur 
 * un utilisateur est caractérisé par un nom et un email. Il possède aussi un id pour les besoins de persistance en BD et une liste de  roles pour lui attribuer des privilèges d'accès
 * un User entretient une relation bidirectionnelle de type 1-n avec un Ticket. 
 * un User entretient une relation unidirectionnelle de type n-n avec Role
 *
 * Date : 03/08/2024
 * @version 1.0
 * 
 * @author Guy Aurelien ZOA
 */
@Entity
@Table(name="utilisateurs")
@JsonIgnoreProperties(value = {"user_id", "tickets"})
public class User implements UserDetails{
	 @Id
	 @GeneratedValue(strategy=GenerationType.IDENTITY)
	 @Column(name="utilisateur_id")
	 private Long user_id;
	 
	 @Column(name="nom_utilisateur")
	 private String username;
	 
	 @Column(name="email_utilisateur")
	 private String email;
	
	 @OneToMany(
	   cascade = CascadeType.ALL,
	   fetch = FetchType.EAGER,
	   orphanRemoval = true,
	   mappedBy = "theUser")
	 private List<Ticket> tickets = new ArrayList<Ticket>();
	 
	 
	 @ManyToMany( fetch = FetchType.EAGER)
	 @JoinTable(
	       name = "utilisateurs_roles",
		   joinColumns = @JoinColumn(name="user_id"),
		   inverseJoinColumns = @JoinColumn(name="role_id")
		   )
	  private Set<Role> authorities = new HashSet<Role>();
	 
	 
	 public User(String username, String email){
		 this.username = username;
		 this.email = email;
	 }
	 
	 public User(){
		 this.username = "";
		 this.email = "";
	 }
	 //useful for test
	 public User(Long id, String username, String email){
		 this.username = username;
		 this.email = email;
		 this.user_id = id;
	 }

	 public String getEmail(){
		 return this.email;
	 }
	 
	 public void setUsername(String username){
		 this.username = username;
	 }
	 public void setEmail(String email){
		 this.email = email;
	 }
	 
	 public List<Ticket>getTickets(){
		 return this.tickets;
	 }
	 
	 public Set<Role> getAuthorities(){
		 return this.authorities;
	 }
	 public void setAuthorities(Set<Role> authorities){
		 this.authorities = authorities;
	 }
	 
	 
	 public void addTicket(Ticket ticket){
		 this.tickets.add(ticket);
		 ticket.setTheUser(this);
	 }
	 public void removeTicket(Ticket ticket){
		 this.tickets.remove(ticket);
		 ticket.setTheUser(null); 
	 }
	 
	 @Override
     public String getUsername() {
        return this.username;
     }
	 
	 public String getPassword() {
		return this.email;
	 }
	 
	
	 public Long getId() {
			return this.user_id;
		 }
	 
     @Override
     public boolean isAccountNonExpired() {
        return true;
     }
     
     @Override
     public boolean isAccountNonLocked() {
        return true;
     }
     
     @Override
     public boolean isCredentialsNonExpired() {
        return true;
     }
     
     @Override
    public boolean isEnabled() {
        return true;
    }
}