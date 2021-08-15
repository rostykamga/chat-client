package com.andy.chatclient.services;

public class Services {

    private static final LoginService loginService =  new LoginService();
    private static final MessageService messageService = new MessageService();

    public static LoginService getLoginService(){
        return loginService;
    }

    public static MessageService getMessageService(){
        return messageService;
    }
}
