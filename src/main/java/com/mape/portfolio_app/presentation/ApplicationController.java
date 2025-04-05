package com.mape.portfolio_app.presentation;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mape.portfolio_app.application.PersonService;
import com.mape.portfolio_app.domain.Person;
import com.mape.portfolio_app.domain.Role;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@RestController
public class ApplicationController {  

    @Autowired
    PersonService personService;
    

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> retriveLoginPage(@RequestBody Person person, HttpServletResponse response) {
        String token = personService.authenticate(person);
        
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);  
        cookie.setSecure(true);    
        cookie.setPath("/");       
        cookie.setMaxAge(3600);    

        response.addCookie(cookie);


        return ResponseEntity.ok(Map.of("message", "Login successful!"));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> retriveLogoutPage(HttpServletResponse response) {
        
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);  
        cookie.setSecure(true);    
        cookie.setPath("/");       
        cookie.setMaxAge(0);    

        response.addCookie(cookie);


        return ResponseEntity.ok(Map.of("message", "Logout successful!"));
    }


    @GetMapping("/register")
    public ResponseEntity<Person> serveRegisterTemplate() {
        Person templatePerson = new Person();
        Role templateRole = new Role();
        templateRole.setRoleName("user");
        templateRole.setRoleId(2);
        templatePerson.setRole(templateRole);
        
        return ResponseEntity.ok(templatePerson);
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> handleRegisterRequest(@RequestBody @Valid Person person) {
        personService.saveUser(person);
        return ResponseEntity.ok(Map.of("message", "User registered successfully.", "email", person.getEmail()));
    }

    @GetMapping("/admin")
    public ResponseEntity<Map<String, String>> serveAdminPageDetails() {
        return ResponseEntity.ok(Map.of("message", "User is granted admin access."));
    }

    @GetMapping("/any")
    public ResponseEntity<Map<String, String>> serveAnyPageDetails() {
        return ResponseEntity.ok(Map.of("message", "Successfully reached the any page."));
    }


}

