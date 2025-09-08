# **Notification Service**

## Overview

Centralized service for handling user and system notifications across multiple channels (Email, SMS, and Push). It ensures reliable delivery, template management, and user preference handling.

## Responsibilities

* Send transactional and user notifications
* Support **Email, SMS, and Push** channels
* Manage reusable notification templates
* Track delivery status and retries
* Handle user notification preferences
* Integrate with other microservices via **Kafka events**

## Technology Stack

* **Framework**: Spring Boot 3.2+
* **Database**: PostgreSQL
* **Cache**: Redis (for retry queues, temporary storage)
* **Messaging**: Kafka
* **Email Providers**: SendGrid / AWS SES / SMTP
* **SMS Providers**: Twilio / Infobip
* **Push Notifications**: Firebase Cloud Messaging (FCM) / Apple Push Notification service (APNs)
* **Documentation**: OpenAPI 3.0

## API Endpoints

### Notifications

* `POST /api/v1/notifications/send` → Send a new notification
* `GET /api/v1/notifications/{id}` → Get notification delivery status
* `POST /api/v1/notifications/preferences` → Update user notification preferences

### Templates

* `POST /api/v1/templates` → Create new template
* `GET /api/v1/templates/{id}` → Fetch template details
* `PUT /api/v1/templates/{id}` → Update existing template

---

## Database Schema

### Notifications Table

```sql
CREATE TABLE notifications (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL,
    type VARCHAR(20) NOT NULL,        -- e.g. TRANSACTION, ALERT, OTP
    channel VARCHAR(20) NOT NULL,     -- EMAIL, SMS, PUSH
    status VARCHAR(20) DEFAULT 'pending',
    priority VARCHAR(20) DEFAULT 'normal',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Notification Templates Table

```sql
CREATE TABLE notification_templates (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,      -- EMAIL, SMS, PUSH
    content TEXT NOT NULL,          -- HTML, plain text, JSON
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### Delivery Status Table

```sql
CREATE TABLE delivery_status (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    notification_id UUID REFERENCES notifications(id),
    channel VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,    -- SENT, DELIVERED, FAILED, RETRY
    provider_response TEXT,
    attempts INT DEFAULT 0,
    last_attempt_at TIMESTAMP
);
```

---

## Business Logic

* **Channel fallback**: If email fails, attempt SMS or push (based on user preference).
* **Retry mechanism**: Retry failed deliveries with exponential backoff.
* **User preferences**: Store opt-in/opt-out settings per channel.
* **Templates**: Ensure consistent messaging across channels.
* **Event-driven**: Other services (Transaction, SecureHold, Dispute) publish events → Notification Service consumes and sends notifications.

---

## Environment Variables

```properties
# Database
DB_HOST=localhost
DB_PORT=5432
DB_NAME=notification_db
DB_USERNAME=prymo_notification
DB_PASSWORD=${DB_PASSWORD}

# Kafka
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
KAFKA_GROUP_ID=notification-service

# Redis
REDIS_HOST=localhost
REDIS_PORT=6379

# Providers
SENDGRID_API_KEY=${SENDGRID_API_KEY}
AWS_SES_ACCESS_KEY=${AWS_SES_ACCESS_KEY}
AWS_SES_SECRET_KEY=${AWS_SES_SECRET_KEY}
TWILIO_ACCOUNT_SID=${TWILIO_ACCOUNT_SID}
TWILIO_AUTH_TOKEN=${TWILIO_AUTH_TOKEN}
INFOBIP_API_KEY=${INFOBIP_API_KEY}
FCM_SERVER_KEY=${FCM_SERVER_KEY}
APNS_CERTIFICATE_PATH=${APNS_CERTIFICATE_PATH}
APNS_CERTIFICATE_PASSWORD=${APNS_CERT_PASSWORD}
```

---

## Integration Points

* **Transaction Service** → Payment confirmations, receipts
* **SecureHold Service** → Hold creation, release, expiry alerts
* **Dispute Management Service** → Dispute updates and resolutions
* **User Management Service** → Welcome emails, password resets
