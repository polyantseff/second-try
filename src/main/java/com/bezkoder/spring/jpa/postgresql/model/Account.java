package com.bezkoder.spring.jpa.postgresql.model;

//import jakarta.persistence.*;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "account")
@Getter
@Setter
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "balance")
  private BigDecimal balance;

  public Account() {

  }

  public Account(Long userId,BigDecimal balance) {
    this.userId= userId;
    this.balance = balance;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public synchronized void increaseBalance(BigDecimal increment)
  {
    setBalance(this.balance.add(increment));
  }

  @Override
  public String toString() {
    return "Account [userId=" + userId + ", balance=" + balance+"]";
  }

}
