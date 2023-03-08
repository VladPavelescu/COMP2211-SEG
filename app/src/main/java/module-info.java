module uk.ac.soton.comp2211 {
    requires javafx.controls;
  requires org.apache.logging.log4j;
  requires java.sql;
  requires javafx.fxml;
  exports uk.ac.soton.comp2211;

  opens uk.ac.soton.comp2211.controllers to javafx.fxml;
}
