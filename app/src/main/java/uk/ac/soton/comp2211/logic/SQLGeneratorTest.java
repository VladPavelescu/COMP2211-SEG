package uk.ac.soton.comp2211.logic;

public class SQLGeneratorTest {

    public static String getSQLQuery(String interval,String metric, String startDate, String endDate) {

        String group = "";
        startDate = String.format("'%s'", startDate);
        endDate = String.format("'%s'", endDate);

        switch (interval){
            case "Hourly" ->
               group = "strftime('%Y-%m-%d %H:00:00', date)";
            case "Daily" ->
                group = "date(date)";
            case "Weekly" ->
                group = "strftime('%Y-%W', date)";
            default -> {
            }
        };

        String groupEntryDate = group.substring(0, group.length() - 5);

        return switch (metric){
            case "bounceCount" ->
                "SELECT " + groupEntryDate + "entry_date)" + " AS time, COUNT(exit_date)" +
                        " FROM server_log" +
                        " WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = 'n/a')" +
                        " GROUP BY time";
//            case "bounceRate" ->
//
            case "clickCount" ->
                    "SELECT " + group + " AS time, COUNT(*) AS frequency" +
                            " FROM click_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time";
            case "conversionCount" ->
                    "SELECT " + groupEntryDate + "entry_date)" + " AS time, COUNT(*) AS frequency" +
                            " FROM server_log" +
                            " WHERE DATE(entry_date) BETWEEN " + startDate + " AND " + endDate + " AND conversion = \"Yes\"" +
                            " GROUP BY time";

//            case "CPA" ->
//
//            case "CPC" ->
//
//            case "CPM" ->
//
//            case "CTR" ->

            case "impressionNum" ->
                    "SELECT " + group + " AS time, COUNT(*) AS frequency" +
                            " FROM impression_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time";
            case "totalCost" ->
                    "SELECT " + group + " AS hour, SUM(impression_cost) + SUM(click_cost) AS total_click_cost" +
                            " FROM (" +
                            " SELECT date, impression_cost, 0 AS click_cost FROM impression_log" +
                            " UNION" +
                            " SELECT date, 0 AS impression_cost, click_cost FROM click_log" +
                            " ) AS logs" +
                            " WHERE date BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY hour";
            case "uniquesCount" ->
                    "SELECT " + group + " AS time, COUNT(DISTINCT id) AS frequency" +
                            " FROM click_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time";
            default -> null;
        };

    }

}
