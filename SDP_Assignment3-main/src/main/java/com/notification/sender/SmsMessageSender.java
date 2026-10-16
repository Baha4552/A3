package com.notification.sender;

import com.notification.MessageSender;

public class SmsMessageSender implements MessageSender {

    private static final int MAX_LENGTH = 160;

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available) {
            System.out.println("SMS service is unavailable.");
            return false;
        }

        if (body.length() > MAX_LENGTH) {
            System.out.println("SMS failed: message exceeds 160 characters.");
            System.out.println("Message length: " + body.length());
            return false;
        }

        System.out.println("Sending SMS...");
        System.out.println("Recipient: " + recipient);
        System.out.println("Message: " + body);
        System.out.println("Message length: " + body.length());

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "SMS";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}