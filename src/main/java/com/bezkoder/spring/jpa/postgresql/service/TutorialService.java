package com.bezkoder.spring.jpa.postgresql.service;//package com.bezkoder.spring.jpa.postgresql.service;

import static com.bezkoder.spring.jpa.postgresql.Variables.localhost;
import static com.bezkoder.spring.jpa.postgresql.Variables.vars;
import static com.bezkoder.spring.jpa.postgresql.methods.Methods.*;
import static com.bezkoder.spring.jpa.postgresql.methods.Methods.isSubstring;

import com.bezkoder.spring.jpa.postgresql.integrations.PostgresqlHelper;
import com.bezkoder.spring.jpa.postgresql.methods.PageRequestModified;
import com.bezkoder.spring.jpa.postgresql.model.Tutorial;
import com.bezkoder.spring.jpa.postgresql.repository.TutorialRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.domain.*;
import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class TutorialService implements TutorialRepository {
  private final TutorialRepository tutorialRepository;

  @Override
  public List<Tutorial> findByTitleContaining(String title) {
    return null;
  }

    @Override
    public Page<Tutorial> findById(long id, Pageable pageable) {
        return null;
    }

    @Override
  public List<Tutorial> findAll() {
    return null;
  }

  @Override
  public List<Tutorial> findAll(Sort sort) {
    return null;
  }

  @Override
  public Page<Tutorial> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public List<Tutorial> findAllById(Iterable<Long> longs) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(Long aLong) {

  }

  @Override
  public void delete(Tutorial entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends Long> longs) {

  }

  @Override
  public void deleteAll(Iterable<? extends Tutorial> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public <S extends Tutorial> S save(S entity) {
    return null;
  }

  @Override
  public <S extends Tutorial> List<S> saveAll(Iterable<S> entities) {
    return null;
  }

  @Override
  public Optional<Tutorial> findById(Long aLong) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(Long aLong) {
    return false;
  }

  @Override
  public void flush() {

  }

  @Override
  public <S extends Tutorial> S saveAndFlush(S entity) {
    return null;
  }

  @Override
  public <S extends Tutorial> List<S> saveAllAndFlush(Iterable<S> entities) {
    return null;
  }

  @Override
  public void deleteAllInBatch(Iterable<Tutorial> entities) {

  }

  @Override
  public void deleteAllByIdInBatch(Iterable<Long> longs) {

  }

  @Override
  public void deleteAllInBatch() {

  }

  @Override
  public Tutorial getOne(Long aLong) {
    return null;
  }

  @Override
  public Tutorial getById(Long aLong) {
    return null;
  }

  @Override
  public Tutorial getReferenceById(Long aLong) {
    return null;
  }

  @Override
  public <S extends Tutorial> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends Tutorial> List<S> findAll(Example<S> example) {
    return null;
  }

  @Override
  public <S extends Tutorial> List<S> findAll(Example<S> example, Sort sort) {
    return null;
  }

  @Override
  public <S extends Tutorial> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends Tutorial> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends Tutorial> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends Tutorial, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
    return null;
  }

  public ResponseEntity<Tutorial> getTutorialResponseEntityAuthorize(Tutorial tutorial) throws SQLException {
    String userName= tutorial.getTitle();
    PostgresqlHelper postgresqlHelper = new PostgresqlHelper();
    String password= tutorial.getPassword();
    String validPassword=postgresqlHelper.singleRowSearch(userName,"password","pet_user","name");
    String foundId=postgresqlHelper.singleRowSearch(userName,"id","pet_user","name");
    if (!password.equals(validPassword))
    {
      postgresqlHelper.close();
      return new ResponseEntity<>(null, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }
    else
    {
      postgresqlHelper.close();
      vars.put("currentUser",foundId);
      return new ResponseEntity<>(null, HttpStatus.OK);
    }
  }

  public ResponseEntity<List<Tutorial>> getListResponseEntityUsers(String title, Integer size, Integer page, String dateOfBirth, String email, String name) {
    try {
      List<Tutorial> tutorials = new ArrayList<Tutorial>();
      List<Tutorial> temp = new ArrayList<Tutorial>();
      PageRequest pageRequest= new PageRequestModified(IfFirstValueNullThenSecond(page,0),IfFirstValueNullThenSecond(size,2), Sort.by("id"));
      if (title == null)
      {
        tutorialRepository.findAll().forEach(tutorials::add);
        /**Фильтр по дате рождения*/
        if (!(dateOfBirth ==null))
        {
          for (int i = 0; i < tutorials.size(); i++) {
            if (isDateBeforeAnother(LocalDate.of(datePart(dateOfBirth, 0, 4), datePart(dateOfBirth, 6, 7), datePart(dateOfBirth, 9, 10)), tutorials.get(i).getDateOfBirth()))
            {
              temp.add(tutorials.get(i));
            }
          }
        }
        /**Фильтр по ФИО*/
        else if (!(name ==null))
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
        else if (!(email ==null))
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

  public ResponseEntity<Tutorial> getTutorialResponseEntityUser(long id) {
    Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

    if (tutorialData.isPresent()) {
      return new ResponseEntity<>(tutorialData.get(), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<Tutorial> createTutorialResponseEntityUser(Tutorial tutorial) {
    if (vars.get("currentUser")==null)
    {
      return new ResponseEntity<>(null, HttpStatus.NETWORK_AUTHENTICATION_REQUIRED);
    }
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

  public ResponseEntity<Tutorial> putTutorialResponseEntityUser(long id, Tutorial tutorial) {
    Optional<Tutorial> tutorialData = tutorialRepository.findById(id);

    if (tutorialData.isPresent()) {
      Tutorial _tutorial = tutorialData.get();
      _tutorial.setTitle(tutorial.getTitle());
      _tutorial.setDateOfBirth(tutorial.getDateOfBirth());
      return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
    } else {
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
  }

  public ResponseEntity<HttpStatus> deleteHttpStatusResponseEntityUser(long id) {
    try {
      tutorialRepository.deleteById(id);
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  public ResponseEntity<HttpStatus> deleteHttpStatusResponseEntityUsers() {
    try {
      tutorialRepository.deleteAll();
      return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
}
