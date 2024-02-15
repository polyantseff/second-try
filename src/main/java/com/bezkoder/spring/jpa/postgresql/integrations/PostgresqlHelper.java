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

//    public void externalTaskContainsExternalId(String externalId) throws SQLException, ClassNotFoundException {
//        ResultSet resultSet = statement.executeQuery("SELECT * FROM adju.external_task");
//        boolean isContainsExternalId = false;
//        while (resultSet.next()) {
//            if (resultSet.getString("external_id").equals(externalId)) {
//                isContainsExternalId = true;
//                vars.put("taskId", resultSet.getString("inner_id"));
//            }
//        }
//        assertTrue(isContainsExternalId, "Таблица external_task не содержит задачи с данным externalId");
//    }

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



//    public void insertIntoWorkGroup(String id,String name,String  rawData,String searchableIndex,String fullName) throws SQLException
//    {
//        try
//        {
//            ResultSet resultSet = statement.executeQuery("INSERT INTO adju." + workGroup + workGroupTableStructure +
//                    "VALUES ('" + id + "','" + name + "',13,true,'84001','2022-12-06 12:19:37.205532','" + rawData + "','"
//                    + searchableIndex + "','" + fullName + "')");
//            resultSet.close();
//        }
//        catch (PSQLException p) {}
//        findEntry(workGroup,"id",id);
//    }
//
//    public void insertIntoWorkGroupMember(String groupId,String userId) throws SQLException {
//        try
//        {
//            ResultSet resultSet = statement.executeQuery("INSERT INTO adju." + workGroupMember + workGroupMemberTableStructure +
//                    "VALUES ('" + groupId + "','" + userId + "',true)");
//            resultSet.close();
//        }
//        catch (PSQLException p) {}
//        findEntry(workGroupMember,"group_id",groupId);
//    }
}
