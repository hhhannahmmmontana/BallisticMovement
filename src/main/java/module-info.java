module com.volodya.ballisticmovement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.volodya.ballisticmovement to javafx.fxml;
    exports com.volodya.ballisticmovement;
}