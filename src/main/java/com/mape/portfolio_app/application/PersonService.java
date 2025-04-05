package com.mape.portfolio_app.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mape.portfolio_app.domain.Person;
import com.mape.portfolio_app.domain.Role;
import com.mape.portfolio_app.exception.UserAlreadyExistException;
import com.mape.portfolio_app.repository.PersonRepository;
import com.mape.portfolio_app.util.JsonWebTokenUtil;

@Service
public class PersonService {
    
    @Autowired
    private PersonRepository personRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JsonWebTokenUtil jsonWebTokenUtil;
    

    public Person saveUser(Person person) throws UserAlreadyExistException {

        person.setEmail(person.getEmail().toLowerCase());


        if(personRepository.findByEmail(person.getEmail()) != null)
            throw new UserAlreadyExistException(person.getEmail() + "', is already in use.", "nonUniqueEmail");
        

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        person.setPassword(encoder.encode(person.getPassword()));
        
        Role role = new Role();
        role.setRoleId(2);
        role.setRoleName("user");
        person.setRole(role);

        personRepository.save(person);
        return person;
    }

    public String authenticate(Person person) throws BadCredentialsException {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(person.getEmail().toLowerCase(), person.getPassword()));

        if(!authentication.isAuthenticated()) {
            throw new BadCredentialsException("Invalid email or password.");
        }
        
        Person loggedInPerson = personRepository.findByEmail(person.getEmail());
        return jsonWebTokenUtil.generateToken(loggedInPerson.getPersonId(), loggedInPerson.getRole().getRoleName());


        
    }
}
