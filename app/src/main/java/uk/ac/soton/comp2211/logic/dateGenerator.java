package uk.ac.soton.comp2211.logic;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class dateGenerator {
    String query;
    String startDate;
    String endDate;
    HashMap statements;
    final DateTimeFormatter formatter;

    public dateGenerator(String query, String startDate, String endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        this.query = query;
        statements = new HashMap();
        formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    }

    public List<LocalDate> getDates() throws ParseException {
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        return start.datesUntil(end).collect(Collectors.toList());
    }

    public void getStatements() throws ParseException {
        List<LocalDate> dates = getDates();
        int len = dates.size();
        String date;
        for (int i = 0; i < len; i++) {
            date = formatter.format(dates.get(i));
            sqlGenerator generator = new sqlGenerator(date, query);
            String day = generator.getQuery();
            statements.put(date, day);
        }
    }

    public String getDay(String date){
        return (String) statements.get(date);
    }


}

