package uk.ac.soton.comp2211.logic;

import java.io.BufferedReader;
import java.sql.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp2211.utility.Utility;

public class dateExecutor {
    HashMap<String, Float> vals;
    // Database connection details
    String jdbcUrl;

    Connection connection;
    PreparedStatement preparedStatement;

    dateGenerator query;

    DateTimeFormatter formatter;

    String select;
    String endDate;
    String startDate;

    public dateExecutor(String startDate, String endDate, String select) throws ParseException {

        this.select = select;
        this.startDate = startDate;
        this.endDate = endDate;

        vals = new HashMap<String, Float>();
        // Database connection details
        String jdbcUrl = "jdbc:sqlite:" + Utility.cleanURL(CSVServerLogReader.class.getResource("/db/logDatabase.db").getPath());

        Connection connection = null;
        PreparedStatement preparedStatement = null;

        query = new dateGenerator(select, startDate, endDate);
        query.getStatements();

        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }



    public HashMap<String, Float> executor() throws ParseException {
        List<LocalDate> dates = query.getDates();
        int len = dates.size();
        String date;
        for (int i = 0; i < len; i++) {
            date = formatter.format(dates.get(i));
            try {
                // Connect to the database
                connection = DriverManager.getConnection(jdbcUrl);
                connection.setAutoCommit(false);

                // Prepare the select statement
                String insertSql = query.getDay(date);
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(date);
                Float answer = resultSet.getFloat(1);

                vals.put(date, answer);

                resultSet.close();
                statement.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return vals;
    }
}
