module com.volodya.ballisticmovement {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.volodya.ballisticmovement to javafx.fxml;
    exports com.volodya.ballisticmovement;
    exports com.volodya.ballisticmovement.types;
    opens com.volodya.ballisticmovement.types to javafx.fxml;
}