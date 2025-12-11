module cinema.reservation {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.cinema to javafx.fxml;
    opens com.cinema.controller to javafx.fxml;
    opens com.cinema.model to javafx.fxml;

    exports com.cinema;
    exports com.cinema.controller;
    exports com.cinema.model;
}
