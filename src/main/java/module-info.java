/**
 *
 */
module com.alex.euclid {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires lombok;
    requires org.controlsfx.controls;

    opens com.alex.euclid to javafx.fxml;
    exports com.alex.euclid;
    exports ContstantString;
    opens ContstantString to javafx.fxml;
}