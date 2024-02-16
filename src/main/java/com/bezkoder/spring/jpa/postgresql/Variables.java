package com.bezkoder.spring.jpa.postgresql;

import java.util.HashMap;

public class Variables {
  public static HashMap<String, String> vars= new HashMap<>();
  public static final String localhost="http://localhost:8080";
  public static final String schema="juntests";
  public static final String petUser="pet_user";
  public static final String account="account";
  public static final String emailData="email_data";
  public static final String petUserFields="(id,date_of_birth,email_list,password,name)";
  public static final String emailFields="(id,email,user_id)";
  public static final String accountFields="(id,balance,user_id)";
}
