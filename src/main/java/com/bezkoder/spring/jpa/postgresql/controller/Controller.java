package com.bezkoder.spring.jpa.postgresql.controller;

import static com.bezkoder.spring.jpa.postgresql.Variables.vars;

import com.bezkoder.spring.jpa.postgresql.integrations.PostgresqlHelper;
import com.bezkoder.spring.jpa.postgresql.model.*;
import com.bezkoder.spring.jpa.postgresql.repository.*;
import java.sql.SQLException;
import java.util.*;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8081")
@RestController
public class Controller {
	@Autowired
	EmailRepository emailRepository;
	@Autowired
	AccountRepository accountRepository;

	PostgresqlHelper postgresqlHelper = new PostgresqlHelper();

	@SneakyThrows
	public long getEntityByUserId(long id,String tableName)
	{
		String emailDataId= postgresqlHelper.singleRowSearch(String.valueOf(id),"id",tableName,"user_id");
		return Long.parseLong(emailDataId);
	}

	@SneakyThrows
	protected boolean valueAlreadyExists(String value,String column,String tableName)
	{
		String id=postgresqlHelper.singleRowSearch(value,"id",tableName,column);
		return (!(id=="0"));
	}

	protected ResponseEntity getDataResponseEntity(long id,Class<?> c) {
		//		Optional<Account> accountData = AccountRepository.findById(id);
		if (vars.get("currentUser")==null)
		{
			return new ResponseEntity<>(null, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
		}
		else
		{
			switch (c.getSimpleName())
			{
				case "EmailData":
					List<EmailData> dataList = new ArrayList<EmailData>();
					emailRepository.findByUserId(id).forEach(dataList::add);
					if (dataList.isEmpty())
					{
						return new ResponseEntity<>(HttpStatus.NO_CONTENT);
					}
					return new ResponseEntity(dataList, HttpStatus.OK);
//					break;
				case "Account":
					Optional<Account> dataAc=getDataAc(id, accountRepository);
					if (dataAc.isPresent())
					{
						return new ResponseEntity<>(dataAc.get(), HttpStatus.OK);
					}
					break;
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	private Optional<EmailData> getData(long id, JpaRepository jpaRepository) {
		Optional<EmailData> data = jpaRepository.findById(getEntityByUserId(id,"email_data"));
		return data;
	}

	private Optional<Account> getDataAc(long id, JpaRepository jpaRepository) {
		Optional<Account> data = jpaRepository.findById(getEntityByUserId(id,"account"));
		return data;
	}
}
