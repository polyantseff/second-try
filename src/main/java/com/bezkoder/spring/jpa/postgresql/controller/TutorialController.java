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
//	String currentUser;

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

	/** Если предварительно не авторизоваться будет 500тить*/
	@GetMapping("/users")
//	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title, @Nullable Integer size,@Nullable Integer page,@Nullable LocalDate dateOfBirth) {
	public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false) String title, @Nullable Integer size,@Nullable Integer page,@Nullable String dateOfBirth,@Nullable String email,@Nullable String name) {
		try {
			List<Tutorial> tutorials = new ArrayList<Tutorial>();
			List<Tutorial> temp = new ArrayList<Tutorial>();
//			PageRequest pageRequest= new PageRequestModified(IfFirstValueNullThenSecond(page,0),IfFirstValueNullThenSecond(size,2), Sort.by("id"));
			PageRequest pageRequest= new PageRequestModified(IfFirstValueNullThenSecond(page,0),IfFirstValueNullThenSecond(size,2), Sort.by("id"));
			if (title == null)
			{
//				tutorialRepository.findAll(pageRequest).forEach(tutorials::add);
				tutorialRepository.findAll().forEach(tutorials::add);
				/**Фильтр по дате рождения*/
				if (!(dateOfBirth==null))
				{
					for (int i = 0; i < tutorials.size(); i++) {
//						if (dateOfBirth.isBefore(tutorials.get(i).getDateOfBirth()))
//						if (LocalDate.of(datePart(dateOfBirth,0,4),datePart(dateOfBirth,6,7),datePart(dateOfBirth,9,10)).isBefore(tutorials.get(i).getDateOfBirth()))
						if (isDateBeforeAnother(LocalDate.of(datePart(dateOfBirth, 0, 4), datePart(dateOfBirth, 6, 7), datePart(dateOfBirth, 9, 10)), tutorials.get(i).getDateOfBirth()))
						{
							temp.add(tutorials.get(i));
						}
					}
				}
					/**Фильтр по ФИО*/
				else if (!(name==null))
					{
						for (int i = 0; i < tutorials.size(); i++)
						{
							if (isSubstring(name,tutorials.get(i).getTitle()))
							{
								temp.add(tutorials.get(i));
							}
						}
					}
				/**Фильтр по email*/
				else if (!(email==null))
				{
					for (int i = 0; i < tutorials.size(); i++)
					{
						if (isSubstring(email,tutorials.get(i).getEmailList()))
						{
							temp.add(tutorials.get(i));
						}
					}
				}
				else
				{
					tutorialRepository.findAll().forEach(tutorials::add);
					temp=tutorials;
				}
			}
			else
				tutorialRepository.findByTitleContaining(title).forEach(temp::add);
			if (tutorials.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}
			return new ResponseEntity<>(temp, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	boolean isDateBeforeAnother(LocalDate firstDate,LocalDate secondDate)
	{
		if ((firstDate==null)||(secondDate==null))
			return false;
		else
			return firstDate.isBefore(secondDate);
	}

	public static int datePart(String dateOfBirth,int beginIndex,int endIndex)
	{
		String temp=dateOfBirth.substring(beginIndex,endIndex);
		if (temp.charAt(0)=='0')
		{
			temp=temp.substring(1,temp.length());
		}
		return Integer.valueOf(temp);
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
//		else
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
//			_tutorial.setPublished(tutorial.isPublished());
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
