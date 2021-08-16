package com.andy.chatclient.services;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.WebSocket;
import java.time.Duration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;



public class MessageService {

    private static final String WS_URI = "ws://localhost:8080/chat";

    private Consumer<String> messageConsumer;
    private AtomicBoolean isLoginResponseAwaited = new AtomicBoolean(false);
    private CountDownLatch latch = new CountDownLatch(1);
    private boolean loggedIn = false;
    private String roomID;

    private WebSocket ws;

    public MessageService() {

    }

    public void setMessageConsumer(Consumer<String> consumer){
        this.messageConsumer = consumer;
    }

    private void initWebSocket(String username, String password){
        HttpClient httpClient = HttpClient.newBuilder()
        		.version(Version.HTTP_1_1)
                .authenticator(new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password.toCharArray());
                    }
                })
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        WebSocket.Builder webSocketBuilder = httpClient.newWebSocketBuilder();
        ws = webSocketBuilder.buildAsync(URI.create(WS_URI), new WebSocketClient(this)).join();

        roomID = username;
    }

    public boolean isValidClient(String username, String password)throws Exception{

        if(ws == null)
            initWebSocket(username, password);

        isLoginResponseAwaited.set(true);

        if(latch.getCount() == 0)
            latch = new CountDownLatch(1);

        loggedIn = false;
        String data = String.format("{username: %s, password: %s}", username, password);
        ws.sendText(data, true);

        latch.await(5000, TimeUnit.MILLISECONDS);

        return loggedIn;
    }

    public void sendMessage(String message)throws Exception{
        isLoginResponseAwaited.set(false);

        String data = String.format("{roomID: %s, message: %s}", roomID, message);
        ws.sendText(data, true);
    }

    public void logout(){
        ws.sendClose(WebSocket.NORMAL_CLOSURE, "disconnect").thenRun(() -> System.out.println("DISCONNECTED"));
        ws = null;
    }

    private static class WebSocketClient implements WebSocket.Listener {

        private MessageService service;

        public WebSocketClient(MessageService service){
            this.service = service;
        }

        @Override
        public void onOpen(WebSocket webSocket) {
            System.out.println("onOpen using subprotocol " + webSocket.getSubprotocol());
            WebSocket.Listener.super.onOpen(webSocket);
        }

        @Override
        public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {

            if(service.messageConsumer != null)
                service.messageConsumer.accept(data.toString());

            if(service.isLoginResponseAwaited.get()){
                service.loggedIn = true;
                service.latch.countDown();
            }

            System.out.println("Received message: "+data);

            return WebSocket.Listener.super.onText(webSocket, data, last);
        }

        @Override
        public void onError(WebSocket webSocket, Throwable error) {
            System.out.println("Bad day! " + webSocket.toString());
            WebSocket.Listener.super.onError(webSocket, error);
        }
    }
}
