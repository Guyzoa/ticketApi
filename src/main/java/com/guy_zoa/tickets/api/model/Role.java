package com.guy_zoa.tickets.api.model;

import org.springframework.security.core.GrantedAuthority;

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
 * Nom de la classe : Role
 * représente un rôle et permet de caractérisé les privilèges ou authorisations octroyés à un User 
 * un rôle est caractérisé par son intitulé qui est une chaîne de caractère.
 * 
 * Date : 03/08/2024
 * @version 1.0
 * 
 * @author Guy Aurélien ZOA
 */

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long role_id;

    @Column(name = "role")
    private String authority;

    public Role() {
    	this.authority = "ROLE_USER";
    }
    
    
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return authority;
	}
	public void setAuthority(String role) {
		// TODO Auto-generated method stub
		this.authority = role;
	}
}