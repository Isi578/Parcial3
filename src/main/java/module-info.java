module smart.home.management.system {
    requires javafx.controls;
    requires javafx.fxml;

    exports app;
    exports controller;
    exports model;
    exports util.enums;
    opens controller to javafx.fxml;
}
