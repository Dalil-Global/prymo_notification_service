CREATE TABLE user_notification_preferences (
                                               id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                               user_id UUID NOT NULL,
                                               email_enabled BOOLEAN DEFAULT TRUE,
                                               sms_enabled BOOLEAN DEFAULT FALSE,
                                               push_enabled BOOLEAN DEFAULT FALSE,
                                               preferred_channel VARCHAR(20) DEFAULT 'EMAIL',
                                               created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                               updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);