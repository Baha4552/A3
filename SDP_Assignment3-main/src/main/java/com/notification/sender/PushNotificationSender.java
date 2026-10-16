package com.notification.sender;

import com.notification.MessageSender;

public class PushNotificationSender implements MessageSender {

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available) {
            System.out.println("Push notification service is unavailable.");
            return false;
        }

        System.out.println("Sending Push Notification...");
        System.out.println("Device: " + recipient);
        System.out.println("Title: " + title);
        System.out.println("Message: " + body);

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Push Notification";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}