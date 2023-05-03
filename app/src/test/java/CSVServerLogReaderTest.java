import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.logic.*;
import uk.ac.soton.comp2211.utility.Utility;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class CSVServerLogReaderTest {

    static String databasePath = "src/test/java/testFiles/testDatabase.db";
    static String testFilePath = "src/test/java/testFiles/testServerLog.csv";

    @BeforeAll
    public static void setup() throws IOException {
        File database = new File(databasePath);
        database.createNewFile();
    }

    @AfterAll
    public static void cleanUp(){
        File database = new File(databasePath);
        database.delete();
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
            List<String> testColumnNames = Arrays.asList("entry_date", "id", "exit_date", "pages_viewed", "conversion");
            List<String> columnNames = new ArrayList<>();

            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            DatabaseMetaData metadata = connection.getMetaData();
            tables = metadata.getTables(null, null, null, new String[] {"TABLE"});

            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                ResultSet columns = metadata.getColumns(null, null, tableName, null);
                if(tableName.equals("server_log")){
                    while (columns.next()) {
                        String columnName = columns.getString("COLUMN_NAME");
                        columnNames.add(columnName);
                    }
                } else{
                    break;
                }
            }
            connection.close();

            assertEquals(testColumnNames, columnNames, "The server_log table was not created. Likely because the column format in the input file is incorrect");

        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close(null, tables, connection);
        }

    }

    @Test
    public void testCheckCorrectData(){

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try{
            List<String> testData = Arrays.asList("2015-01-01 12:01:01,1,n/a,1,No", "2015-01-02 12:02:02,2,2015-01-02 12:02:04,6,Yes");
            List<String> data = new ArrayList<>();

            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM  server_log");

            while (resultSet.next()) {

                String entry_date = resultSet.getString("entry_date");
                int id = resultSet.getInt("ID");
                String exit_date = resultSet.getString("exit_date");
                int pages_viewed = resultSet.getInt("pages_viewed");
                String conversion = resultSet.getString("conversion");

                data.add(entry_date + "," + id + "," + exit_date + "," + pages_viewed + "," + conversion);
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
