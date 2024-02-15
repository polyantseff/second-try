package com.bezkoder.spring.jpa.postgresql.methods;

import static com.bezkoder.spring.jpa.postgresql.methods.Methods.UseRestService;

import com.bezkoder.spring.jpa.postgresql.integrations.PostgresqlHelper;
import com.bezkoder.spring.jpa.postgresql.methods.Methods.RequestType;
import java.math.BigDecimal;
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
      if ((entry.getValue().doubleValue()) < (accountInitialBalance.get(entry.getKey()).doubleValue() * 207 / 110)) {
        Double currentBalanceD = entry.getValue().doubleValue() * 1.1;
        entry.setValue(BigDecimal.valueOf(currentBalanceD));
        BigDecimal increment=BigDecimal.valueOf(entry.getValue().doubleValue()/11);
        UseRestService("/api/account/" + entry.getKey(), RequestType.PUT, "{\"balance\":" + increment + "}");
      }
    }
  }
}
