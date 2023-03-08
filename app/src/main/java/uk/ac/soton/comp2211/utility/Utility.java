package uk.ac.soton.comp2211.utility;

public class Utility {

  /**
   * Cleans the path to a file when using class.getResource().getPath()
   * @param url the path to a file returned by class.getResource().getPath();
   * @return the cleaned url to that file
   */
  public static String cleanURL(String url) {
    return url.substring(1).replace("%20", " ").replace("/", "\\");
  }

}
