package com.bezkoder.spring.jpa.postgresql.methods;

import static com.bezkoder.spring.jpa.postgresql.Variables.localhost;
import static com.bezkoder.spring.jpa.postgresql.Variables.vars;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.springframework.web.client.RestTemplate;
import static io.restassured.RestAssured.given;

public class Methods {

  public enum RequestType
  {
    GET, POST, PUT
  }

  public static String removeLastChars(String str, int chars) {
    return str.substring(0, str.length() - chars);
  }

  public static int TwoValuesByCondition(boolean condition,int value1,int value2)
  {
    if (condition)
    {
      return value1;
    }
    else
    {
      return value2;
    }
  }

  public static int IfFirstValueNullThenSecond(Integer value1,int value2)
  {
    if (value1==null)
    {
      return value2;
    }
    return value1;
  }

  public static boolean isSubstring(String part, String source)
  {
    if (source.indexOf(part) != -1) {
      return true;
    }
    return false;
  }

  public static String UseRestService(String serviceUrlPart,RequestType requestType,String body)
  {
    String uri=localhost+serviceUrlPart;
    RestTemplate restTemplate = new RestTemplate();
    boolean userSetByJob=false;
    if (vars.get("CurrentUser")==null)
    {
      vars.put("currentUser","1");
      userSetByJob=true;
    }
    String temp="";
    switch (requestType)
    {
      case GET:
        temp = restTemplate.getForObject(uri, String.class);
        break;
      case PUT:
        PutEntity(uri,body);
        break;
      default:
        temp = restTemplate.getForObject(uri, String.class);
        break;
    }
    if (userSetByJob)
    {
      vars.put("currentUser",null);
    }
    return temp;
  }

  public static String UseRestService(String serviceUrlPart)
  {
    return UseRestService(serviceUrlPart,RequestType.GET,"");
  }

  @SneakyThrows
  public static JSONObject PutEntity(String url,String body)
  {
    JSONObject response = new JSONObject(given().log().all()
        .header("Content-Type", "application/json")
        .header("Cache-Control", "no-cache")
        .header("Content-Type", "application/json")
        .body(body)
        .when().put(url)
        .then().extract().response().asString());
    return response;
  }

}
