module com.volodya.ballisticmovement {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.volodya.ballisticmovement to javafx.fxml;
    exports com.volodya.ballisticmovement;
}