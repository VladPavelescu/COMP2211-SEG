package uk.ac.soton.comp2211.logic;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;


public class sqlGenerator {

    String date;
    HashMap statements;
    String query;

    public sqlGenerator(String date, String query) {
        this.date = date;
        this.query = query;
        statements = new HashMap<>();
        statements.put("bounceCount",
                ("SELECT COUNT(exit_date) FROM server_log WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = \"n/a\") AND DATE(entry_date) = \"" + this.date + "\""));
        statements.put("bounceRate",
                "SELECT (SELECT CAST(COUNT(exit_date) AS REAL) FROM server_log WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = \"n/a\") AND DATE(entry_date) = \"" + this.date + "\") / (SELECT CAST(COUNT(id) AS REAL) FROM click_log WHERE DATE(date) = \"" + this.date + "\"");
        statements.put("clickCount", "SELECT COUNT(id) FROM click_log WHERE DATE(date) = \"" + this.date + "\"");
        statements.put("conversionCount",
                "SELECT COUNT(conversion) FROM server_log WHERE conversion = \"Yes\" AND DATE(entry_date) = \"" + this.date + "\"");
        statements.put("CPA",
                "SELECT (WITH totals AS (SELECT SUM(impression_cost) AS total FROM impression_log  WHERE DATE(date) = \"" + this.date + "\" UNION SELECT SUM(click_cost) AS total FROM click_log WHERE DATE(date) = \"" + this.date + "\") SELECT SUM(total) / 100 FROM totals) / (SELECT CAST(COUNT(conversion) AS FLOAT) FROM server_log WHERE conversion = \"Yes\") AND DATE(start_date) = \"" + this.date + "\"");
        statements.put("CPC",
                "SELECT (WITH totals AS(SELECT SUM(impression_cost) AS total FROM impression_log WHERE DATE(date) = \"" + this.date + "\" UNION SELECT SUM(click_cost) AS total FROM click_log WHERE DATE(date) = \"" + this.date + "\") SELECT SUM(total) / 100 FROM totals) / (SELECT CAST(COUNT(id) AS FLOAT) FROM click_log WHERE DATE(date) = \"" + this.date + "\")");
        statements.put("CPM",
                "SELECT (WITH totals AS(SELECT SUM(impression_cost) AS total FROM impression_log  WHERE DATE(date) = \"" + this.date + "\" UNION SELECT SUM(click_cost) AS total FROM click_log WHERE DATE(date) = \"" + this.date + "\") SELECT SUM(total) / 100 FROM totals) / (SELECT (SELECT COUNT(age) FROM impression_log WHERE DATE(date) = \"" +this.date+ "\") / 1000)");
        statements.put("CTR",
                "SELECT((SELECT CAST(COUNT(id) AS FLOAT) FROM click_log WHERE DATE(date) = \"" +this.date+ "\") / (SELECT CAST(COUNT(id) AS FLOAT) FROM impression_log WHERE DATE(date) = \"" +this.date+ "\"))");
        statements.put("impressionNum", "SELECT COUNT(age) FROM impression_log WHERE DATE(date) = \"" + this.date + "\"");
        statements.put("totalCost",
                "WITH totals AS(SELECT SUM(impression_cost) AS total FROM impression_log WHERE DATE(date) = \"" +this.date+ "\" UNION SELECT SUM(click_cost) AS total FROM click_log WHERE DATE(date) = \"" +this.date+ "\") SELECT SUM(total) / 100 FROM totals");
        statements.put("uniquesCount", "SELECT COUNT(DISTINCT id) FROM click_log WHERE DATE(date) = \"" +this.date + "\"");
    }

    public String getQuery(){
        return (String) statements.get(query);
    }
}


