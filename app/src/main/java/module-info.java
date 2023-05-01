module uk.ac.soton.comp2211 {
    requires javafx.controls;
  requires org.apache.logging.log4j;
  requires java.sql;
  requires javafx.fxml;
  requires javafx.swing;
  exports uk.ac.soton.comp2211;
  exports uk.ac.soton.comp2211.logic;

  opens uk.ac.soton.comp2211.controllers to javafx.fxml;
}
