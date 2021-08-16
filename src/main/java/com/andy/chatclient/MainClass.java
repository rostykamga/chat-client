package com.andy.chatclient;

import com.andy.chatclient.controller.LoginController;
import com.andy.chatclient.controller.MainClassController;
import com.andy.chatclient.services.Services;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class MainClass extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        stage.setOnCloseRequest(e->exit());

        //Let's avoid to exit the app when all stages are closed
        Platform.setImplicitExit(false);
        showLoginPage(stage);
    }


    public void exit(){
        Platform.exit();
        System.exit(0);
    }

    /**
     * Shows the login page
     * @param stage
     * @throws IOException
     */
    public static void showLoginPage(Stage stage) throws IOException {

        LoginController loginController = new LoginController(Services.getMessageService());

        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("/fxml/Login.fxml"));

        fxmlLoader.setController(loginController);

        if(stage.getScene() == null){
            Scene scene = new Scene(fxmlLoader.load());
            stage.setScene(scene);
        }
        else{
            stage.getScene().setRoot(fxmlLoader.load());
        }

        stage.setTitle("Login | Lite Messenger");
        stage.hide();
        stage.show();
    }

    public static void showMainAppPage(String user, Stage stage) throws IOException {
        MainClassController controller = new MainClassController(user, Services.getMessageService());
        FXMLLoader fxmlLoader = new FXMLLoader(MainClass.class.getResource("/fxml/MainApp.fxml"));
        fxmlLoader.setController(controller);
        Parent mainAppRoot = fxmlLoader.load();
        stage.getScene().setRoot(mainAppRoot);
        stage.setTitle("Home | Lite Messenger");
        stage.hide();
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}