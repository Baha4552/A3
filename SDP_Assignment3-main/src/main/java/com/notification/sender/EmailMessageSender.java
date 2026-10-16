package com.notification.sender;

import com.notification.MessageSender;

public class EmailMessageSender implements MessageSender {

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available) {
            System.out.println("Email is unavailable.");
            return false;
        }

        System.out.println("Sending Email...");
        System.out.println("To: " + recipient);
        System.out.println("Subject: " + title);
        System.out.println("Body: " + body);
        System.out.println("Signature: Best regards, Notification Gateway");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Email";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}