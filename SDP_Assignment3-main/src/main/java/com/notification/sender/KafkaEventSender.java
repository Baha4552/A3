package com.notification.sender;

import com.notification.MessageSender;

public class KafkaEventSender implements MessageSender {

    private boolean available = true;

    @Override
    public boolean sendMessage(String recipient, String title, String body) {
        if (!available) {
            System.out.println("Kafka is unavailable.");
            return false;
        }

        System.out.println("Publishing Kafka event...");
        System.out.println("Topic: " + recipient);
        System.out.println("Event type: " + title);
        System.out.println("Payload: " + body);

        return true;
    }

    @Override
    public boolean isAvailable() {
        return available;
    }

    @Override
    public String getChannelName() {
        return "Kafka";
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}