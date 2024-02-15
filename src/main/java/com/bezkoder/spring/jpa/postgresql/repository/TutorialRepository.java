package com.bezkoder.spring.jpa.postgresql.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.bezkoder.spring.jpa.postgresql.model.Tutorial;

public interface TutorialRepository extends JpaRepository<Tutorial, Long> {

  List<Tutorial> findByTitleContaining(String title);

  Page<Tutorial> findById(long id,Pageable pageable);

  Page<Tutorial> findAll(Pageable pageable);
}
