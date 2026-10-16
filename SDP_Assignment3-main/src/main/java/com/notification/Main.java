package com.notification;

import java.util.List;

import com.notification.sender.EmailMessageSender;
import com.notification.sender.KafkaEventSender;
import com.notification.sender.PushNotificationSender;
import com.notification.sender.SmsMessageSender;
import com.notification.sender.TelegramMessageSender;

public class Main {

    public static void main(String[] args) {

        TelegramMessageSender telegram = new TelegramMessageSender();
        EmailMessageSender email = new EmailMessageSender();
        SmsMessageSender sms = new SmsMessageSender();
        KafkaEventSender kafka = new KafkaEventSender();
        PushNotificationSender push = new PushNotificationSender();

        System.out.println("==================================");
        System.out.println("1. UrgentNotification + Telegram");
        System.out.println("==================================");

        UrgentNotification urgent =
                new UrgentNotification(telegram, sms);

        urgent.send("user123", "Server is unavailable!");

        System.out.println("\n==================================");
        System.out.println("2. EncryptedNotification + Email");
        System.out.println("==================================");

        EncryptedNotification encrypted =
                new EncryptedNotification(email);

        encrypted.send("student@example.com", "Confidential message");

        System.out.println("\n==================================");
        System.out.println("3. ScheduledBatchNotification + SMS");
        System.out.println("==================================");

        ScheduledBatchNotification batch =
                new ScheduledBatchNotification(
                        sms,
                        List.of(
                                "+77001234567",
                                "+77007654321",
                                "+77005554433"
                        )
                );

        batch.send(null, "System maintenance at 22:00.");

        System.out.println("\n==================================");
        System.out.println("4. Runtime Switching: Telegram -> Email");
        System.out.println("==================================");

        Notification switchable =
                new UrgentNotification(telegram, sms);

        switchable.send("user123", "First message via Telegram");

        switchable.setSender(email);

        switchable.send("user123", "Second message via Email");

        System.out.println("\n==================================");
        System.out.println("5. Telegram unavailable -> SMS fallback");
        System.out.println("==================================");

        telegram.setAvailable(false);

        UrgentNotification fallbackNotification =
                new UrgentNotification(telegram, sms);

        fallbackNotification.send("user123", "Emergency alert!");

        telegram.setAvailable(true);

        System.out.println("\n==================================");
        System.out.println("6. MarketingNotification + Push");
        System.out.println("==================================");

        MarketingNotification marketing =
                new MarketingNotification(push);

        marketing.send(
                "device-123",
                "Get 20% off your next purchase!"
        );

        System.out.println("\n==================================");
        System.out.println("Additional demonstration: Kafka");
        System.out.println("==================================");

        Notification eventNotification =
                new EncryptedNotification(kafka);

        eventNotification.send(
                "system-events",
                "Server started"
        );
    }
}