package uk.ac.soton.comp2211.logic;

public class SQLGeneratorOLD {

  public static String getSQLQuery(String date, String query) {

    return switch (query) {
      case "bounceCount" ->
          "SELECT COUNT(exit_date) FROM server_log WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = \"n/a\") AND DATE(entry_date) = \""
              + date + "\"";
      case "bounceRate" -> "SELECT (SELECT CAST(COUNT(exit_date) AS REAL) FROM server_log WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = \"n/a\") AND DATE(entry_date) = \"" + date + "\") / (SELECT CAST(COUNT(id) AS REAL) FROM click_log WHERE DATE(date) = \"" + date + "\")";
      case "clickCount" -> "SELECT COUNT(id) FROM click_log WHERE DATE(date) = \"" + date + "\"";
      case "conversionCount" ->
          "SELECT COUNT(conversion) FROM server_log WHERE conversion = \"Yes\" AND DATE(entry_date) = \""
              + date + "\"";
      case "CPA" ->
          "SELECT (WITH totals AS (SELECT SUM(impression_cost) AS total FROM impression_log  WHERE DATE(date) = \""
              + date
              + "\" UNION SELECT SUM(click_cost) AS total FROM click_log WHERE DATE(date) = \""
              + date
              + "\") SELECT SUM(total) / 100 FROM totals) / (SELECT CAST(COUNT(conversion) AS FLOAT) FROM server_log WHERE conversion = \"Yes\" AND DATE(entry_date) = \""
              + date + "\")";
      case "CPC" ->
          "SELECT (WITH totals AS(SELECT SUM(impression_cost) AS total FROM impression_log WHERE DATE(date) = \""
              + date
              + "\" UNION SELECT SUM(click_cost) AS total FROM click_log WHERE DATE(date) = \""
              + date
              + "\") SELECT SUM(total) / 100 FROM totals) / (SELECT CAST(COUNT(id) AS FLOAT) FROM click_log WHERE DATE(date) = \""
              + date + "\")";
      case "CPM" ->
          "SELECT (WITH totals AS(SELECT SUM(impression_cost) AS total FROM impression_log  WHERE DATE(date) = \""
              + date
              + "\" UNION SELECT SUM(click_cost) AS total FROM click_log WHERE DATE(date) = \""
              + date
              + "\") SELECT SUM(total) / 100 FROM totals) / (SELECT (SELECT COUNT(age) FROM impression_log WHERE DATE(date) = \""
              + date + "\") / 1000)";
      case "CTR" -> "SELECT((SELECT CAST(COUNT(id) AS FLOAT) FROM click_log WHERE DATE(date) = \""
          + date
          + "\") / (SELECT CAST(COUNT(id) AS FLOAT) FROM impression_log WHERE DATE(date) = \""
          + date + "\"))";
      case "impressionNum" -> "SELECT COUNT(id) FROM impression_log WHERE DATE(date) = \"" + date
          + "\"";
      case "totalCost" ->
          "WITH totals AS(SELECT SUM(impression_cost) AS total FROM impression_log WHERE DATE(date) = \""
              + date
              + "\" UNION SELECT SUM(click_cost) AS total FROM click_log WHERE DATE(date) = \""
              + date + "\") SELECT SUM(total) / 100 FROM totals";
      case "uniquesCount" -> "SELECT COUNT(DISTINCT id) FROM click_log WHERE DATE(date) = \"" + date
          + "\"";
      default -> null;
    };
  }
}


