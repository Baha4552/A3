package com.notification;

import java.util.Objects;

public class UrgentNotification extends Notification {

    private final MessageSender fallbackSender;

    public UrgentNotification(
            MessageSender sender,
            MessageSender fallbackSender
    ) {
        super(sender);
        this.fallbackSender = Objects.requireNonNull(
                fallbackSender,
                "Fallback sender cannot be null"
        );
    }

    @Override
    public void send(String recipient, String message) {
        String urgentMessage = "[URGENT] " + message;

        if (sender != null && sender.isAvailable()) {
            if (sender.sendMessage(
                    recipient,
                    "Urgent Notification",
                    urgentMessage
            )) {
                return;
            }
        }

        System.out.println("Primary sender unavailable or failed.");
        System.out.println(
                "Trying fallback: " + fallbackSender.getChannelName()
        );

        if (fallbackSender.isAvailable()
                && fallbackSender.sendMessage(
                        recipient,
                        "Urgent Notification",
                        urgentMessage
                )) {
            System.out.println("Fallback delivery successful.");
        } else {
            System.out.println("Notification delivery failed.");
        }
    }
}