package com.notification.sender;

import com.notification.MessageSender;

public class TelegramMessageSender implements MessageSender {

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available) {
            System.out.println("Telegram is unavailable.");
            return false;
        }

        System.out.println("Sending Telegram message...");
        System.out.println("Recipient: " + recipient);
        System.out.println("Title: " + title);
        System.out.println("Message: <b>" + body + "</b>");

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Telegram";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}