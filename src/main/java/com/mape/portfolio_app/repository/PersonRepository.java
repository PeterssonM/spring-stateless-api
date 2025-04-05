package com.mape.portfolio_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mape.portfolio_app.domain.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer>{

    /**
     * Retrieves a specific {@link Person} entity from the database.
     * @param email A persons associated email. 
     * @return The found {@link Person} entity or null if no {@link Person} was found.
     */
    Person findByEmail(String email);
    
}
