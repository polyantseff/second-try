package com.bezkoder.spring.jpa.postgresql.model;

import static com.bezkoder.spring.jpa.postgresql.methods.Methods.UseRestService;
import static com.bezkoder.spring.jpa.postgresql.methods.Methods.removeLastChars;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import javax.persistence.*;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@Table(name = "pet_user")
public class Tutorial {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(name = "name", length = 500)
  private String title;

  @Column(name = "date_of_birth")
  private LocalDate dateOfBirth;

  @Column(name = "password", length = 500)
  private String password;

  private String emailList;

  public Tutorial() {

  }

  public Tutorial(String title, LocalDate dateOfBirth,String password, boolean published) {
    this.title = title;
    this.dateOfBirth = dateOfBirth;
    this.password=password;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  @JsonIgnore
  @JsonProperty(value = "password")
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setEmailList(String emailList) {
    this.emailList = emailList;
  }

  public String getEmailList()
  {
    String temp = UseRestService("/api/emails/"+getId());
    if (temp==null)
    {
      return "";
    }
    JSONArray jsonArray=new JSONArray(temp);
    String result;
    if (!(jsonArray==null))
    {
      String tempResult="";
      for (int i=0;i<jsonArray.length();i++)
      {
        JSONObject json=jsonArray.getJSONObject(i);
        tempResult+=json.getString("email")+",";
      }
      result=removeLastChars(tempResult,1);
    }
    else
    {
      result="";
    }
    setEmailList(result);
    return emailList;
  }

  @Override
  public String toString() {
    return "Tutorial [id=" + id + ", name=" + title + ", date of birth=" + dateOfBirth  + "]";
  }
}
