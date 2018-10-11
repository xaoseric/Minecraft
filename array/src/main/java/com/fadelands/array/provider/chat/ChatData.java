package com.fadelands.array.provider.chat;

public class ChatData {

    private String message;
    private long timeSent;

    public ChatData(String message){
        this(message, System.currentTimeMillis());
    }

    public ChatData(String message, long timeSent) {
        this.message = message;
        this.timeSent = timeSent;
    }

    public String getMessage() {
        return message;
    }

    public long getTimeSent() {
        return timeSent;
    }
}
