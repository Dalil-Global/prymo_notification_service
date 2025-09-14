CREATE TABLE delivery_status (
                                 id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
                                 notification_id UUID REFERENCES notifications(id) ON DELETE CASCADE,
                                 status VARCHAR(20) NOT NULL,
                                 provider VARCHAR(50),
                                 error_message TEXT,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);