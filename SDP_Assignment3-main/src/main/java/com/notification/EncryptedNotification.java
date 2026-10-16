package com.notification;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class EncryptedNotification extends Notification {

    public EncryptedNotification(MessageSender sender) {
        super(sender);
    }

    @Override
    public void send(String recipient, String message) {
        if (sender == null || !sender.isAvailable()) {
            System.out.println("Notification sender is unavailable.");
            return;
        }

        String encodedMessage = Base64.getEncoder()
                .encodeToString(message.getBytes(StandardCharsets.UTF_8));

        boolean success = sender.sendMessage(
                recipient,
                "Encrypted Notification",
                encodedMessage
        );

        if (!success) {
            System.out.println("Encrypted notification delivery failed.");
        }
    }
}