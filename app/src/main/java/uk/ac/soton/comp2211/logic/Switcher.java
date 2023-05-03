package uk.ac.soton.comp2211.logic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Switcher {

  private static final Logger logger = LogManager.getLogger(Switcher.class);

  public static void readFirstLine(String filePath, String database) throws Exception{
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String firstLine = br.readLine();
      if (firstLine == null) {
        return;
      }
      String[] fields = firstLine.split(",");
      switch (fields.length) {
        case 3:
          if (fields[0].equals("Date") && fields[1].equals("ID") && fields[2].equals("Click Cost")) {
            CSVClickLogReader.readFile(filePath, database);
          } else {
            logger.error("File is in the wrong format.");
            throw new Exception("File is in the wrong format.");
          }
          break;
        case 7:
          if (fields[0].equals("Date") && fields[1].equals("ID") && fields[2].equals("Gender")
              && fields[3].equals("Age") && fields[4].equals("Income") && fields[5].equals("Context")
              && fields[6].equals("Impression Cost")) {
            CSVImpressionLogReader.readFile(filePath, database);
          } else {
            logger.error("File is in the wrong format.");
            throw new Exception("File is in the wrong format.");
          }
          break;
        case 5:
          if (fields[0].equals("Entry Date") && fields[1].equals("ID") && fields[2].equals("Exit Date")
              && fields[3].equals("Pages Viewed") && fields[4].equals("Conversion")) {
            CSVServerLogReader.readFile(filePath, database);
          } else {
            logger.error("File is in the wrong format.");
            throw new Exception("File is in the wrong format.");
          }
          break;
        default:
          logger.error("The file you uploaded doesn't match any of the supported formats.");
          throw new Exception("The file you uploaded doesn't match any of the supported formats.");
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}