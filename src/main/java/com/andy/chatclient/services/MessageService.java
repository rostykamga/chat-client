package com.andy.chatclient.services;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class MessageService {

    private Consumer<String> messageConsumer;
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    Runnable fakeSender = ()->{
        try {
            Thread.sleep(2000);
            messageConsumer.accept("Server response");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    };

    public void sendMessage(String message)throws Exception{
        executor.submit(fakeSender);
    }

    public void setMessageConsumer(Consumer<String> messageConsumer){
        this.messageConsumer = messageConsumer;
    }

    public void logout(){
        this.messageConsumer = null;
    }
}
