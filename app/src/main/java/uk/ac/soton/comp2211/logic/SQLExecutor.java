package uk.ac.soton.comp2211.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import uk.ac.soton.comp2211.utility.Utility;

public class SQLExecutor {

  /**
   * Generate a range of dates given a start date and an end date
   * @param startDate The lower bound of the date range
   * @param endDate The upper bound of the date range
   * @return A list containing all the dates within the range
   */
  public static List<LocalDate> getDates(String startDate, String endDate) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    LocalDate start = LocalDate.parse(startDate, formatter);
    LocalDate end = LocalDate.parse(endDate, formatter);
    return start.datesUntil(end).collect(Collectors.toList());
  }

  /**
   * Executes an SQL query to retrieve a given metric on a specific date
   * @param date The date for the metric query
   * @param metric The name of the metric (e.g. CPA, CPC, CPM)
   * @return A list with the results of the queries
   */
  public static String[] executeSQL(String date, String metric) {

    String jdbcUrl = "jdbc:sqlite:" + Utility.cleanURL(
            SQLExecutor.class.getResource("/db/logDatabase.db").getPath());
    List<String> resultList = new ArrayList<>();

    try {
      // Load the SQLite JDBC driver
      Class.forName("org.sqlite.JDBC");

      // Create a connection to the database
      Connection connection = DriverManager.getConnection(jdbcUrl);

      // Read the SQL script from a file
      String sqlScript = SQLGeneratorOLD.getSQLQuery(date, metric);

      // Split the SQL script into individual queries
      String[] queries = sqlScript.split(";");

      // Execute each query in the script
      for (String query : queries) {
        // Skip over empty or whitespace-only queries
        if (query.trim().isEmpty()) {
          continue;
        }

        // Create a new statement and execute the query
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        // Append the results of the query to the result list
        ResultSetMetaData metadata = resultSet.getMetaData();
        int columnCount = metadata.getColumnCount();
        while (resultSet.next()) {
          StringBuilder row = new StringBuilder();
          for (int i = 1; i <= columnCount; i++) {
            row.append(resultSet.getString(i)).append("\t");
          }
          resultList.add(row.toString());
        }

        // Clean up resources
        resultSet.close();
        statement.close();
      }

      // Close the connection
      connection.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return resultList.toArray(new String[0]);
  }

  //Modified executeSQL
  public static String[] executeSQL(String interval, String metric, String startDate, String endDate) {

//    String jdbcUrl = "jdbc:sqlite:" + Utility.cleanURL(
//            SQLExecutor.class.getResource("/db/logDatabase.db").getPath());
    String currentPath = "/" + System.getProperty("user.dir") + "/logDatabase.db";
    String jdbcUrl = "jdbc:sqlite:" + Utility.cleanURL(currentPath);
    List<String> resultList = new ArrayList<>();

    try {
      // Load the SQLite JDBC driver
      Class.forName("org.sqlite.JDBC");

      // Create a connection to the database
      Connection connection = DriverManager.getConnection(jdbcUrl);

      // Read the SQL script from a file
      String sqlScript = SQLGenerator.getSQLQuery(interval, metric, startDate, endDate);

      Statement statement = connection.createStatement();
      ResultSet resultSet = statement.executeQuery(sqlScript);

      // Append the results of the query to the result list
      ResultSetMetaData metadata = resultSet.getMetaData();
      int columnCount = metadata.getColumnCount();
      while (resultSet.next()) {
        StringBuilder row = new StringBuilder();
        for (int i = 1; i <= columnCount; i++) {
          row.append(resultSet.getString(i)).append("\t");
        }
        resultList.add(row.toString());
      }

      // Clean up resources
      resultSet.close();
      statement.close();

      // Close the connection
      connection.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return resultList.toArray(new String[0]);

  }
}
