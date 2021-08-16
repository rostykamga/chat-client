package com.andy.chatclient.services;

public class Services {

    private static final MessageService messageService = new MessageService();

    public static MessageService getMessageService(){
        return messageService;
    }
}
