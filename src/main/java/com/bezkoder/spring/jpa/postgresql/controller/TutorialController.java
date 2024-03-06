package com.bezkoder.spring.jpa.postgresql.controller;

import com.bezkoder.spring.jpa.postgresql.integrations.PostgresqlHelper;

import static com.bezkoder.spring.jpa.postgresql.Variables.vars;
import static com.bezkoder.spring.jpa.postgresql.methods.Methods.*;

import com.bezkoder.spring.jpa.postgresql.methods.PageRequestModified;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.jpa.postgresql.service.TutorialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.jpa.postgresql.model.Tutorial;
import com.bezkoder.spring.jpa.postgresql.repository.TutorialRepository;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController{

	@Autowired
	TutorialRepository tutorialRepository;
	TutorialService tutorialService=new TutorialService(tutorialRepository);


	@PostMapping("/authorize")
	public ResponseEntity<Tutorial> authorize(@RequestBody Tutorial tutorial) throws SQLException {
		return tutorialService.getTutorialResponseEntityAuthorize(tutorial);
	}

	/** Если предварительно не авторизоваться будет 500тить*/
	@GetMapping("/users")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title, @Nullable Integer size,@Nullable Integer page,@Nullable String dateOfBirth,@Nullable String email,@Nullable String name) {
		return tutorialService.getListResponseEntityUsers(title, size, page, dateOfBirth, email, name);
	}

	@GetMapping("/users/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
		return tutorialService.getTutorialResponseEntityUser(id);
	}

	@PostMapping("/users")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		return tutorialService.createTutorialResponseEntityUser(tutorial);
	}

	@PutMapping("/users/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		return tutorialService.putTutorialResponseEntityUser(id, tutorial);
	}

	@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
		return tutorialService.deleteHttpStatusResponseEntityUser(id);
	}

	@DeleteMapping("/users")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		return tutorialService.deleteHttpStatusResponseEntityUsers();
	}
}
