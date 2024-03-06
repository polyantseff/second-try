package com.bezkoder.spring.jpa.postgresql.methods;

import static com.bezkoder.spring.jpa.postgresql.methods.Methods.UseRestService;

import com.bezkoder.spring.jpa.postgresql.integrations.PostgresqlHelper;
import com.bezkoder.spring.jpa.postgresql.methods.Methods.RequestType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class CustomTask extends TimerTask {
  PostgresqlHelper postgresqlHelper=new PostgresqlHelper();

  public CustomTask()
  {
    try {
      if (postgresqlHelper.isEnvironmentEmpty())
      {
        postgresqlHelper.PreFill();
        System.out.println("");
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    JSONArray jsonArray=new JSONArray(UseRestService("/api/accounts"));
    for (int i=0;i<jsonArray.length();i++)
    {
      JSONObject jsonObject=jsonArray.getJSONObject(i);
      if (!(jsonObject.get("balance").equals(null)))
      {
        accountInitialBalance.put(jsonObject.getInt("id"), jsonObject.getBigDecimal("balance"));
        accountCurrentBalance.put(jsonObject.getInt("id"), jsonObject.getBigDecimal("balance"));
      }
    };
  }

  public static HashMap<Integer, BigDecimal> accountInitialBalance = new HashMap<>();
  public static HashMap<Integer, BigDecimal> accountCurrentBalance = new HashMap<>();

  public void run()
  {
    for (Map.Entry<Integer, BigDecimal> entry : accountCurrentBalance.entrySet())
    {
      if (entry.getValue().compareTo(accountInitialBalance.get(entry.getKey()).multiply(BigDecimal.valueOf(207)).divide(BigDecimal.valueOf(110),2, RoundingMode.HALF_UP))<1 && (!(accountInitialBalance.get(entry.getKey()).compareTo(BigDecimal.valueOf(0))==0)))
      {
        BigDecimal currentBalance = entry.getValue().multiply(BigDecimal.valueOf(1.1));
        entry.setValue(currentBalance);
        BigDecimal increment=entry.getValue().divide(BigDecimal.valueOf(11),5,RoundingMode.HALF_UP);
        UseRestService("/api/account/" + entry.getKey(), RequestType.PUT, "{\"balance\":" + increment + "}");
      }
    }
  }
}