package uk.ac.soton.comp2211.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.utility.Utility;

class CSVClickLogReader {

  private static final Logger logger = LogManager.getLogger(CSVClickLogReader.class);

  /**
   * Reads the contents of a click_log csv file and writes the results to a database
   * @param filePath the path to the csv file
   */
  public static void readFile(String filePath, String database) {

    BufferedReader bufferedReader = null;
    String line = "";
    String delimiter = ",";

    // Database connection details
    String currentPath = "/" + System.getProperty("user.dir") + "/" + database;
    String jdbcUrl = "jdbc:sqlite:" + Utility.cleanURL(currentPath);
    logger.info("Writing to: " + jdbcUrl);

    Connection connection = null;
    PreparedStatement preparedStatement = null;

    int batchSize = 500000;

    try {
      // Connect to the database
      connection = DriverManager.getConnection(jdbcUrl);
      connection.setAutoCommit(false);

      // Create the click_log table if it doesn't exist
      Statement statement = connection.createStatement();
      statement.executeUpdate(
              "DROP TABLE IF EXISTS click_log");
      statement.executeUpdate(
          "CREATE TABLE IF NOT EXISTS click_log (date TEXT, id TEXT, click_cost REAL)");

      // Prepare the insert statement
      String insertSql = "INSERT INTO click_log (date, id, click_cost) VALUES (?, ?, ?)";
      preparedStatement = connection.prepareStatement(insertSql);

      bufferedReader = new BufferedReader(new FileReader(filePath));
      bufferedReader.readLine();
      logger.info("File successfully read: " + filePath);

      int count = 0;

      while ((line = bufferedReader.readLine()) != null) {
        String[] row = line.split(delimiter);

        // Set the values for the prepared statement
        preparedStatement.setString(1, row[0]);
        preparedStatement.setString(2, row[1]);
        preparedStatement.setDouble(3, Double.parseDouble(row[2]));
        preparedStatement.addBatch();

        count += 1;

        if (count % batchSize == 0) {
          preparedStatement.executeBatch();
          logger.info("Executing batch at count: " + count);
        }
      }
      preparedStatement.executeBatch();
      connection.commit();
      logger.info("Data has been inserted successfully");

    } catch (IOException e) {
      System.err.println("Error reading file: " + e.getMessage());
    } catch (SQLException e) {
      System.err.println("Database error: " + e.getMessage());
    } finally {
      // Close the database connection and prepared statement
      try {
        if (preparedStatement != null) {
          preparedStatement.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException e) {
        System.err.println("Error closing connection: " + e.getMessage());
      }

      // Close the file reader
      if (bufferedReader != null) {
        try {
          bufferedReader.close();
        } catch (IOException e) {
          System.err.println("Error closing file: " + e.getMessage());
        }
      }
    }
  }
}
