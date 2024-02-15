package com.bezkoder.spring.jpa.postgresql.model;

//import jakarta.persistence.*;

import java.math.BigDecimal;
import javax.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "email_data")
@Getter
@Setter
public class EmailData {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @Column(name = "user_id")
  private long userId;

  @Column(name = "email")
  private String email;

  public EmailData() {

  }

  public EmailData(String email, long userId) {
    this.email = email;
    this.userId=userId;
  }

//  public EmailData(long userId,String email) {
//    this.userId= userId;
//    this.email = email;
//  }



  @Override
  public String toString() {
    return "Email Data [userId=" + userId + ", email=" + email+"]";
  }

}
