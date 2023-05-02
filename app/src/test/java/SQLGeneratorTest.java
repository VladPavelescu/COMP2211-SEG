import com.sun.source.tree.SwitchTree;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import uk.ac.soton.comp2211.logic.SQLGenerator;
import uk.ac.soton.comp2211.logic.Switcher;
import uk.ac.soton.comp2211.utility.Utility;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SQLGeneratorTest {

    static String databasePath = "src/test/java/testFiles/testDatabase.db";

    @BeforeAll
    public static void createDatabase() throws IOException {
        File database = new File(databasePath);
        database.createNewFile();

        String server_log = "src/test/java/testFiles/testServerLog.csv";
        String click_log = "src/test/java/testFiles/testClickLog.csv";
        String impression_log = "src/test/java/testFiles/testImpressionLog.csv";

        Switcher.readFirstLine(server_log, databasePath);
        Switcher.readFirstLine(click_log, databasePath);
        Switcher.readFirstLine(impression_log, databasePath);

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

    private static String[] sqlExecutor(String bounceDef, String interval, String metric,
                                    String startDate, String endDate, String context, String income, String age, String gender){

        List<String> resultList = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
            String sqlScript = SQLGenerator.getSQLQuery(bounceDef, interval, metric, startDate, endDate, context, income, age, gender);

            statement = connection.createStatement();
            resultSet = statement.executeQuery(sqlScript);

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            while(resultSet.next()){
                StringBuilder row = new StringBuilder();
                for(int i = 1; i<= columnCount; i++){
                    row.append(resultSet.getString(i)).append("\t");
                }
                resultList.add(row.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(statement, resultSet, connection);
        }

        return resultList.toArray(new String[0]);

    }


    @Test
    public void bounceCountTest() {
        String[] shortTimeSpentResultList = sqlExecutor("Short Time Spent", "Daily" , "bounceCount", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] shortTimeSpentTestResults = {"2015-01-01\t1\t","2015-01-02\t1\t"};

        assertArrayEquals(shortTimeSpentTestResults, shortTimeSpentResultList);

        String[] singlePageVisitsResultList = sqlExecutor("Single Page Visits", "Daily" , "bounceCount", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] singlePageVisitsTestResults = {"2015-01-01\t1\t"};

        assertArrayEquals(singlePageVisitsTestResults, singlePageVisitsResultList);
    }

    @Test
    public void bounceRateTest(){
        String[] resultList = sqlExecutor(null, "Daily", "bounceRate", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t1.0\t","2015-01-02\t1.0\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void clickCountTest(){
        String[] resultList = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t1\t","2015-01-02\t1\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void conversionCountTest(){
        String[] resultList = sqlExecutor(null, "Daily", "conversionCount", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-02\t1\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void CPATest(){
        String[] resultList = sqlExecutor(null, "Daily", "CPA", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t0\t","2015-01-02\t3.22\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void CPCTest(){
        String[] resultList = sqlExecutor(null, "Daily", "CPC", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t3.11\t","2015-01-02\t3.22\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void CPMTest(){
        String[] resultList = sqlExecutor(null, "Daily", "CPM", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t3110.0\t","2015-01-02\t3220.0\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void CTRTest(){
        String[] resultList = sqlExecutor(null, "Daily", "CTR", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t1.0\t","2015-01-02\t1.0\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void ImpressionNumTest(){
        String[] resultList = sqlExecutor(null, "Daily", "impressionNum", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t1\t","2015-01-02\t1\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void totalCostTest(){
        String[] resultList = sqlExecutor(null, "Daily", "totalCost", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t3.11\t","2015-01-02\t3.22\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void uniquesCount(){
        String[] resultList = sqlExecutor(null, "Daily", "uniquesCount", "2015-01-01", "2015-01-02", null, null, null, null);
        String[] testResults = {"2015-01-01\t1\t","2015-01-02\t1\t"};

        assertArrayEquals(testResults, resultList);
    }

    @Test
    public void dateRangeTest(){
        String[] resultList1 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-01", null, null, null, null);
        String[] testResults1 = {"2015-01-01\t1\t"};

        assertArrayEquals(testResults1, resultList1, "Date range of data returned is incorrect");

        String[] resultList2 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-04", null, null, null, null);
        String[] testResults2 = {"2015-01-01\t1\t", "2015-01-02\t1\t"};

        assertArrayEquals(testResults2, resultList2, "Date range of data returned is incorrect");

    }

    @Test
    public void contextFilterTest(){
        String[] resultList1 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", "Blog", null, null, null);
        String[] testResults1 = {"2015-01-01\t1\t"};

        assertArrayEquals(testResults1, resultList1);

        String[] resultList2 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", "News", null, null, null);
        String[] testResults2 = {"2015-01-02\t1\t"};

        assertArrayEquals(testResults2, resultList2);
    }

    @Test
    public void incomeFilterTest(){
        String[] resultList1 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", null, "High", null, null);
        String[] testResults1 = {"2015-01-01\t1\t"};

        assertArrayEquals(testResults1, resultList1);

        String[] resultList2 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", null, "Medium", null, null);
        String[] testResults2 = {"2015-01-02\t1\t"};

        assertArrayEquals(testResults2, resultList2);
    }

    @Test
    public void ageFilterTest(){
        String[] resultList1 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", null, null, "25-34", null);
        String[] testResults1 = {"2015-01-01\t1\t"};

        assertArrayEquals(testResults1, resultList1);

        String[] resultList2 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", null, null, "35-44", null);
        String[] testResults2 = {"2015-01-02\t1\t"};

        assertArrayEquals(testResults2, resultList2);
    }

    @Test
    public void genderFilterTest(){
        String[] resultList1 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", null, null, null, "Male");
        String[] testResults1 = {"2015-01-01\t1\t"};

        assertArrayEquals(testResults1, resultList1);

        String[] resultList2 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", null, null, null, "Female");
        String[] testResults2 = {"2015-01-02\t1\t"};

        assertArrayEquals(testResults2, resultList2);
    }

    @Test
    public void multipleFilterTest(){
        String[] resultList1 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", "Blog", "High", "25-34", "Male");
        String[] testResults1 = {"2015-01-01\t1\t"};

        assertArrayEquals(testResults1, resultList1);

        String[] resultList2 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", "News", "Medium", "35-44", "Female");
        String[] testResults2 = {"2015-01-02\t1\t"};

        assertArrayEquals(testResults2, resultList2);

        //Empty cases
        String[] resultList3 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", "Blog", "High", "25-34", "Female");
        String[] testResults3 = {};

        assertArrayEquals(testResults3, resultList3);

        String[] resultList4 = sqlExecutor(null, "Daily", "clickCount", "2015-01-01", "2015-01-02", "Shopping", "High", "25-34", "Male");
        String[] testResults4 = {};

        assertArrayEquals(testResults4, resultList4);
    }



}
