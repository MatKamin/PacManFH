module com.example.peck {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.peck to javafx.fxml;
    exports com.example.peck;
}