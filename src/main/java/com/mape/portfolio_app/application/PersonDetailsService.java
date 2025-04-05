package com.mape.portfolio_app.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mape.portfolio_app.domain.Person;
import com.mape.portfolio_app.repository.PersonRepository;

@Service
public class PersonDetailsService implements UserDetailsService {
    
    @Autowired
    private PersonRepository personRepository;
    

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Person person = personRepository.findByEmail(email);
        
        if(person == null)
            throw new UsernameNotFoundException(email);
        else{
            return person; 
        }
    }
}
