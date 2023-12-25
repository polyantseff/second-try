package com.bezkoder.spring.jpa.postgresql.controller;

import static com.bezkoder.spring.jpa.postgresql.Variables.vars;

import com.bezkoder.spring.jpa.postgresql.integrations.PostgresqlHelper;
import com.bezkoder.spring.jpa.postgresql.model.Account;
import com.bezkoder.spring.jpa.postgresql.model.Tutorial;
import com.bezkoder.spring.jpa.postgresql.repository.AccountRepository;
import com.bezkoder.spring.jpa.postgresql.repository.TutorialRepository;
import java.sql.SQLException;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin (origins ="http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    AccountRepository accountRepository;

@GetMapping("/account/{userId}")
    public ResponseEntity<Account> getAccountlByUserId(@PathVariable("userId") long id) {
// Optional<Account> accountData = AccountRepository.findById(id);
        if (vars.get("currentUser")==null)
        {
            return new ResponseEntity<>(null,HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
        }
        else
        {
            Optional<Account> accountData = accountRepository.findById(id);

            if (accountData.isPresent()) {
                return new ResponseEntity<>(accountData.get(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        }
    }

// "/users/{id}"
// public ResponseEntity<Tutorial> updateTutorial("id" long id, @RequestBody Tutorial tutorial) {
// Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
//
// if (tutorialData.isPresent()) {
// Tutorial _tutorial = tutorialData.get();
// _tutorial.setTitle(tutorial.getTitle());
// _tutorial.setDateOfBirth(tutorial.getDateOfBirth());
// _tutorial.setPublished(tutorial.isPublished());
// return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
// } else {
// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
// }
// }
//
// "/users/{id}"
// public ResponseEntity<HttpStatus> deleteTutorial("id" long id) {
// try {
// tutorialRepository.deleteById(id);
// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// } catch (Exception e) {
// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }
//
// "/users"
// public ResponseEntity<HttpStatus> deleteAllTutorials() {
// try {
// tutorialRepository.deleteAll();
// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// } catch (Exception e) {
// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// }
//
// }
//
// "/users/published"
// public ResponseEntity<List<Tutorial» findByPublished() {
// try {
// List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
//
// if (tutorials.isEmpty()) {
// return new ResponseEntity<>(HttpStatus.NO_CONTENT);
// }
// return new ResponseEntity<>(tutorials, HttpStatus.OK);
// } catch (Exception e) {
// return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
// }
// }

}