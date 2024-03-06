package com.bezkoder.spring.jpa.postgresql.integrations;

import static com.bezkoder.spring.jpa.postgresql.Variables.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.bezkoder.spring.jpa.postgresql.configs.ConfProperties;
import java.sql.*;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import org.postgresql.util.*;

public class PostgresqlHelper {
    private String url = ConfProperties.getProperty("db.url");
    private String username = ConfProperties.getProperty("db.username");
    private String password = System.getProperty("database.password");
    private Connection connection;
    private Statement statement;

    @SneakyThrows
    public PostgresqlHelper()
    {
        if (this.password == null) {
            this.password = ConfProperties.getProperty("db.password");
        }
        connection = DriverManager.getConnection(url, username, password);
        renewStatement();
    }

    private void renewStatement() throws SQLException {
        statement = connection.createStatement();
    }

    public ResultSet executeQuery(String sqlRequest) throws SQLException {
        statement.executeQuery("deallocate \"S_1\";");
        return statement.executeQuery(sqlRequest);
    }

    public void close() throws SQLException {
        statement.close();
        connection.close();
    }

    /**
     * Метод ищет значение колонки по значению другой или той же колонке в указанной таблице, предполагается что значение единственное
     */
    public String singleRowSearch(String searchId,String entityColumn,String tableName,String searchColumn) throws SQLException {
        ResultSet resultSet;
        try
        {
            resultSet = statement.executeQuery("SELECT " + entityColumn + " FROM "+schema+"." + tableName + " where " + searchColumn + "='" + searchId + "'");
        }
        catch (PSQLException p)
        {
           renewStatement();
           resultSet = statement.executeQuery("SELECT " + entityColumn + " FROM "+schema+"." + tableName + " where " + searchColumn + "='" + searchId + "'");
        }
        resultSet.next();
        try
        {
            String temp = resultSet.getString(1);
            return temp;
        }
        catch (PSQLException p)
        {
            return "0";
        }
    }

    public String singleRowSearchForCurrentUser(String searchId,String entityColumn,String tableName,String searchColumn) throws SQLException {
        ResultSet resultSet = statement.executeQuery("SELECT "+entityColumn+" FROM "+schema+"." + tableName+" where "+searchColumn+"='"+searchId+"' and user_id="+vars.get("currentUser"));
        resultSet.next();
        try
        {
            String temp = resultSet.getString(1);
            return temp;
        }
        catch (PSQLException p)
        {
            return "0";
        }
    }

    public String singleRowSearchLike(String searchId,String entityColumn,String tableName,String searchColumn,String addtitional) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement("SELECT "+entityColumn+" FROM "+schema+"." + tableName+" where "+searchColumn+" like'%"+searchId+"%'"+addtitional);
        try {
            TimeUnit.MINUTES.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ResultSet resultSet = pstmt.executeQuery();
        resultSet.next();
            String temp = resultSet.getString(1);
            return temp;
    }

    public String singleRowSearchLike(String searchId,String entityColumn,String tableName,String searchColumn) throws SQLException {
        return singleRowSearchLike(searchId,entityColumn,tableName,searchColumn,"");
    }

    public void findEntry(String table, String column, String entry ) throws SQLException
    {
        assertTrue(findEntryB(table,column,entry), "В таблице " + table + " в столбце " + column + " не найдена запись " + entry);
    }

    public boolean findEntryB(String table, String column, String entry ) throws SQLException
    {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+schema+"." + table);
        while (resultSet.next()) {
            if (resultSet.getString(column).equals(entry))
            {
                resultSet.close();
                return true;
            }
        }
        resultSet.close();
        return false;
    }

    public boolean isEnvironmentEmpty() throws SQLException
    {
        ResultSet resultSet = statement.executeQuery("SELECT * FROM "+schema+"." + petUser);
        if (resultSet.next())
        {
            resultSet.close();
            return false;
        }
        resultSet.close();
        return true;
    }


    public void PreFill()
    {
            PreFillUser("751","1985-01-01","IHmSDoqmBe8=","Testov");
            PreFillUser("1","2000-02-01","IHmSDoqmBe8=","Медоэв");
            PreFillUser("401","1900-12-12","IHmSDoqmBe8=","TestCreate");
            PreFillEmailData("2","test@mail.ru","1");
            PreFillEmailData("451","UPP@test.cz","401");
            PreFillEmailData("551","another@test.cz","401");
            PreFillAccount("3","0","401");
            PreFillAccount("1","1000","751");
            PreFillAccount("2","100","1");
    }
    public void PreFillTable(String tableName,String userQuery,String id) throws SQLException
    {
        try
        {
            ResultSet resultSet = statement.executeQuery(userQuery);
            resultSet.close();
        }
        catch (PSQLException p) {}
        findEntry(tableName,"id",id);
    }

    public void PreFillUser(String id,String date,String password,String name)
    {
        try {
            PreFillTable(petUser,UserQuery(id,date,password,name),id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void PreFillAccount(String id,String balance,String userId)
    {
        try {
            PreFillTable(account,AccountQuery(id,balance,userId),id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void PreFillEmailData(String id,String email,String userId)
    {
        try {
            PreFillTable(emailData,EmailQuery(id,email,userId),id);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String UserQuery(String id,String date,String password,String name)
    {
        return "INSERT INTO " +schema+"."+petUser +petUserFields+" VALUES ('"+id+"','"+date+"',null,'"+password+"','"+name+"')";
    }

    public String AccountQuery(String id,String balance,String userId)
    {
        return "INSERT INTO " +schema+"."+account +accountFields+" VALUES ('"+id+"','"+balance+"','"+userId+"')";
    }

    public String EmailQuery(String id,String email,String userId)
    {
        return "INSERT INTO " +schema+"."+emailData +emailFields+" VALUES ('"+id+"','"+email+"','"+userId+"')";
    }
}
