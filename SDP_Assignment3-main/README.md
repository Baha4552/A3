
# Multi-Channel Notification Gateway

## Overview

This project implements a Multi-Channel Notification Gateway using the Bridge Design Pattern in Java.

The system separates notification types from message delivery channels. This allows notification types and delivery mechanisms to be extended independently.

All message senders simulate delivery. No real external messaging APIs are required.

## Technologies

- Java 17+
- Maven
- JUnit 5
- Bridge Design Pattern

## Project Features

### Notification Types

- UrgentNotification — adds an urgent prefix and supports fallback delivery.
- EncryptedNotification — encodes message content using Base64.
- ScheduledBatchNotification — sends a message to multiple recipients and displays a delivery summary.
- MarketingNotification — adds a promotional prefix.

### Message Senders

- TelegramMessageSender — simulates formatted Telegram messages.
- EmailMessageSender — simulates email delivery with a subject and signature.
- SmsMessageSender — simulates SMS delivery with a 160-character limit.
- KafkaEventSender — simulates publishing an event to Kafka.
- PushNotificationSender — simulates push notifications.

### Additional Features

- Runtime switching between delivery channels.
- Fallback delivery when the primary sender is unavailable.
- Automated unit tests using JUnit 5.

## Project Structure

```text
src/main/java/com/notification/
├── MessageSender.java
├── Notification.java
├── UrgentNotification.java
├── EncryptedNotification.java
├── ScheduledBatchNotification.java
├── MarketingNotification.java
├── Main.java
└── sender/
    ├── TelegramMessageSender.java
    ├── EmailMessageSender.java
    ├── SmsMessageSender.java
    ├── KafkaEventSender.java
    └── PushNotificationSender.java
```

## Design Pattern: Bridge

The Bridge Pattern separates an abstraction from its implementation so that both can vary independently.

### Pattern Participants

- Abstraction: Notification
- Refined Abstractions: UrgentNotification, EncryptedNotification, ScheduledBatchNotification, MarketingNotification
- Implementor: MessageSender
- Concrete Implementors: TelegramMessageSender, EmailMessageSender, SmsMessageSender, KafkaEventSender, PushNotificationSender

The Notification class contains a reference to the MessageSender interface. It does not depend directly on a particular sender implementation.

## UML Class Diagram

![UML Class Diagram](docs/uml-diagram.png)

## How to Run

### Requirements

- Java 17 or newer
- Maven

### Compile the Project

```bash
mvn clean compile
```

### Run the Demonstration

```bash
java -cp target/classes com.notification.Main
```

The demonstration includes urgent notifications, Base64 encoding, batch delivery, runtime channel switching, fallback delivery, marketing notifications, and Kafka event simulation.

## Running Tests

Run all automated tests using:

```bash
mvn clean test
```

The tests cover urgent notifications, fallback delivery, Base64 encoding, batch delivery, runtime switching, SMS length validation, push notifications, marketing notifications, and sender availability.

## Design Questions

### 1. How does the Bridge Pattern allow notification types and delivery channels to vary independently?

Notification types depend on the MessageSender interface rather than concrete sender classes. New notification types can be created without changing existing senders. New delivery channels can implement MessageSender without modifying existing notification classes.

### 2. Where is the Bridge implemented in this project?

The bridge is the reference from Notification to MessageSender. Notification stores the sender and delegates message delivery to it. The concrete sender is supplied through the constructor or changed using setSender().

### 3. What problems could arise if inheritance were used for every notification-channel combination?

A separate class would be needed for every combination of notification type and channel. This would cause class proliferation, duplicated code, and difficult maintenance when new types or channels are introduced.

### 4. How can a new delivery provider be added?

Create a new class that implements MessageSender and implement sendMessage(), isAvailable(), and getChannelName(). Existing notification classes can use the new provider without modification.

### 5. How can a new notification type be added?

Create a new class that extends Notification and implement the send() method. The new class can use any MessageSender implementation through the bridge.

### 6. How does Bridge differ from Adapter?

Bridge separates two dimensions of a design so they can evolve independently. Adapter makes incompatible interfaces work together. Bridge is generally planned as part of the architecture, while Adapter is commonly used to integrate an existing interface with another interface.

## Limitations

- Message delivery is simulated; no real external services are connected.
- Base64 is encoding, not secure encryption.
- ScheduledBatchNotification demonstrates batch delivery but does not implement a real scheduling service.
- KafkaEventSender simulates publishing and does not connect to a Kafka broker.
