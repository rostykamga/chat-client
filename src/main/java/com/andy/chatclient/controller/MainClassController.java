package com.andy.chatclient.controller;

import com.andy.chatclient.MainClass;
import com.andy.chatclient.services.MessageService;
import com.andy.chatclient.utils.Utils;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalField;
import java.util.ResourceBundle;

public class MainClassController implements Initializable {

    private static final String SUCCESS_STYLE = "success";
    private static final String ERROR_STYLE = "error";

    private static final String SENT_MESSAGE_STYLE = "sent";
    private static final String RECEIVED_MESSAGE_STYLE = "received";

    private static final Pos SENT_MESSAGE_ALIGNMENT  = Pos.CENTER_RIGHT;
    private static final Pos RECEIVED_MESSAGE_ALIGNMENT  = Pos.CENTER_LEFT;

    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    @FXML
    private Parent rootPane;
    @FXML
    private Hyperlink logoutLink;

    @FXML
    private Label userLabel, statusLabel;

    @FXML
    private Button sendButton;

    @FXML
    private TextField messageTextField;

    @FXML
    private ScrollPane scrollpane;

    @FXML
    private VBox textFlow;

    private final MessageService messageService;
    private final String loggedUser;

    public MainClassController(String loggedUser, MessageService messageService) {
        this.messageService = messageService;
        this.loggedUser = loggedUser;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Set the logged in user
        userLabel.setText(Utils.capitalize(loggedUser));

        // disables send button when no text is typed
        sendButton.disableProperty().bind(Bindings.isEmpty(messageTextField.textProperty()));

        //set logout handler
        logoutLink.setOnAction(e->logoutAction());

        //set send button handler
        sendButton.setOnAction(e->sendMessageAction());

        //set the message service consumer, to run in fx thread.
        messageService.setMessageConsumer(message-> Platform.runLater(()->appendReceivedMessage(message)));

        setSuccessStatus("Connected !");

        // Send message on enter key pressed.
        messageTextField.setOnKeyReleased(keyEvent->{
            if(keyEvent.getCode() == KeyCode.ENTER && !Utils.isEmptyOrNullString(messageTextField.getText())){
                keyEvent.consume();
                sendButton.fire();
            }
        });
        // autofocus the message field
        Platform.runLater(()->messageTextField.requestFocus());
    }

    private void sendMessageAction(){

        try {
            messageService.sendMessage(messageTextField.getText());
            appendSentMessage(messageTextField.getText());
            setSuccessStatus("Sent !");
            messageTextField.setText("");
            messageTextField.requestFocus();
        } catch (Exception e) {
            setErrorStatus(e.getMessage());
        }
    }

    /**
     * Append any received message to the log
     * @param message
     */
    private void appendReceivedMessage(String message){
        appendMessage(message, RECEIVED_MESSAGE_ALIGNMENT, RECEIVED_MESSAGE_STYLE);
    }

    /**
     * Append any message sent by the user to the log
     * @param message
     */
    private void appendSentMessage(String message){
        appendMessage(message, SENT_MESSAGE_ALIGNMENT, SENT_MESSAGE_STYLE);
    }

    /**
     * Appends a new message into the message history using a given style and alignment
     * @param message
     * @param alignment
     * @param style
     */
    private void appendMessage(String message, Pos alignment, String style){

        HBox hbox = new HBox();
        hbox.setAlignment(alignment);

        LocalTime now = LocalTime.now();

        VBox vbox = new VBox();
        vbox.setAlignment(alignment);

        Label time = new Label(now.format(TIME_FORMATTER));
        time.getStyleClass().add("time");
        vbox.getChildren().add(time);

        Label label = new Label(message);
        label.setWrapText(true);
        label.setMaxWidth(400);
        label.getStyleClass().add("message");

        vbox.getStyleClass().add(style);
        vbox.getChildren().add(label);

        if(alignment == Pos.CENTER_RIGHT){
            vbox.setPadding(new Insets(0,0,0,20));
        }
        else{
            vbox.setPadding(new Insets(0,20,0,0));
        }


        hbox.getChildren().add(vbox);

        textFlow.getChildren().add(hbox);
        scrollpane.setVvalue(textFlow.getHeight());
    }

    private void logoutAction(){
        try {
            MainClass.showLoginPage((Stage)rootPane.getScene().getWindow());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Set the status bar message  to error
     * @param errorMessage
     */
    private void setErrorStatus(String errorMessage){
        statusLabel.setText(errorMessage);
        statusLabel.getStyleClass().remove(SUCCESS_STYLE);
        statusLabel.getStyleClass().add(ERROR_STYLE);
    }

    /**
     * Set the status bar to success.
     * @param status
     */
    private void setSuccessStatus(String status){
        statusLabel.setText(status);
        statusLabel.getStyleClass().remove(ERROR_STYLE);
        statusLabel.getStyleClass().add(SUCCESS_STYLE);
    }
}
