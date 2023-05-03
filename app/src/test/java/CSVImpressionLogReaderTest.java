import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.logic.Switcher;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CSVImpressionLogReaderTest {

    static String databasePath = "src/test/java/testFiles/testDatabase.db";
    static String testFilePath = "src/test/java/testFiles/testImpressionLog.csv";

    @BeforeAll
    public static void setup() throws IOException {
        File database = new File(databasePath);
        database.createNewFile();
    }

    @AfterAll
    public static void cleanUp(){
        File database = new File(databasePath);
        System.out.println(database.delete());
    }

    private static void close(Statement statement, ResultSet resultSet, Connection connection){
        try{
            if(statement != null){
                statement.close();
            }
            if(resultSet != null){
                resultSet.close();
            }
            if(connection != null){
                connection.close();
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateTable(){
        try {
            Switcher.readFirstLine(testFilePath, databasePath);
        } catch (Exception e) {
            fail("Test file is in the wrong format");
        }

        Connection connection = null;
        ResultSet tables = null;

        try{
            List<String> testFullColumnNames = Arrays.asList("date", "id", "gender", "age", "income", "context", "impression_cost");
            List<String> testIDColumnNames = Arrays.asList("id", "gender", "age", "income", "context");
            List<String> fullColumnNames = new ArrayList<>();
            List<String> idColumnNames = new ArrayList<>();

            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            DatabaseMetaData metadata = connection.getMetaData();
            tables = metadata.getTables(null, null, null, new String[] {"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                ResultSet columns = metadata.getColumns(null, null, tableName, null);
                if(tableName.equals("impression_log")){
                    while (columns.next()) {
                        String columnName = columns.getString("COLUMN_NAME");
                        fullColumnNames.add(columnName);
                    }
                } else if(tableName.equals("impressionIDs")){
                    while (columns.next()) {
                        String columnName = columns.getString("COLUMN_NAME");
                        idColumnNames.add(columnName);
                    }
                }
            }
            connection.close();
            assertEquals(testFullColumnNames, fullColumnNames, "The impression_log table was not created. Likely because the column format in the input file is incorrect");
            assertEquals(testIDColumnNames, idColumnNames, "The impressionIDs table was not created. Likely because the column format in the input file is incorrect");

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(null, tables, connection);
        }

    }

    @Test
    public void testCheckCorrectDataForFullTable(){

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            List<String> testData = Arrays.asList("2015-01-01 12:01:01,1,Male,25-34,High,Blog,0.1", "2015-01-02 12:02:02,2,Female,35-44,Medium,News,0.2");
            List<String> data = new ArrayList<>();

            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM  impression_log");

            while (resultSet.next()) {

                String date = resultSet.getString("date");
                int id = resultSet.getInt("ID");
                String gender = resultSet.getString("gender");
                String age = resultSet.getString("age");
                String income = resultSet.getString("income");
                String context = resultSet.getString("context");
                double impression_cost = resultSet.getDouble("impression_cost");

                data.add(date + "," + id + "," + gender + "," + age + "," + income + "," + context + "," + impression_cost);
            }
            connection.close();

            assertEquals(testData, data, "Data was not inputted into the database correctly");

        } catch (Exception e){
            e.printStackTrace();
            fail();
        } finally {
            close(statement, resultSet, connection);
        }
    }

    @Test
    public void testCheckCorrectDataForIDTable(){

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            List<String> testData = Arrays.asList("1,Male,25-34,High,Blog", "2,Female,35-44,Medium,News");
            List<String> data = new ArrayList<>();

            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM  impressionIDs");

            while (resultSet.next()) {

                int id = resultSet.getInt("ID");
                String gender = resultSet.getString("gender");
                String age = resultSet.getString("age");
                String income = resultSet.getString("income");
                String context = resultSet.getString("context");

                data.add(id + "," + gender + "," + age + "," + income + "," + context);
            }
            connection.close();

            assertEquals(testData, data, "Data was not inputted into the database correctly");

        } catch (Exception e){
            e.printStackTrace();
            fail();
        } finally {
            close(statement, resultSet, connection);
        }
    }

}
