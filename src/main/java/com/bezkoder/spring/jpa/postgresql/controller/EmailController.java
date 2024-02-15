package com.bezkoder.spring.jpa.postgresql.controller;

import static com.bezkoder.spring.jpa.postgresql.Variables.localhost;
import static com.bezkoder.spring.jpa.postgresql.Variables.vars;
import static java.lang.String.valueOf;

import com.bezkoder.spring.jpa.postgresql.model.*;
import com.bezkoder.spring.jpa.postgresql.repository.EmailRepository;
import java.util.*;
import lombok.SneakyThrows;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin(origins = "http://localhost:8081")
@CrossOrigin(origins = localhost)
@RestController
@RequestMapping("/api")
public class EmailController extends Controller{

	@GetMapping("/emails/{userId}")
	public ResponseEntity<EmailData> getEmailByUserId(@PathVariable("userId") long id) {
		return getDataResponseEntity(id,EmailData.class);
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<EmailData> getEmailByUserId(@PathVariable("email") String email)
	{
		return getDataResponseEntity(getUserIdByEmail(email),EmailData.class);
	}


//	@GetMapping("/email/{userId}")
//	public ResponseEntity<EmailData> getEmailByUserId(@PathVariable("userId") long id) {
//		try {
//			List<EmailData> dataList = new ArrayList<EmailData>();
//
////			if (title == null)
////				tutorialRepository.findAll().forEach(tutorials::add);
////			else
//				emailRepository.findByUserId(id).forEach(dataList::add);
//
//			if (dataList.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			}
//
//			return new ResponseEntity(dataList, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@PostMapping("/email")
	public ResponseEntity<EmailData> createEmail(@RequestBody EmailData emailData) {
		if (vars.get("currentUser")==null)
		{
			return new ResponseEntity<>(null,HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
		}
		if (valueAlreadyExists(emailData.getEmail(),"email","email_data"))
		{
			return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
		}
		{
			try {
				EmailData _emailData = emailRepository
						.save(new EmailData(emailData.getEmail(), Long.parseLong(vars.get("currentUser"))));
				return new ResponseEntity<>(_emailData, HttpStatus.CREATED);
			} catch (Exception e) {
				return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
	}

	@SneakyThrows
	public long getIdByEmail(String email)
	{
		String emailDataId= postgresqlHelper.singleRowSearch(email,"id","email_data","email");
		return Long.parseLong(emailDataId);
	}

	@SneakyThrows
	public long getUserIdByEmail(String email)
	{
		String emailDataId= postgresqlHelper.singleRowSearch(email,"user_id","email_data","email");
		return Long.parseLong(emailDataId);
	}

	@SneakyThrows
	public long getIdByEmailForCurrentUser(String email)
	{
		String emailDataId= postgresqlHelper.singleRowSearchForCurrentUser(email,"id","email_data","email");
		return Long.parseLong(emailDataId);
	}

	@PutMapping("/email/{email}")
	public ResponseEntity<EmailData> updateEmail(@PathVariable("email") String email, @RequestBody EmailData emailData) {
//		Optional<EmailData> emailDataV = emailRepository.findById(getIdByEmail(email));
		if (vars.get("currentUser")==null)
		{
			return new ResponseEntity<>(null,HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
		}
		if (valueAlreadyExists(email,"email","email_data"))
		{
			return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
		}
		Optional<EmailData> emailDataV;
		try
		{
			emailDataV = emailRepository.findById(getIdByEmailForCurrentUser(email));
		}
		catch (SQLGrammarException sg)
		{
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		if (emailDataV.isPresent()) {
			EmailData _emailData = emailDataV.get();
			_emailData.setEmail(emailData.getEmail());
			return new ResponseEntity<>(emailRepository.save(_emailData), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/email/{email}")
	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("email") String email) {
		if (vars.get("currentUser")==null)
		{
			return new ResponseEntity<>(null,HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
		}
		try {
			long id=getIdByEmailForCurrentUser(email);
			if (id==0)
			{
				return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			}
			else
			{
				emailRepository.deleteById(id);
				return new ResponseEntity<>(HttpStatus.FOUND);
			}
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//	@SneakyThrows
//	public long getEmailIdByUserId(long id)
//	{
//		String emailDataId= postgresqlHelper.singleRowSearch(String.valueOf(id),"id","email_data","user_id");
//		return Long.parseLong(emailDataId);
//	}

//	@PutMapping("/users/{id}")
//	public ResponseEntity<Tutorial> updateTutorial(@PathVariable("id") long id, @RequestBody Tutorial tutorial) {
//		Optional<Tutorial> tutorialData = tutorialRepository.findById(id);
//
//		if (tutorialData.isPresent()) {
//			Tutorial _tutorial = tutorialData.get();
//			_tutorial.setTitle(tutorial.getTitle());
//			_tutorial.setDateOfBirth(tutorial.getDateOfBirth());
//			_tutorial.setPublished(tutorial.isPublished());
//			return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
//		} else {
//			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//		}
//	}
//
//	@DeleteMapping("/users/{id}")
//	public ResponseEntity<HttpStatus> deleteTutorial(@PathVariable("id") long id) {
//		try {
//			tutorialRepository.deleteById(id);
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//
//	@DeleteMapping("/users")
//	public ResponseEntity<HttpStatus> deleteAllTutorials() {
//		try {
//			tutorialRepository.deleteAll();
//			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//
//	}
//
//	@GetMapping("/users/published")
//	public ResponseEntity<List<Tutorial>> findByPublished() {
//		try {
//			List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
//
//			if (tutorials.isEmpty()) {
//				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//			}
//			return new ResponseEntity<>(tutorials, HttpStatus.OK);
//		} catch (Exception e) {
//			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

}
