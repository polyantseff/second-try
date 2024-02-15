package com.bezkoder.spring.jpa.postgresql.service;//package com.bezkoder.spring.jpa.postgresql.service;
//
//import static com.bezkoder.spring.jpa.postgresql.Variables.localhost;
//import static com.bezkoder.spring.jpa.postgresql.methods.Methods.removeLastChars;
//
//import com.bezkoder.spring.jpa.postgresql.model.Tutorial;
//import com.bezkoder.spring.jpa.postgresql.repository.TutorialRepository;
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//import lombok.RequiredArgsConstructor;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import org.springframework.data.domain.*;
//import org.springframework.data.repository.query.FluentQuery.FetchableFluentQuery;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//@Service
//@RequiredArgsConstructor
//public class TutorialService implements TutorialRepository {
//  private final TutorialRepository tutorialRepository;
//
//  @Override
//  public List<Tutorial> findByPublished(boolean published) {
//    return null;
//  }
//
//  @Override
//  public List<Tutorial> findByTitleContaining(String title) {
//    return null;
//  }
//
//  @Override
//  public List<Tutorial> findAll() {
//    return null;
//  }
//
//  @Override
//  public List<Tutorial> findAll(Sort sort) {
//    return null;
//  }
//
//  @Override
//  public Page<Tutorial> findAll(Pageable pageable) {
//    return null;
//  }
//
//  @Override
//  public List<Tutorial> findAllById(Iterable<Long> longs) {
//    return null;
//  }
//
//  @Override
//  public long count() {
//    return 0;
//  }
//
//  @Override
//  public void deleteById(Long aLong) {
//
//  }
//
//  @Override
//  public void delete(Tutorial entity) {
//
//  }
//
//  @Override
//  public void deleteAllById(Iterable<? extends Long> longs) {
//
//  }
//
//  @Override
//  public void deleteAll(Iterable<? extends Tutorial> entities) {
//
//  }
//
//  @Override
//  public void deleteAll() {
//
//  }
//
//  @Override
//  public <S extends Tutorial> S save(S entity) {
//    return null;
//  }
//
//  @Override
//  public <S extends Tutorial> List<S> saveAll(Iterable<S> entities) {
//    return null;
//  }
//
//  @Override
//  public Optional<Tutorial> findById(Long aLong) {
//    return Optional.empty();
//  }
//
//  @Override
//  public boolean existsById(Long aLong) {
//    return false;
//  }
//
//  @Override
//  public void flush() {
//
//  }
//
//  @Override
//  public <S extends Tutorial> S saveAndFlush(S entity) {
//    return null;
//  }
//
//  @Override
//  public <S extends Tutorial> List<S> saveAllAndFlush(Iterable<S> entities) {
//    return null;
//  }
//
//  @Override
//  public void deleteAllInBatch(Iterable<Tutorial> entities) {
//
//  }
//
//  @Override
//  public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//  }
//
//  @Override
//  public void deleteAllInBatch() {
//
//  }
//
//  @Override
//  public Tutorial getOne(Long aLong) {
//    return null;
//  }
//
//  @Override
//  public Tutorial getById(Long aLong) {
//    return null;
//  }
//
//  @Override
//  public Tutorial getReferenceById(Long aLong) {
//    return null;
//  }
//
//  @Override
//  public <S extends Tutorial> Optional<S> findOne(Example<S> example) {
//    return Optional.empty();
//  }
//
//  @Override
//  public <S extends Tutorial> List<S> findAll(Example<S> example) {
//    return null;
//  }
//
//  @Override
//  public <S extends Tutorial> List<S> findAll(Example<S> example, Sort sort) {
//    return null;
//  }
//
//  @Override
//  public <S extends Tutorial> Page<S> findAll(Example<S> example, Pageable pageable) {
//    return null;
//  }
//
//  @Override
//  public <S extends Tutorial> long count(Example<S> example) {
//    return 0;
//  }
//
//  @Override
//  public <S extends Tutorial> boolean exists(Example<S> example) {
//    return false;
//  }
//
//  @Override
//  public <S extends Tutorial, R> R findBy(Example<S> example, Function<FetchableFluentQuery<S>, R> queryFunction) {
//    return null;
//  }
//}
