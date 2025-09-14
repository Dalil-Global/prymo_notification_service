CREATE TABLE user_notification_preferences (
                                               id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                                               user_id UUID NOT NULL,
                                               email_enabled BOOLEAN DEFAULT TRUE,
                                               sms_enabled BOOLEAN DEFAULT FALSE,
                                               push_enabled BOOLEAN DEFAULT FALSE,
                                               preferred_channel VARCHAR(20) DEFAULT 'email', -- e.g., email, sms, push
                                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
