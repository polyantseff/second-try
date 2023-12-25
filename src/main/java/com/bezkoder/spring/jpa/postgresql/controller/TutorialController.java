package com.bezkoder.spring.jpa.postgresql.controller;

import com.bezkoder.spring.jpa.postgresql.integrations.PostgresqlHelper;

import static com.bezkoder.spring.jpa.postgresql.Variables.vars;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@CrossOrigin (origins ="http://localhost:8081")
@RestController
@RequestMapping("/api")
public class TutorialController {
	String currentUser;

	@Autowired
	TutorialRepository tutorialRepository;

@PostMapping("/authorize")
	public ResponseEntity<Tutorial> authorize(@RequestBody Tutorial tutorial) throws SQLException {
		String userName=tutorial.getTitle();
		PostgresqlHelper postgresqlHelper = new PostgresqlHelper();
		String password=tutorial.getPassword();
		String validPassword=postgresqlHelper.singleRowSearch(userName,"password","pet_user","name");
		String foundId=postgresqlHelper.singleRowSearch(userName,"id","pet_user","name");
		if (!password.equals(validPassword))
		{
			postgresqlHelper.close();
			return new ResponseEntity<>(null,HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
		}
		else
		{
			postgresqlHelper.close();
			vars.put("currentUser",foundId);
			return new ResponseEntity<>(null, HttpStatus.OK);
		}
	}

@GetMapping("/users")
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam( required = false) String title) {
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();

			if (title == null)
				tutorialRepository.findAll().forEach(tutorials::add);
			else
				tutorialRepository.findByTitleContaining(title).forEach(tutorials::add);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

@GetMapping("/users/{id}")
	public ResponseEntity<Tutorial> getTutorialById(@PathVariable("id") long id) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

@PostMapping("/users")
	public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
		if (vars.get("currentUser")==null)
		{
			return new ResponseEntity<>(null,HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
		}
// else
		{
			try {
				Tutorial _tutorial = tutorialRepository
						.save(new Tutorial(tutorial.getTitle(), tutorial.getDateOfBirth(), tutorial.getPassword(), false));
				return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

@PutMapping("/users/{id}")
	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

		if (tutorialData.isPresent()) {
			Tutorial _tutorial = tutorialData.get();
			_tutorial.setTitle(tutorial.getTitle());
			_tutorial.setDateOfBirth(tutorial.getDateOfBirth());
			_tutorial.setPublished(tutorial.isPublished());
			return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

@DeleteMapping("/users/{id}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
		try {
			tutorialRepository.deleteById(id);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

@DeleteMapping("/users")
	public ResponseEntity<HttpStatus> deleteAllTutorials() {
		try {
			tutorialRepository.deleteAll();
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

@DeleteMapping("/users/published")
	public ResponseEntity<List<Tutorial>> findByPublished() {
		try {
			List<Tutorial> tutorials = tutorialRepository.findByPublished(true);

			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(tutorials, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}