package uk.ac.soton.comp2211.logic;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import uk.ac.soton.comp2211.utility.Utility;

public class SQLExecutor {

  public static String[] executeSQL() {
    String scriptPath = Utility.cleanURL(
        SQLExecutor.class.getResource("/sql/bounceRate.sql").getPath()); //placeholder while testing

    String jdbcUrl = "jdbc:sqlite:" + Utility.cleanURL(
        SQLExecutor.class.getResource("/db/logDatabase.db").getPath());
    List<String> resultList = new ArrayList<>();

    try {
      // Load the SQLite JDBC driver
      Class.forName("org.sqlite.JDBC");

      // Create a connection to the database
      Connection connection = DriverManager.getConnection(jdbcUrl);

      // Read the SQL script from a file
      String sqlScript = new String(Files.readAllBytes(Paths.get(scriptPath)),
          StandardCharsets.UTF_8);

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
}
