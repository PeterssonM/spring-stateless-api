package com.mape.portfolio_app.domain;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "person")
public class Person implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id")
    private int personId;

    @Column(name = "name")
    @NotBlank(message = "Name must not be blank.")
    private String name;

    @Column(name = "surname")
    @NotBlank(message = "Surname must not be blank.")
    private String surname;

    @Column(name = "email")
    @Email(message = "Invalid email format.")
    @NotBlank(message = "Email must not be blank.")
    private String email;

    @Column(name = "password")
    @Size(min = 6, message = "Password must be atleast 6 characters.")
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;  // Foreign Key to Role

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    /**
	 * Returns a persons authorities. 
	 * @return A list of granted authorities. 
	 */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRoleName());
        return Collections.singletonList(authority);
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    
}

