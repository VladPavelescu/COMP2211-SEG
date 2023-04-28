package uk.ac.soton.comp2211.logic;

public class SQLGenerator {

    public static String getSQLQuery(String bounceDef, String interval,String metric, String startDate, String endDate, String context, String income, String age, String gender) {

        String group = "";
        startDate = String.format("'%s'", startDate);
        endDate = String.format("'%s'", endDate);

        // Group by time intervals
        switch (interval){
            case "Hourly" ->
               group = "strftime('%Y-%m-%d %H:00:00', date)";
            case "Daily" ->
                group = "date(date)";
            case "Weekly" ->
                group = "strftime('%Y-%W', date)";
            default -> {
            }
        }

        // If the table uses a different column name other than date
        String groupTrimmed = group.substring(0, group.length() - 5);

        String filters = "";

        if (context != null && !context.equals("")) {
            filters = filters + " AND context = '" + context + "'";
        }
        if (income != null && !income.equals("")) {
            filters = filters + " AND income = '" + income + "'";
        }
        if (age != null && !age.equals("")) {
            filters = filters + " AND age = '" + age + "'";
        }
        if (gender != null && !gender.equals("")) {
            filters = filters + " AND gender = '" + gender + "'";
        }

        if(!filters.equals("")){
            return switch (metric){
                case "bounceCount" ->
                        switch (bounceDef) {
                            case "Short Time Spent" ->
                                    "SELECT " + groupTrimmed + "entry_date) AS time, COUNT(exit_date)" +
                                            " FROM server_log" +
                                            " JOIN impressionIDs ON server_log.id = impressionIDs.id" +
                                            " WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = 'n/a')" +
                                            " AND DATE(entry_date) BETWEEN " + startDate + " AND " + endDate +
                                            filters +
                                            " GROUP BY time";
                            case "Single Page Visits" ->
                                    "SELECT " + groupTrimmed + "entry_date) AS time, COUNT(pages_viewed)" +
                                            " FROM server_log" +
                                            " JOIN impressionIDs ON server_log.id = impressionIDs.id" +
                                            " WHERE (pages_viewed) == 1" +
                                            filters +
                                            " AND DATE(entry_date) BETWEEN " + startDate + " AND " + endDate +
                                            " GROUP BY time";
                            default -> "";
                        };
                case "bounceRate" ->
                        "SELECT bounce.time, COALESCE(CAST(bounce.count AS REAL) / CAST(click.frequency AS REAL), 0)" +
                                " FROM (" +
                                " SELECT " + groupTrimmed + "entry_date) AS time, COUNT(*) AS count" +
                                " FROM server_log" +
                                " JOIN impressionIDs ON server_log.id = impressionIDs.id" +
                                " WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = 'n/a')" +
                                " AND DATE(entry_date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time) AS bounce" +
                                " JOIN (" +
                                " SELECT " + groupTrimmed + "click_log.date) AS time, COUNT(*) AS frequency" +
                                " FROM click_log" +
                                " JOIN impressionIDs ON click_log.id = impressionIDs.id" +
                                " WHERE DATE(click_log.date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time) AS click" +
                                " ON bounce.time = click.time";
                case "clickCount" ->
                        "SELECT " + groupTrimmed + "click_log.date) AS time, COUNT(*) AS frequency" +
                                " FROM click_log" +
                                " JOIN impressionIDs ON click_log.id = impressionIDs.id" +
                                " WHERE DATE(click_log.date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time";
                case "conversionCount" ->
                        "SELECT " + groupTrimmed + "entry_date)" + " AS time, COUNT(*) AS frequency" +
                                " FROM server_log" +
                                " JOIN impressionIDs ON server_log.id = impressionIDs.id" +
                                " WHERE DATE(entry_date) BETWEEN " + startDate + " AND " + endDate + " AND conversion = \"Yes\"" +
                                filters +
                                " GROUP BY time";
                case "CPA" ->
                        "SELECT time, COALESCE(CAST(SUM((total_cost)) AS REAL) / CAST(total as REAL), 0) AS cpa" +
                                " FROM (" +
                                " SELECT " + groupTrimmed + "entry_date)" + " AS time, 0 AS total_cost, COUNT(*) AS total" +
                                " FROM server_log" +
                                " JOIN impressionIDs ON server_log.id = impressionIDs.id" +
                                " WHERE conversion = 'Yes' AND DATE(entry_date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time" +
                                " UNION" +
                                " SELECT " + group + " AS time, SUM(impression_cost) AS total_cost, 0 AS total" +
                                " FROM impression_log" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time" +
                                " UNION" +
                                " SELECT " + group + " AS time, SUM(click_cost) AS total_cost, 0 AS total" +
                                " FROM click_log" +
                                " JOIN impressionIDs ON click_log.id = impressionIDs.id" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time" +
                                " ) GROUP BY time";
                case "CPC" ->
                        "SELECT time, COALESCE(CAST(SUM((total_cost)) AS REAL) / CAST(total as REAL), 0) AS cpc" +
                                " FROM (" +
                                " SELECT " + group + " AS time, SUM(click_cost) AS total_cost, COUNT(*) AS total" +
                                " FROM click_log" +
                                " JOIN impressionIDs ON click_log.id = impressionIDs.id" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time" +
                                " UNION ALL" +
                                " SELECT " + group + " AS time, SUM(impression_cost) AS total_cost, 0 AS total" +
                                " FROM impression_log" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time" +
                                " ) GROUP BY time";
                case "CPM" ->
                        "SELECT time, COALESCE(CAST(SUM((total_cost)) AS REAL) / CAST(total as REAL),0) * 1000 AS cpm" +
                                " FROM (" +
                                " SELECT " + group + " AS time, SUM(impression_cost) AS total_cost, COUNT(*) AS total" +
                                " FROM impression_log" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time" +
                                " UNION" +
                                " SELECT " + group + " AS time, SUM(click_cost) AS total_cost, COUNT(*) AS total" +
                                " FROM click_log" +
                                " JOIN impressionIDs ON click_log.id = impressionIDs.id" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time" +
                                " ) GROUP BY time";
                case "CTR" ->
                        "SELECT impression.time, COALESCE(CAST(click.count AS FLOAT) / CAST(impression.count AS FLOAT), 0) AS ctr" +
                                " FROM (" +
                                " SELECT " + group + " AS time, COUNT(*) AS count" +
                                " FROM impression_log" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time) AS impression" +
                                " LEFT JOIN (" +
                                " SELECT " + group + " AS time, COUNT(*) AS count" +
                                " FROM click_log" +
                                " JOIN impressionIDs ON click_log.id = impressionIDs.id" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time) AS click" +
                                " ON impression.time = click.time";
                case "impressionNum" ->
                        "SELECT " + group + " AS time, COUNT(*) AS frequency" +
                                " FROM impression_log" +
                                " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time";
                case "totalCost" ->
                        "SELECT " + group + " AS time, SUM(impression_cost) + SUM(click_cost) AS total_click_cost" +
                                " FROM (" +
                                " SELECT date, impression_cost, 0 AS click_cost" +
                                " FROM impression_log" +
                                " WHERE" +
                                filters.substring(4) +
                                " UNION" +
                                " SELECT date, 0 AS impression_cost, click_cost" +
                                " FROM click_log" +
                                " JOIN impressionIDs ON click_log.id = impressionIDs.id" +
                                " WHERE" +
                                filters.substring(4) +
                                " ) AS logs" +
                                " WHERE date BETWEEN " + startDate + " AND DATE(" + endDate + ", '+1 day')" + //only returns endDate-1 for some reason
                                " GROUP BY time";
                case "uniquesCount" ->
                        "SELECT " + groupTrimmed + "click_log.date) AS time, COUNT(DISTINCT click_log.id) AS frequency" +
                                " FROM click_log" +
                                " JOIN impressionIDs ON click_log.id = impressionIDs.id" +
                                " WHERE DATE(click_log.date) BETWEEN " + startDate + " AND " + endDate +
                                filters +
                                " GROUP BY time";
                default -> null;
            };
        }

        return switch (metric){
            case "bounceCount" ->
                switch (bounceDef){
                    case "Short Time Spent" ->
                        "SELECT " + groupTrimmed + "entry_date) AS time, COUNT(exit_date)" +
                            " FROM server_log" +
                            " WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = 'n/a')" +
                            " AND DATE(entry_date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time";
                    case "Single Page Visits" ->
                        "SELECT " + groupTrimmed + "entry_date) AS time, COUNT(pages_viewed)" +
                            " FROM server_log" +
                            " WHERE (pages_viewed) == 1" +
                            " AND DATE(entry_date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time";
                    default -> "";
                }
                ;
            case "bounceRate" ->
                "SELECT bounce.time, COALESCE(CAST(bounce.count AS REAL) / CAST(click.frequency AS REAL), 0)" +
                        " FROM (" +
                        " SELECT " + groupTrimmed + "entry_date) AS time, COUNT(exit_date) AS count" +
                        " FROM server_log" +
                        " WHERE (unixepoch(exit_date) - unixepoch(entry_date) < 10 OR exit_date = 'n/a')" +
                        " AND DATE(entry_date) BETWEEN " + startDate + " AND " + endDate +
                        " GROUP BY time) AS bounce" +
                        " JOIN (" +
                        " SELECT " + group + " AS time, COUNT(*) AS frequency" +
                        " FROM click_log" +
                        " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                        " GROUP BY time) AS click" +
                        " ON bounce.time = click.time";
            case "clickCount" ->
                    "SELECT " + group + " AS time, COUNT(*) AS frequency" +
                            " FROM click_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time";
            case "conversionCount" ->
                    "SELECT " + groupTrimmed + "entry_date)" + " AS time, COUNT(*) AS frequency" +
                            " FROM server_log" +
                            " WHERE DATE(entry_date) BETWEEN " + startDate + " AND " + endDate + " AND conversion = \"Yes\"" +
                            " GROUP BY time";
            case "CPA" ->
                    "SELECT time, COALESCE(CAST(SUM((total_cost)) AS REAL) / CAST(total as REAL), 0) AS cpa" +
                            " FROM (" +
                            " SELECT " + groupTrimmed + "entry_date)" + " AS time, 0 AS total_cost, COUNT(*) AS total" +
                            " FROM server_log" +
                            " WHERE conversion = 'Yes' AND DATE(entry_date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time" +
                            " UNION" +
                            " SELECT " + group + " AS time, SUM(impression_cost) AS total_cost, 0 AS total" +
                            " FROM impression_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time" +
                            " UNION" +
                            " SELECT " + group + " AS time, SUM(click_cost) AS total_cost, 0 AS total" +
                            " FROM click_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time" +
                            " ) GROUP BY time";
            case "CPC" ->
                     "SELECT time, COALESCE(CAST(SUM((total_cost)) AS REAL) / CAST(total as REAL), 0) AS cpc" +
                            " FROM (" +
                            " SELECT " + group + " AS time, SUM(click_cost) AS total_cost, COUNT(*) AS total" +
                            " FROM click_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time" +
                            " UNION ALL" +
                            " SELECT " + group + " AS time, SUM(impression_cost) AS total_cost, 0 AS total" +
                            " FROM impression_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time" +
                            " ) GROUP BY time";
            case "CPM" ->
                    "SELECT time, COALESCE(CAST(SUM((total_cost)) AS REAL) / CAST(total as REAL),0) * 1000 AS cpm" +
                            " FROM (" +
                            " SELECT " + group + " AS time, SUM(impression_cost) AS total_cost, COUNT(*) AS total" +
                            " FROM impression_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time" +
                            " UNION" +
                            " SELECT " + group + " AS time, SUM(click_cost) AS total_cost, COUNT(*) AS total" +
                            " FROM click_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time" +
                            " ) GROUP BY time";
            case "CTR" ->
                    "SELECT impression.time, COALESCE(CAST(click.count AS FLOAT) / CAST(impression.count AS FLOAT), 0) AS ctr" +
                            " FROM (" +
                            " SELECT " + group + " AS time, COUNT(*) AS count" +
                            " FROM impression_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time) AS impression" +
                            " LEFT JOIN (" +
                            " SELECT " + group + " AS time, COUNT(*) AS count" +
                            " FROM click_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time) AS click" +
                            " ON impression.time = click.time";
            case "impressionNum" ->
                    "SELECT " + group + " AS time, COUNT(*) AS frequency" +
                            " FROM impression_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time";
            case "totalCost" ->
                    "SELECT " + group + " AS time, SUM(impression_cost) + SUM(click_cost) AS total_click_cost" +
                            " FROM (" +
                            " SELECT date, impression_cost, 0 AS click_cost FROM impression_log" +
                            " UNION" +
                            " SELECT date, 0 AS impression_cost, click_cost FROM click_log" +
                            " ) AS logs" +
                            " WHERE date BETWEEN " + startDate + " AND DATE(" + endDate + ", '+1 day')" + //only returns endDate-1 for some reason
                            " GROUP BY time";
            case "uniquesCount" ->
                    "SELECT " + group + " AS time, COUNT(DISTINCT id) AS frequency" +
                            " FROM click_log" +
                            " WHERE DATE(date) BETWEEN " + startDate + " AND " + endDate +
                            " GROUP BY time";
            default -> null;
        };

    }

    public static String getHistogramSQL(String lowerBound, String upperBound) {
        return "SELECT count(click_cost) FROM click_log WHERE click_cost >= " + lowerBound + " and click_cost < " + upperBound +";";
    }

}
