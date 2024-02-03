module com.example.logiccircuit {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.logiccircuit to javafx.fxml;
    exports com.example.logiccircuit;
}