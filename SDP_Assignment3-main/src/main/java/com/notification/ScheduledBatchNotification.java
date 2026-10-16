package com.notification;

import java.util.List;

public class ScheduledBatchNotification extends Notification {

    private final List<String> recipients;

    public ScheduledBatchNotification(
            MessageSender sender,
            List<String> recipients
    ) {
        super(sender);
        this.recipients = List.copyOf(recipients);
    }

    @Override
    public void send(String ignoredRecipient, String message) {
        int successful = 0;
        int failed = 0;

        for (String recipient : recipients) {
            if (sender != null
                    && sender.isAvailable()
                    && sender.sendMessage(
                            recipient,
                            "Batch Notification",
                            message
                    )) {
                successful++;
            } else {
                failed++;
            }
        }

        System.out.println("\n--- Batch Notification Summary ---");
        System.out.println("Total: " + recipients.size());
        System.out.println("Successful: " + successful);
        System.out.println("Failed: " + failed);
    }
}