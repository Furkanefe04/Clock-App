module com.furkanoffice.clock {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.furkanoffice.clock to javafx.fxml;
    exports com.furkanoffice.clock;
}