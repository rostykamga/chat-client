module com.andy.chatclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    exports com.andy.chatclient;
    exports com.andy.chatclient.services;
    opens com.andy.chatclient.services to javafx.fxml;
    exports com.andy.chatclient.utils;
    opens com.andy.chatclient.utils to javafx.fxml;
    exports com.andy.chatclient.controller;
    opens com.andy.chatclient.controller to javafx.fxml;
    opens com.andy.chatclient to javafx.fxml;
}