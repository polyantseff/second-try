package com.bezkoder.spring.jpa.postgresql.model;

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
  private Long id;

  @Column(name = "user_id")
  private Long userId;

  @Column(name = "email")
  private String email;

  public EmailData() {

  }

  public EmailData(String email, Long userId) {
    this.email = email;
    this.userId=userId;
  }

  @Override
  public String toString() {
    return "Email Data [userId=" + userId + ", email=" + email+"]";
  }

}
