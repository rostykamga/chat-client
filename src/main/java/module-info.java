module com.andy {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.andy to javafx.fxml;
    exports com.andy;
    exports com.andy.chatclient.services;
    opens com.andy.chatclient.services to javafx.fxml;
    exports com.andy.chatclient.utils;
    opens com.andy.chatclient.utils to javafx.fxml;
    exports com.andy.chatclient.controller;
    opens com.andy.chatclient.controller to javafx.fxml;
    exports com.andy.chatclient;
    opens com.andy.chatclient to javafx.fxml;
}