package com.notification;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.notification.sender.EmailMessageSender;
import com.notification.sender.PushNotificationSender;
import com.notification.sender.SmsMessageSender;
import com.notification.sender.TelegramMessageSender;

class NotificationTest {

    // Fake sender for testing without external services.
    static class FakeSender implements MessageSender {

        private boolean available = true;
        private boolean sendResult = true;

        private int sendCount = 0;
        private String lastRecipient;
        private String lastTitle;
        private String lastBody;

        @Override
        public boolean sendMessage(
                String recipient,
                String title,
                String body
        ) {
            sendCount++;

            lastRecipient = recipient;
            lastTitle = title;
            lastBody = body;

            return available && sendResult;
        }

        @Override
        public boolean isAvailable() {
            return available;
        }

        @Override
        public String getChannelName() {
            return "Fake";
        }

        void setAvailable(boolean available) {
            this.available = available;
        }

        void setSendResult(boolean sendResult) {
            this.sendResult = sendResult;
        }
    }

    // TEST 1: Urgent notification sends successfully.
    @Test
    void urgentNotificationSendsMessage() {
        FakeSender sender = new FakeSender();

        UrgentNotification notification =
                new UrgentNotification(sender, new FakeSender());

        notification.send("user1", "Server error");

        assertEquals(1, sender.sendCount);
        assertEquals("user1", sender.lastRecipient);
        assertTrue(sender.lastBody.startsWith("[URGENT]"));
    }

    // TEST 2: Unavailable primary sender uses fallback.
    @Test
    void urgentNotificationUsesFallback() {
        FakeSender primary = new FakeSender();
        FakeSender fallback = new FakeSender();

        primary.setAvailable(false);

        UrgentNotification notification =
                new UrgentNotification(primary, fallback);

        notification.send("user1", "Emergency");

        assertEquals(0, primary.sendCount);
        assertEquals(1, fallback.sendCount);
    }

    // TEST 3: Failed primary delivery uses fallback.
    @Test
    void urgentNotificationUsesFallbackAfterFailure() {
        FakeSender primary = new FakeSender();
        FakeSender fallback = new FakeSender();

        primary.setSendResult(false);

        UrgentNotification notification =
                new UrgentNotification(primary, fallback);

        notification.send("user1", "Emergency");

        assertEquals(1, primary.sendCount);
        assertEquals(1, fallback.sendCount);
    }

    // TEST 4: Encrypted notification encodes the message.
    @Test
    void encryptedNotificationEncodesMessage() {
        FakeSender sender = new FakeSender();

        EncryptedNotification notification =
                new EncryptedNotification(sender);

        notification.send("user1", "Hello");

        assertNotEquals("Hello", sender.lastBody);
        assertEquals("SGVsbG8=", sender.lastBody);
    }

    // TEST 5: Encrypted notification handles unavailable sender.
    @Test
    void encryptedNotificationHandlesUnavailableSender() {
        FakeSender sender = new FakeSender();
        sender.setAvailable(false);

        EncryptedNotification notification =
                new EncryptedNotification(sender);

        notification.send("user1", "Secret");

        assertEquals(0, sender.sendCount);
    }

    // TEST 6: Batch notification sends to every recipient.
    @Test
    void batchNotificationSendsToAllRecipients() {
        FakeSender sender = new FakeSender();

        ScheduledBatchNotification notification =
                new ScheduledBatchNotification(
                        sender,
                        List.of("user1", "user2", "user3")
                );

        notification.send(null, "Announcement");

        assertEquals(3, sender.sendCount);
        assertEquals("user3", sender.lastRecipient);
    }

    // TEST 7: Batch notification handles unavailable sender.
    @Test
    void batchNotificationHandlesUnavailableSender() {
        FakeSender sender = new FakeSender();
        sender.setAvailable(false);

        ScheduledBatchNotification notification =
                new ScheduledBatchNotification(
                        sender,
                        List.of("user1", "user2")
                );

        notification.send(null, "Announcement");

        assertEquals(0, sender.sendCount);
    }

    // TEST 8: Notification supports runtime sender switching.
    @Test
    void notificationSupportsRuntimeSwitching() {
        FakeSender firstSender = new FakeSender();
        FakeSender secondSender = new FakeSender();

        Notification notification =
                new UrgentNotification(
                        firstSender,
                        new FakeSender()
                );

        notification.send("user1", "First message");

        notification.setSender(secondSender);

        notification.send("user1", "Second message");

        assertEquals(1, firstSender.sendCount);
        assertEquals(1, secondSender.sendCount);
    }

    // TEST 9: SMS rejects messages longer than 160 characters.
    @Test
    void smsRejectsMessageLongerThan160Characters() {
        SmsMessageSender sms = new SmsMessageSender();

        String message = "A".repeat(161);

        boolean result = sms.sendMessage(
                "+77001234567",
                "Test",
                message
        );

        assertFalse(result);
    }

    // TEST 10: SMS accepts a message of 160 characters.
    @Test
    void smsAcceptsMessageOf160Characters() {
        SmsMessageSender sms = new SmsMessageSender();

        String message = "A".repeat(160);

        boolean result = sms.sendMessage(
                "+77001234567",
                "Test",
                message
        );

        assertTrue(result);
    }

    // TEST 11: Push sender sends successfully.
    @Test
    void pushSenderSendsSuccessfully() {
        PushNotificationSender push =
                new PushNotificationSender();

        assertTrue(
                push.sendMessage(
                        "device-123",
                        "Offer",
                        "Discount available"
                )
        );
    }

    // TEST 12: Marketing notification adds a promotional prefix.
    @Test
    void marketingNotificationAddsOfferPrefix() {
        FakeSender sender = new FakeSender();

        MarketingNotification notification =
                new MarketingNotification(sender);

        notification.send("device-123", "20% discount");

        assertEquals(1, sender.sendCount);

        assertTrue(
                sender.lastBody.startsWith("[SPECIAL OFFER]")
        );
    }

    // TEST 13: Email sender reports successful delivery.
    @Test
    void emailSenderSendsSuccessfully() {
        EmailMessageSender email =
                new EmailMessageSender();

        assertTrue(
                email.sendMessage(
                        "test@example.com",
                        "Test subject",
                        "Test body"
                )
        );
    }

    // TEST 14: Telegram handles unavailable status.
    @Test
    void telegramSenderHandlesUnavailableStatus() {
        TelegramMessageSender telegram =
                new TelegramMessageSender();

        telegram.setAvailable(false);

        assertFalse(telegram.isAvailable());

        assertFalse(
                telegram.sendMessage(
                        "user1",
                        "Test",
                        "Hello"
                )
        );
    }
}