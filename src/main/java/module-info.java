module com.example.citizenshipassessment {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires com.opencsv;

    opens com.example.citizenshipassessment to javafx.fxml;
    exports com.example.citizenshipassessment;
    exports com.example.citizenshipassessment.controller;
    opens com.example.citizenshipassessment.controller to javafx.fxml;
}