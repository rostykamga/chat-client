package com.andy.chatclient.controller;

import com.andy.chatclient.MainClass;
import com.andy.chatclient.services.LoginService;
import com.andy.chatclient.utils.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class LoginController implements Initializable {

    @FXML private VBox rootPane;
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginButton;
    @FXML private Label errorLabel;

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Initially clear the error label
        errorLabel.setText("");

        //set login button action
        loginButton.setOnAction(e->onLoginAction(e));

        //try to login when enter key is pressed, and clear the error message otherwise
        rootPane.addEventFilter(KeyEvent.KEY_PRESSED, event->{
            if(event.getCode() == KeyCode.ENTER){
                event.consume();
                loginButton.fire();
            }
            else{
                errorLabel.setText("");
            }
        });
    }

    private void onLoginAction(ActionEvent event){

        String login = loginField.getText();
        String password = passwordField.getText();

        // checks for login and password emptiness first

        if(Utils.isEmptyOrNullString(login) || Utils.isEmptyOrNullString(password)) {
            errorLabel.setText("Invalid login or password !");
            return;
        }

        try {
            if(!loginService.isValidClient(login, password)){
                errorLabel.setText("Wrong login or password !");
                return;
            }
        } catch (Exception e) {
            errorLabel.setText("Fatal error during authentication with server.\n"+e.getMessage());
            return;
        }

        try {
            MainClass.showMainAppPage(login, (Stage)loginButton.getScene().getWindow());

            /*MainClassController controller = new MainClassController(login, Services.getMessageService());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/MainApp.fxml"));
            fxmlLoader.setController(controller);
            Parent mainAppRoot = fxmlLoader.load();
            loginButton.getScene().setRoot(mainAppRoot);*/

            //Stage mainAppStage = new Stage();
           // mainAppStage.setScene(new Scene(mainAppRoot));
            //mainAppStage.show();
        } catch (IOException e) {
            errorLabel.setText("Fatal IO error occurred");
        }
    }
}
