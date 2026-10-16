package com.notification;

public class MarketingNotification extends Notification {

    public MarketingNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        if (sender == null || !sender.isAvailable()) {
            System.out.println("Marketing notification sender is unavailable.");
            return;
        }

        String marketingMessage = "[SPECIAL OFFER] " + message;

        boolean success = sender.sendMessage(
                recipient,
                "Special Offer",
                marketingMessage
        );

        if (!success) {
            System.out.println("Marketing notification delivery failed.");
        }
    }
}