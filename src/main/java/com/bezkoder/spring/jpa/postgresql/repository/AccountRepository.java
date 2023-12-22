package com.bezkoder.spring.jpa.postgresql.repository;

import com.bezkoder.spring.jpa.postgresql.model.Account;
import com.bezkoder.spring.jpa.postgresql.model.Tutorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}