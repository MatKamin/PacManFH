module com.example.peck {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.bouncycastle.pkix;
    requires org.bouncycastle.provider;
    requires org.bouncycastle.util;
    requires javafx.media;

    opens com.example.peck to javafx.fxml;
    exports com.example.peck;
}