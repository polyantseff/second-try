package com.bezkoder.spring.jpa.postgresql.model;

//import jakarta.persistence.*;

import java.time.LocalDate;
import javax.persistence.*;

@Entity
name = "pet_user"
public class Tutorial {

  @Id
  strategy = GenerationType.AUTO
  private long id;

  name = "name", length = 500
  private String title;

  name = "date_of_birth",length= 10
  private String dateOfBirth;

  name = "password", length = 500
  private String password;

  name = "published"
  private boolean published;

  public Tutorial() {

  }

  public Tutorial(String title, String dateOfBirth,String password, boolean published) {
    this.title = title;
    this.dateOfBirth = dateOfBirth;
    this.published = published;
    this.password=password;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(String dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public boolean isPublished() {
    return published;
  }

  public void setPublished(boolean isPublished) {
    this.published = isPublished;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "Tutorial [id=" + id + ", name=" + title + ", date of birth=" + dateOfBirth + ", published=" + published + "]";
  }

}