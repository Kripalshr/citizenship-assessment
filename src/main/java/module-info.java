module com.example.citizenshipassessment {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens com.example.citizenshipassessment to javafx.fxml;
    exports com.example.citizenshipassessment;
}