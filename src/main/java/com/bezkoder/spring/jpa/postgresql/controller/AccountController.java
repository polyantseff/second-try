package com.bezkoder.spring.jpa.postgresql.controller;

import static java.lang.Thread.sleep;

import com.bezkoder.spring.jpa.postgresql.model.*;
import com.bezkoder.spring.jpa.postgresql.repository.AccountRepository;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
@RequestMapping("/api")
public class AccountController extends Controller{

	@Autowired
	AccountRepository accountRepository;

	@GetMapping("/account/{userId}")
	public ResponseEntity<Account> getAccountlByUserId(@PathVariable("userId") long id) {
		return getDataResponseEntity(id, Account.class);
	}

	@PutMapping("/account/{id}")
	public ResponseEntity<Account> updateAccount(@PathVariable("id") long id, @RequestBody Account account) {
		Optional<Account> accountData;
try
{
	accountData = accountRepository.findById(id);
}
catch (Exception e)
{
	try {
		sleep(1000);
	} catch (InterruptedException ex) {
		ex.printStackTrace();
	}
	accountData = accountRepository.findById(id);
}

		if (accountData.isPresent()) {
			Account _account = accountData.get();
			_account.increaseBalance(account.getBalance());
			try
			{
				return new ResponseEntity<>(accountRepository.save(_account), HttpStatus.OK);
			}
			catch (Exception e)
			{
				try
				{
					sleep(1000);
				} catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
				return new ResponseEntity<>(accountRepository.save(_account), HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	/**Деньги перечисляются со счета того который в body на счет, указанный в id*/
	@PutMapping("/account/trasfer/{id}")
	public synchronized ResponseEntity<Account> transferBetweenAccounts(@PathVariable("id") long id, @RequestBody Account account) {
		Optional<Account> accountData;
		try
		{
			accountData = accountRepository.findById(id);
		}
		catch (Exception e)
		{
			try {
				sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			accountData = accountRepository.findById(id);
		}

		if (accountData.isPresent()) {
			Account _account = accountData.get();
			_account.increaseBalance(account.getBalance());
			try
			{
				Account temp=account;
				temp.setBalance(account.getBalance().negate());
				updateAccount(account.getId(),temp);
				return new ResponseEntity<>(accountRepository.save(_account), HttpStatus.OK);
			}
			catch (Exception e)
			{
				try
				{
					sleep(1000);
				} catch (InterruptedException ex)
				{
					ex.printStackTrace();
				}
				return new ResponseEntity<>(accountRepository.save(_account), HttpStatus.OK);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

		@GetMapping("/accounts")
	public ResponseEntity<EmailData> getAllAccounts() {
		try {
			List<Account> accountData = new ArrayList<Account>();
			try
			{
				accountRepository.findAll().forEach(accountData::add);
			}
			catch (Exception e)
			{
				sleep(1000);
				accountRepository.findAll().forEach(accountData::add);
			}

			if (accountData.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity(accountData, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
